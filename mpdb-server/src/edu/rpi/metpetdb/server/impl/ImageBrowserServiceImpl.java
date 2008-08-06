package edu.rpi.metpetdb.server.impl;

import java.util.List;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.model.GridDTO;
import edu.rpi.metpetdb.client.model.ImageOnGridDTO;
import edu.rpi.metpetdb.client.service.ImageBrowserService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.impl.GridDAO;
import edu.rpi.metpetdb.server.dao.impl.ImageOnGridDAO;
import edu.rpi.metpetdb.server.model.Grid;
import edu.rpi.metpetdb.server.model.ImageOnGrid;

public class ImageBrowserServiceImpl extends MpDbServlet implements
		ImageBrowserService {
	private static final long serialVersionUID = 1L;

	public GridDTO details(final long id) throws DAOException {
		Grid g = new Grid();
		g.setId(new Long(id).intValue());

		g = (new GridDAO(this.currentSession())).fill(g);

		return cloneBean(g);
	}

	public List<ImageOnGridDTO> imagesOnGrid(long id) {
		List<ImageOnGrid> l = (new ImageOnGridDAO(this.currentSession()))
				.getImagesByGrid(id);
		return cloneBean(l);
	}

	public GridDTO saveGrid(GridDTO grid) throws LoginRequiredException,
			DAOException {
		if (grid.getSubsample().getSample().getOwner().getId() != currentUser())
			throw new SecurityException("Cannot modify grids you don't own.");
		Grid g = mergeBean(grid);

		g = (new GridDAO(this.currentSession())).save(g);
		commit();
		return cloneBean(g);
	}
}
