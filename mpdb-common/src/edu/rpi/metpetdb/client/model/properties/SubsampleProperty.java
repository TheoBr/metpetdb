package edu.rpi.metpetdb.client.model.properties;

import java.util.Set;

import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.SubsampleType;
import edu.rpi.metpetdb.client.model.User;

public enum SubsampleProperty implements Property<Subsample> {
	name {
		public String get(final Subsample subsample) {
			return ((Subsample) subsample).getName();
		}

		public void set(final Subsample subsample, final Object name) {
			((Subsample) subsample).setName((String) name);
		}
	},
	subsampleType {
		public Object get(final Subsample subsample) {
			return ((Subsample) subsample).getSubsampleType();
		}

		public void set(final Subsample subsample, final Object type) {
			if (!(type instanceof SubsampleType)) {
				final SubsampleType st = new SubsampleType();
				st.setSubsampleType(type == null ? "" : type.toString());
				subsample.setSubsampleType(st);
			} else {
				subsample.setSubsampleType((SubsampleType) type);
			}
		}
	},
	images {
		public Set<Image> get(final Subsample subsample) {
			return ((Subsample) subsample).getImages();
		}

		public void set(final Subsample subsample, final Object images) {
			((Subsample) subsample).setImages((Set<Image>) images);
		}
	},
	imageCount {
		public Integer get(final Subsample subsample) {
			return ((Subsample) subsample).getImageCount();
		}

		public void set(final Subsample subsample, final Object imageCount) {
			((Subsample) subsample).setImageCount((Integer) imageCount);
		}
	},
	analysisCount {
		public Integer get(final Subsample subsample) {
			return ((Subsample) subsample).getAnalysisCount();
		}

		public void set(final Subsample subsample, final Object analysisCount) {
			((Subsample) subsample).setAnalysisCount((Integer) analysisCount);
		}
	},
	sampleName {
		public String get(final Subsample subsample) {
			return ((Subsample) subsample).getSampleName();
		}

		public void set(final Subsample subsample, final Object sample) {
			// FIXME this should thrown an exception
		}
	},
	publicData {
		public Boolean get(final Subsample subsample) {
			return ((Subsample) subsample).isPublicData();
		}

		public void set(final Subsample subsample, final Object publicData) {
			((Subsample) subsample).setPublicData((Boolean) publicData);
		}
	},
	owner {
		public User get(final Subsample subsample) {
			return ((Subsample) subsample).getOwner();
		}

		public void set(final Subsample subsample, final Object owner) {
			((Subsample) subsample).setOwner((User) owner);
		}
	},
}
