package edu.rpi.metpetdb.client.model.properties;

import edu.rpi.metpetdb.client.model.GeoReference;

public enum GeoReferenceProperty implements Property<GeoReference> {
	title {
		public String get(final GeoReference geoReference) {
			return ((GeoReference) geoReference).getTitle();
		}

		public void set(final GeoReference geoReference, final Object title) {
			((GeoReference) geoReference).setTitle((String) title);
		}
	},
	firstAuthor {
		public String get(final GeoReference geoReference) {
			return ((GeoReference) geoReference).getFirstAuthor();
		}

		public void set(final GeoReference geoReference, final Object firstAuthor) {
			((GeoReference) geoReference).setFirstAuthor((String) firstAuthor);
		}
	},
	secondAuthors {
		public String get(final GeoReference geoReference) {
			return ((GeoReference) geoReference).getSecondAuthors();
		}

		public void set(final GeoReference geoReference, final Object secondAuthors) {
			((GeoReference) geoReference).setSecondAuthors((String) secondAuthors);
		}
	},
	journalName {
		public String get(final GeoReference geoReference) {
			return ((GeoReference) geoReference).getJournalName();
		}

		public void set(final GeoReference geoReference, final Object journalName) {
			((GeoReference) geoReference).setJournalName((String) journalName);
		}
	},
	fullText {
		public String get(final GeoReference geoReference) {
			return ((GeoReference) geoReference).getFullText();
		}

		public void set(final GeoReference geoReference, final Object fullText) {
			((GeoReference) geoReference).setFullText((String) fullText);
		}
	};
}