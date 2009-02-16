package edu.rpi.metpetdb.server.dao.chemical.analysis;
import static org.junit.Assert.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import edu.rpi.metpetdb.client.error.DAOException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisOxide;
import edu.rpi.metpetdb.client.model.Element;
import edu.rpi.metpetdb.client.model.Oxide;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.server.DatabaseTestCase;
import edu.rpi.metpetdb.server.InitDatabase;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.bulk.upload.AnalysisParser;
import edu.rpi.metpetdb.server.dao.impl.ChemicalAnalysisDAO;
import edu.rpi.metpetdb.server.dao.impl.ElementDAO;
import edu.rpi.metpetdb.server.dao.impl.OxideDAO;

public class ChemicalAnalysisTest extends DatabaseTestCase {

	public ChemicalAnalysisTest() {
		super("test-data/test-sample-data.xml");
	}
	

	@Test
	public void testBug252() throws NoSuchObjectException,
			FileNotFoundException, MpDbException {
		List<Element> elements;
		List<Oxide> oxides;
		try {
			elements = ((new ElementDAO(InitDatabase.getSession())).getAll());
			oxides = ((new OxideDAO(InitDatabase.getSession())).getAll());
			AnalysisParser.setElements(elements);
			AnalysisParser.setOxides(oxides);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final AnalysisParser ap = new AnalysisParser(new FileInputStream(
				MpDbServlet.getFileUploadPath()
						+ "PrivateExampleUpload_analyses.xls"));
		final User owner = byId("User",1);
		ap.parse();
		final Map<Integer, ChemicalAnalysis> analyses = ap
				.getChemicalAnalyses();
		final ChemicalAnalysisDAO dao = new ChemicalAnalysisDAO(InitDatabase.getSession());
		int id = 0;
		InitDatabase.getSession().getTransaction().begin();
		for (Entry<Integer, ChemicalAnalysis> s : analyses.entrySet()) {
			s.getValue().getSubsample().getSample().setOwner(owner);
			s.getValue().setOwner(owner);
			id =dao.save(s.getValue()).getId();
		}
		InitDatabase.getSession().getTransaction().commit();
		
		InitDatabase.getSession().getTransaction().begin();
		final ChemicalAnalysis ca = byId("ChemicalAnalysis", id);
		ca.setDescription("testing123");
		for(ChemicalAnalysisOxide cao: ca.getOxides()) {
			cao.setMeasurementUnit(cao.getMeasurementUnit().toUpperCase());
			cao.setPrecisionUnit(cao.getPrecisionUnit().toUpperCase());
			cao.setPrecision(0.123321f);
		}
		System.err.println("BEFORE SAVE");
		dao.save(ca);
		InitDatabase.getSession().getTransaction().commit();
		
		final ChemicalAnalysis loadedCa = byId("ChemicalAnalysis", id);
		assertEquals("testing123", loadedCa.getDescription());
	}
}
