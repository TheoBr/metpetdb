package edu.rpi.metpetrest.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import edu.rpi.metpetrest.dao.mapper.PublicationDataMapper;
import edu.rpi.metpetrest.model.PublicationData;

public class PublicationDAOImpl extends JdbcTemplate {

	
	private static final String publicationQuery = "select title, first_author, second_authors, journal_name, full_text as abstract, reference_number, reference_id, journal_name_2, doi from georeference;";

	private DataSource dataSource = null;
	
	public List<PublicationData> getAllPublications()
	{
		return this.query(publicationQuery, new PublicationDataMapper());
		
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
