package edu.rpi.metpetrest.dao;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

public class LoginDAOImpl extends JdbcDaoImpl {

	private static final String selectUserRolesProjects = "select users.user_id, users.email, users.password, projects.name as authority from users LEFT JOIN project_members ON (project_members.user_id = users.user_id) LEFT JOIN projects ON ( project_members.project_id = projects.project_id) where users.user_id = ? UNION select users.user_id, users.email, users.password, roles.role_name as authority from users LEFT JOIN users_roles ON ( users.user_id = users_roles.user_id) LEFT JOIN roles ON (users_roles.role_id = roles.role_id) where users.user_id = ?";

	/**
	 * Custom filter is what translates the hashed token to a userId
	 */
	public UserDetails loadUserByUsername(String userId) {
		return this.getJdbcTemplate().query(selectUserRolesProjects,
				new Object[] { userId, userId }, new UserCredentialsMapper());
	}

}
