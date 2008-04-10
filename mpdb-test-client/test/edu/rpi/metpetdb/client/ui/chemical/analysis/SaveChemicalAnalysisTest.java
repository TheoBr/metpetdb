package edu.rpi.metpetdb.client.ui.chemical.analysis;

import java.util.HashSet;
import java.util.Set;

import edu.rpi.metpetdb.client.MpDbTestCase;
import edu.rpi.metpetdb.client.TestServerOp;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisDTO;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisElementDTO;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisOxideDTO;
import edu.rpi.metpetdb.client.model.ElementDTO;
import edu.rpi.metpetdb.client.model.OxideDTO;
import edu.rpi.metpetdb.client.ui.MpDb;

public class SaveChemicalAnalysisTest extends MpDbTestCase {

	private static final String SPOT_ID = String.valueOf(System
			.currentTimeMillis());
	private ChemicalAnalysisDTO chemicalAnalysis;

	@Override
	public void setUp() {
		super.setUp();
		chemicalAnalysis = new ChemicalAnalysisDTO();
		chemicalAnalysis.setSubsample(getSubsample());
		chemicalAnalysis.setSpotId(SPOT_ID);
		chemicalAnalysis.setLargeRock(Boolean.TRUE);

		final Set<ChemicalAnalysisElementDTO> elements = new HashSet<ChemicalAnalysisElementDTO>();
		final ChemicalAnalysisElementDTO element = new ChemicalAnalysisElementDTO();
		final ElementDTO e = new ElementDTO();
		e.setId((short) 1);
		element.setElement(e);
		element.setAmount(0.0f);
		elements.add(element);

		final Set<ChemicalAnalysisOxideDTO> oxides = new HashSet<ChemicalAnalysisOxideDTO>();
		final ChemicalAnalysisOxideDTO oxide = new ChemicalAnalysisOxideDTO();
		final OxideDTO o = new OxideDTO();
		o.setOxideId((short) 1);
		oxide.setOxide(o);
		oxide.setAmount(0.0f);
		oxides.add(oxide);
		chemicalAnalysis.setElements(elements);
		chemicalAnalysis.setOxides(oxides);
	}

	/**
	 * Test saving a sample that satisfies all of the constraints
	 */
	public void testSaveChemicalAnalysis() {
		new TestServerOp<ChemicalAnalysisDTO>(this) {
			public void begin() {
				MpDb.chemicalAnalysis_svc.save(chemicalAnalysis, this);
			}

			public void onSuccess(final ChemicalAnalysisDTO s) {
				assertEquals(s.getSpotId(), chemicalAnalysis.getSpotId());
				assertEquals(s.getElements().size(), 1);
				assertEquals(s.getOxides().size(), 1);
				finishTest();
			}
		}.begin();
		delayTestFinish(10000);
	}
}
