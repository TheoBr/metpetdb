package edu.rpi.metpetdb.client.ui.objects.list;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.locale.LocaleEntity;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisDTO;
import edu.rpi.metpetdb.client.model.properties.ChemicalAnalysisProperty;
import edu.rpi.metpetdb.client.paging.Column;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;

public class ChemicalAnalysisListEx extends ListEx<ChemicalAnalysisDTO> {

	public ChemicalAnalysisListEx() {
		super(columns);
	}

	private static final LocaleEntity enttxt = LocaleHandler.lc_entity;

	// new Column(enttxt.MineralAnalysis_spotId(), "spotId"),
	// new Column(enttxt.MineralAnalysis_sample(), "spotId"), // not ordered
	// properly
	// new Column(enttxt.MineralAnalysis_subsample(), "spotId"), // not ordered
	// properly
	// new Column(enttxt.MineralAnalysis_pointX(), "pointX"),
	// new Column(enttxt.MineralAnalysis_pointY(), "pointY"),
	// new Column(enttxt.MineralAnalysis_method(), "method"),
	// new Column(enttxt.MineralAnalysis_location(), "location"),
	// new Column(enttxt.MineralAnalysis_analyst(), "analyst"),
	// new Column(enttxt.MineralAnalysis_analysisDate(),
	// "analysisDate"),
	public static Column[] columns = {
			new Column(enttxt.ChemicalAnalysis_spotId(),
					ChemicalAnalysisProperty.spotId),

			// not ordered properly
			new Column(enttxt.ChemicalAnalysis_sample()),

			// not ordered properly
			new Column(enttxt.ChemicalAnalysis_subsample()),
			new Column(enttxt.ChemicalAnalysis_pointX(),
					ChemicalAnalysisProperty.pointX),
			new Column(enttxt.ChemicalAnalysis_pointY(),
					ChemicalAnalysisProperty.pointY),
			new Column(enttxt.ChemicalAnalysis_method(),
					ChemicalAnalysisProperty.analysisMethod),
			new Column(enttxt.ChemicalAnalysis_location(),
					ChemicalAnalysisProperty.location),
			new Column(enttxt.ChemicalAnalysis_analyst(),
					ChemicalAnalysisProperty.analyst),
			new Column(enttxt.ChemicalAnalysis_analysisDate(),
					ChemicalAnalysisProperty.analysisDate),
	};

	@Override
	public String getDefaultSortParameter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(PaginationParameters p,
			AsyncCallback<Results<ChemicalAnalysisDTO>> ac) {
		// TODO Auto-generated method stub

	}

}
