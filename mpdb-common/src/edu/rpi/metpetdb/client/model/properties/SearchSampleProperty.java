package edu.rpi.metpetdb.client.model.properties;

import java.sql.Timestamp;
import java.util.Set;

import org.postgis.Geometry;

import edu.rpi.metpetdb.client.model.MObjectDTO;
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
			return "SesarNumber";
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
			return "Location";
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
			return "Owner";
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
			return "Alias";
		}
	},
	collectionDate {
		public <T extends MObjectDTO> Timestamp get(final T sample) {
			return ((SearchSampleDTO) sample).getCollectionDate();
		}

		public <T extends MObjectDTO, K> void set(final T sample,
				final K collectionDate) {
			((SearchSampleDTO) sample)
					.setCollectionDate((Timestamp) collectionDate);
		}

		public String columnName() {
			return "CollectionDate";
		}
	},
	publicData {
		public <T extends MObjectDTO> Boolean get(final T sample) {
			return ((SearchSampleDTO) sample).isPublicData();
		}

		public <T extends MObjectDTO, K> void set(final T sample,
				final K publicData) {
			((SearchSampleDTO) sample).setPublicData((Boolean) publicData);
		}

		public String columnName() {
			return "PublicData";
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
			return "RockType";
		}
	};

}
