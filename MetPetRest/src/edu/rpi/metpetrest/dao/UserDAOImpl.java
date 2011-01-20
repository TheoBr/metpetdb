package edu.rpi.metpetrest.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import edu.rpi.metpetrest.dao.mapper.UserMapper;
import edu.rpi.metpetrest.model.UserData;

public class UserDAOImpl extends JdbcTemplate {

	private static final String userListQuery = "select user_id, name, institution, professional_url from users where user_id IN ( select distinct(samples.user_id) from samples, users where samples.user_id = users.user_id AND samples.public_data = 'Y' AND users.name <> \'PUBLICATION\')";

	public List<UserData> getAllUsersExceptPUBLICATION() {
		return this.query(userListQuery, new UserMapper());

	}
}
