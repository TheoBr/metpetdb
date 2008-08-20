package edu.rpi.metpetdb.client.model.properties;

import java.util.Set;

import org.postgis.Geometry;

import edu.rpi.metpetdb.client.model.ChemicalAnalysisElementDTO;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisOxideDTO;
import edu.rpi.metpetdb.client.model.DateSpan;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.SampleMineralDTO;
import edu.rpi.metpetdb.client.model.SearchSampleDTO;
import edu.rpi.metpetdb.client.model.UserDTO;

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
	location {
		public <T extends MObjectDTO> Geometry get(final T sample) {
			return ((SearchSampleDTO) sample).getLocation();
		}

		public <T extends MObjectDTO, K> void set(final T sample,
				final K geometry) {
			((SearchSampleDTO) sample).setLocation((Geometry) geometry);
		}

		public String columnName() {
			return "location";
		}
	},
	owner {
		public <T extends MObjectDTO> UserDTO get(final T sample) {
			return ((SearchSampleDTO) sample).getOwner();
		}

		public <T extends MObjectDTO, K> void set(final T sample, final K owner) {
			((SearchSampleDTO) sample).setOwner((UserDTO) owner);
		}

		public String columnName() {
			return "owner";
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
			return "sample_minerals_minerals";
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
		public <T extends MObjectDTO> Set<String> get(final T sample) {
			return ((SearchSampleDTO) sample).getPossibleRockTypes();
		}

		public <T extends MObjectDTO, K> void set(final T sample,
				final K rockType) {
			((SearchSampleDTO) sample)
					.setPossibleRockTypes((Set<String>) rockType);
		}

		public String columnName() {
			return "rockType";
		}
	},
	elements {
		public <T extends MObjectDTO> Set<ChemicalAnalysisElementDTO> get(
				final T sample) {
			return ((SearchSampleDTO) sample).getElements();
		}

		public <T extends MObjectDTO, K> void set(final T sample,
				final K elements) {
			((SearchSampleDTO) sample)
					.setElements((Set<ChemicalAnalysisElementDTO>) elements);
		}
		public String columnName() {
			return "elements";
		}
	},
	oxides {
		public <T extends MObjectDTO> Set<ChemicalAnalysisOxideDTO> get(
				final T sample) {
			return ((SearchSampleDTO) sample).getOxides();
		}

		public <T extends MObjectDTO, K> void set(final T sample, final K oxides) {
			((SearchSampleDTO) sample)
					.setOxides((Set<ChemicalAnalysisOxideDTO>) oxides);
		}
		public String columnName() {
			return "oxides";
		}
	};

}
