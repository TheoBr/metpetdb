package edu.rpi.metpetdb.server;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.net.URL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.gilead.core.PersistentBeanManager;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.Component;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.Formula;
import org.hibernate.mapping.OneToMany;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.Set;
import org.hibernate.mapping.Value;
import org.postgis.Geometry;

import edu.rpi.metpetdb.client.model.MObject;
import edu.rpi.metpetdb.client.model.Mineral;
import edu.rpi.metpetdb.client.model.validation.DatabaseObjectConstraints;
import edu.rpi.metpetdb.client.model.validation.GeometryConstraint;
import edu.rpi.metpetdb.client.model.validation.MObjectConstraint;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraint;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraints;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.TimestampConstraint;
import edu.rpi.metpetdb.client.model.validation.ValueInCollectionConstraint;
import edu.rpi.metpetdb.client.model.validation.primitive.BooleanConstraint;
import edu.rpi.metpetdb.client.model.validation.primitive.DoubleConstraint;
import edu.rpi.metpetdb.client.model.validation.primitive.FloatConstraint;
import edu.rpi.metpetdb.client.model.validation.primitive.IntegerConstraint;
import edu.rpi.metpetdb.client.model.validation.primitive.ShortConstraint;
import edu.rpi.metpetdb.client.model.validation.primitive.StringConstraint;
import edu.rpi.metpetdb.server.dao.impl.MineralDAO;
import edu.rpi.metpetdb.server.security.permissions.PermissionInterceptor;

/** Global service support. */
public class DataStore {
	private static final short attributeNullable = DatabaseMetaData.attributeNullable;

	protected static Configuration config;

	private static SessionFactory factory;

	private static DatabaseObjectConstraints databaseObjectConstraints;

	private static ObjectConstraints objectConstraints;

	private final static DataStore instance = new DataStore();

	private static PersistentBeanManager hbm;

	public static DataStore getInstance() {
		return instance;
	}

	public static void setBeanManager(PersistentBeanManager hbm) {
		DataStore.hbm = hbm;
	}

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

	public static synchronized SessionFactory getFactory() {
		if (factory == null)
			factory = getConfiguration().buildSessionFactory();
		return factory;
	}

	public static synchronized void initFactory() {
		if (factory == null)
			getFactory();
	}

	static synchronized void destoryFactory() {
		final SessionFactory f = factory;
		config = null;
		factory = null;
		try {
			if (f != null)
				f.close();
		} catch (Throwable err) {
		}
	}

	public synchronized void resetConstraints() {
		objectConstraints = null;
		databaseObjectConstraints = null;
	}

	public synchronized ObjectConstraints getObjectConstraints() {
		if (objectConstraints == null) {
			final ObjectConstraints oc = new ObjectConstraints();
			setConstraints(oc);
			objectConstraints = oc;
		}
		return objectConstraints;
	}

	public synchronized DatabaseObjectConstraints getDatabaseObjectConstraints() {
		if (databaseObjectConstraints == null) {
			final DatabaseObjectConstraints oc = new DatabaseObjectConstraints();
			databaseObjectConstraints = oc;
			setConstraints(oc);
		}
		return databaseObjectConstraints;
	}

	synchronized void setConstraints(final DatabaseObjectConstraints oc) {
		final Session s = open();
		try {
			final Connection conn = getConfiguration().buildSettings()
					.getConnectionProvider().getConnection();
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
		} catch (SQLException err) {
			throw new HibernateException("getConnection() error");
		} finally {
			s.close();
		}
		oc.finishInitialization(databaseObjectConstraints);
	}

	private void populateObjectConstraintField(final DatabaseMetaData md,
			final DatabaseObjectConstraints oc, final Field f)
			throws SQLException, IllegalAccessException, InstantiationException {
		if (f.getType().isArray())
			return;
		final String[] v = f.getName().split("_");

		final String pkg = "edu.rpi.metpetdb.client.model";
		final String entityName;
		if (v.length == 2)
			entityName = v[0];
		else
			entityName = v[1];
		final String entityNameProperty;
		if (v.length == 2)
			entityNameProperty = entityName;
		else
			entityNameProperty = v[0];
		final String attributeName;
		if (v.length == 2)
			attributeName = v[1];
		else
			attributeName = v[2];
		final String attributeNameProperty;
		if (v.length == 2)
			attributeNameProperty = attributeName;
		else
			attributeNameProperty = v[3];
		final PersistentClass cm;
		final Property prop;
		final Column col;
		 Property tempProp;
		boolean isFormula = false;

		if (oc instanceof ObjectConstraints)
			cm = null;
		else
			cm = DataStore.getConfiguration().getClassMapping(
					pkg + "." + entityName);


		tempProp = null;
		
		try
		{
			tempProp = cm != null ? cm.getProperty(attributeName) : null;
		}
		catch (MappingException me)
		{
			me.printStackTrace();
		}
		
		if (v.length == 2)
			prop = tempProp;
		else if (v.length == 4) {
			if (((Set) tempProp.getValue()).getElement() instanceof Component)
				prop = ((Component) ((Set) tempProp.getValue()).getElement())
						.getProperty(v[3]);
			else
				prop = tempProp;
		} else {
			prop = null;
		}

		Class toClass = null;
		if (prop != null) {
			if (prop.getValue().getClass() == org.hibernate.mapping.Set.class
					|| prop.getValue().getClass() == org.hibernate.mapping.Bag.class) {
				col = null;

				final Iterator fkItr = ((org.hibernate.mapping.Set) prop
						.getValue()).getCollectionTable()
						.getForeignKeyIterator();
				while (fkItr.hasNext()) {
					final ForeignKey fk = (ForeignKey) fkItr.next();
					
					// The from of the foreign key
					final String fromEntity = entityName;
					// The to of the foreign key
					final String toEntity = fk.getReferencedEntityName();
					try {
						if (!toEntity.equals(pkg + "." + entityName))
							toClass = Class.forName(toEntity);
					} catch (ClassNotFoundException cnfe) {

					}
				}
				isFormula = false;
			} else {
				final Iterator i = prop.getColumnIterator();
				if (!i.hasNext())
					throw new RuntimeException("No cols: " + f.getName());
				final Object next = i.next();
				if (next instanceof Column) {
					col = (Column) next;;
				} else if (next instanceof Formula) {
					col = null;
					isFormula = true;
				} else {
					throw new RuntimeException("Unrecgonized column format:"
							+ f.getName());
				}
				if (i.hasNext())
					throw new RuntimeException("Too many cols: " + f.getName());
			}
		} else {
			col = null;
		}

		PropertyConstraint pc = (PropertyConstraint) f.get(oc);
		if (pc == null) {
			pc = createPropertyConstraint(prop, f, toClass);
			pc.formula = isFormula;
			if (prop != null && prop.getValue() != null && prop.getValue() instanceof Set)
				pc.lazy = ((Set) prop.getValue()).isLazy();
			else
				pc.lazy = false;
			f.set(oc, pc);
		}

		pc.entityName = entityNameProperty;
		pc.propertyName = attributeNameProperty;
		if (v.length == 2)
			pc.property = property(clazz(cm, pc.entityName), pc.propertyName);
		else
			pc.property = property(clazz(null, v[0]), pc.propertyName);
		appendToAllArray(oc, pc);

		if (cm != null && col != null) {
			final ResultSet rs = md.getColumns(null, null, prop.getValue()
					.getTable().getName(), col.getName());
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
					// get the constraints from the database
					final SQLQuery q = open()
							.createSQLQuery(
									"select consrc from pg_constraint, pg_class where pg_class.relname='"
											+ prop.getValue().getTable()
													.getName()
											+ "' and pg_class.relfilenode=pg_constraint.conrelid and pg_constraint.contype='c' and conkey[1]=(select attnum from pg_attribute, pg_class where relname='"
											+ prop.getValue().getTable()
													.getName()
											+ "' and attname='" + col.getName()
											+ "' limit 1);");
					final Iterator<String> consItr = (Iterator<String>) q
							.list().iterator();
					while (consItr.hasNext()) {
						final String constraint = consItr.next();
						handleConstraint(constraint, (FloatConstraint) pc);
					}
				}

				if (rs.next())
					throw new RuntimeException("Too many cols: " + f.getName());
			} finally {
				rs.close();
			}
		}
	}
	private void handleConstraint(final String constraint,
			final FloatConstraint fc) {
		final String compareRegex = "[<]";
		final String numberRegex = "[><=]\\s*\\(([\\d\\.]*)\\).*";
		final Pattern numberPattern = Pattern.compile(numberRegex);
		final Matcher numberMatcher = numberPattern.matcher(constraint);
		final Pattern comparePattern = Pattern.compile(compareRegex);
		final Matcher compareMatcher = comparePattern.matcher(constraint);
		if (numberMatcher.find()) {
			final float number = Float.parseFloat(numberMatcher.group(1));
			if (!compareMatcher.find())
				fc.setMinValue(number);
			else
				fc.setMaxValue(number);
			fc.required = true;
		}
	}
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

	private PropertyConstraint createPropertyConstraint(final Property p,
			final Field f, final Class toClass) throws IllegalAccessException,
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
		else if (rc == Short.class)
			return ShortConstraint.class.isAssignableFrom(c) ? (PropertyConstraint) c
					.newInstance()
					: new ShortConstraint();
		else if (rc == Double.class)
			return DoubleConstraint.class.isAssignableFrom(c) ? (PropertyConstraint) c
					.newInstance()
					: new DoubleConstraint();
		else if (c == ValueInCollectionConstraint.class) {
			// We want to fetch the applicable values for the one end of a
			// Many-to-one
			// association
			ValueInCollectionConstraint cc;
			String queryName = className + ".all";
			if (ValueInCollectionConstraint.class.isAssignableFrom(c)) {
				cc = (ValueInCollectionConstraint) c.newInstance();
			} else {
				cc = new ValueInCollectionConstraint();
			}
			if (className.equals("Set")) {
				if (name.equals("elements"))
					queryName = "Element.all";
				else if (name.equals("oxides"))
					queryName = "Oxide.all";
				else if (name.equals("metamorphicGrades"))
					queryName = "MetamorphicGrades.all";
				else if (name.equals("metamorphicRegions"))
					queryName = "MetamorphicRegion.all";
			}
			final Session session = open();
			try {
				if (name.equals("mineral")) {
					final Collection<Mineral> minerals = session.getNamedQuery(
							queryName).list();
					new MineralDAO(session).initMinerals(minerals);
					cc.setValues((Collection<? extends MObject>) (hbm
							.clone(minerals)));
				} else {
					cc.setValues((Collection<? extends MObject>) (hbm
							.clone(session.getNamedQuery(queryName).list())));
				}
			} catch (Exception me) {
				cc.setValues(new HashSet<MObject>());
			} finally {
				session.close();
			}

			return cc;
		} else if ("timestamp".equals(tn)) {
			return TimestampConstraint.class.isAssignableFrom(c) ? (TimestampConstraint) c
					.newInstance()
					: new TimestampConstraint();
		} else if (rc == Boolean.class || "yes_no".equals(tn)) {
			return BooleanConstraint.class.isAssignableFrom(c) ? (BooleanConstraint) c
					.newInstance()
					: new BooleanConstraint();
		} else if (c == ObjectConstraint.class) {
			final Value v = p.getValue();
			final ObjectConstraint oc = new ObjectConstraint();
			if (v instanceof Set) {
				final Set s = (Set) v;
				final Class componentClass;
				if (s.getElement() instanceof Component) {
					final Component component = (Component) s.getElement();
					componentClass = component.getComponentClass();
				} else if (s.getElement() instanceof OneToMany) {
					componentClass = ((OneToMany) s.getElement())
							.getAssociatedClass().getMappedClass();
				} else {
					componentClass = toClass;
				}
				try {
					final Field componentField = DatabaseObjectConstraints.class
							.getField(componentClass.getSimpleName() + "__all");
					oc.setConstraints((PropertyConstraint[]) componentField
							.get(DataStore.databaseObjectConstraints));
				} catch (NoSuchFieldException nsfe) {
					throw new RuntimeException(
							"Unable to find the correct field("
									+ componentClass.getSimpleName() + "__all"
									+ ")  to set the constraints to");
				}

			}
			return oc;
		} else if (MObject.class.isAssignableFrom(rc))
			return (MObjectConstraint) c.newInstance();
		else
			throw new RuntimeException("Unsupported type for "
					+ p.getPersistentClass().getClassName() + " property "
					+ p.getName() + ".");
	}
	private static edu.rpi.metpetdb.client.model.properties.Property property(
			final Class who, final String name) throws IllegalAccessException {
		try {
			// Get the enum class
			final Class enumClass = Class
					.forName("edu.rpi.metpetdb.client.model.properties."
							+ who.getSimpleName() + "Property");
			return (edu.rpi.metpetdb.client.model.properties.Property) Enum
					.valueOf(enumClass, name);
		} catch (ClassNotFoundException nsfe) {
			throw new RuntimeException("Class not found " + name + " in "
					+ who.getName());
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
	
	/**
	 * Enables the security filters on a session so that when loading public
	 * data, private data belonging to other users is not loaded
	 * 
	 * @param session
	 * @param userId
	 */
	public static void enableSecurityFilters(final Session session, final int userId) {
		session.enableFilter("samplePublicOrUser").setParameter(
				"userId", userId);
		session.enableFilter("subsamplePublicOrUser").setParameter(
				"userId", userId);
		session.enableFilter("chemicalAnalysisPublicOrUser")
				.setParameter("userId", userId);
	}

	public static void disableSecurityFilters(final Session session) {
		session.disableFilter("samplePublicOrUser");
		session.disableFilter("subsamplePublicOrUser");
		session.disableFilter("chemicalAnalysisPublicOrUser");
	}
	protected DataStore() {
	}
}
