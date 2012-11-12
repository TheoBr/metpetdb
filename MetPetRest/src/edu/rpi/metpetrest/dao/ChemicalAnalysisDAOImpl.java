package edu.rpi.metpetrest.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import edu.rpi.metpetrest.dao.mapper.ChemicalAnalysisDataListMapper;

public class ChemicalAnalysisDAOImpl extends NamedParameterJdbcTemplate {

	public ChemicalAnalysisDAOImpl(DataSource dataSource) {
		super(dataSource);
	}
	
	private static final String chemicalAnalysisQuery = "select  chemical_analyses.chemical_analysis_id, chemical_analyses.spot_id, chemical_analyses.reference_x, chemical_analyses.reference_y, chemical_analyses.analysis_method, minerals.name as analysis_material, chemical_analyses.where_done as analysis_location, chemical_analyses.analyst, chemical_analyses.analysis_date, chemical_analyses.total, (select count(chemical_analysis_id) from chemical_analyses where subsample_id = :subsampleId) as count " 
	+ "from chemical_analyses, minerals "
	+ "where left outer join (chemical_analyses.mineral_id on minerals.mineral_id) "
	+ "and chemical_analyses.subsample_id = :subsampleId "
	+ "AND (user_id = :userId "
	+ "OR subsample_id IN (select subsample_id from subsamples where sample_id IN (select distinct(samples.sample_id) from samples, project_samples, projects where samples.sample_id = project_samples.sample_id and project_samples.project_id = projects.project_id and projects.name IN (:projects) )) "
	+ "OR public_data = 'Y' ) " 
	+ "order by chemical_analyses.chemical_analysis_id asc LIMIT :maxResults OFFSET :startResult";
	
	
	private DataSource dataSource = null;

	public List<Map<String, String>> getAllChemicalAnalyses(String subSampleId,
			Long startResult, Long maxResults) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("subsampleId", Integer.valueOf(subSampleId));
		params.put("userId", Integer.valueOf(((UserDetails) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal()).getPassword()));
		params.put("projects", buildProjectCriteria());
		params.put("startResult", startResult);
		params.put("maxResults", maxResults);

		List<Map<String, String>> results = this.query(chemicalAnalysisQuery,
				params, new ChemicalAnalysisDataListMapper());

		return results;

	}

	/**
	 * Determine any project permissions based on the authen
	 * @return
	 */
	public List<String> buildProjectCriteria() {
		Collection<GrantedAuthority> authz = SecurityContextHolder.getContext()
				.getAuthentication().getAuthorities();

		List<String> projectNames = new ArrayList<String>();

		for (GrantedAuthority currentAuth : authz) {
			projectNames.add(currentAuth.getAuthority());
		}

		return projectNames;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
