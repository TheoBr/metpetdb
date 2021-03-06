package edu.rpi.metpetrest.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.text.NumberFormat;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.jdbc.core.ResultSetExtractor;

public class ChemicalAnalysisDataListMapper implements ResultSetExtractor<List> {

	private static final DateFormatter formatter = new DateFormatter(
			"yyyy-MM-dd HH:mm:ss");

		NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);

		public ChemicalAnalysisDataListMapper()
		{
		nf.setMinimumFractionDigits(2);
		}

	@Override
	public List<Map<String, String>> extractData(ResultSet rs)
			throws SQLException, DataAccessException {

		List<Map<String, String>> data = new ArrayList<Map<String, String>>();

		while (rs.next()) {
			Map<String, String> item = new HashMap<String, String>();

			item.put("id", rs.getString("chemical_analysis_id"));
			item.put("spot_id", rs.getString("spot_id"));
			
			if (rs.getBigDecimal("reference_x") != null)
					item.put ("x", nf.format(rs.getDouble("reference_x")));
			
			if (rs.getBigDecimal("reference_y") != null)
					item.put ("y", nf.format(rs.getDouble("reference_y")));
		
			item.put("analysis_method", rs.getString("analysis_method"));
			item.put("analysis_material", rs.getString("analysis_material"));

			item.put("analysis_location", rs.getString("analysis_location"));
			item.put("analyst", rs.getString("analyst"));

			item.put("count", rs.getString("count"));

			if (rs.getDate("analysis_date") != null)
				item.put("analysis_date", formatter.print(rs.getDate("analysis_date"), Locale.ENGLISH));

			if (rs.getBigDecimal("total") != null)
				item.put("total", nf.format(rs.getDouble("total")));

			data.add(item);

		}
		return data;
	}

}
