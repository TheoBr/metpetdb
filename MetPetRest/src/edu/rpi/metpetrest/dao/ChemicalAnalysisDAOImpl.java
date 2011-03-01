package edu.rpi.metpetrest.dao;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import edu.rpi.metpetrest.dao.mapper.ChemicalAnalysisDataListMapper;

public class ChemicalAnalysisDAOImpl extends JdbcTemplate {

	private static final String chemicalAnalysisQuery = "select  chemical_analyses.chemical_analysis_id, chemical_analyses.spot_id, chemical_analyses.reference_x, chemical_analyses.reference_y, chemical_analyses.analysis_method, minerals.name as analysis_material, chemical_analyses.where_done as analysis_location, chemical_analyses.analyst, chemical_analyses.analysis_date, chemical_analyses.total, (select count(chemical_analysis_id) from chemical_analyses where subsample_id = ?) as count from chemical_analyses, minerals where chemical_analyses.mineral_id = minerals.mineral_id and chemical_analyses.subsample_id = ? order by chemical_analyses.chemical_analysis_id asc LIMIT ? OFFSET ? ";

	private DataSource dataSource = null;

	public List<Map<String, String>> getAllChemicalAnalyses(String subSampleId,
			Long startResult, Long maxResults) {
		// List<Map<String, String>> results = this.query(chemicalAnalysisQuery,
		// new Object[]{subSampleId}, new ChemicalAnalysisDataMapper());

		// Map<String, Map<String, String>> results =
		// this.query(chemicalAnalysisQuery, new Object[]{subSampleId,
		// maxResults, startResult}, new ChemicalAnalysisDataMapper());

		List<Map<String, String>> results = this.query(chemicalAnalysisQuery,
				new Object[] { subSampleId, subSampleId, maxResults,
						startResult }, new ChemicalAnalysisDataListMapper());

		return results;

	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
