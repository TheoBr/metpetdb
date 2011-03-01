package edu.rpi.metpetrest.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

public class ChemicalAnalysisDataMapper implements
		ResultSetExtractor<Map<String, Map<String, String>>> {

	@Override
	public Map<String, Map<String, String>> extractData(ResultSet rs)
			throws SQLException, DataAccessException {
		
		Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();

		while (rs.next())
		{
			Map<String, String> item = new HashMap<String, String>();
			
			item.put("id", rs.getString(1));
			item.put("spot_id", rs.getString(2));
			item.put("x", rs.getString(3));
			item.put("y", rs.getString(4));
		
			
			data.put(rs.getString(1), item);
		
		}
		return data;
	}

}
