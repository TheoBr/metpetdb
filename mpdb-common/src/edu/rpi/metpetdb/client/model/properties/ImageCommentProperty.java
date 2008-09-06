package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.ImageCommentDTO;
import edu.rpi.metpetdb.client.model.MObjectDTO;

public enum ImageCommentProperty  implements Property {
	text {
		public <T extends MObjectDTO> String get(final T sampleComment) {
			return ((ImageCommentDTO) sampleComment).getText();
		}

		public <T extends MObjectDTO, K> void set(final T sampleComment,
				final K text) {
			((ImageCommentDTO) sampleComment).setText((String) text);
		}
	},
}
