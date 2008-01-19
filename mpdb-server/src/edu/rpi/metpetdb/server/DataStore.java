package edu.rpi.metpetdb.server;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.net.URL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Iterator;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.postgis.Geometry;

import edu.rpi.metpetdb.client.model.MObject;
import edu.rpi.metpetdb.client.model.validation.BooleanConstraint;
import edu.rpi.metpetdb.client.model.validation.CollectionConstraint;
import edu.rpi.metpetdb.client.model.validation.DatabaseObjectConstraints;
import edu.rpi.metpetdb.client.model.validation.FloatConstraint;
import edu.rpi.metpetdb.client.model.validation.GeometryConstraint;
import edu.rpi.metpetdb.client.model.validation.ImageConstraint;
import edu.rpi.metpetdb.client.model.validation.IntegerConstraint;
import edu.rpi.metpetdb.client.model.validation.MObjectConstraint;
import edu.rpi.metpetdb.client.model.validation.MineralConstraint;
import edu.rpi.metpetdb.client.model.validation.MultiValuedStringConstraint;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraints;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.StringConstraint;
import edu.rpi.metpetdb.client.model.validation.TimestampConstraint;

/** Global service support. */
public class DataStore {
	private static final short attributeNullable = DatabaseMetaData.attributeNullable;

	protected static Configuration config;

	private static SessionFactory factory;

	private static DatabaseObjectConstraints databaseObjectConstraints;

	private static ObjectConstraints objectConstraints;

	protected static synchronized Configuration getConfiguration() {
		if (config == null) {
			final Configuration cfg = new Configuration();
			final URL x = DataStore.class.getResource("dao/hibernate.cfg.xml");
			if (x == null)
				throw new MappingException("Missing dao/hibernate.cfg.xml.");
			cfg.configure(x);
			config = cfg;
		}
		return config;
	}

	private static synchronized SessionFactory getFactory() {
		if (factory == null)
			factory = getConfiguration().buildSessionFactory();
		return factory;
	}

	public static synchronized void init() {
		if (factory == null)
			getFactory();
	}

	static synchronized void destroy() {
		final SessionFactory f = factory;
		config = null;
		factory = null;
		try {
			if (f != null)
				f.close();
		} catch (Throwable err) {
		}
	}

	static synchronized ObjectConstraints getObjectConstraints() {
		if (objectConstraints == null) {
			final ObjectConstraints oc = new ObjectConstraints();
			setConstraints(oc);
			objectConstraints = oc;
		}
		return objectConstraints;
	}

	static synchronized DatabaseObjectConstraints getDatabaseObjectConstraints() {
		if (databaseObjectConstraints == null) {
			final DatabaseObjectConstraints oc = new DatabaseObjectConstraints();
			setConstraints(oc);
			databaseObjectConstraints = oc;
		}
		return databaseObjectConstraints;
	}

	@SuppressWarnings("deprecation")
	static synchronized void setConstraints(final DatabaseObjectConstraints oc) {
		final Session s = open();
		try {
			final Connection conn = s.connection();
			try {
				final DatabaseMetaData md = conn.getMetaData();
				final Field[] fields = oc.getClass().getDeclaredFields();
				for (int k = 0; k < fields.length; k++)
					populateObjectConstraintField(md, oc, fields[k]);
			} catch (SQLException err) {
				throw new HibernateException("Database catalog error", err);
			} catch (IllegalAccessException err) {
				throw new RuntimeException("Internal Java error", err);
			} catch (InstantiationException err) {
				throw new RuntimeException("Internal Java error", err);
			} finally {
				try {
					conn.close();
				} catch (SQLException err) {
				}
			}
		} finally {
			s.close();
		}
		oc.finishInitialization();
	}

	@SuppressWarnings("unchecked")
	private static void populateObjectConstraintField(
			final DatabaseMetaData md, final DatabaseObjectConstraints oc,
			final Field f) throws SQLException, IllegalAccessException,
			InstantiationException {
		if (f.getType().isArray())
			return;
		final String[] v = f.getName().split("_");
		if (v.length != 2)
			throw new RuntimeException("Cannot populate field " + f.getName());

		final String pkg = "edu.rpi.metpetdb.client.model";
		final String entityName = v[0];
		final String attributeName = v[1];
		final PersistentClass cm;
		final Property prop;
		final Column col;

		if (oc instanceof ObjectConstraints)
			cm = null;
		else
			cm = DataStore.getConfiguration().getClassMapping(
					pkg + "." + entityName);
		prop = cm != null ? cm.getProperty(attributeName) : null;
		if (prop != null) {
			if (prop.getValue().getClass() == org.hibernate.mapping.Set.class
					|| prop.getValue().getClass() == org.hibernate.mapping.Bag.class) {
				col = null;
				// final Iterator fkItr = ((org.hibernate.mapping.Set) prop
				// .getValue()).getCollectionTable()
				// .getForeignKeyIterator();
				// while (fkItr.hasNext()) {
				// TODO make it get the constraints of referenced columns
				// final ForeignKey fk = (ForeignKey) fkItr.next();
				// fk.getReferencedColumns().get(0);
				// }
			} else {
				final Iterator i = prop.getColumnIterator();
				if (!i.hasNext())
					throw new RuntimeException("No cols: " + f.getName());
				col = (Column) i.next();
				if (i.hasNext())
					throw new RuntimeException("Too many cols: " + f.getName());
			}
		} else {
			col = null;
		}

		PropertyConstraint pc = (PropertyConstraint) f.get(oc);
		if (pc == null) {
			pc = createPropertyConstraint(prop, f);
			f.set(oc, pc);
		}

		pc.entityName = entityName;
		pc.propertyName = attributeName;
		pc.propertyId = propertyId(clazz(cm, entityName), pc.propertyName);
		appendToAllArray(oc, pc);

		if (cm != null && col != null) {
			final ResultSet rs = md.getColumns(null, null, cm.getTable()
					.getName(), col.getName());
			try {
				if (!rs.next())
					throw new RuntimeException("No cols: " + f.getName());

				final int type = rs.getInt("DATA_TYPE");
				pc.required = rs.getInt("NULLABLE") != attributeNullable;

				if (pc instanceof StringConstraint) {
					final StringConstraint sc = (StringConstraint) pc;

					sc.maxLength = rs.getInt("COLUMN_SIZE");

					// Typically a CHAR type is used because we want to
					// enforce minLength == maxLength.
					//
					if (type == Types.CHAR)
						sc.minLength = sc.maxLength;

					// If the field is required its minLength is at least 1.
					//
					if (sc.minLength == 0 && pc.required)
						sc.minLength = 1;
				} else if (pc instanceof IntegerConstraint) {

				} else if (pc instanceof FloatConstraint) {

				}

				if (rs.next())
					throw new RuntimeException("Too many cols: " + f.getName());
			} finally {
				rs.close();
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static Class clazz(final PersistentClass cm, final String entityName) {
		if (cm != null)
			return cm.getMappedClass();
		try {
			final String pkg = "edu.rpi.metpetdb.client.model";
			return Class.forName(pkg + "." + entityName);
		} catch (ClassNotFoundException cnfe) {
			throw new RuntimeException("No " + entityName + " in Hibernate?");
		}
	}

	@SuppressWarnings("unchecked")
	private static PropertyConstraint createPropertyConstraint(
			final Property p, final Field f) throws IllegalAccessException,
			InstantiationException {
		try {
			final String pkg = "edu.rpi.metpetdb.client.model.validation";
			final Class c = Class.forName(pkg + "." + f.getName());
			return (PropertyConstraint) c.newInstance();
		} catch (ClassNotFoundException cnfe) {
		}

		final Class c = f.getType();
		if (p == null)
			return (PropertyConstraint) c.newInstance();
		final Class rc = p.getType().getReturnedClass();
		final String tn = p.getType().getName();
		final String name = p.getName();
		final String className = rc.getName().substring(
				rc.getName().lastIndexOf(".") + 1);

		if (rc == String.class)
			return StringConstraint.class.isAssignableFrom(c) ? (PropertyConstraint) c
					.newInstance()
					: new StringConstraint();
		else if (rc == Geometry.class)
			return GeometryConstraint.class.isAssignableFrom(c) ? (PropertyConstraint) c
					.newInstance()
					: new GeometryConstraint();
		else if (rc == Integer.class)
			return IntegerConstraint.class.isAssignableFrom(c) ? (PropertyConstraint) c
					.newInstance()
					: new IntegerConstraint();
		else if (rc == Float.class)
			return FloatConstraint.class.isAssignableFrom(c) ? (PropertyConstraint) c
					.newInstance()
					: new FloatConstraint();
		else if ("minerals".equals(name) || "mineral".equals(name)) {
			MineralConstraint mc;
			if (MineralConstraint.class.isAssignableFrom(c)) {
				mc = (MineralConstraint) c.newInstance();
			} else {
				mc = new MineralConstraint();
			}
			final Session session = open();
			try {
				mc.setMinerals(session.getNamedQuery("Mineral.parents").list());
			} catch (org.hibernate.exception.GenericJDBCException dbe) {
				session.cancelQuery();
			} finally {
				session.clear();
				session.close();
			}
			return mc;
		} else if (rc == java.util.Set.class
				|| rc == java.util.Collection.class) {
			// Many-to-many or one-to-many association
			if ("images".equals(name)) {
				return ImageConstraint.class.isAssignableFrom(c) ? (ImageConstraint) c
						.newInstance()
						: new ImageConstraint();
			} else {
				MultiValuedStringConstraint rcc;
				if (MultiValuedStringConstraint.class.isAssignableFrom(c)) {
					rcc = (MultiValuedStringConstraint) c.newInstance();
				} else {
					rcc = new MultiValuedStringConstraint();
				}
				return rcc;
			}
		} else if ("timestamp".equals(tn)) {
			return TimestampConstraint.class.isAssignableFrom(c) ? (TimestampConstraint) c
					.newInstance()
					: new TimestampConstraint();
		} else if (rc == Boolean.class || "yes_no".equals(tn)) {
			return BooleanConstraint.class.isAssignableFrom(c) ? (BooleanConstraint) c
					.newInstance()
					: new BooleanConstraint();
		} else if ("image".equals(name)) {
			return ImageConstraint.class.isAssignableFrom(c) ? (ImageConstraint) c
					.newInstance()
					: new ImageConstraint();
		} else if (c == CollectionConstraint.class) {
			// We want to fetch the applicable values for the one end of a
			// Many-to-one
			// association
			CollectionConstraint cc;
			if (CollectionConstraint.class.isAssignableFrom(c)) {
				cc = (CollectionConstraint) c.newInstance();
			} else {
				cc = new CollectionConstraint();
			}
			final Session session = open();
			cc.setValues(session.getNamedQuery(className + ".all").list());
			session.clear();
			session.close();
			return cc;
		} else if (MObject.class.isAssignableFrom(rc))
			return (MObjectConstraint) c.newInstance();
		else
			throw new RuntimeException("Unsupported type for "
					+ p.getPersistentClass().getClassName() + " property "
					+ p.getName() + ".");
	}

	@SuppressWarnings("unchecked")
	private static int propertyId(final Class who, final String name)
			throws IllegalAccessException {
		try {
			return ((Integer) who.getField("P_" + name).get(null)).intValue();
		} catch (NoSuchFieldException nsfe) {
			throw new RuntimeException("No P_" + name + " in " + who.getName());
		}
	}

	@SuppressWarnings("unchecked")
	private static void appendToAllArray(final DatabaseObjectConstraints oc,
			final PropertyConstraint pc) throws IllegalAccessException {
		try {
			final String allName = pc.entityName + "__all";
			final Field all = oc.getClass().getField(allName);
			final Class ct = all.getType().getComponentType();
			final Object[] a = (Object[]) all.get(oc);
			final Object[] b;
			if (a == null) {
				b = (Object[]) Array.newInstance(ct, 1);
				b[0] = pc;
			} else {
				for (int k = 0; k < a.length; k++)
					if (a[k] == pc)
						return;
				b = (Object[]) Array.newInstance(ct, a.length + 1);
				System.arraycopy(a, 0, b, 0, a.length);
				b[a.length] = pc;
			}
			all.set(oc, b);
		} catch (NoSuchFieldException nsfe) {
			// Not needed? Ok... ignore it.
		}
	}

	/**
	 * Obtain a new Hibernate session.
	 * 
	 * @return a new hibernate session. This will always be a new session, even
	 *         if the caller already has one.
	 */
	public static Session open() {
		return getFactory().openSession();
	}

	protected DataStore() {
	}
}