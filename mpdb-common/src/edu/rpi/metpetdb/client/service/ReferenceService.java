package edu.rpi.metpetdb.client.service;

import java.util.Set;

import com.google.gwt.user.client.rpc.RemoteService;

public interface ReferenceService extends RemoteService {
	Set<String> allReferences();
}

