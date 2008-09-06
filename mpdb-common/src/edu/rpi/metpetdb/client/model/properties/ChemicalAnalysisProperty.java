package edu.rpi.metpetdb.client.model.properties;

import java.sql.Timestamp;
import java.util.Set;

import edu.rpi.metpetdb.client.model.ChemicalAnalysisDTO;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisElementDTO;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisOxideDTO;
import edu.rpi.metpetdb.client.model.ImageDTO;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.MineralDTO;
import edu.rpi.metpetdb.client.model.ReferenceDTO;

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
			((ChemicalAnalysisDTO) chemicalAnalysis).setPointX(Integer
					.valueOf(pointX.toString()));
		}
	},
	pointY {
		public <T extends MObjectDTO> Integer get(final T chemicalAnalysis) {
			return ((ChemicalAnalysisDTO) chemicalAnalysis).getPointY();
		}

		public <T extends MObjectDTO, K> void set(final T chemicalAnalysis,
				final K pointY) {
			((ChemicalAnalysisDTO) chemicalAnalysis).setPointY(Integer
					.valueOf(pointY.toString()));
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
				final K ref) {
			final ReferenceDTO reference;
			if (ref instanceof String) {
				reference = new ReferenceDTO();
				reference.setName(ref.toString());
			} else if (ref instanceof ReferenceDTO) {
				reference = (ReferenceDTO) ref;
			} else {
				reference = null;
			}
			((ChemicalAnalysisDTO) chemicalAnalysis).setReference(reference);
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
	subsampleName {
		public <T extends MObjectDTO> String get(final T chemicalAnalysis) {
			return ((ChemicalAnalysisDTO) chemicalAnalysis).getSubsampleName();
		}

		public <T extends MObjectDTO, K> void set(final T chemicalAnalysis,
				final K subsample) {

		}
	},
	sampleName {
		public <T extends MObjectDTO> String get(final T chemicalAnalysis) {
			return ((ChemicalAnalysisDTO) chemicalAnalysis).getSampleName();
		}

		public <T extends MObjectDTO, K> void set(final T chemicalAnalysis,
				final K subsample) {
			// FIXME this should throw an exception
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
		public <T extends MObjectDTO> Set<ChemicalAnalysisElementDTO> get(
				final T chemicalAnalysis) {
			return ((ChemicalAnalysisDTO) chemicalAnalysis).getElements();
		}

		public <T extends MObjectDTO, K> void set(final T chemicalAnalysis,
				final K elements) {
			((ChemicalAnalysisDTO) chemicalAnalysis)
					.setElements((Set<ChemicalAnalysisElementDTO>) elements);
		}
	},
	oxides {
		public <T extends MObjectDTO> Set<ChemicalAnalysisOxideDTO> get(
				final T chemicalAnalysis) {
			return ((ChemicalAnalysisDTO) chemicalAnalysis).getOxides();
		}

		public <T extends MObjectDTO, K> void set(final T chemicalAnalysis,
				final K oxides) {
			((ChemicalAnalysisDTO) chemicalAnalysis)
					.setOxides((Set<ChemicalAnalysisOxideDTO>) oxides);
		}
	},
	total {
		public <T extends MObjectDTO> Float get(final T chemicalAnalysis) {
			return ((ChemicalAnalysisDTO) chemicalAnalysis).getTotal();
		}

		public <T extends MObjectDTO, K> void set(final T chemicalAnalysis,
				final K total) {
			((ChemicalAnalysisDTO) chemicalAnalysis).setTotal(Float
					.valueOf(total.toString()));
		}
	},
	datePrecision {
		public <T extends MObjectDTO> Object get(final T sample) {
			return ((ChemicalAnalysisDTO) sample).getDatePrecision();
		}

		public <T extends MObjectDTO, K> void set(final T sample,
				final K datePrecision) {
			((ChemicalAnalysisDTO) sample).setDatePrecision(Short
					.parseShort(datePrecision.toString()));
		}
	};
}
