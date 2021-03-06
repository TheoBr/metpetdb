package edu.rpi.metpetdb.client.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;

/** @see SubsampleService */
public interface SubsampleServiceAsync {
	void all(PaginationParameters pp, final long sampleId,
			AsyncCallback<Results<Subsample>> ac);

	void allWithImages(PaginationParameters pp, final long sampleId,
			AsyncCallback<Results<Subsample>> ac);

	void details(long id, AsyncCallback<Subsample> ac);
	
	void detailsWithAnalyses(long id, AsyncCallback<Subsample> ac);
	
	void details(List<Long> ids, AsyncCallback<List<Subsample>> ac);

	void all(long sampleId, AsyncCallback<List<Subsample>> ac);
	
	void allIdsForSample(long sampleId, AsyncCallback< Map<Object,Boolean>> ac);
 
	void save(final Subsample s, AsyncCallback<Subsample> ac);

	void delete(long id, AsyncCallback<MObject> ac);
	
	void deleteAll(Collection<Long> ids, AsyncCallback<Void> ac);
	
	void saveAll(Collection<Subsample> subsamples, AsyncCallback<Void> ac);
	
	/**
	 * Maps a sample id to a list of subsamples
	 * @param sampleIds
	 * @param ac
	 */
	void allFromManySamples(final Collection<Long> sampleIds, AsyncCallback<Map<Long, List<Subsample>>> ac);
}
