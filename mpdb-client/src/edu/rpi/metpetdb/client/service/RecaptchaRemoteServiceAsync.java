package edu.rpi.metpetdb.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RecaptchaRemoteServiceAsync {

	void verifyChallenge(String challenge, String response, AsyncCallback<Boolean> callback);
}
