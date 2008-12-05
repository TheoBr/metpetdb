package edu.rpi.metpetdb.client.model.properties;

import java.sql.Timestamp;
import java.util.Set;

import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisElement;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisOxide;
import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.Mineral;
import edu.rpi.metpetdb.client.model.Reference;
import edu.rpi.metpetdb.client.model.User;

public enum ChemicalAnalysisProperty implements Property<ChemicalAnalysis> {

	spotId {
		public  String get(final ChemicalAnalysis chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).getSpotId();
		}

		public  void set(final ChemicalAnalysis chemicalAnalysis,
				final Object spotId) {
			((ChemicalAnalysis) chemicalAnalysis).setSpotId((String) spotId);
		}
	},
	pointX {
		public  Integer get(final ChemicalAnalysis chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).getPointX();
		}

		public  void set(final ChemicalAnalysis chemicalAnalysis,
				final Object pointX) {
			((ChemicalAnalysis) chemicalAnalysis).setPointX(Integer
					.valueOf(pointX.toString()));
		}
	},
	pointY {
		public  Integer get(final ChemicalAnalysis chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).getPointY();
		}

		public  void set(final ChemicalAnalysis chemicalAnalysis,
				final Object pointY) {
			((ChemicalAnalysis) chemicalAnalysis).setPointY(Integer
					.valueOf(pointY.toString()));
		}
	},
	analysisMethod {
		public  String get(final ChemicalAnalysis chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).getAnalysisMethod();
		}

		public  void set(final ChemicalAnalysis chemicalAnalysis,
				final Object analysisMethod) {
			((ChemicalAnalysis) chemicalAnalysis)
					.setAnalysisMethod((String) analysisMethod);
		}
	},
	location {
		public  String get(final ChemicalAnalysis chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).getLocation();
		}

		public  void set(final ChemicalAnalysis chemicalAnalysis,
				final Object location) {
			((ChemicalAnalysis) chemicalAnalysis)
					.setLocation((String) location);
		}
	},
	analyst {
		public  String get(final ChemicalAnalysis chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).getAnalyst();
		}

		public  void set(final ChemicalAnalysis chemicalAnalysis,
				final Object analyst) {
			((ChemicalAnalysis) chemicalAnalysis).setAnalyst((String) analyst);
		}
	},
	analysisDate {
		public  Timestamp get(final ChemicalAnalysis chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).getAnalysisDate();
		}

		public  void set(final ChemicalAnalysis chemicalAnalysis,
				final Object analysisDate) {
			((ChemicalAnalysis) chemicalAnalysis)
					.setAnalysisDate((Timestamp) analysisDate);
		}
	},
	reference {
		public  Reference get(final ChemicalAnalysis chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).getReference();
		}

		public  void set(final ChemicalAnalysis chemicalAnalysis,
				final Object ref) {
			final Reference reference;
			if (ref instanceof String) {
				reference = new Reference(ref.toString());
			} else if (ref instanceof Reference) {
				reference = (Reference) ref;
			} else {
				reference = null;
			}
			((ChemicalAnalysis) chemicalAnalysis).setReference(reference);
		}
	},
	description {
		public  String get(final ChemicalAnalysis chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).getDescription();
		}

		public  void set(final ChemicalAnalysis chemicalAnalysis,
				final Object description) {
			((ChemicalAnalysis) chemicalAnalysis)
					.setDescription((String) description);
		}
	},
	mineral {
		public  Mineral get(final ChemicalAnalysis chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).getMineral();
		}

		public  void set(final ChemicalAnalysis chemicalAnalysis,
				final Object mineral) {
			((ChemicalAnalysis) chemicalAnalysis).setMineral((Mineral) mineral);
		}
	},
	subsampleName {
		public  String get(final ChemicalAnalysis chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).getSubsampleName();
		}

		public  void set(final ChemicalAnalysis chemicalAnalysis,
				final Object subsample) {

		}
	},
	sampleName {
		public  String get(final ChemicalAnalysis chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).getSampleName();
		}

		public  void set(final ChemicalAnalysis chemicalAnalysis,
				final Object subsample) {
			// FIXME this should throw an exception
		}
	},
	image {
		public  Image get(final ChemicalAnalysis chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).getImage();
		}

		public  void set(final ChemicalAnalysis chemicalAnalysis,
				final Object image) {
			((ChemicalAnalysis) chemicalAnalysis).setImage((Image) image);
		}
	},
	largeRock {
		public  Boolean get(final ChemicalAnalysis chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).getLargeRock();
		}

		public  void set(final ChemicalAnalysis chemicalAnalysis,
				final Object largeRock) {
			((ChemicalAnalysis) chemicalAnalysis)
					.setLargeRock((Boolean) largeRock);
		}
	},
	elements {
		public  Set<ChemicalAnalysisElement> get(
				final ChemicalAnalysis chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).getElements();
		}

		public  void set(final ChemicalAnalysis chemicalAnalysis,
				final Object elements) {
			((ChemicalAnalysis) chemicalAnalysis)
					.setElements((Set<ChemicalAnalysisElement>) elements);
		}
	},
	oxides {
		public  Set<ChemicalAnalysisOxide> get(
				final ChemicalAnalysis chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).getOxides();
		}

		public  void set(final ChemicalAnalysis chemicalAnalysis,
				final Object oxides) {
			((ChemicalAnalysis) chemicalAnalysis)
					.setOxides((Set<ChemicalAnalysisOxide>) oxides);
		}
	},
	total {
		public  Float get(final ChemicalAnalysis chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).getTotal();
		}

		public  void set(final ChemicalAnalysis chemicalAnalysis,
				final Object total) {
			((ChemicalAnalysis) chemicalAnalysis).setTotal(Float.valueOf(total
					.toString()));
		}
	},
	datePrecision {
		public  Object get(final ChemicalAnalysis sample) {
			return ((ChemicalAnalysis) sample).getDatePrecision();
		}

		public  void set(final ChemicalAnalysis sample,
				final Object datePrecision) {
			((ChemicalAnalysis) sample).setDatePrecision(Short
					.parseShort(datePrecision.toString()));
		}
	},
	publicData {
		public  Boolean get(final ChemicalAnalysis chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).isPublicData();
		}

		public  void set(final ChemicalAnalysis chemicalAnalysis,
				final Object publicData) {
			((ChemicalAnalysis) chemicalAnalysis).setPublicData((Boolean) publicData);
		}
	},
	owner {
		public  User get(final ChemicalAnalysis chemicalAnalysis) {
			return ((ChemicalAnalysis) chemicalAnalysis).getOwner();
		}

		public  void set(final ChemicalAnalysis chemicalAnalysis, final Object owner) {
			((ChemicalAnalysis) chemicalAnalysis).setOwner((User) owner);
		}
	},
}
