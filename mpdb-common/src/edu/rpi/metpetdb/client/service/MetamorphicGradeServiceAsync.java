package edu.rpi.metpetdb.client.service;

import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MetamorphicGradeServiceAsync {
	void allMetamorphicGrades(AsyncCallback<Set<String>> ac);
}
