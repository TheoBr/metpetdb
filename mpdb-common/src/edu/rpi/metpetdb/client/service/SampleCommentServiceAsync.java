package edu.rpi.metpetdb.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.SampleComment;

public interface SampleCommentServiceAsync {
	
	void details(long id, AsyncCallback<SampleComment> ac);

	void all(long sampleId, AsyncCallback<List<SampleComment>> ac);

	void save(final SampleComment s, AsyncCallback<SampleComment> ac);

	void delete(long id, AsyncCallback<Object> ac);

}
