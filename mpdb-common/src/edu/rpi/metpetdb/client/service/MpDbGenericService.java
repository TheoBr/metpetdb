package edu.rpi.metpetdb.client.service;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.model.UserDTO;

public interface MpDbGenericService extends RemoteService {

	String getBuildDate();
	UserDTO getAutomaticLoginUser();

}
