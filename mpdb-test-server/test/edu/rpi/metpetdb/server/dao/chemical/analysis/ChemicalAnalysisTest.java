package edu.rpi.metpetdb.server.dao.chemical.analysis;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.rpi.metpetdb.client.error.NoSuchObjectException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.server.DatabaseTestCase;
import edu.rpi.metpetdb.server.MpDbServlet;
import edu.rpi.metpetdb.server.security.permissions.principals.OwnerPrincipal;

public class ChemicalAnalysisTest extends DatabaseTestCase {

	public ChemicalAnalysisTest() {
		super("test-data/test-sample-data.xml");
	}

	@Before
	public void setUp() {
		super.setUp();
	}

	@Test
	public void sampleName() throws NoSuchObjectException {
		final ChemicalAnalysis ca = super.byId("ChemicalAnalysis",
				PUBLIC_CHEMICAL_ANALYSIS);
		assertEquals("testing public", ca.getSampleName());
	}

	@Test
	public void subsampleName() throws NoSuchObjectException {
		final ChemicalAnalysis ca = super.byId("ChemicalAnalysis",
				PUBLIC_CHEMICAL_ANALYSIS);
		assertEquals("temp4", ca.getSubsampleName());
	}

	/**
	 * Tests loading only the public chemical analysis
	 */
	@Test
	public void publicAll() {
		session.enableFilter("chemicalAnalysisPublicOrUser").setParameter("userId", 0l);
		final List<ChemicalAnalysis> cas = session.getNamedQuery(
				"ChemicalAnalysis.bySubsampleId").setParameter("id",
				PUBLIC_SUBSAMPLE).list();
		assertEquals(1, cas.size());
	}

	/**
	 * Tests loading all of the chemical analyses that belong to the current
	 * user or if they are public
	 * 
	 * @throws NoSuchObjectException
	 */
	@Test
	public void privateAll() throws NoSuchObjectException {
		MpDbServlet.currentReq().user = super.byId("User", 1);
		MpDbServlet.currentReq().principals.add(new OwnerPrincipal(MpDbServlet
				.currentReq().user));
		session.enableFilter("chemicalAnalysisPublicOrUser").setParameter("userId", 1l);
		final List<ChemicalAnalysis> cas = session.getNamedQuery(
				"ChemicalAnalysis.bySubsampleId").setParameter("id",
				PUBLIC_SUBSAMPLE).list();
		assertEquals(3, cas.size());
	}
}
