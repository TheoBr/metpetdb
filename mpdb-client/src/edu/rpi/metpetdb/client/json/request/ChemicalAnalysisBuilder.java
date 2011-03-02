package edu.rpi.metpetdb.client.json.request;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.json.client.JSONArray;

import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.paging.Results;

public class ChemicalAnalysisBuilder {

	public static Results<ChemicalAnalysis> buildChemicalAnalyses(JSONArray json) {
		Results<ChemicalAnalysis> results = new Results<ChemicalAnalysis>();
		List<ChemicalAnalysis> list = new ArrayList<ChemicalAnalysis>();
		results.setList(list);
		return results;
	}

}
