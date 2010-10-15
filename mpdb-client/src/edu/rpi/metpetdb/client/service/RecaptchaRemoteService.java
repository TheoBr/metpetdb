package edu.rpi.metpetdb.client.service;

import com.google.gwt.user.client.rpc.RemoteService;


public interface RecaptchaRemoteService extends RemoteService {

	Boolean verifyChallenge(String challenge, String response);
}
