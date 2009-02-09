package edu.rpi.metpetdb.client.service;

import java.util.Set;

import com.google.gwt.user.client.rpc.RemoteService;

import edu.rpi.metpetdb.client.error.MpDbException;

public interface MetamorphicGradeService extends RemoteService {
	Set<String> allMetamorphicGrades() throws MpDbException;
}

