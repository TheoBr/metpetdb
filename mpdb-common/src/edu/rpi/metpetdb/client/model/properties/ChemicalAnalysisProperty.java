package edu.rpi.metpetdb.client.model.properties;

import java.sql.Timestamp;
import java.util.Set;

import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisElement;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisOxide;
import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.Mineral;
import edu.rpi.metpetdb.client.model.Reference;

public enum ChemicalAnalysisProperty implements Property {

	spotId {
		public <T extends MObject> String get(final T chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).getSpotId();
		}

		public <T extends MObject, K> void set(final T chemicalAnalysis,
				final K spotId) {
			((ChemicalAnalysis) chemicalAnalysis).setSpotId((String) spotId);
		}
	},
	pointX {
		public <T extends MObject> Integer get(final T chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).getPointX();
		}

		public <T extends MObject, K> void set(final T chemicalAnalysis,
				final K pointX) {
			((ChemicalAnalysis) chemicalAnalysis).setPointX(Integer
					.valueOf(pointX.toString()));
		}
	},
	pointY {
		public <T extends MObject> Integer get(final T chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).getPointY();
		}

		public <T extends MObject, K> void set(final T chemicalAnalysis,
				final K pointY) {
			((ChemicalAnalysis) chemicalAnalysis).setPointY(Integer
					.valueOf(pointY.toString()));
		}
	},
	analysisMethod {
		public <T extends MObject> String get(final T chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).getAnalysisMethod();
		}

		public <T extends MObject, K> void set(final T chemicalAnalysis,
				final K analysisMethod) {
			((ChemicalAnalysis) chemicalAnalysis)
					.setAnalysisMethod((String) analysisMethod);
		}
	},
	location {
		public <T extends MObject> String get(final T chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).getLocation();
		}

		public <T extends MObject, K> void set(final T chemicalAnalysis,
				final K location) {
			((ChemicalAnalysis) chemicalAnalysis)
					.setLocation((String) location);
		}
	},
	analyst {
		public <T extends MObject> String get(final T chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).getAnalyst();
		}

		public <T extends MObject, K> void set(final T chemicalAnalysis,
				final K analyst) {
			((ChemicalAnalysis) chemicalAnalysis).setAnalyst((String) analyst);
		}
	},
	analysisDate {
		public <T extends MObject> Timestamp get(final T chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).getAnalysisDate();
		}

		public <T extends MObject, K> void set(final T chemicalAnalysis,
				final K analysisDate) {
			((ChemicalAnalysis) chemicalAnalysis)
					.setAnalysisDate((Timestamp) analysisDate);
		}
	},
	reference {
		public <T extends MObject> Reference get(final T chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).getReference();
		}

		public <T extends MObject, K> void set(final T chemicalAnalysis,
				final K ref) {
			final Reference reference;
			if (ref instanceof String) {
				reference = new Reference();
				reference.setName(ref.toString());
			} else if (ref instanceof Reference) {
				reference = (Reference) ref;
			} else {
				reference = null;
			}
			((ChemicalAnalysis) chemicalAnalysis).setReference(reference);
		}
	},
	description {
		public <T extends MObject> String get(final T chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).getDescription();
		}

		public <T extends MObject, K> void set(final T chemicalAnalysis,
				final K description) {
			((ChemicalAnalysis) chemicalAnalysis)
					.setDescription((String) description);
		}
	},
	mineral {
		public <T extends MObject> Mineral get(final T chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).getMineral();
		}

		public <T extends MObject, K> void set(final T chemicalAnalysis,
				final K mineral) {
			((ChemicalAnalysis) chemicalAnalysis).setMineral((Mineral) mineral);
		}
	},
	subsampleName {
		public <T extends MObject> String get(final T chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).getSubsampleName();
		}

		public <T extends MObject, K> void set(final T chemicalAnalysis,
				final K subsample) {

		}
	},
	sampleName {
		public <T extends MObject> String get(final T chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).getSampleName();
		}

		public <T extends MObject, K> void set(final T chemicalAnalysis,
				final K subsample) {
			// FIXME this should throw an exception
		}
	},
	image {
		public <T extends MObject> Image get(final T chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).getImage();
		}

		public <T extends MObject, K> void set(final T chemicalAnalysis,
				final K image) {
			((ChemicalAnalysis) chemicalAnalysis).setImage((Image) image);
		}
	},
	largeRock {
		public <T extends MObject> Boolean get(final T chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).getLargeRock();
		}

		public <T extends MObject, K> void set(final T chemicalAnalysis,
				final K largeRock) {
			((ChemicalAnalysis) chemicalAnalysis)
					.setLargeRock((Boolean) largeRock);
		}
	},
	elements {
		public <T extends MObject> Set<ChemicalAnalysisElement> get(
				final T chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).getElements();
		}

		public <T extends MObject, K> void set(final T chemicalAnalysis,
				final K elements) {
			((ChemicalAnalysis) chemicalAnalysis)
					.setElements((Set<ChemicalAnalysisElement>) elements);
		}
	},
	oxides {
		public <T extends MObject> Set<ChemicalAnalysisOxide> get(
				final T chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).getOxides();
		}

		public <T extends MObject, K> void set(final T chemicalAnalysis,
				final K oxides) {
			((ChemicalAnalysis) chemicalAnalysis)
					.setOxides((Set<ChemicalAnalysisOxide>) oxides);
		}
	},
	total {
		public <T extends MObject> Float get(final T chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).getTotal();
		}

		public <T extends MObject, K> void set(final T chemicalAnalysis,
				final K total) {
			((ChemicalAnalysis) chemicalAnalysis).setTotal(Float.valueOf(total
					.toString()));
		}
	},
	datePrecision {
		public <T extends MObject> Object get(final T sample) {
			return ((ChemicalAnalysis) sample).getDatePrecision();
		}

		public <T extends MObject, K> void set(final T sample,
				final K datePrecision) {
			((ChemicalAnalysis) sample).setDatePrecision(Short
					.parseShort(datePrecision.toString()));
		}
	},
	publicData {
		public <T extends MObject> Boolean get(final T chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).isPublicData();
		}

		public <T extends MObject, K> void set(final T chemicalAnalysis,
				final K publicData) {
			((ChemicalAnalysis) chemicalAnalysis).setPublicData((Boolean) publicData);
		}
	},
	owner {
		public <T extends MObject> User get(final T chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).getOwner();
		}

		public <T extends MObject, K> void set(final T chemicalAnalysis, final K owner) {
			((ChemicalAnalysis) chemicalAnalysis).setOwner((User) owner);
		}
	},
}
