package edu.rpi.metpetdb.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.model.SearchSampleDTO;

public interface SearchServiceAsync {
	void search(final SearchSampleDTO searchSamp,
			AsyncCallback<List<SampleDTO>> ac);
}
