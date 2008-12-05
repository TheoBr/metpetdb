package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.SampleComment;

public enum SampleCommentProperty implements Property<SampleComment> {
	text {
		public String get(final SampleComment sampleComment) {
			return ((SampleComment) sampleComment).getText();
		}

		public void set(final SampleComment sampleComment, final Object text) {
			((SampleComment) sampleComment).setText((String) text);
		}
	},
}
