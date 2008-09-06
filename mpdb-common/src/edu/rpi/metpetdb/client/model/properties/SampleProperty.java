package edu.rpi.metpetdb.client.model.properties;

import java.sql.Timestamp;
import java.util.Set;

import org.postgis.Geometry;

import edu.rpi.metpetdb.client.model.ImageDTO;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.MetamorphicGradeDTO;
import edu.rpi.metpetdb.client.model.ReferenceDTO;
import edu.rpi.metpetdb.client.model.RegionDTO;
import edu.rpi.metpetdb.client.model.RockTypeDTO;
import edu.rpi.metpetdb.client.model.SampleCommentDTO;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.model.SampleMineralDTO;
import edu.rpi.metpetdb.client.model.UserDTO;

public enum SampleProperty implements Property {

	sesarNumber {
		public <T extends MObjectDTO> String get(final T sample) {
			return ((SampleDTO) sample).getSesarNumber();
		}

		public <T extends MObjectDTO, K> void set(final T sample,
				final K sesarNumber) {
			((SampleDTO) sample).setSesarNumber((String) sesarNumber);
		}
	},
	location {
		public <T extends MObjectDTO> Geometry get(final T sample) {
			return ((SampleDTO) sample).getLocation();
		}

		public <T extends MObjectDTO, K> void set(final T sample,
				final K geometry) {
			((SampleDTO) sample).setLocation((Geometry) geometry);
		}
	},
	owner {
		public <T extends MObjectDTO> UserDTO get(final T sample) {
			return ((SampleDTO) sample).getOwner();
		}

		public <T extends MObjectDTO, K> void set(final T sample, final K owner) {
			((SampleDTO) sample).setOwner((UserDTO) owner);
		}
	},
	alias {
		public <T extends MObjectDTO> String get(final T sample) {
			return ((SampleDTO) sample).getAlias();
		}

		public <T extends MObjectDTO, K> void set(final T sample, final K alias) {
			((SampleDTO) sample).setAlias((String) alias);
		}
	},
	collectionDate {
		public <T extends MObjectDTO> Timestamp get(final T sample) {
			return ((SampleDTO) sample).getCollectionDate();
		}

		public <T extends MObjectDTO, K> void set(final T sample,
				final K collectionDate) {
			((SampleDTO) sample).setCollectionDate((Timestamp) collectionDate);
		}
	},
	publicData {
		public <T extends MObjectDTO> Boolean get(final T sample) {
			return ((SampleDTO) sample).isPublicData();
		}

		public <T extends MObjectDTO, K> void set(final T sample,
				final K publicData) {
			((SampleDTO) sample).setPublicData((Boolean) publicData);
		}
	},
	rockType {
		public <T extends MObjectDTO> Object get(final T sample) {
			return ((SampleDTO) sample).getRockType();
		}

		public <T extends MObjectDTO, K> void set(final T sample,
				final K rockType) {
			((SampleDTO) sample).setRockType((RockTypeDTO) rockType);
		}
	},
	images {
		public <T extends MObjectDTO> Set<ImageDTO> get(final T sample) {
			return ((SampleDTO) sample).getImages();
		}

		public <T extends MObjectDTO, K> void set(final T sample, final K images) {
			((SampleDTO) sample).setImages((Set<ImageDTO>) images);
		}
	},
	minerals {
		public <T extends MObjectDTO> Set<SampleMineralDTO> get(final T sample) {
			return ((SampleDTO) sample).getMinerals();
		}

		public <T extends MObjectDTO, K> void set(final T sample,
				final K minerals) {
			((SampleDTO) sample).setMinerals((Set<SampleMineralDTO>) minerals);
		}
	},
	country {
		public <T extends MObjectDTO> String get(final T sample) {
			return ((SampleDTO) sample).getCountry();
		}

		public <T extends MObjectDTO, K> void set(final T sample,
				final K country) {
			((SampleDTO) sample).setCountry((String) country);
		}
	},
	description {
		public <T extends MObjectDTO> String get(final T sample) {
			return ((SampleDTO) sample).getDescription();
		}

		public <T extends MObjectDTO, K> void set(final T sample,
				final K description) {
			((SampleDTO) sample).setDescription((String) description);
		}
	},
	collector {
		public <T extends MObjectDTO> String get(final T sample) {
			return ((SampleDTO) sample).getCollector();
		}

		public <T extends MObjectDTO, K> void set(final T sample,
				final K collector) {
			((SampleDTO) sample).setCollector((String) collector);
		}
	},
	locationText {
		public <T extends MObjectDTO> String get(final T sample) {
			return ((SampleDTO) sample).getLocationText();
		}

		public <T extends MObjectDTO, K> void set(final T sample,
				final K locationText) {
			((SampleDTO) sample).setLocationText((String) locationText);
		}
	},
	latitudeError {
		public <T extends MObjectDTO> Float get(final T sample) {
			return ((SampleDTO) sample).getLatitudeError();
		}

		public <T extends MObjectDTO, K> void set(final T sample,
				final K latitudeError) {
			((SampleDTO) sample).setLatitudeError(PropertyUtils
					.convertToFloat(latitudeError));
		}
	},
	longitudeError {
		public <T extends MObjectDTO> Float get(final T sample) {
			return ((SampleDTO) sample).getLongitudeError();
		}

		public <T extends MObjectDTO, K> void set(final T sample,
				final K longitudeError) {
			((SampleDTO) sample).setLongitudeError(PropertyUtils
					.convertToFloat(longitudeError));
		}
	},
	regions {
		public <T extends MObjectDTO> Set<RegionDTO> get(final T sample) {
			return ((SampleDTO) sample).getRegions();
		}

		public <T extends MObjectDTO, K> void set(final T sample,
				final K regions) {
			((SampleDTO) sample).setRegions((Set<RegionDTO>) regions);
		}
	},
	metamorphicGrades {
		public <T extends MObjectDTO> Set<MetamorphicGradeDTO> get(
				final T sample) {
			return ((SampleDTO) sample).getMetamorphicGrades();
		}

		public <T extends MObjectDTO, K> void set(final T sample,
				final K metamorphicGrades) {
			((SampleDTO) sample)
					.setMetamorphicGrades((Set<MetamorphicGradeDTO>) metamorphicGrades);
		}
	},
	references {
		public <T extends MObjectDTO> Set<ReferenceDTO> get(final T sample) {
			return ((SampleDTO) sample).getReferences();
		}

		public <T extends MObjectDTO, K> void set(final T sample,
				final K references) {
			((SampleDTO) sample).setReferences((Set<ReferenceDTO>) references);
		}
	},
	comments {
		public <T extends MObjectDTO> Object get(final T sample) {
			return ((SampleDTO) sample).getComments();
		}

		public <T extends MObjectDTO, K> void set(final T sample,
				final K comments) {
			((SampleDTO) sample).setComments((Set<SampleCommentDTO>) comments);
		}
	},
	subsampleCount {
		public <T extends MObjectDTO> Integer get(final T sample) {
			return ((SampleDTO) sample).getSubsampleCount();
		}

		public <T extends MObjectDTO, K> void set(final T sample,
				final K subsampleCount) {
			((SampleDTO) sample).setSubsampleCount((Integer) subsampleCount);
		}
	},
	datePrecision {
		public <T extends MObjectDTO> Object get(final T sample) {
			return ((SampleDTO) sample).getDatePrecision();
		}

		public <T extends MObjectDTO, K> void set(final T sample,
				final K datePrecision) {
			((SampleDTO) sample).setDatePrecision(Short
					.parseShort(datePrecision.toString()));
		}
	};

}
