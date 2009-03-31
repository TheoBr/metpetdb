package edu.rpi.metpetdb.client.model.properties;

import java.util.Set;

import org.postgis.Geometry;

import edu.rpi.metpetdb.client.model.DateSpan;
import edu.rpi.metpetdb.client.model.MetamorphicGrade;
import edu.rpi.metpetdb.client.model.Mineral;
import edu.rpi.metpetdb.client.model.Reference;
import edu.rpi.metpetdb.client.model.Region;
import edu.rpi.metpetdb.client.model.RockType;
import edu.rpi.metpetdb.client.model.SearchElement;
import edu.rpi.metpetdb.client.model.SearchOxide;
import edu.rpi.metpetdb.client.model.SearchSample;

public enum SearchSampleProperty implements SearchProperty {

	sesarNumber {
		public String get(final SearchSample sample) {
			return ((SearchSample) sample).getSesarNumber();
		}

		public void set(final SearchSample sample, final Object sesarNumber) {
			((SearchSample) sample).setSesarNumber((String) sesarNumber);
		}

		public String columnName() {
			return "sesarNumber";
		}
	},
	boundingBox {
		public Geometry get(final SearchSample sample) {
			return ((SearchSample) sample).getBoundingBox();
		}

		public void set(final SearchSample sample, final Object geometry) {
			((SearchSample) sample).setBoundingBox((Geometry) geometry);
		}

		public String columnName() {
			return "location";
		}
	},
	region {
		public Set<Region> get(final SearchSample sample) {
			return ((SearchSample) sample).getRegions();
		}

		public void set(final SearchSample sample, final Object regions) {
			((SearchSample) sample).setRegions((Set<Region>) regions);
		}
		public String columnName() {
			return "region_name";
		}
	},
	owner {
		public Set<String> get(final SearchSample sample) {
			return ((SearchSample) sample).getOwners();
		}

		public void set(final SearchSample sample, final Object owner) {
			((SearchSample) sample).setOwners((Set<String>) owner);
		}

		public String columnName() {
			return "user_name";
		}
	},
	minerals {
		public Set<Mineral> get(final SearchSample sample) {
			return ((SearchSample) sample).getMinerals();
		}

		public void set(final SearchSample sample, final Object minerals) {
			((SearchSample) sample).setMinerals((Set<Mineral>) minerals);
		}
		public String columnName() {
			return "sampleMineral_mineral_name";
		}
	},
	number {
		public String get(final SearchSample sample) {
			return ((SearchSample) sample).getNumber();
		}

		public void set(final SearchSample sample, final Object number) {
			((SearchSample) sample).setNumber((String) number);
		}

		public String columnName() {
			return "number";
		}
	},
	collectionDateRange {
		public DateSpan get(final SearchSample sample) {
			return ((SearchSample) sample).getCollectionDateRange();
		}

		public void set(final SearchSample sample,
				final Object collectionDateRange) {
			((SearchSample) sample)
					.setCollectionDateRange((DateSpan) collectionDateRange);
		}

		public String columnName() {
			return "collectionDate";
		}
	},
	possibleRockTypes {
		public Object get(final SearchSample sample) {
			return ((SearchSample) sample).getPossibleRockTypes();
		}

		public void set(final SearchSample sample, final Object rockType) {
			((SearchSample) sample)
					.setPossibleRockTypes((Set<RockType>) rockType);
		}

		public String columnName() {
			return "rockType_rockType";
		}
	},
	elements {
		public Set<SearchElement> get(final SearchSample sample) {
			return ((SearchSample) sample).getElements();
		}

		public void set(final SearchSample sample, final Object elements) {
			((SearchSample) sample).setElements((Set<SearchElement>) elements);
		}
		public String columnName() {
			return "subsample_chemicalAnalysis_elements";
		}
	},
	oxides {
		public Set<SearchOxide> get(final SearchSample sample) {
			return ((SearchSample) sample).getOxides();
		}

		public void set(final SearchSample sample, final Object oxides) {
			((SearchSample) sample).setOxides((Set<SearchOxide>) oxides);
		}
		public String columnName() {
			return "subsample_chemicalAnalysis_oxides";
		}
	},
	collector {
		public Set<String> get(final SearchSample sample) {
			return ((SearchSample) sample).getCollectors();
		}

		public void set(final SearchSample sample, final Object collector) {
			((SearchSample) sample).setCollectors((Set<String>) collector);
		}

		public String columnName() {
			return "collector";
		}
	},
	country {
		public Set<String> get(final SearchSample sample) {
			return ((SearchSample) sample).getCountries();
		}

		public void set(final SearchSample sample, final Object country) {
			((SearchSample) sample).setCountries((Set<String>) country);
		}

		public String columnName() {
			return "country";
		}
	},
	references {
		public Set<Reference> get(final SearchSample sample) {
			return ((SearchSample) sample).getReferences();
		}

		public void set(final SearchSample sample, final Object references) {
			((SearchSample) sample).setReferences((Set<Reference>) references);
		}
		public String columnName() {
			return "reference_name";
		}
	},
	metamorphicGrades {
		public Set<MetamorphicGrade> get(final SearchSample sample) {
			return ((SearchSample) sample).getMetamorphicGrades();
		}

		public void set(final SearchSample sample,
				final Object metamorphicGrades) {
			((SearchSample) sample)
					.setMetamorphicGrades((Set<MetamorphicGrade>) metamorphicGrades);
		}
		public String columnName() {
			return "metamorphicGrade_name";
		}
	},
	getPublic {
		public Integer get(final SearchSample sample) {
			return ((SearchSample) sample).getGetPublic();
		}

		public void set(final SearchSample sample,
				final Object getPublic) {
			((SearchSample) sample)
					.setGetPublic((Integer)getPublic);
		}
		public String columnName() {
			return "publicData";
		}
	},
	tabs {
		public String get(final SearchSample sample) {
			return "";
		}

		public void set(final SearchSample sample, final Object tabs) {

		}
		public String columnName() {
			return "";
		}
	};
}
