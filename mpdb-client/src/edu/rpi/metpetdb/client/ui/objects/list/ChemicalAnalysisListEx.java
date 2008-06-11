package edu.rpi.metpetdb.client.ui.objects.list;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.locale.LocaleEntity;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisDTO;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.properties.ChemicalAnalysisProperty;
import edu.rpi.metpetdb.client.paging.Column;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.input.attributes.DateAttribute;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.MText;

public abstract class ChemicalAnalysisListEx extends
		ListEx<ChemicalAnalysisDTO> {

	public ChemicalAnalysisListEx() {
		super(columns);
	}

	private static final LocaleEntity enttxt = LocaleHandler.lc_entity;

	public static Column[] columns = {
			new Column(enttxt.ChemicalAnalysis_spotId(),
					ChemicalAnalysisProperty.spotId, true) {
				protected Object getWidget(final MObjectDTO data,
						final int currentRow) {
					return new MLink((String) data
							.mGet(ChemicalAnalysisProperty.spotId), TokenSpace
							.detailsOf((ChemicalAnalysisDTO) data));
				}
			},

			// not ordered properly
			// new Column(enttxt.ChemicalAnalysis_sample(),
			// ChemicalAnalysisProperty.sampleName),
			//
			// // not ordered properly
			// new Column(enttxt.ChemicalAnalysis_subsample(),
			// ChemicalAnalysisProperty.subsampleName),
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
					ChemicalAnalysisProperty.analysisDate, true) {
				protected Object getWidget(final MObjectDTO data,
						final int currentRow) {
					DateAttribute temp = new DateAttribute(
							MpDb.doc.ChemicalAnalysis_analysisDate,
							MpDb.doc.ChemicalAnalysis_datePrecision);
					return ((MText) temp.createDisplayWidget(data)[0])
							.getText();
				}
			}
	};

	@Override
	public String getDefaultSortParameter() {
		// TODO Auto-generated method stub
		return ChemicalAnalysisProperty.analysisDate.name();
	}

	@Override
	public abstract void update(PaginationParameters p,
			AsyncCallback<Results<ChemicalAnalysisDTO>> ac);

}
