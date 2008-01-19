package edu.rpi.metpetdb.server.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.gwtwidgets.client.ui.pagination.PaginationParameters;
import org.gwtwidgets.client.ui.pagination.Results;
import org.hibernate.Query;
import org.hibernate.exception.ConstraintViolationException;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.model.Grid;
import edu.rpi.metpetdb.client.model.ImageOnGrid;
import edu.rpi.metpetdb.client.service.ImageBrowserService;
import edu.rpi.metpetdb.server.MpDbServlet;

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

	@SuppressWarnings("unchecked")
	public Grid details(final long id) throws NoSuchObjectException {
		final Grid g = (Grid) byId("Grid", id);
		g.setImagesOnGrid(load(g.getImagesOnGrid()));
		g.getSubsample().setMineralAnalyses(
				load(g.getSubsample().getMineralAnalyses()));
		forgetChanges();
		return g;
	}

	@SuppressWarnings("unchecked")
	public ArrayList imagesOnGrid(long id) throws NoSuchObjectException {
		return (ArrayList) byKeySet("ImageOnGrid", "gridId", id);
	}

	@SuppressWarnings("unchecked")
	public Grid saveGrid(Grid g) throws LoginRequiredException {
		if (g.getSubsample().getSample().getOwner().getId() != currentUser())
			throw new SecurityException("Cannot modify grids you don't own.");

		try {
			if (g.mIsNew())
				insert(g);
			else
				g = (Grid) update(merge(g));
			commit();
			ImageOnGrid imageOnGrid;
			if (g.getImagesOnGrid() != null) {
				final HashSet imagesOnGridLoaded = (HashSet) load(g
						.getImagesOnGrid());
				final Iterator itr = imagesOnGridLoaded.iterator();
				while (itr.hasNext()) {
					imageOnGrid = (ImageOnGrid) itr.next();
					SampleServiceImpl.resetSample(imageOnGrid.getImage()
							.getSubsample().getSample());
					SubsampleServiceImpl.resetSubsample(imageOnGrid.getImage()
							.getSubsample());
				}
				g.setImagesOnGrid(imagesOnGridLoaded);
			}
			SampleServiceImpl.resetSample(g.getSubsample().getSample());
			g.getSubsample().setImages(null);
			g.getSubsample().setMineralAnalyses(
					load(g.getSubsample().getMineralAnalyses()));
			forgetChanges();
			return g;
		} catch (ConstraintViolationException cve) {
			throw cve;
		}
	}
	
	public static final void resetGrid(final Grid g) {
		g.setImagesOnGrid(null);
	}
}
