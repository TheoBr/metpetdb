package edu.rpi.metpetdb.client.model.properties;

import java.util.Set;

import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.SubsampleType;
import edu.rpi.metpetdb.client.model.User;

public enum SubsampleProperty implements Property {
	name {
		public <T extends MObject> String get(final T subsample) {
			return ((Subsample) subsample).getName();
		}

		public <T extends MObject, K> void set(final T subsample, final K name) {
			((Subsample) subsample).setName((String) name);
		}
	},
	subsampleType {
		public <T extends MObject> Object get(final T subsample) {
			return ((Subsample) subsample).getSubsampleType();
		}

		public <T extends MObject, K> void set(final T subsample, final K type) {
			((Subsample) subsample).setSubsampleType((SubsampleType) type);
		}
	},
	images {
		public <T extends MObject> Set<Image> get(final T subsample) {
			return ((Subsample) subsample).getImages();
		}

		public <T extends MObject, K> void set(final T subsample, final K images) {
			((Subsample) subsample).setImages((Set<Image>) images);
		}
	},
	imageCount {
		public <T extends MObject> Integer get(final T subsample) {
			return ((Subsample) subsample).getImageCount();
		}

		public <T extends MObject, K> void set(final T subsample,
				final K imageCount) {
			((Subsample) subsample).setImageCount((Integer) imageCount);
		}
	},
	analysisCount {
		public <T extends MObject> Integer get(final T subsample) {
			return ((Subsample) subsample).getAnalysisCount();
		}

		public <T extends MObject, K> void set(final T subsample,
				final K analysisCount) {
			((Subsample) subsample).setAnalysisCount((Integer) analysisCount);
		}
	},
	sampleName {
		public <T extends MObject> String get(final T subsample) {
			return ((Subsample) subsample).getSampleName();
		}

		public <T extends MObject, K> void set(final T subsample, final K sample) {
			// FIXME this should thrown an exception
		}
	},
	publicData {
		public <T extends MObject> Boolean get(final T subsample) {
			return ((Subsample) subsample).isPublicData();
		}

		public <T extends MObject, K> void set(final T subsample,
				final K publicData) {
			((Subsample) subsample).setPublicData((Boolean) publicData);
		}
	},
	owner {
		public <T extends MObject> User get(final T subsample) {
			return ((Subsample) subsample).getOwner();
		}

		public <T extends MObject, K> void set(final T subsample, final K owner) {
			((Subsample) subsample).setOwner((User) owner);
		}
	},
}
