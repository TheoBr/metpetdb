package edu.rpi.metpetrest.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import edu.rpi.metpetrest.model.UserSampleData;

public class UserSampleDataMapper implements RowMapper<UserSampleData> {

	@Override
	public UserSampleData mapRow(ResultSet rs, int arg1) throws SQLException {

		UserSampleData userSampleData = new UserSampleData();
		userSampleData.setSampleId(rs.getString(1));

		userSampleData.setUserId(rs.getString(2));

		userSampleData.setSampleOwner(rs.getString(3));

		return userSampleData;
	}

}
