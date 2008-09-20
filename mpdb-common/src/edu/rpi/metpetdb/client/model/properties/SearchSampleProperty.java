package edu.rpi.metpetdb.client.model.properties;

import java.util.Set;

import org.postgis.Geometry;

import edu.rpi.metpetdb.client.model.DateSpan;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.Region;
import edu.rpi.metpetdb.client.model.RockType;
import edu.rpi.metpetdb.client.model.SampleMineral;
import edu.rpi.metpetdb.client.model.SearchElement;
import edu.rpi.metpetdb.client.model.SearchOxide;
import edu.rpi.metpetdb.client.model.SearchSample;
import edu.rpi.metpetdb.client.model.interfaces.MObject;

public enum SearchSampleProperty implements SearchProperty {

	sesarNumber {
		public <T extends MObject> String get(final T sample) {
			return ((SearchSample) sample).getSesarNumber();
		}

		public <T extends MObject, K> void set(final T sample,
				final K sesarNumber) {
			((SearchSample) sample).setSesarNumber((String) sesarNumber);
		}

		public String columnName() {
			return "sesarNumber";
		}
	},
	boundingBox {
		public <T extends MObject> Geometry get(final T sample) {
			return ((SearchSample) sample).getBoundingBox();
		}

		public <T extends MObject, K> void set(final T sample, final K geometry) {
			((SearchSample) sample).setBoundingBox((Geometry) geometry);
		}

		public String columnName() {
			return "location";
		}
	},
	region {
		public <T extends MObject> Set<Region> get(final T sample) {
			return ((SearchSample) sample).getRegions();
		}

		public <T extends MObject, K> void set(final T sample, final K regions) {
			((SearchSample) sample).setRegions((Set<Region>) regions);
		}
		public String columnName() {
			return "region_name";
		}
	},
	owner {
		public <T extends MObject> Object get(final T sample) {
			return ((SearchSample) sample).getOwner();
		}

		public <T extends MObject, K> void set(final T sample, final K owner) {
			((SearchSample) sample).setOwner((String) owner);
		}

		public String columnName() {
			return "user_username";
		}
	},
	minerals {
		public <T extends MObject> Set<SampleMineral> get(final T sample) {
			return ((SearchSample) sample).getMinerals();
		}

		public <T extends MObject, K> void set(final T sample, final K minerals) {
			((SearchSample) sample).setMinerals((Set<SampleMineral>) minerals);
		}
		public String columnName() {
			return "sampleMineral_mineral_name";
		}
	},
	alias {
		public <T extends MObject> String get(final T sample) {
			return ((SearchSample) sample).getAlias();
		}

		public <T extends MObject, K> void set(final T sample, final K alias) {
			((SearchSample) sample).setAlias((String) alias);
		}

		public String columnName() {
			return "alias";
		}
	},
	collectionDateRange {
		public <T extends MObject> DateSpan get(final T sample) {
			return ((SearchSample) sample).getCollectionDateRange();
		}

		public <T extends MObject, K> void set(final T sample,
				final K collectionDateRange) {
			((SearchSample) sample)
					.setCollectionDateRange((DateSpan) collectionDateRange);
		}

		public String columnName() {
			return "collectionDate";
		}
	},
	possibleRockTypes {
		public <T extends MObject> Object get(final T sample) {
			return ((SearchSample) sample).getPossibleRockTypes();
		}

		public <T extends MObject, K> void set(final T sample, final K rockType) {
			((SearchSample) sample)
					.setPossibleRockTypes((Set<RockType>) rockType);
		}

		public String columnName() {
			return "rockType_rockType";
		}
	},
	elements {
		public <T extends MObject> Set<SearchElement> get(final T sample) {
			return ((SearchSample) sample).getElements();
		}

		public <T extends MObject, K> void set(final T sample, final K elements) {
			((SearchSample) sample).setElements((Set<SearchElement>) elements);
		}
		public String columnName() {
			return "subsample_chemicalAnalysis_elements";
		}
	},
	oxides {
		public <T extends MObject> Set<SearchOxide> get(final T sample) {
			return ((SearchSample) sample).getOxides();
		}

		public <T extends MObject, K> void set(final T sample, final K oxides) {
			((SearchSample) sample).setOxides((Set<SearchOxide>) oxides);
		}
		public String columnName() {
			return "subsample_chemicalAnalysis_oxides";
		}
	};
}
