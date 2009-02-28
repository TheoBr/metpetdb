package edu.rpi.metpetdb.server.impl;

import java.util.List;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.model.Grid;
import edu.rpi.metpetdb.client.model.ImageOnGrid;
import edu.rpi.metpetdb.client.service.ImageBrowserService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.impl.GridDAO;
import edu.rpi.metpetdb.server.dao.impl.ImageOnGridDAO;

public class ImageBrowserServiceImpl extends MpDbServlet implements
		ImageBrowserService {
	private static final long serialVersionUID = 1L;

	public Grid details(final long id) throws MpDbException {
		Grid g = new Grid();
		g.setId(new Long(id).intValue());

		g = (new GridDAO(this.currentSession())).fill(g);

		return (g);
	}

	public List<ImageOnGrid> imagesOnGrid(long id) throws MpDbException {
		List<ImageOnGrid> l = (new ImageOnGridDAO(this.currentSession()))
				.getImagesByGrid(id);
		return (l);
	}

	public Grid saveGrid(Grid grid) throws LoginRequiredException, MpDbException {
		grid = (new GridDAO(this.currentSession())).save(grid);
		commit();
		return grid;
	}
}
