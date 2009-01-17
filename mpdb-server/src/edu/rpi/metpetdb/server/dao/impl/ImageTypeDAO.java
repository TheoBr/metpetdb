package edu.rpi.metpetdb.server.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.dao.FunctionNotImplementedException;
import edu.rpi.metpetdb.client.error.dao.ImageTypeNotFoundException;
import edu.rpi.metpetdb.client.model.Element;
import edu.rpi.metpetdb.client.model.ImageType;
import edu.rpi.metpetdb.server.dao.MpDbDAO;

public class ImageTypeDAO extends MpDbDAO<ImageType> {

	public ImageTypeDAO(Session session) {
		super(session);
	}

	@Override
	public ImageType delete(ImageType inst) throws DAOException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}

	@Override
	public ImageType fill(ImageType m) throws DAOException {
		// Use Image Type
		final Query imageTypes = namedQuery("ImageType.byImageType");
		imageTypes.setString("imageType", m.getImageType());
		if (imageTypes.uniqueResult() != null)
			return (ImageType) imageTypes.uniqueResult();

		throw new ImageTypeNotFoundException();
	}

	@Override
	public ImageType save(ImageType inst) throws DAOException {
		// TODO Auto-generated method stub
		throw new FunctionNotImplementedException();
	}
	
	public Object[] allImageTypes() throws DAOException{
		final Query q = namedQuery("ImageType.all/ImageType");
		return	((List<ImageType>)getResults(q)).toArray();
	}
	
	public List<ImageType> getAll() throws DAOException {
		final Query q = namedQuery("ImageType.all");
		return (List<ImageType>) getResults(q);
	}
}
