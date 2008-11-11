package edu.rpi.metpetdb.client.model.properties;

import java.sql.Timestamp;
import java.util.Set;

import org.postgis.Geometry;

import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.MetamorphicGrade;
import edu.rpi.metpetdb.client.model.Reference;
import edu.rpi.metpetdb.client.model.Region;
import edu.rpi.metpetdb.client.model.RockType;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.SampleComment;
import edu.rpi.metpetdb.client.model.SampleMineral;
import edu.rpi.metpetdb.client.model.User;

public enum SampleProperty implements Property {

	sesarNumber {
		public <T extends MObject> String get(final T sample) {
			return ((Sample) sample).getSesarNumber();
		}

		public <T extends MObject, K> void set(final T sample,
				final K sesarNumber) {
			((Sample) sample).setSesarNumber((String) sesarNumber);
		}
	},
	location {
		public <T extends MObject> Geometry get(final T sample) {
			return ((Sample) sample).getLocation();
		}

		public <T extends MObject, K> void set(final T sample, final K geometry) {
			((Sample) sample).setLocation((Geometry) geometry);
		}
	},
	owner {
		public <T extends MObject> User get(final T sample) {
			return ((Sample) sample).getOwner();
		}

		public <T extends MObject, K> void set(final T sample, final K owner) {
			((Sample) sample).setOwner((User) owner);
		}
	},
	alias {
		public <T extends MObject> String get(final T sample) {
			return ((Sample) sample).getAlias();
		}

		public <T extends MObject, K> void set(final T sample, final K alias) {
			((Sample) sample).setAlias((String) alias);
		}
	},
	collectionDate {
		public <T extends MObject> Timestamp get(final T sample) {
			return ((Sample) sample).getCollectionDate();
		}

		public <T extends MObject, K> void set(final T sample,
				final K collectionDate) {
			((Sample) sample).setCollectionDate((Timestamp) collectionDate);
		}
	},
	publicData {
		public <T extends MObject> Boolean get(final T sample) {
			return ((Sample) sample).isPublicData();
		}

		public <T extends MObject, K> void set(final T sample,
				final K publicData) {
			((Sample) sample).setPublicData((Boolean) publicData);
		}
	},
	rockType {
		public <T extends MObject> Object get(final T sample) {
			return ((Sample) sample).getRockType();
		}

		public <T extends MObject, K> void set(final T sample, final K rockType) {
			((Sample) sample).setRockType((RockType) rockType);
		}
	},
	images {
		public <T extends MObject> Set<Image> get(final T sample) {
			return ((Sample) sample).getImages();
		}

		public <T extends MObject, K> void set(final T sample, final K images) {
			((Sample) sample).setImages((Set<Image>) images);
		}
	},
	minerals {
		public <T extends MObject> Set<SampleMineral> get(final T sample) {
			return ((Sample) sample).getMinerals();
		}

		public <T extends MObject, K> void set(final T sample, final K minerals) {
			((Sample) sample).setMinerals((Set<SampleMineral>) minerals);
		}
	},
	country {
		public <T extends MObject> String get(final T sample) {
			return ((Sample) sample).getCountry();
		}

		public <T extends MObject, K> void set(final T sample, final K country) {
			((Sample) sample).setCountry((String) country);
		}
	},
	description {
		public <T extends MObject> String get(final T sample) {
			return ((Sample) sample).getDescription();
		}

		public <T extends MObject, K> void set(final T sample,
				final K description) {
			((Sample) sample).setDescription((String) description);
		}
	},
	collector {
		public <T extends MObject> String get(final T sample) {
			return ((Sample) sample).getCollector();
		}

		public <T extends MObject, K> void set(final T sample, final K collector) {
			((Sample) sample).setCollector((String) collector);
		}
	},
	locationText {
		public <T extends MObject> String get(final T sample) {
			return ((Sample) sample).getLocationText();
		}

		public <T extends MObject, K> void set(final T sample,
				final K locationText) {
			((Sample) sample).setLocationText((String) locationText);
		}
	},
	latitudeError {
		public <T extends MObject> Float get(final T sample) {
			return ((Sample) sample).getLatitudeError();
		}

		public <T extends MObject, K> void set(final T sample,
				final K latitudeError) {
			((Sample) sample).setLatitudeError(PropertyUtils
					.convertToFloat(latitudeError));
		}
	},
	longitudeError {
		public <T extends MObject> Float get(final T sample) {
			return ((Sample) sample).getLongitudeError();
		}

		public <T extends MObject, K> void set(final T sample,
				final K longitudeError) {
			((Sample) sample).setLongitudeError(PropertyUtils
					.convertToFloat(longitudeError));
		}
	},
	regions {
		public <T extends MObject> Set<Region> get(final T sample) {
			return ((Sample) sample).getRegions();
		}

		public <T extends MObject, K> void set(final T sample, final K regions) {
			((Sample) sample).setRegions((Set<Region>) regions);
		}
	},
	metamorphicGrades {
		public <T extends MObject> Set<MetamorphicGrade> get(final T sample) {
			return ((Sample) sample).getMetamorphicGrades();
		}

		public <T extends MObject, K> void set(final T sample,
				final K metamorphicGrades) {
			((Sample) sample)
					.setMetamorphicGrades((Set<MetamorphicGrade>) metamorphicGrades);
		}
	},
	references {
		public <T extends MObject> Set<Reference> get(final T sample) {
			return ((Sample) sample).getReferences();
		}

		public <T extends MObject, K> void set(final T sample,
				final K references) {
			((Sample) sample).setReferences((Set<Reference>) references);
		}
	},
	comments {
		public <T extends MObject> Object get(final T sample) {
			return ((Sample) sample).getComments();
		}

		public <T extends MObject, K> void set(final T sample, final K comments) {
			((Sample) sample).setComments((Set<SampleComment>) comments);
		}
	},
	subsampleCount {
		public <T extends MObject> Integer get(final T sample) {
			return ((Sample) sample).getSubsampleCount();
		}

		public <T extends MObject, K> void set(final T sample,
				final K subsampleCount) {
			((Sample) sample).setSubsampleCount((Integer) subsampleCount);
		}
	},
	imageCount {
		public <T extends MObject> Integer get(final T sample) {
			return ((Sample) sample).getImageCount();
		}

		public <T extends MObject, K> void set(final T sample,
				final K subsampleCount) {
			((Sample) sample).setImageCount((Integer) subsampleCount);
		}
	},
	analysisCount {
		public <T extends MObject> Integer get(final T sample) {
			return ((Sample) sample).getAnalysisCount();
		}

		public <T extends MObject, K> void set(final T sample,
				final K subsampleCount) {
			((Sample) sample).setAnalysisCount((Integer) subsampleCount);
		}
	},
	datePrecision {
		public <T extends MObject> Object get(final T sample) {
			return ((Sample) sample).getDatePrecision();
		}

		public <T extends MObject, K> void set(final T sample,
				final K datePrecision) {
			((Sample) sample).setDatePrecision(Short.parseShort(datePrecision
					.toString()));
		}
	};

}
