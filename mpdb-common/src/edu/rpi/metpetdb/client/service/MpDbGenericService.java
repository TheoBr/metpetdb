package edu.rpi.metpetdb.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.model.ResumeSessionResponse;
import edu.rpi.metpetdb.client.model.User;

public interface MpDbGenericService extends RemoteService {

	String getBuildDate();

	User getAutomaticLoginUser();

	ResumeSessionResponse regenerateConstraints();
	
	long getCurrentTime();

	List<List> getStatistics();
}
