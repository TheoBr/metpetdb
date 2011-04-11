package edu.rpi.metpetdb.server.json.request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import edu.rpi.metpetdb.client.model.MetamorphicGrade;
import edu.rpi.metpetdb.client.model.MetamorphicRegion;
import edu.rpi.metpetdb.client.model.Mineral;
import edu.rpi.metpetdb.client.model.Reference;
import edu.rpi.metpetdb.client.model.Region;
import edu.rpi.metpetdb.client.model.RockType;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.SampleMineral;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.util.ServicesConstants;
import edu.rpi.metpetdb.server.security.SessionEncrypter;

public class JSONSampleSearchService {

	private Properties props = new Properties();

	public JSONSampleSearchService() {
		try {
			props.load(ServicesConstants.class
					.getResourceAsStream("/edu/rpi/metpetdb/client/util/ServicesConstants.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public Results<Sample> getSamples(PaginationParameters parameters,
			String sampleIds, User u) {

		final String identity;

		if (u == null) {
			identity = "ANONYMOUS";
		} else {
			identity = SessionEncrypter.crypt(u.getId());
		}

		// TODO: Externalize the resource
		String requestUrl = props.get("serviceUrl")
				+ "/secure/search_samples/" + parameters.getFirstResult() + "/"
				+ parameters.getMaxResults() + ".json" + "?identity="
				+ identity + "&ids=" + sampleIds;

		ClientResource res = new ClientResource(requestUrl);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			res.get(MediaType.APPLICATION_JSON).write(baos);
		} catch (ResourceException e2) {
			e2.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		JsonRepresentation rep = new JsonRepresentation(new String(baos
				.toByteArray()));

		Results<Sample> rs = new Results<Sample>();
		rs.setList(new ArrayList<Sample>());

		int jsonArrayLength = 0;
		JSONArray searchSamples = null;

		try {
			searchSamples = ((JSONObject) rep.getJsonObject())
					.getJSONArray("searchSamples");

			jsonArrayLength = searchSamples.length();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		for (int i = 0; i < jsonArrayLength; i++) {

			try {

				JSONObject currSample = searchSamples.getJSONObject(i);
				Sample sample = new Sample();

				if (currSample.get("sampleId") != null)
					sample.setId(currSample.getLong("sampleId"));

				if (currSample.get("sampleNumber") != null)
					sample.setNumber(currSample.getString("sampleNumber"));

				String publicData = currSample.getString("publicData");

				if (publicData != null && publicData.equals("Y"))
					sample.setPublicData(Boolean.TRUE);
				else if (publicData != null && publicData.equals("N"))
					sample.setPublicData(Boolean.FALSE);
				else
					sample.setPublicData(null);
				// end if

				sample.setRockType(new RockType(currSample
						.getString("rockType")));

				sample.setImageCount(Integer.valueOf(currSample
						.getString("imageCount")));
				sample.setAnalysisCount(Integer.valueOf(currSample
						.getString("analysisCount")));
				sample.setSubsampleCount(Integer.valueOf(currSample
						.getString("subsampleCount")));

				sample.setCountry(currSample.getString("country"));

				if (currSample.get("collector") != null
						&& !currSample.getString("collector").equals("null"))
					sample.setCollector(currSample.getString("collector"));

				sample.setMetamorphicGrades(new HashSet<MetamorphicGrade>());

				if (currSample.get("gradeName") != null
						&& !currSample.getString("gradeName").equals("null"))
					sample.getMetamorphicGrades().add(
							new MetamorphicGrade(currSample
									.getString("gradeName")));

				if (currSample.get("locationText") != null
						&& !currSample.getString("locationText").equals("null"))
					sample
							.setLocationText(currSample
									.getString("locationText"));

				sample.setLongitude(Double.valueOf(currSample
						.getString("longitude")));
				sample.setLatitude(Double.valueOf(currSample
						.getString("latitude")));

				JSONArray jsonMinerals = currSample.getJSONArray("minerals");
				sample.setMinerals(new HashSet<SampleMineral>());

				for (int j = 0; j < jsonMinerals.length(); j++) {
					sample.getMinerals().add(
							new SampleMineral(new Mineral((String) jsonMinerals
									.get(j))));
				}

				JSONArray jsonRegions = currSample.getJSONArray("regions");
				sample.setRegions(new HashSet<Region>());

				for (int j = 0; j < jsonRegions.length(); j++) {
					sample.getRegions().add(
							new Region((String) jsonRegions.get(j)));
				}

				JSONArray jsonMetRegions = currSample
						.getJSONArray("metamorphicRegions");
				sample.setMetamorphicRegions(new HashSet<MetamorphicRegion>());

				for (int j = 0; j < jsonMetRegions.length(); j++) {
					sample.getMetamorphicRegions().add(
							new MetamorphicRegion((String) jsonMetRegions
									.get(j)));
				}

				JSONArray jsonReferences = currSample
						.getJSONArray("references");
				sample.setReferences(new HashSet<Reference>());

				for (int j = 0; j < jsonReferences.length(); j++) {
					sample.getReferences().add(
							new Reference((String) jsonReferences.get(j)));
				}

				sample.setOwner(new User(currSample.getString("owner")));

				if (currSample.getString("collectionDate") != null) {
					try {
						sample.setCollectionDate(Timestamp.valueOf(currSample
								.getString("collectionDate")));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				rs.getList().add(sample);

				Integer count = Integer.valueOf(currSample.getString("count"));

				if (count != null)
					rs.setCount(count);
				else
					System.out.println("no count");

			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		res.release();

		return rs;
	}
}
