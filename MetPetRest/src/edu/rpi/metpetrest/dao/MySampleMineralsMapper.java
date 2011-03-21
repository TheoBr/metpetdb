package edu.rpi.metpetrest.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

public class MySampleMineralsMapper extends JdbcTemplate implements
		ResultSetExtractor<List<Map<String, Object>>> {

	private DataSource dataSource = null;

	private static final DateFormatter formatter = new DateFormatter(
	"yyyy-MM-dd HH:mm:ss");
	
	private static final String mySampleMineralsQuery = "select minerals.name from samples, sample_minerals, minerals where samples.sample_id = sample_minerals.sample_id and sample_minerals.mineral_id = minerals.mineral_id and samples.sample_id = ?";

	@Override
	public List<Map<String, Object>> extractData(ResultSet rs)
			throws SQLException, DataAccessException {

		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

		while (rs.next()) {
			Map<String, Object> currentSample = new HashMap<String, Object>();

			
			currentSample.put("sampleId", rs.getString("sample_id"));
			currentSample.put("sampleNumber", rs.getString("number"));
			currentSample.put("publicData", rs.getString("public_data"));
			currentSample.put("owner", rs.getString("owner"));
			currentSample.put("rockType", rs.getString("rock_type"));
			currentSample.put("collectionDate",formatter.print(rs.getDate("collection_date"), Locale.ENGLISH));
			currentSample.put("count", rs.getString("count"));
			
			currentSample.put("imageCount", rs.getString("image_count"));
			currentSample.put("analysisCount", rs.getString("analysis_count"));
			currentSample.put("subsampleCount", rs.getString("subsample_count"));
			
			
			List<String> minerals = this.query(mySampleMineralsQuery,
					new Object[] { rs.getInt("sample_id") },

					new ResultSetExtractor<List<String>>() {

						public List<String> extractData(ResultSet rs2)
								throws SQLException, DataAccessException {
							List<String> minerals = new ArrayList<String>();

							while (rs2.next()) {
								minerals.add(rs2.getString("name"));
							}

							return minerals;
						}
					});

			currentSample.put("minerals", minerals);

			data.add(currentSample);
		}

		return data;

	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
