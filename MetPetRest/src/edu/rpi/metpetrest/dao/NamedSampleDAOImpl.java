package edu.rpi.metpetrest.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class NamedSampleDAOImpl extends NamedParameterJdbcTemplate implements
		ApplicationContextAware {

	private DataSource dataSource = null;

	private ApplicationContext applicationContext;

	public NamedSampleDAOImpl(DataSource dataSource) {
		super(dataSource);
	}

	private static final String getSampleQuery = "select samples.sample_id, samples.number, samples.country, samples.public_data, (select count(image_id) from images where sample_id = samples.sample_id) as image_count, (select count(chemical_analysis_id) from chemical_analyses where subsample_id IN (select distinct(subsample_id) from subsamples where sample_id = samples.sample_id)) as analysis_count, (select count(subsample_id) from subsamples where sample_id = samples.sample_id) as subsample_count, (select name from users where user_id = samples.user_id) as owner, (select rock_type.rock_type from rock_type where rock_type_id = samples.rock_type_id ) as rock_type, collection_date, collector, metamorphic_grades.name as grade_name, location , location_text, :count as count from samples LEFT OUTER JOIN sample_metamorphic_grades ON ( samples.sample_id = sample_metamorphic_grades.sample_id) LEFT OUTER JOIN metamorphic_grades ON (sample_metamorphic_grades.metamorphic_grade_id = metamorphic_grades.metamorphic_grade_id) where samples.sample_id IN (:sample_ids) and samples.user_id IN (:userId, (select user_id from users where name = 'PUBLICATION')) order by sample_id asc LIMIT :maxResults OFFSET :startResult";

	public List<Map<String, Object>> getSampleSearch(List<Long> sampleIds,
			Long maxResults, Long startResult) {
		String userId = ((UserDetails) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal()).getPassword();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", Long.valueOf(userId));
		
		Long topSlice = startResult + maxResults;
		
		
		//Check to make sure you don't step over the List size limit
		if (topSlice > Long.valueOf(sampleIds.size()))
			topSlice = (long)sampleIds.size();
		//end if
		
		params.put("sample_ids", sampleIds.subList(startResult.intValue(), topSlice.intValue()));
		params.put("maxResults", (topSlice - startResult));
		params.put("startResult", 0);
		params.put("count", sampleIds.size());

		return this
				.query(getSampleQuery,
						params,
						((ResultSetExtractor<List<Map<String, Object>>>) applicationContext
								.getBean("sampleMineralMapper")));
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

}
