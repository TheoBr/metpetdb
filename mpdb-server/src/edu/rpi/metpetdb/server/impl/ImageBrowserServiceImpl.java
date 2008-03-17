package edu.rpi.metpetdb.server.impl;

import java.util.List;

import org.gwtwidgets.client.ui.pagination.PaginationParameters;
import org.gwtwidgets.client.ui.pagination.Results;
import org.hibernate.Query;
import org.hibernate.exception.ConstraintViolationException;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.model.GridDTO;
import edu.rpi.metpetdb.client.model.ImageOnGridDTO;
import edu.rpi.metpetdb.client.service.ImageBrowserService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.model.Grid;

public class ImageBrowserServiceImpl extends MpDbServlet
		implements
			ImageBrowserService {
	private static final long serialVersionUID = 1L;

	public Results bySampleId(long id, PaginationParameters p)
			throws NoSuchObjectException {
		final String name = "Grid.bySampleId";
		final Query sizeQuery = sizeQuery(name);
		final Query pageQuery = pageQuery(name, p);
		sizeQuery.setLong("id", id);
		pageQuery.setLong("id", id);
		return toResults(sizeQuery, pageQuery);
	}

	public GridDTO details(final long id) throws NoSuchObjectException {
		final Grid g = byId("Grid", id);
		return cloneBean(g);
	}

	public List<ImageOnGridDTO> imagesOnGrid(long id) throws NoSuchObjectException {
		return cloneBean(byKey("ImageOnGrid", "gridId", id));
	}

	public GridDTO saveGrid(GridDTO grid) throws LoginRequiredException {
		if (grid.getSubsample().getSample().getOwner().getId() != currentUser())
			throw new SecurityException("Cannot modify grids you don't own.");
		Grid g = mergeBean(grid);
		try {
			if (g.mIsNew())
				insert(g);
			else
				g = (Grid) update(merge(g));
			commit();
			return cloneBean(g);
		} catch (ConstraintViolationException cve) {
			throw cve;
		}
	}
	
	@Deprecated
	public static final void resetGrid(final Grid g) {
		g.setImagesOnGrid(null);
	}
}
