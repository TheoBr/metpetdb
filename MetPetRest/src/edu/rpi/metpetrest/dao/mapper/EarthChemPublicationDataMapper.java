package edu.rpi.metpetrest.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import edu.rpi.metpetrest.model.PublicationData;

public class EarthChemPublicationDataMapper implements
		RowMapper<PublicationData> {

	@Override
	public PublicationData mapRow(ResultSet rs, int arg1) throws SQLException {

		PublicationData publicationData = new PublicationData();
		 
		publicationData.setTitle((rs.getString(6) != null) ? rs.getString(6)
				: "");
		publicationData.setAuthor((rs.getString(7) != null) ? rs.getString(7)
				: "");
		publicationData.setSecondAuthors((rs.getString(8) != null) ? rs
				.getString(8) : "");
		publicationData.setJournalName((rs.getString(9) != null) ? rs
				.getString(9) : "");

		return publicationData;

	}

}
