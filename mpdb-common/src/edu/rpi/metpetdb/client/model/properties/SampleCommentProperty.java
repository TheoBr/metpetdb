package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.SampleComment;

public enum SampleCommentProperty implements Property {
	text {
		public <T extends MObject> String get(final T sampleComment) {
			return ((SampleComment) sampleComment).getText();
		}

		public <T extends MObject, K> void set(final T sampleComment,
				final K text) {
			((SampleComment) sampleComment).setText((String) text);
		}
	},
}
