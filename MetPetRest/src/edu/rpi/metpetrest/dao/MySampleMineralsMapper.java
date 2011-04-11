package edu.rpi.metpetrest.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;

import org.postgis.PGgeometry;
import org.postgis.Point;
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

	private static final String myRegionsQuery = "select name from regions LEFT OUTER JOIN sample_regions ON (regions.region_id = sample_regions.region_id) where sample_regions.sample_id = ?";
	
	private static final String myMetamorphicRegionsQuery = "select name from metamorphic_regions LEFT OUTER JOIN sample_metamorphic_regions ON (metamorphic_regions.metamorphic_region_id = sample_metamorphic_regions.metamorphic_region_id) where sample_metamorphic_regions.sample_id = ?";
	
	private static final String myReferencesQuery = "select name from georeference LEFT OUTER JOIN reference ON (georeference.reference_id = reference.reference_id) LEFT OUTER JOIN sample_reference ON (sample_reference.reference_id = reference.reference_id) where sample_reference.sample_id = ?";
	
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
			
			if (rs.getDate("collection_date") != null)
				currentSample.put("collectionDate",formatter.print(rs.getDate("collection_date"), Locale.ENGLISH));
			else
				currentSample.put("collectionDate", null);
			//end if
			
			currentSample.put("count", rs.getString("count"));
			
			currentSample.put("imageCount", rs.getString("image_count"));
			currentSample.put("analysisCount", rs.getString("analysis_count"));
			currentSample.put("subsampleCount", rs.getString("subsample_count"));
			
			currentSample.put("country", rs.getString("country"));
			
			
			currentSample.put("collector", rs.getString("collector"));
			

			currentSample.put("gradeName", rs.getString("grade_name"));

			currentSample.put("locationText", rs.getString("location_text"));
			
			
			if (rs.getObject("location") != null)
			{
				PGgeometry geometry = (PGgeometry)rs.getObject("location");
				
				Point point = (Point)geometry.getGeometry();
				
				currentSample.put("longitude", String.valueOf(point.getX()));
				currentSample.put("latitude", String.valueOf(point.getY()));
				
				
			}
			
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
			
			List<String> regions = this.query(myRegionsQuery, new Object[] {rs.getInt("sample_id")}, new ResultSetExtractor<List<String>>(){

				@Override
				public List<String> extractData(ResultSet rs)
						throws SQLException, DataAccessException {


					List<String> regions = new ArrayList<String>();

					while (rs.next()) {
						regions.add(rs.getString("name"));
					}

					return regions;

					
				}
				
			});
			
			currentSample.put("regions", regions);
			
			List<String> metamorphic_regions = this.query(myMetamorphicRegionsQuery, new Object[] {rs.getInt("sample_id")}, new ResultSetExtractor<List<String>>(){

				@Override
				public List<String> extractData(ResultSet rs)
						throws SQLException, DataAccessException {


					List<String> metamorphic_regions = new ArrayList<String>();

					while (rs.next()) {
						metamorphic_regions.add(rs.getString("name"));
					}

					return metamorphic_regions;

				}
				
			});

			currentSample.put("metamorphicRegions", metamorphic_regions);
		
			
			List<String> references = this.query(myReferencesQuery, new Object[] {rs.getInt("sample_id")}, new ResultSetExtractor<List<String>>(){

				@Override
				public List<String> extractData(ResultSet rs)
						throws SQLException, DataAccessException {


					List<String> references = new ArrayList<String>();

					while (rs.next()) {
						references.add(rs.getString("name"));
					}

					return references;

				}
				
			});

			currentSample.put("references", references);
		
			
			
			
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
