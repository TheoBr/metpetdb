package edu.rpi.metpetdb.gwt.client.dto;

import java.io.Serializable;

import com.google.gwt.validation.client.NotEmpty;
import com.google.gwt.validation.client.NotNull;
import com.google.gwt.validation.client.interfaces.IValidatable;

@SuppressWarnings("serial")
public class LoginRequestDTO implements IValidatable, Serializable  {

	@NotNull
	@NotEmpty
	private String username;
	
	@NotNull
	@NotEmpty
	private String password;
	
	
	public LoginRequestDTO()
	{
		
	}
	
	public LoginRequestDTO(String username, String password)
	{
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
