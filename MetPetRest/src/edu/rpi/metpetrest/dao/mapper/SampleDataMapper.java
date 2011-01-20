package edu.rpi.metpetrest.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import edu.rpi.metpetrest.model.SampleData;

public class SampleDataMapper implements RowMapper<SampleData> {

	@Override
	public SampleData mapRow(ResultSet rs, int arg1) throws SQLException {

		SampleData sampleData = new SampleData();
		sampleData.setSampleId(rs.getLong(1));
		sampleData.setTitle(rs.getString(2));
		sampleData.setAuthor(rs.getString(3));
		sampleData.setDescription(rs.getString(4));

		return sampleData;

	}
}
