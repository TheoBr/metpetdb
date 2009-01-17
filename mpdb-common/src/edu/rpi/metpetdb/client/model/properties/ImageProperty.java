package edu.rpi.metpetdb.client.model.properties;

import java.util.HashSet;
import java.util.Set;

import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.ImageComment;
import edu.rpi.metpetdb.client.model.ImageType;
import edu.rpi.metpetdb.client.model.Reference;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.SubsampleType;

public enum ImageProperty implements Property<Image> {
	imageType {
		public Object get(final Image image) {
			return ((Image) image).getImageType();
		}

		public void set(final Image image, final Object type) {
			if (!(type instanceof ImageType)) {
				final ImageType st = new ImageType();
				st.setImageType(type == null ? "" : type.toString());
				image.setImageType(st);
			} else {
				image.setImageType((ImageType) type);
			}
		}
	},
	subsample {
		public Subsample get(final Image image) {
			return ((Image) image).getSubsample();
		}

		public void set(final Image image, final Object subsample) {
			((Image) image).setSubsample((Subsample) subsample);
		}
	},
	checksum {
		public String get(final Image image) {
			return ((Image) image).getChecksum();
		}

		public void set(final Image image, final Object checksum) {
			((Image) image).setChecksum((String) checksum);
		}
	},
	scale {
		public Integer get(final Image image) {
			return ((Image) image).getScale();
		}

		public void set(final Image image, final Object scale) {
			((Image) image).setScale(PropertyUtils.convertToInteger(scale));
		}
	},
	comments {
		public Object get(final Image sample) {
			return ((Image) sample).getComments();
		}

		public void set(final Image sample, final Object comments) {
			((Image) sample).setComments((Set<ImageComment>) comments);
		}
	},
	collector {
		public Object get(final Image sample) {
			return ((Image) sample).getCollector();
		}

		public void set(final Image sample, final Object collector) {
			((Image) sample).setCollector((String) collector);
		}
	},
	description {
		public Object get(final Image sample) {
			return sample.getDescription();
		}

		public void set(final Image sample, final Object collector) {
			sample.setDescription((String) collector);
		}
	},
	filename {
		public Object get(final Image sample) {
			return sample.getFilename();
		}

		public void set(final Image sample, final Object collector) {
			sample.setFilename((String) collector);
		}
	},
	references {
		public Set<Reference> get(final Image sample) {
			return sample.getReferences();
		}

		public void set(final Image sample, final Object references) {
			if (references instanceof String) {
				if (sample.getReferences() == null)
					sample.setReferences(new HashSet<Reference>());
				sample.getReferences().add(new Reference((String) references));
			} else {
				sample.setReferences((Set<Reference>) references);
			}
		}
	},
}
