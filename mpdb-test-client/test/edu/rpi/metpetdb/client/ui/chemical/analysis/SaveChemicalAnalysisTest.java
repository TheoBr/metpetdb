package edu.rpi.metpetdb.client.ui.chemical.analysis;

import java.util.HashSet;
import java.util.Set;

import edu.rpi.metpetdb.client.MpDbTestCase;
import edu.rpi.metpetdb.client.TestServerOp;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisElement;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisOxide;
import edu.rpi.metpetdb.client.model.Element;
import edu.rpi.metpetdb.client.model.Oxide;
import edu.rpi.metpetdb.client.ui.MpDb;

public class SaveChemicalAnalysisTest extends MpDbTestCase {

	private static final String SPOT_ID = String.valueOf(System
			.currentTimeMillis());
	private ChemicalAnalysis chemicalAnalysis;

	@Override
	public void gwtSetUp() {
		chemicalAnalysis = new ChemicalAnalysis();
		chemicalAnalysis.setSubsample(getSubsample());
		chemicalAnalysis.setSpotId(SPOT_ID);
		chemicalAnalysis.setLargeRock(Boolean.TRUE);

		final Set<ChemicalAnalysisElement> elements = new HashSet<ChemicalAnalysisElement>();
		final ChemicalAnalysisElement element = new ChemicalAnalysisElement();
		final Element e = new Element();
		e.setId((short) 1);
		element.setElement(e);
		element.setAmount(0.0);
		elements.add(element);

		final Set<ChemicalAnalysisOxide> oxides = new HashSet<ChemicalAnalysisOxide>();
		final ChemicalAnalysisOxide oxide = new ChemicalAnalysisOxide();
		final Oxide o = new Oxide();
		o.setOxideId((short) 1);
		oxide.setOxide(o);
		oxide.setAmount(0.0);
		oxides.add(oxide);
		chemicalAnalysis.setElements(elements);
		chemicalAnalysis.setOxides(oxides);
	}

	/**
	 * Test saving a sample that satisfies all of the constraints
	 */
	public void testSaveChemicalAnalysis() {
		new TestServerOp<ChemicalAnalysis>(this) {
			public void begin() {
				MpDb.chemicalAnalysis_svc.save(chemicalAnalysis, this);
			}

			public void onSuccess(final ChemicalAnalysis s) {
				assertEquals(s.getSpotId(), chemicalAnalysis.getSpotId());
				assertEquals(s.getElements().size(), 1);
				assertEquals(s.getOxides().size(), 1);
				finishTest();
			}
		}.begin();
		delayTestFinish(10000);
	}
}
