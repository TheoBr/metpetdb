package edu.rpi.metpetrest.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import edu.rpi.metpetrest.model.UserData;

public class UserMapper implements RowMapper<UserData> {

	@Override
	public UserData mapRow(ResultSet rs, int arg1) throws SQLException {

		UserData userData = new UserData();
		userData.setUserId(rs.getLong(1));
		userData.setName(rs.getString(2));
		userData.setInstitution(rs.getString(3));
		userData.setProfessionalUrl(rs.getString(4));

		return userData;
	}

}
