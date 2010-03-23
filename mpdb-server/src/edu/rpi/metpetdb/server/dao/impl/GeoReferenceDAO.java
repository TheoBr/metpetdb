package edu.rpi.metpetdb.server.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javassist.bytecode.Descriptor.Iterator;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.dao.FunctionNotImplementedException;
import edu.rpi.metpetdb.client.error.dao.ReferenceNotFoundException;
import edu.rpi.metpetdb.client.model.GeoReference;
import edu.rpi.metpetdb.server.dao.MpDbDAO;

public class GeoReferenceDAO extends MpDbDAO<GeoReference>{
	public GeoReferenceDAO(Session session) {
		super(session);
	}
	public long getCount(){
		final Query q = namedQuery("GeoReference.Count");
		return (Long) q.uniqueResult();
	}
	@Override
	public GeoReference delete(GeoReference inst) throws MpDbException,
			HibernateException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}
	@Override
	public GeoReference fill(GeoReference ref) throws MpDbException,
			HibernateException {
		// Use Reference Name
        final org.hibernate.Query geoRefrences = namedQuery("GeoReference.byId");
        geoRefrences.setShort("id", ref.getId());
        if (geoRefrences.uniqueResult() != null)
                return (GeoReference) geoRefrences.uniqueResult();

        throw new ReferenceNotFoundException();
	}
	@Override
	public GeoReference save(GeoReference inst) throws MpDbException,
			HibernateException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}
	
	public Object[] allReferences()throws MpDbException {
        final Query q = namedQuery("GeoReference.all/title");
        return ((List<GeoReference>)getResults(q)).toArray();
    }
	
	public ArrayList<GeoReference> referencesByNumber(ArrayList<String> refNums) throws MpDbException {
		final Query q = namedQuery("GeoReference.byNumber");
		ArrayList<GeoReference> results = new ArrayList<GeoReference>();
		java.util.Iterator<String> refItr = refNums.iterator();
		while(refItr.hasNext()){
			q.setString("number", refItr.next());
			GeoReference currentRef = (GeoReference) getResult(q);
			if(currentRef != null){
				results.add(currentRef);
			}
		}
		
		return results;
	}
}
