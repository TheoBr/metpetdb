package edu.rpi.metpetdb.client.ui.objects.list;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Image;

import edu.rpi.metpetdb.client.locale.LocaleEntity;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.properties.SubsampleProperty;
import edu.rpi.metpetdb.client.paging.Column;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.MText;

public abstract class SubsampleListEx extends ListEx<Subsample> {

	public SubsampleListEx() {
		super(new ArrayList<Column>(Arrays.asList(columns)));
	}

	public SubsampleListEx(final String noResultsMessage) {
		super(new ArrayList<Column>(Arrays.asList(columns)), noResultsMessage);
	}

	private static final LocaleEntity enttxt = LocaleHandler.lc_entity;

	public static Column[] columns = {
			new Column(true, "Check", false, true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					return new CheckBox();
				}
			},
			new Column(true, enttxt.Subsample_name(), SubsampleProperty.name, true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					return new MLink(
							(String) data.mGet(SubsampleProperty.name),
							TokenSpace.detailsOf((Subsample) data));
				}
			},
			new Column(true,enttxt.Sample_publicData(), SubsampleProperty.publicData,
					false, true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					if ((Boolean) data.mGet(SubsampleProperty.publicData)) { 
						return new Image("images/checkmark.jpg"){
							public String toString(){
								return "True";
							}
						};
					}
					return new Image("images/xmark.jpg"){
						public String toString(){
							return "False";
						}
					};
				}
			},
			new Column(true, enttxt.Subsample_subsampleType(), SubsampleProperty.subsampleType, false, false),
			new Column(true, enttxt.Subsample_analysisCount(),
					SubsampleProperty.analysisCount, false, false),
			new Column(true, enttxt.Subsample_imageCount(),
					SubsampleProperty.imageCount,false, true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					return new MLink(String.valueOf(data.mGet(SubsampleProperty.imageCount)) +" Images", TokenSpace
							.ViewOf((Subsample) data));
				}
			},
			new Column(true, "Image Map", true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					final Subsample s = (Subsample) data;
					if (s.getGrid() == null) {
						//Only the owner should be able to create an image map
						if(s.getOwner() != null && MpDb.currentUser() != null && 
							s.getOwner().equals(MpDb.currentUser())){
								return new MLink("Create Map", TokenSpace
										.createNewImageMap(s));
						} else {
							return new MText();
						}
					} else {
						return new MLink("View Map", TokenSpace.detailsOf(s
								.getGrid()));
					}
				}
			},
			
	};

	public String getDefaultSortParameter() {
		return SubsampleProperty.name.name();
	}

	public abstract void update(final PaginationParameters p,
			final AsyncCallback<Results<Subsample>> ac);
}
