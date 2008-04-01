package edu.rpi.metpetdb.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.model.SearchSampleDTO;

public interface SearchService extends RemoteService {
	List<SampleDTO> search(final SearchSampleDTO searchSamp);
}
