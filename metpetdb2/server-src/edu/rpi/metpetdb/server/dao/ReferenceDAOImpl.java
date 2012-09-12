package edu.rpi.metpetdb.server.dao;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.rpi.metpetdb.server.model.Georeference;
import edu.rpi.metpetdb.server.model.Reference;

public class ReferenceDAOImpl extends HibernateDaoSupport implements
		ReferenceDAO {

	@Override
	public void saveGeoreference(Reference reference) {

		this.getSession().save(reference);
		this.getSession().flush();
		this.getSession().clear();
	}

	@Override
	public Reference lookupReference(Georeference geoRef) {
		Query q = this.getSession().createQuery(
				"from Reference ref where ref.name = :refNum");
		q.setParameter("refNum", geoRef.getReferenceNumber());
		Reference ref = null;
		
		try
		{
			ref = (Reference) q.uniqueResult();
		}
		catch (HibernateException he)
		{
			throw new RuntimeException(he);
		}

		if (ref != null && ref.getGeoreference() != null) {
			this.getSession().merge(ref.getGeoreference());			
			this.getSession().delete(ref.getGeoreference());
			ref.setGeoreference(null);	
		}
		return ref;
	}
}
