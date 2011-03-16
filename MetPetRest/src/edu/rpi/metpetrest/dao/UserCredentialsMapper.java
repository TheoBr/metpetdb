package edu.rpi.metpetrest.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class UserCredentialsMapper implements ResultSetExtractor<UserDetails> {

	@Override
	public UserDetails extractData(ResultSet rs) throws SQLException,
			DataAccessException {

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		String username = null;
		String userId = null;

		while (rs.next()) {

			username = rs.getString("email");
			userId = rs.getString("user_id");
			if (rs.getString("authority") != null
					&& !rs.getString("authority").equals("")) {
				GrantedAuthority ga = new GrantedAuthorityImpl(
						rs.getString("authority"));
				authorities.add(ga);
			}

		}

		//TODO:  Remove later as long as you edit services-servlet.xml to reflect same
		authorities.add(new GrantedAuthorityImpl("ROLE_USER"));

		User user = new User(username, userId, true, true, true, true,
				authorities);

		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(user, userId,
						authorities));

		return user;

	}

}