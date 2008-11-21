package edu.rpi.metpetdb.client.service;

import java.util.Set;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.DAOException;

public interface ReferenceService extends RemoteService {
	Set<String> allReferences() throws DAOException;
}

