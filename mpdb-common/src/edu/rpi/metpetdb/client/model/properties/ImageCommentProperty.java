package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.ImageComment;
import edu.rpi.metpetdb.client.model.interfaces.MObject;

public enum ImageCommentProperty implements Property {
	text {
		public <T extends MObject> String get(final T sampleComment) {
			return ((ImageComment) sampleComment).getText();
		}

		public <T extends MObject, K> void set(final T sampleComment,
				final K text) {
			((ImageComment) sampleComment).setText((String) text);
		}
	},
}
