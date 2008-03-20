package edu.rpi.metpetdb.client.model.properties;

import java.sql.Timestamp;
import java.util.Set;

import org.postgis.Geometry;

import edu.rpi.metpetdb.client.model.ImageDTO;
import edu.rpi.metpetdb.client.model.MetamorphicGradeDTO;
import edu.rpi.metpetdb.client.model.ReferenceDTO;
import edu.rpi.metpetdb.client.model.RegionDTO;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.model.SampleMineralDTO;
import edu.rpi.metpetdb.client.model.UserDTO;

public enum SampleProperty implements Property {

	sesarNumber {
		public String get(final SampleDTO sample) {
			return sample.getSesarNumber();
		}

		public <T> void set(final SampleDTO sample, final T sesarNumber) {
			sample.setSesarNumber((String) sesarNumber);
		}
	},
	location {
		public Geometry get(final SampleDTO sample) {
			return sample.getLocation();
		}

		public <T> void set(final SampleDTO sample, final T sesarNumber) {
			sample.setSesarNumber((String) sesarNumber);
		}
	},
	owner {
		public UserDTO get(final SampleDTO sample) {
			return sample.getOwner();
		}

		public <T> void set(final SampleDTO sample, final T sesarNumber) {
			sample.setSesarNumber((String) sesarNumber);
		}
	},
	alias {
		public String get(final SampleDTO sample) {
			return sample.getAlias();
		}

		public <T> void set(final SampleDTO sample, final T sesarNumber) {
			sample.setSesarNumber((String) sesarNumber);
		}
	},
	collectionDate {
		public Timestamp get(final SampleDTO sample) {
			return sample.getCollectionDate();
		}

		public <T> void set(final SampleDTO sample, final T sesarNumber) {
			sample.setSesarNumber((String) sesarNumber);
		}
	},
	publicData {
		public Boolean get(final SampleDTO sample) {
			return sample.isPublicData();
		}

		public <T> void set(final SampleDTO sample, final T sesarNumber) {
			sample.setSesarNumber((String) sesarNumber);
		}
	},
	rockType {
		public String get(final SampleDTO sample) {
			return sample.getRockType();
		}

		public <T> void set(final SampleDTO sample, final T sesarNumber) {
			sample.setSesarNumber((String) sesarNumber);
		}
	},
	images {
		public Set<ImageDTO> get(final SampleDTO sample) {
			return sample.getImages();
		}

		public <T> void set(final SampleDTO sample, final T sesarNumber) {
			sample.setSesarNumber((String) sesarNumber);
		}
	},
	minerals {
		public Set<SampleMineralDTO> get(final SampleDTO sample) {
			return sample.getMinerals();
		}

		public <T> void set(final SampleDTO sample, final T sesarNumber) {
			sample.setSesarNumber((String) sesarNumber);
		}
	},
	country {
		public String get(final SampleDTO sample) {
			return sample.getCountry();
		}

		public <T> void set(final SampleDTO sample, final T sesarNumber) {
			sample.setSesarNumber((String) sesarNumber);
		}
	},
	description {
		public String get(final SampleDTO sample) {
			return sample.getDescription();
		}

		public <T> void set(final SampleDTO sample, final T sesarNumber) {
			sample.setSesarNumber((String) sesarNumber);
		}
	},
	collector {
		public String get(final SampleDTO sample) {
			return sample.getCollector();
		}

		public <T> void set(final SampleDTO sample, final T sesarNumber) {
			sample.setSesarNumber((String) sesarNumber);
		}
	},
	locationText {
		public String get(final SampleDTO sample) {
			return sample.getLocationText();
		}

		public <T> void set(final SampleDTO sample, final T sesarNumber) {
			sample.setSesarNumber((String) sesarNumber);
		}
	},
	latitudeError {
		public Float get(final SampleDTO sample) {
			return sample.getLatitudeError();
		}

		public <T> void set(final SampleDTO sample, final T sesarNumber) {
			sample.setSesarNumber((String) sesarNumber);
		}
	},
	longitudeError {
		public Float get(final SampleDTO sample) {
			return sample.getLongitudeError();
		}

		public <T> void set(final SampleDTO sample, final T sesarNumber) {
			sample.setSesarNumber((String) sesarNumber);
		}
	},
	regions {
		public Set<RegionDTO> get(final SampleDTO sample) {
			return sample.getRegions();
		}

		public <T> void set(final SampleDTO sample, final T sesarNumber) {
			sample.setSesarNumber((String) sesarNumber);
		}
	},
	metamorphicGrades {
		public Set<MetamorphicGradeDTO> get(final SampleDTO sample) {
			return sample.getMetamorphicGrades();
		}

		public <T> void set(final SampleDTO sample, final T sesarNumber) {
			sample.setSesarNumber((String) sesarNumber);
		}
	},
	references {
		public Set<ReferenceDTO> get(final SampleDTO sample) {
			return sample.getReferences();
		}

		public <T> void set(final SampleDTO sample, final T sesarNumber) {
			sample.setSesarNumber((String) sesarNumber);
		}
	},
	subsampleCount {
		public Integer get(final SampleDTO sample) {
			return sample.getSubsampleCount();
		}

		public <T> void set(final SampleDTO sample, final T sesarNumber) {
			sample.setSesarNumber((String) sesarNumber);
		}
	};

}
