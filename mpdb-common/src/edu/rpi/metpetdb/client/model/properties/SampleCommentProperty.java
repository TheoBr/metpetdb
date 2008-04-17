package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.SampleCommentDTO;

public enum SampleCommentProperty implements Property {
	text {
		public <T extends MObjectDTO> String get(final T sampleComment) {
			return ((SampleCommentDTO) sampleComment).getText();
		}

		public <T extends MObjectDTO, K> void set(final T sampleComment,
				final K text) {
			((SampleCommentDTO) sampleComment).setText((String) text);
		}
	},
}
