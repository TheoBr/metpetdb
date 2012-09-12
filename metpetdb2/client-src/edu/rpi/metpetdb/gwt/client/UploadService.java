package edu.rpi.metpetdb.gwt.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.rpi.metpetdb.gwt.client.dto.UploadRequestDTO;
import edu.rpi.metpetdb.gwt.client.dto.UserDTO;

@RemoteServiceRelativePath(value = "services/upload")
public interface UploadService extends RemoteService
{
	 public String upload(UploadRequestDTO uploadRequest, UserDTO user);
	
}
