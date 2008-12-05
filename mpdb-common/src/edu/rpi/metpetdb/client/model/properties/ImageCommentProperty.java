package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.ImageComment;

public enum ImageCommentProperty implements Property<ImageComment> {
	text {
		public  String get(final ImageComment sampleComment) {
			return ((ImageComment) sampleComment).getText();
		}

		public void set(final ImageComment sampleComment,
				final Object text) {
			((ImageComment) sampleComment).setText((String) text);
		}
	},
}
