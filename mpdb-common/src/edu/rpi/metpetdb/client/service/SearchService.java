package edu.rpi.metpetdb.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.security.NoPermissionsException;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.SearchSample;
import edu.rpi.metpetdb.client.model.User;

public interface SearchService extends RemoteService {
	List<Sample> search(final SearchSample searchSamp, User userSearching) throws NoPermissionsException;
}
