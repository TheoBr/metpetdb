package edu.rpi.metpetdb.client.model.properties;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import org.postgis.Geometry;
import org.postgis.Point;

import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.MetamorphicGrade;
import edu.rpi.metpetdb.client.model.Reference;
import edu.rpi.metpetdb.client.model.Region;
import edu.rpi.metpetdb.client.model.RockType;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.SampleComment;
import edu.rpi.metpetdb.client.model.SampleMineral;
import edu.rpi.metpetdb.client.model.User;

public enum SampleProperty implements Property<Sample> {

	sesarNumber {
		public String get(final Sample sample) {
			return sample.getSesarNumber();
		}

		public void set(final Sample sample, final Object sesarNumber) {
			sample.setSesarNumber((String) sesarNumber);
		}
	},
	location {
		public Geometry get(final Sample sample) {
			return sample.getLocation();
		}

		public void set(final Sample sample, final Object geometry) {
			sample.setLocation((Geometry) geometry);
		}
	},
	latitude {
		public Object get(final Sample sample) {
			return ((Point) sample.getLocation()).y;
		}

		public void set(final Sample sample, final Object geometry) {
			sample.setLatitude(Double.valueOf(geometry.toString()));
		}
	},
	longitude {
		public Object get(final Sample sample) {
			return ((Point) sample.getLocation()).x;
		}

		public void set(final Sample sample, final Object geometry) {
			sample.setLongitude(Double.valueOf(geometry.toString()));
		}
	},
	owner {
		public User get(final Sample sample) {
			return sample.getOwner();
		}

		public void set(final Sample sample, final Object owner) {
			sample.setOwner((User) owner);
		}
	},
	alias {
		public String get(final Sample sample) {
			return sample.getAlias();
		}

		public void set(final Sample sample, final Object alias) {
			sample.setAlias((String) alias);
		}
	},
	collectionDate {
		public Timestamp get(final Sample sample) {
			return sample.getCollectionDate();
		}

		public void set(final Sample sample, final Object collectionDate) {
			sample.setCollectionDate((Timestamp) collectionDate);
		}
	},
	publicData {
		public Boolean get(final Sample sample) {
			return sample.isPublicData();
		}

		public void set(final Sample sample, final Object publicData) {
			sample.setPublicData((Boolean) publicData);
		}
	},
	rockType {
		public Object get(final Sample sample) {
			return sample.getRockType();
		}

		public void set(final Sample sample, final Object rockType) {
			if (rockType instanceof RockType)
				sample.setRockType((RockType) rockType);
			else {
				final RockType rt = new RockType(rockType.toString());
				sample.setRockType(rt);
			}
		}
	},
	images {
		public Set<Image> get(final Sample sample) {
			return sample.getImages();
		}

		public void set(final Sample sample, final Object images) {
			sample.setImages((Set<Image>) images);
		}
	},
	minerals {
		public Set<SampleMineral> get(final Sample sample) {
			return sample.getMinerals();
		}

		public void set(final Sample sample, final Object minerals) {
			//TODO make it accepts different types of minerals
			sample.setMinerals((Set<SampleMineral>) minerals);
		}
	},
	country {
		public String get(final Sample sample) {
			return sample.getCountry();
		}

		public void set(final Sample sample, final Object country) {
			sample.setCountry((String) country);
		}
	},
	description {
		public String get(final Sample sample) {
			return sample.getDescription();
		}

		public void set(final Sample sample, final Object description) {
			sample.setDescription((String) description);
		}
	},
	collector {
		public String get(final Sample sample) {
			return sample.getCollector();
		}

		public void set(final Sample sample, final Object collector) {
			sample.setCollector((String) collector);
		}
	},
	locationText {
		public String get(final Sample sample) {
			return sample.getLocationText();
		}

		public void set(final Sample sample, final Object locationText) {
			sample.setLocationText((String) locationText);
		}
	},
	latitudeError {
		public Float get(final Sample sample) {
			return sample.getLatitudeError();
		}

		public void set(final Sample sample, final Object latitudeError) {
			sample
					.setLatitudeError(PropertyUtils
							.convertToFloat(latitudeError));
		}
	},
	longitudeError {
		public Float get(final Sample sample) {
			return sample.getLongitudeError();
		}

		public void set(final Sample sample, final Object longitudeError) {
			sample.setLongitudeError(PropertyUtils
					.convertToFloat(longitudeError));
		}
	},
	regions {
		public Set<Region> get(final Sample sample) {
			return sample.getRegions();
		}

		public void set(final Sample sample, final Object regions) {
			if (regions instanceof String) {
				if (sample.getRegions() == null)
					sample.setRegions(new HashSet<Region>());
				sample.getRegions().add(new Region((String) regions));
			} else {
				sample.setRegions((Set<Region>) regions);
			}
		}
	},
	metamorphicGrades {
		public Set<MetamorphicGrade> get(final Sample sample) {
			return sample.getMetamorphicGrades();
		}

		public void set(final Sample sample, final Object metamorphicGrades) {
			if (metamorphicGrades instanceof String) {
				if (sample.getMetamorphicGrades() == null)
					sample.setMetamorphicGrades(new HashSet<MetamorphicGrade>());
				sample.getMetamorphicGrades().add(new MetamorphicGrade((String) metamorphicGrades));
			} else{
			sample
					.setMetamorphicGrades((Set<MetamorphicGrade>) metamorphicGrades);
			}
		}
	},
	references {
		public Set<Reference> get(final Sample sample) {
			return sample.getReferences();
		}

		public void set(final Sample sample, final Object references) {
			if (references instanceof String) {
				if (sample.getReferences() == null)
					sample.setReferences(new HashSet<Reference>());
				sample.getReferences().add(new Reference((String) references));
			} else {
				sample.setReferences((Set<Reference>) references);
			}
		}
	},
	comments {
		public Object get(final Sample sample) {
			return sample.getComments();
		}

		public void set(final Sample sample, final Object comments) {
			if (comments instanceof String) {
				if (sample.getComments() == null)
					sample.setComments(new HashSet<SampleComment>());
				sample.getComments().add(new SampleComment((String) comments));
			} else {
			sample.setComments((Set<SampleComment>) comments);
			}
		}
	},
	subsampleCount {
		public Integer get(final Sample sample) {
			return sample.getSubsampleCount();
		}

		public void set(final Sample sample, final Object subsampleCount) {
			sample.setSubsampleCount((Integer) subsampleCount);
		}
	},
	imageCount {
		public Integer get(final Sample sample) {
			return sample.getImageCount();
		}

		public void set(final Sample sample, final Object subsampleCount) {
			sample.setImageCount((Integer) subsampleCount);
		}
	},
	analysisCount {
		public Integer get(final Sample sample) {
			return sample.getAnalysisCount();
		}

		public void set(final Sample sample, final Object subsampleCount) {
			sample.setAnalysisCount((Integer) subsampleCount);
		}
	},
	datePrecision {
		public Object get(final Sample sample) {
			return sample.getDatePrecision();
		}

		public void set(final Sample sample, final Object datePrecision) {
			sample.setDatePrecision(Short.parseShort(datePrecision.toString()));
		}
	};

}
