package edu.rpi.metpetdb.client.model.properties;

import java.sql.Timestamp;
import java.util.Set;

import edu.rpi.metpetdb.client.model.ChemicalAnalysisDTO;
import edu.rpi.metpetdb.client.model.ElementDTO;
import edu.rpi.metpetdb.client.model.ImageDTO;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.MineralDTO;
import edu.rpi.metpetdb.client.model.OxideDTO;
import edu.rpi.metpetdb.client.model.ReferenceDTO;
import edu.rpi.metpetdb.client.model.SubsampleDTO;

public enum ChemicalAnalysisProperty implements Property {

	spotId {
		public <T extends MObjectDTO> String get(final T chemicalAnalysis) {
			return ((ChemicalAnalysisDTO) chemicalAnalysis).getSpotId();
		}

		public <T extends MObjectDTO, K> void set(final T chemicalAnalysis,
				final K spotId) {
			((ChemicalAnalysisDTO) chemicalAnalysis).setSpotId((String) spotId);
		}
	},
	pointX {
		public <T extends MObjectDTO> Integer get(final T chemicalAnalysis) {
			return ((ChemicalAnalysisDTO) chemicalAnalysis).getPointX();
		}

		public <T extends MObjectDTO, K> void set(final T chemicalAnalysis,
				final K pointX) {
			((ChemicalAnalysisDTO) chemicalAnalysis)
					.setPointX((Integer) pointX);
		}
	},
	pointY {
		public <T extends MObjectDTO> Integer get(final T chemicalAnalysis) {
			return ((ChemicalAnalysisDTO) chemicalAnalysis).getPointY();
		}

		public <T extends MObjectDTO, K> void set(final T chemicalAnalysis,
				final K pointY) {
			((ChemicalAnalysisDTO) chemicalAnalysis)
					.setPointY((Integer) pointY);
		}
	},
	analysisMethod {
		public <T extends MObjectDTO> String get(final T chemicalAnalysis) {
			return ((ChemicalAnalysisDTO) chemicalAnalysis).getAnalysisMethod();
		}

		public <T extends MObjectDTO, K> void set(final T chemicalAnalysis,
				final K analysisMethod) {
			((ChemicalAnalysisDTO) chemicalAnalysis)
					.setAnalysisMethod((String) analysisMethod);
		}
	},
	location {
		public <T extends MObjectDTO> String get(final T chemicalAnalysis) {
			return ((ChemicalAnalysisDTO) chemicalAnalysis).getLocation();
		}

		public <T extends MObjectDTO, K> void set(final T chemicalAnalysis,
				final K location) {
			((ChemicalAnalysisDTO) chemicalAnalysis)
					.setLocation((String) location);
		}
	},
	analyst {
		public <T extends MObjectDTO> String get(final T chemicalAnalysis) {
			return ((ChemicalAnalysisDTO) chemicalAnalysis).getAnalyst();
		}

		public <T extends MObjectDTO, K> void set(final T chemicalAnalysis,
				final K analyst) {
			((ChemicalAnalysisDTO) chemicalAnalysis)
					.setAnalyst((String) analyst);
		}
	},
	analysisDate {
		public <T extends MObjectDTO> Timestamp get(final T chemicalAnalysis) {
			return ((ChemicalAnalysisDTO) chemicalAnalysis).getAnalysisDate();
		}

		public <T extends MObjectDTO, K> void set(final T chemicalAnalysis,
				final K analysisDate) {
			((ChemicalAnalysisDTO) chemicalAnalysis)
					.setAnalysisDate((Timestamp) analysisDate);
		}
	},
	reference {
		public <T extends MObjectDTO> ReferenceDTO get(final T chemicalAnalysis) {
			return ((ChemicalAnalysisDTO) chemicalAnalysis).getReference();
		}

		public <T extends MObjectDTO, K> void set(final T chemicalAnalysis,
				final K reference) {
			((ChemicalAnalysisDTO) chemicalAnalysis)
					.setReference((ReferenceDTO) reference);
		}
	},
	description {
		public <T extends MObjectDTO> String get(final T chemicalAnalysis) {
			return ((ChemicalAnalysisDTO) chemicalAnalysis).getDescription();
		}

		public <T extends MObjectDTO, K> void set(final T chemicalAnalysis,
				final K description) {
			((ChemicalAnalysisDTO) chemicalAnalysis)
					.setDescription((String) description);
		}
	},
	mineral {
		public <T extends MObjectDTO> MineralDTO get(final T chemicalAnalysis) {
			return ((ChemicalAnalysisDTO) chemicalAnalysis).getMineral();
		}

		public <T extends MObjectDTO, K> void set(final T chemicalAnalysis,
				final K mineral) {
			((ChemicalAnalysisDTO) chemicalAnalysis)
					.setMineral((MineralDTO) mineral);
		}
	},
	subsample {
		public <T extends MObjectDTO> SubsampleDTO get(final T chemicalAnalysis) {
			return ((ChemicalAnalysisDTO) chemicalAnalysis).getSubsample();
		}

		public <T extends MObjectDTO, K> void set(final T chemicalAnalysis,
				final K subsample) {
			((ChemicalAnalysisDTO) chemicalAnalysis)
					.setSubsample((SubsampleDTO) subsample);
		}
	},
	image {
		public <T extends MObjectDTO> ImageDTO get(final T chemicalAnalysis) {
			return ((ChemicalAnalysisDTO) chemicalAnalysis).getImage();
		}

		public <T extends MObjectDTO, K> void set(final T chemicalAnalysis,
				final K image) {
			((ChemicalAnalysisDTO) chemicalAnalysis).setImage((ImageDTO) image);
		}
	},
	largeRock {
		public <T extends MObjectDTO> Boolean get(final T chemicalAnalysis) {
			return ((ChemicalAnalysisDTO) chemicalAnalysis).getLargeRock();
		}

		public <T extends MObjectDTO, K> void set(final T chemicalAnalysis,
				final K largeRock) {
			((ChemicalAnalysisDTO) chemicalAnalysis)
					.setLargeRock((Boolean) largeRock);
		}
	},
	elements {
		public <T extends MObjectDTO> Set<ElementDTO> get(
				final T chemicalAnalysis) {
			return ((ChemicalAnalysisDTO) chemicalAnalysis).getElements();
		}

		public <T extends MObjectDTO, K> void set(final T chemicalAnalysis,
				final K elements) {
			((ChemicalAnalysisDTO) chemicalAnalysis)
					.setElements((Set<ElementDTO>) elements);
		}
	},
	oxides {
		public <T extends MObjectDTO> Set<OxideDTO> get(final T chemicalAnalysis) {
			return ((ChemicalAnalysisDTO) chemicalAnalysis).getOxides();
		}

		public <T extends MObjectDTO, K> void set(final T chemicalAnalysis,
				final K oxides) {
			((ChemicalAnalysisDTO) chemicalAnalysis)
					.setOxides((Set<OxideDTO>) oxides);
		}
	};
}
