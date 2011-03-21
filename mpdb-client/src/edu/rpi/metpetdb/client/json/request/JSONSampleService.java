package edu.rpi.metpetdb.client.json.request;

import java.util.ArrayList;

import com.gargoylesoftware.htmlunit.util.Cookie;
import com.google.gwt.core.client.GWT;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.SampleArray;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.util.ServicesConstants;

public class JSONSampleService {

	private ServicesConstants constants = GWT.create(ServicesConstants.class);

	
	public void allSamplesForUser(PaginationParameters parameters,
			final AsyncCallback<Results<Sample>> ac)
	{
		
		
		
		for (String cookieName : Cookies.getCookieNames())
		{
		
			String cookieVal = Cookies.getCookie(cookieName);
			
			System.out.println(cookieName + ":" + cookieVal);
		
		}
		
		
		//TODO: Externalize the resource
		String requestUrl =  constants.serviceUrl()  + "/secure/mysamples/"
				+ parameters.getFirstResult()
				+ "/"
				+ parameters.getMaxResults() + ".json" + "?identity=" + Cookies.getCookie("identity");

		JsonpRequestBuilder builder = new JsonpRequestBuilder();
		builder.setTimeout(120000);

		
		
		builder.requestObject(requestUrl,
				new AsyncCallback<SampleArray>() {

					public void onFailure(Throwable caught) {
						Window.alert(caught.toString());
						caught.printStackTrace();
					}

					public void onSuccess(SampleArray cal) {

						Results<Sample> rs = new Results<Sample>();
						rs.setList(new ArrayList<Sample>());

						for (int i = 0; i < cal.getSamples().length(); i++) {
							rs.getList().add(
									new Sample(cal
											.getSamples().get(i)));
							rs.setCount(Integer.valueOf(cal
									.getSamples().get(i).getCount()));
						}

						ac.onSuccess(rs);

					}
				});
	}
	
	public void allIdsForUser(Long id, AsyncCallback<Results<Sample>> ac)
	{
		
	}
	
}
