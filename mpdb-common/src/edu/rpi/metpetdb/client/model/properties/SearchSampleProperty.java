package edu.rpi.metpetdb.client.model.properties;

import java.util.Set;

import org.postgis.Geometry;

import edu.rpi.metpetdb.client.model.DateSpan;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.RegionDTO;
import edu.rpi.metpetdb.client.model.RockTypeDTO;
import edu.rpi.metpetdb.client.model.SampleMineralDTO;
import edu.rpi.metpetdb.client.model.SearchElementDTO;
import edu.rpi.metpetdb.client.model.SearchOxideDTO;
import edu.rpi.metpetdb.client.model.SearchSampleDTO;

public enum SearchSampleProperty implements SearchProperty {

	sesarNumber {
		public <T extends MObjectDTO> String get(final T sample) {
			return ((SearchSampleDTO) sample).getSesarNumber();
		}

		public <T extends MObjectDTO, K> void set(final T sample,
				final K sesarNumber) {
			((SearchSampleDTO) sample).setSesarNumber((String) sesarNumber);
		}

		public String columnName() {
			return "sesarNumber";
		}
	},
	boundingBox {
		public <T extends MObjectDTO> Geometry get(final T sample) {
			return ((SearchSampleDTO) sample).getBoundingBox();
		}

		public <T extends MObjectDTO, K> void set(final T sample,
				final K geometry) {
			((SearchSampleDTO) sample).setBoundingBox((Geometry) geometry);
		}

		public String columnName() {
			return "location";
		}
	},
	region {
		public <T extends MObjectDTO> Set<RegionDTO> get(final T sample) {
			return ((SearchSampleDTO) sample).getRegions();
		}

		public <T extends MObjectDTO, K> void set(final T sample,
				final K regions) {
			((SearchSampleDTO) sample)
					.setRegions((Set<RegionDTO>) regions);
		}
		public String columnName() {
			return "region_name";
		}
	},
	owner {
		public <T extends MObjectDTO> Object get(final T sample) {
			return ((SearchSampleDTO) sample).getOwner();
		}

		public <T extends MObjectDTO, K> void set(final T sample, final K owner) {
			((SearchSampleDTO) sample).setOwner((String) owner);
		}

		public String columnName() {
			return "user_username";
		}
	},
	minerals {
		public <T extends MObjectDTO> Set<SampleMineralDTO> get(final T sample) {
			return ((SearchSampleDTO) sample).getMinerals();
		}

		public <T extends MObjectDTO, K> void set(final T sample,
				final K minerals) {
			((SearchSampleDTO) sample)
					.setMinerals((Set<SampleMineralDTO>) minerals);
		}
		public String columnName() {
			return "sampleMineral_mineral_name";
		}
	},
	alias {
		public <T extends MObjectDTO> String get(final T sample) {
			return ((SearchSampleDTO) sample).getAlias();
		}

		public <T extends MObjectDTO, K> void set(final T sample, final K alias) {
			((SearchSampleDTO) sample).setAlias((String) alias);
		}

		public String columnName() {
			return "alias";
		}
	},
	collectionDateRange {
		public <T extends MObjectDTO> DateSpan get(final T sample) {
			return ((SearchSampleDTO) sample).getCollectionDateRange();
		}

		public <T extends MObjectDTO, K> void set(final T sample,
				final K collectionDateRange) {
			((SearchSampleDTO) sample)
					.setCollectionDateRange((DateSpan) collectionDateRange);
		}

		public String columnName() {
			return "collectionDate";
		}
	},
	possibleRockTypes {
		public <T extends MObjectDTO> Object get(final T sample) {
			return ((SearchSampleDTO) sample).getPossibleRockTypes();
		}

		public <T extends MObjectDTO, K> void set(final T sample,
				final K rockType) {
			((SearchSampleDTO) sample)
					.setPossibleRockTypes((Set<RockTypeDTO>) rockType);
		}

		public String columnName() {
			return "rockType_rockType";
		}
	},
	elements {
		public <T extends MObjectDTO> Set<SearchElementDTO> get(
				final T sample) {
			return ((SearchSampleDTO) sample).getElements();
		}

		public <T extends MObjectDTO, K> void set(final T sample,
				final K elements) {
			((SearchSampleDTO) sample)
					.setElements((Set<SearchElementDTO>) elements);
		}
		public String columnName() {
			return "subsample_chemicalAnalysis_elements";
		}
	},
	oxides {
		public <T extends MObjectDTO> Set<SearchOxideDTO> get(
				final T sample) {
			return ((SearchSampleDTO) sample).getOxides();
		}

		public <T extends MObjectDTO, K> void set(final T sample, final K oxides) {
			((SearchSampleDTO) sample)
					.setOxides((Set<SearchOxideDTO>) oxides);
		}
		public String columnName() {
			return "subsample_chemicalAnalysis_oxides";
		}
	};
}
