package edu.rpi.metpetdb.client.model.properties;

import java.util.Set;

import edu.rpi.metpetdb.client.model.ImageDTO;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.SubsampleDTO;
import edu.rpi.metpetdb.client.model.SubsampleTypeDTO;

public enum SubsampleProperty implements Property {
	name {
		public <T extends MObjectDTO> String get(final T subsample) {
			return ((SubsampleDTO) subsample).getName();
		}

		public <T extends MObjectDTO, K> void set(final T subsample,
				final K name) {
			((SubsampleDTO) subsample).setName((String) name);
		}
	},
	subsampleType {
		public <T extends MObjectDTO> Object get(final T subsample) {
			return ((SubsampleDTO) subsample).getSubsampleType();
		}

		public <T extends MObjectDTO, K> void set(final T subsample,
				final K type) {
			((SubsampleDTO) subsample).setSubsampleType((SubsampleTypeDTO) type);
		}
	},
	images {
		public <T extends MObjectDTO> Set<ImageDTO> get(final T subsample) {
			return ((SubsampleDTO) subsample).getImages();
		}

		public <T extends MObjectDTO, K> void set(final T subsample,
				final K images) {
			((SubsampleDTO) subsample).setImages((Set<ImageDTO>) images);
		}
	},
	imageCount {
		public <T extends MObjectDTO> Integer get(final T subsample) {
			return ((SubsampleDTO) subsample).getImageCount();
		}

		public <T extends MObjectDTO, K> void set(final T subsample,
				final K imageCount) {
			((SubsampleDTO) subsample).setImageCount((Integer) imageCount);
		}
	},
	analysisCount {
		public <T extends MObjectDTO> Integer get(final T subsample) {
			return ((SubsampleDTO) subsample).getAnalysisCount();
		}

		public <T extends MObjectDTO, K> void set(final T subsample,
				final K analysisCount) {
			((SubsampleDTO) subsample)
					.setAnalysisCount((Integer) analysisCount);
		}
	},
	sampleName {
		public <T extends MObjectDTO> String get(final T subsample) {
			return ((SubsampleDTO) subsample).getSampleName();
		}

		public <T extends MObjectDTO, K> void set(final T subsample,
				final K sample) {
			// FIXME this should thrown an exception
		}
	},
}
