package edu.rpi.metpetdb.client.json.request;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisArray;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.util.ServicesConstants;

public class JSONChemicalAnalysisService

{

	private ServicesConstants constants = GWT.create(ServicesConstants.class);
	
	public void all(PaginationParameters parameters, long subsampleId,
			final AsyncCallback<Results<ChemicalAnalysis>> ac) {

		//TODO: Externalize the resource
		
		final String identity;
		
		if (Cookies.getCookie("identity") == null)
		{
			identity = "ANONYMOUS";
		}
		else
		{
			identity = Cookies.getCookie("identity");
		}
	
		String requestUrl =  constants.serviceUrl()  + "/secure/chemical_analyses/"
				+ subsampleId
				+ "/"
				+ parameters.getFirstResult()
				+ "/"
				+ parameters.getMaxResults() + ".json" + "?identity=" + identity;

		JsonpRequestBuilder builder = new JsonpRequestBuilder();
		builder.setTimeout(120000);

		
		
		builder.requestObject(requestUrl,
				new AsyncCallback<ChemicalAnalysisArray>() {

					public void onFailure(Throwable caught) {
						Window.alert(caught.toString());
						caught.printStackTrace();
					}

					public void onSuccess(ChemicalAnalysisArray cal) {

						Results<ChemicalAnalysis> rs = new Results<ChemicalAnalysis>();
						rs.setList(new ArrayList<ChemicalAnalysis>());

						for (int i = 0; i < cal.getChemicalAnalyses().length(); i++) {
							rs.getList().add(
									new ChemicalAnalysis(cal
											.getChemicalAnalyses().get(i)));
							rs.setCount(Integer.valueOf(cal
									.getChemicalAnalyses().get(i).getCount()));
						}

						ac.onSuccess(rs);

					}
				});

	}
	public void all(long subsampleId, AsyncCallback<List<ChemicalAnalysis>> ac) {
	// TODO Auto-generated method stub

	}

	public void allFromManySubsamples(Collection<Long> subsampleIds,
			AsyncCallback<Map<Long, List<ChemicalAnalysis>>> ac) {
	// TODO Auto-generated method stub

	}

	public void allIdsForSubsample(long subsampleId,
			AsyncCallback<Map<Object, Boolean>> ac) {
	// TODO Auto-generated method stub

	}

	public void delete(long id, AsyncCallback<MObject> ac) {
	// TODO Auto-generated method stub

	}

	public void deleteAll(Collection<Integer> ids, AsyncCallback<Void> ac) {
	// TODO Auto-generated method stub

	}

	public void details(long id, AsyncCallback<ChemicalAnalysis> ac) {
	// TODO Auto-generated method stub

	}

	public void details(List<Integer> ids,
			AsyncCallback<List<ChemicalAnalysis>> ac) {
	// TODO Auto-generated method stub

	}

	public void getPrivateCount(AsyncCallback<Long> ac) {
	// TODO Auto-generated method stub

	}

	public void getPublicCount(AsyncCallback<Long> ac) {
	// TODO Auto-generated method stub

	}

	public void getPublicationCount(AsyncCallback<Long> ac) {
	// TODO Auto-generated method stub

	}

	public void makePublic(
			ArrayList<ChemicalAnalysis> selectedChemicalAnalyses,
			AsyncCallback<Void> ac) {
	// TODO Auto-generated method stub

	}

	public void save(ChemicalAnalysis chemicalAnalysis,
			AsyncCallback<ChemicalAnalysis> ac) {
	// TODO Auto-generated method stub

	}

	public void saveAll(Collection<ChemicalAnalysis> chemicalAnalyses,
			AsyncCallback<Void> ac) {
	// TODO Auto-generated method stub

	}

}
