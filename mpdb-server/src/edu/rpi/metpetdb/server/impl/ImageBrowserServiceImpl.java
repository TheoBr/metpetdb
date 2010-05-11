package edu.rpi.metpetdb.server.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.Grid;
import edu.rpi.metpetdb.client.model.ImageOnGrid;
import edu.rpi.metpetdb.client.service.ImageBrowserService;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.dao.impl.ChemicalAnalysisDAO;
import edu.rpi.metpetdb.server.dao.impl.GridDAO;
import edu.rpi.metpetdb.server.dao.impl.ImageOnGridDAO;

public class ImageBrowserServiceImpl extends MpDbServlet implements
		ImageBrowserService {
	private static  long serialVersionUID = 1L;

	public Grid details(final long id) throws MpDbException {
		Grid g = new Grid();
		g.setId(new Long(id).intValue());

		g = (new GridDAO(this.currentSession())).fill(g);

		return (g);
	}
	
	public List<Grid> allFromManySubsamples(final Collection<Long> subsampleIds) throws MpDbException {
		final List<Grid> l = new ArrayList<Grid>();
		for (Long id : subsampleIds){
			final Grid g = (new GridDAO(this
					.currentSession())).get(id);
			if (g != null){
				l.add(g);
			}
		}
		return (l);
	}

	public List<ImageOnGrid> imagesOnGrid(long id) throws MpDbException {
		List<ImageOnGrid> l = (new ImageOnGridDAO(this.currentSession()))
				.getImagesByGrid(id);
		return (l);
	}

	public Grid saveGrid(Grid grid) throws LoginRequiredException, MpDbException {
		for (ChemicalAnalysis ca : grid.getSubsample().getChemicalAnalyses()){
			ca = (new ChemicalAnalysisDAO(this.currentSession())).save(ca);
		}
	    grid = (new GridDAO(this.currentSession())).save(grid);
		commit();
		return grid;
	}
	
	public void saveAll(Collection<Grid> grids)
			throws ValidationException, LoginRequiredException, MpDbException {
		final GridDAO dao = new GridDAO(this.currentSession());
		for(Grid grid : grids) {
			dao.save(grid);
		}
		commit();
	}
}
