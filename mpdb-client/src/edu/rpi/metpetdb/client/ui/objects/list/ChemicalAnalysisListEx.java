package edu.rpi.metpetdb.client.ui.objects.list;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleEntity;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.Mineral;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.properties.ChemicalAnalysisProperty;
import edu.rpi.metpetdb.client.paging.Column;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.image.browser.dialogs.ViewImage;
import edu.rpi.metpetdb.client.ui.input.attributes.DateAttribute;
import edu.rpi.metpetdb.client.ui.widgets.ImageHyperlink;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.MText;

public abstract class ChemicalAnalysisListEx extends ListEx<ChemicalAnalysis> {

	public ChemicalAnalysisListEx() {
		super(new ArrayList<Column>(Arrays.asList(columns)));
	}

	public ChemicalAnalysisListEx(final String noResultsMessage) {
		super(new ArrayList<Column>(Arrays.asList(columns)), noResultsMessage);
	}

	private static final LocaleEntity enttxt = LocaleHandler.lc_entity;

	public static Column[] columns = {
			new Column(true,enttxt.ChemicalAnalysis_spotId(),
					ChemicalAnalysisProperty.spotId, true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					return new MLink((String) data
							.mGet(ChemicalAnalysisProperty.spotId), TokenSpace
							.detailsOf((ChemicalAnalysis) data));
				}
			},
			new Column(true,enttxt.ChemicalAnalysis_method(),
					ChemicalAnalysisProperty.analysisMethod, true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					String text = ((String) data
							.mGet(ChemicalAnalysisProperty.analysisMethod));
					if (text == null)
						text = "------";
					return new MText(text);
				}
			},
			new Column(true,enttxt.ChemicalAnalysis_mineral(),
					ChemicalAnalysisProperty.mineral, true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					String text = enttxt.ChemicalAnalysis_largeRock();
					Boolean largeRock = (Boolean)data.mGet(ChemicalAnalysisProperty.largeRock);
					if (!largeRock){
					text = ((Mineral) data
							.mGet(ChemicalAnalysisProperty.mineral)).getName();
					}
					return new MText(text);
				}
			},	
			new Column(true,enttxt.ChemicalAnalysis_location(),
					ChemicalAnalysisProperty.location, true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					String text = ((String) data
							.mGet(ChemicalAnalysisProperty.location));
					if (text == null)
						text = "------";
					return new MText(text);
				}
			},
			new Column(true,enttxt.ChemicalAnalysis_analyst(),
					ChemicalAnalysisProperty.analyst, true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					String text = ((String) data
							.mGet(ChemicalAnalysisProperty.analyst));
					if (text == null)
						text = "------";
					return new MText(text);
				}
			},
			new Column(true,enttxt.ChemicalAnalysis_analysisDate(),
					ChemicalAnalysisProperty.analysisDate, true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					DateAttribute temp = new DateAttribute(
							MpDb.doc.ChemicalAnalysis_analysisDate,
							MpDb.doc.ChemicalAnalysis_datePrecision);
					return ((MText) temp.createDisplayWidget(data)[0])
							.getText();
				}
			},
			new Column(true,enttxt.ChemicalAnalysis_reference(),
					ChemicalAnalysisProperty.reference, true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					String text = ((String) data
							.mGet(ChemicalAnalysisProperty.reference));
					if (text == null)
						text = "------";
					return new MText(text);
				}
			},
			new Column(true,enttxt.ChemicalAnalysis_pointX(),
					ChemicalAnalysisProperty.pointX),
			new Column(true,enttxt.ChemicalAnalysis_pointY(),
					ChemicalAnalysisProperty.pointY),	
			new Column(true,enttxt.ChemicalAnalysis_image(),
					ChemicalAnalysisProperty.image, false, true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					
					if (data.mGet(ChemicalAnalysisProperty.image) != null){
						final Image currentImage = (Image) (data.mGet(ChemicalAnalysisProperty.image));
						final com.google.gwt.user.client.ui.Image image = new com.google.gwt.user.client.ui.Image();
						image.setUrl(currentImage.get64x64ServerPath());
						final com.google.gwt.user.client.ui.Image bigImage = new com.google.gwt.user.client.ui.Image();
						bigImage.setUrl(currentImage.getServerPath());
						final ArrayList imagesToDisplay = new ArrayList();
						imagesToDisplay.add(currentImage);
							return new ImageHyperlink(image, new ClickListener() {
								public void onClick(final Widget sender) {
									new ViewImage(imagesToDisplay, bigImage, 0).show();
								}
							});
					}
					else return new MText("------");
				}
			},
	};

	@Override
	public String getDefaultSortParameter() {
		// TODO Auto-generated method stub
		return ChemicalAnalysisProperty.analysisDate.name();
	}

	@Override
	public abstract void update(PaginationParameters p,
			AsyncCallback<Results<ChemicalAnalysis>> ac);

}
