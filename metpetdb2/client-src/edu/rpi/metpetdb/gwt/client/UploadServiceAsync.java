package edu.rpi.metpetdb.gwt.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.gwt.client.dto.UploadRequestDTO;
import edu.rpi.metpetdb.gwt.client.dto.UserDTO;

public interface UploadServiceAsync {

	void upload(UploadRequestDTO uploadRequest, UserDTO userDTO,  AsyncCallback<String> callback)
	throws IllegalArgumentException;
}
