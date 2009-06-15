package edu.rpi.metpetdb.client.service;

import java.util.Set;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.MpDbException;

public interface ReferenceService extends RemoteService {
	Set<String> allReferences() throws MpDbException;
	
	Set<String> viewableReferencesForUser(final int userId) throws MpDbException;
}

