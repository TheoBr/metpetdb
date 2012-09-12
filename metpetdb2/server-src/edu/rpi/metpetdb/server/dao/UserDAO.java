package edu.rpi.metpetdb.server.dao;

import edu.rpi.metpetdb.server.model.User;

public interface UserDAO {

	public User authenticateUser(String username, byte[] password);
	
	public void saveUser(User user);
	
	public User loadUser(Long id);
}
