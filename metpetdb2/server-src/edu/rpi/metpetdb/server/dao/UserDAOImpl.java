package edu.rpi.metpetdb.server.dao;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.rpi.metpetdb.server.model.User;

public class UserDAOImpl extends HibernateDaoSupport implements UserDAO {

	@Override
	public User authenticateUser(String username, byte[] password) {
		List<User> userFound = this.getHibernateTemplate().find(
				"from User user where user.name = ? ",
				new Object[] { username });

		// TODO: Learn more about the encryption class
		// List<User> userFound =
		// this.getHibernateTemplate().find("from User user where user.name = ? and user.encryptedPassword = ?",
		// new Object[] {username, password});

		if (userFound != null && userFound.size() == 1)
			return userFound.get(0);
		else
			return null;
		// end if

	}

	@Override
	public void saveUser(User user) {
		this.getSession().save(user);

		this.getSession().flush();

	}

	public User loadUser(Long id) {
		return (User) this.getSession().load(User.class, id);
	}

}
