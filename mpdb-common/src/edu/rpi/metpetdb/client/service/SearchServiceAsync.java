package edu.rpi.metpetdb.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.model.SearchSampleDTO;
import edu.rpi.metpetdb.client.paging.Results;

public interface SearchServiceAsync {
	void search(final SearchSampleDTO searchSamp,
			AsyncCallback<Results<SampleDTO>> ac);
}
