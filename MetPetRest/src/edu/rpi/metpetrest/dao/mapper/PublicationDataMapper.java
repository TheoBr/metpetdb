package edu.rpi.metpetrest.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import edu.rpi.metpetrest.model.PublicationData;

public class PublicationDataMapper implements RowMapper<PublicationData> {

	@Override
	public PublicationData mapRow(ResultSet rs, int arg1) throws SQLException {

		PublicationData publicationData = new PublicationData();
		publicationData.setTitle((rs.getString(1) != null) ? rs.getString(1)
				: "");
		publicationData.setAuthor((rs.getString(2) != null) ? rs.getString(2)
				: "");
		publicationData.setSecondAuthors((rs.getString(3) != null) ? rs
				.getString(3) : "");
		publicationData.setJournalName((rs.getString(4) != null) ? rs
				.getString(4) : "");
		publicationData.setAbstractTxt((rs.getString(5) != null) ? rs
				.getString(5) : "");
		publicationData.setReferenceId((rs.getString(6) != null) ? rs
				.getString(6) : "");
		publicationData.setJournalName2((rs.getString(7) != null) ? rs.getString(7) : "")
		publicationData.setDOI((rs.getString(8) != null) ? rs.getString(8) : "")
		return publicationData;

	}

}
