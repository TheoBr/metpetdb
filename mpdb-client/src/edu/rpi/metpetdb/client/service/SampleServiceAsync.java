package edu.rpi.metpetdb.client.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;

/** @see SampleService */
public interface SampleServiceAsync {
	void all(PaginationParameters parameters, AsyncCallback<Results<Sample>> ac);

	void allPublicSamples(PaginationParameters p,
			AsyncCallback<Results<Sample>> ac);

	void allSamplesForUser(PaginationParameters p, long id,
			AsyncCallback<Results<Sample>> ac);

	void projectSamples(PaginationParameters p, long id,
			AsyncCallback<Results<Sample>> ac);

	void details(long id, AsyncCallback<Sample> ac);
	
	void details(List<Long> ids, AsyncCallback<List<Sample>> ac);
	
	void allIdsForUser(long id, AsyncCallback<Map<Object,Boolean>> ac);

	void save(Sample sample, AsyncCallback<Sample> ac);
	
	void saveAll(Collection<Sample> samples, AsyncCallback<Void> ac);

	void delete(long id, AsyncCallback<MObject> ac);
	
	void deleteAll(Collection<Long> ids, AsyncCallback<Void> ac);
	
	void allCollectors(AsyncCallback<Set<String>> ac);
	
	void allCountries(AsyncCallback<Set<String>> ac);
	
	void viewableCountriesForUser(int userId, AsyncCallback<Set<String>> ac);
	
	void viewableCollectorsForUser(int userId, AsyncCallback<Set<String>> ac);
	
	void allSamplesForUser(long id, AsyncCallback<List<Sample>> ac);

	void allSamplesForProject(PaginationParameters p, long projectId,
			AsyncCallback<Results<Sample>> ac);
	
	void canEdit(long id, AsyncCallback<Boolean> ac);
	
	void getPublicationCount(AsyncCallback<Long> ac);
	
	void getPublicCount(AsyncCallback<Long> ac);
	
	void getPrivateCount(AsyncCallback<Long> ac);
	
	void sampleMetamorphicRegionsRetroactive(AsyncCallback<Void> ac);
}
