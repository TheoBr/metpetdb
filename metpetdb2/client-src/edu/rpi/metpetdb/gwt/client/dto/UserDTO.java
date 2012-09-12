package edu.rpi.metpetdb.gwt.client.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.validation.client.interfaces.IValidatable;


public class UserDTO implements IValidatable, Serializable {

	private List<String> errors = new ArrayList<String>();

	private Long userId;
	
	public UserDTO() {


	}

	public UserDTO(String userError) {
		this.errors.add(userError);
	}

	public void addError(String error) {
		this.addError(error);
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
