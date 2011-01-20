package edu.rpi.metpetrest.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class IdMapper implements RowMapper<Long> {

	@Override
	public Long mapRow(ResultSet rs, int arg1) throws SQLException {		
		return rs.getLong(1);
	
	}
	
	

}
