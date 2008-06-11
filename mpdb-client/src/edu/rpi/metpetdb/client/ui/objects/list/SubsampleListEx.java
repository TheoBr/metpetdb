package edu.rpi.metpetdb.client.ui.objects.list;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;

import edu.rpi.metpetdb.client.locale.LocaleEntity;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.SubsampleDTO;
import edu.rpi.metpetdb.client.model.properties.SubsampleProperty;
import edu.rpi.metpetdb.client.paging.Column;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.widgets.MLink;

public abstract class SubsampleListEx extends ListEx<SubsampleDTO> {

	public SubsampleListEx() {
		super(columns);
	}

	private static final LocaleEntity enttxt = LocaleHandler.lc_entity;
	// new Column("Image", "name"),
	// new Column("Name", "name"),
	// new Column(enttxt.Subsample_type(), "name"),
	// new Column("Map", "name"),
	// new Column(enttxt.Subsample_imageCount()),
	// new Column(enttxt.Subsample_analysisCount()),};
	public static Column[] columns = {
			new Column("Check", true, true) {
				protected Object getWidget(final MObjectDTO data,
						final int currentRow) {
					return new CheckBox();
				}
			},
			new Column(enttxt.Subsample_name(), SubsampleProperty.name, true) {
				protected Object getWidget(final MObjectDTO data,
						final int currentRow) {
					return new MLink(
							(String) data.mGet(SubsampleProperty.name),
							TokenSpace.detailsOf((SubsampleDTO) data));
				}
			},
			new Column(enttxt.Subsample_type(), SubsampleProperty.type),
			new Column(enttxt.Subsample_imageCount(),
					SubsampleProperty.imageCount),
			new Column(enttxt.Subsample_images(), SubsampleProperty.images,
					true) {
				// protected Object getWidget(final MObjectDTO data,
				// final int currentRow) {
				// return new ImageList(data.mGet(getId()),
				// (ArrayList) data.images, false);
				// }
			}, // TODO image thumbnail browser
			new Column("Image Map") {
				// TODO Mlink to create map/view map
			},
			new Column(enttxt.Subsample_analysisCount(),
					SubsampleProperty.analysisCount),

	};

	public String getDefaultSortParameter() {
		return SubsampleProperty.name.name();
	}

	public abstract void update(final PaginationParameters p,
			final AsyncCallback<Results<SubsampleDTO>> ac);
}
