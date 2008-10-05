package edu.rpi.metpetdb.client.ui.objects.list;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;

import edu.rpi.metpetdb.client.locale.LocaleEntity;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.properties.SubsampleProperty;
import edu.rpi.metpetdb.client.paging.Column;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.widgets.MLink;

public abstract class SubsampleListEx extends ListEx<Subsample> {

	public SubsampleListEx() {
		super(new ArrayList<Column>(Arrays.asList(columns)));
	}

	public SubsampleListEx(final String noResultsMessage) {
		super(new ArrayList<Column>(Arrays.asList(columns)), noResultsMessage);
	}

	private static final LocaleEntity enttxt = LocaleHandler.lc_entity;

	public static Column[] columns = {
			new Column("Check", true, true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					return new CheckBox();
				}
			},
			new Column(enttxt.Subsample_name(), SubsampleProperty.name, true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					return new MLink(
							(String) data.mGet(SubsampleProperty.name),
							TokenSpace.detailsOf((Subsample) data));
				}
			},
			new Column(enttxt.Subsample_subsampleType(), SubsampleProperty.subsampleType),
			new Column(enttxt.Subsample_imageCount(),
					SubsampleProperty.imageCount),
			new Column(enttxt.Subsample_images(), SubsampleProperty.images,
					true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					return new MLink("Images", TokenSpace
							.ViewOf((Subsample) data));
				}
			}, // TODO image thumbnail browser
			new Column("Image Map", true) {
				protected Object getWidget(final MObject data,
						final int currentRow) {
					final Subsample s = (Subsample) data;
					if (s.getGrid() == null) {
						return new MLink("Create Map", TokenSpace
								.createNewImageMap(s));
					} else {
						return new MLink("View Map", TokenSpace.detailsOf(s
								.getGrid()));
					}
				}
			},
			new Column(enttxt.Subsample_analysisCount(),
					SubsampleProperty.analysisCount),

	};

	public String getDefaultSortParameter() {
		return SubsampleProperty.name.name();
	}

	public abstract void update(final PaginationParameters p,
			final AsyncCallback<Results<Subsample>> ac);
}
