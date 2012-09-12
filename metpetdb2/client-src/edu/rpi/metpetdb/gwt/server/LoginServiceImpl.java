package edu.rpi.metpetdb.gwt.server;

import org.gwtwidgets.server.spring.ServletUtils;

import edu.rpi.metpetdb.gwt.client.LoginService;
import edu.rpi.metpetdb.gwt.client.dto.LoginRequestDTO;
import edu.rpi.metpetdb.gwt.client.dto.UserDTO;
import edu.rpi.metpetdb.server.dao.UserDAO;
import edu.rpi.metpetdb.server.model.User;

public class LoginServiceImpl implements LoginService {
	

	private UserDAO userDAO = null;
	
	public LoginServiceImpl()
	{
		
	}
	
	@Override	
	public UserDTO login(LoginRequestDTO loginRequestDTO) {

		User user = userDAO.authenticateUser(loginRequestDTO.getUsername(), loginRequestDTO.getPassword().getBytes());
		
		if (user != null)
			{
			ServletUtils.getRequest().getSession().setAttribute("userId", user.getUser_id() );
			
			//TODO:  Get user pieces and put them in the DTO
			return new UserDTO();
			
			//return "Login successful";
			}
		else
		{
			return new UserDTO("Invalid login."); 
		}
		
	
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	
	
}
