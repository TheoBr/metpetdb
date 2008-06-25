package edu.rpi.metpetdb.client.ui.objects.list;

import org.postgis.Point;

import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.locale.LocaleEntity;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.model.UserDTO;
import edu.rpi.metpetdb.client.model.properties.SampleProperty;
import edu.rpi.metpetdb.client.paging.Column;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.input.attributes.DateAttribute;
import edu.rpi.metpetdb.client.ui.widgets.MCheckBox;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.MText;

public abstract class SampleListEx extends ListEx<SampleDTO> {

	public SampleListEx() {
		super(columns);
	}

	private static final LocaleEntity enttxt = LocaleHandler.lc_entity;

	private static Column[] columns = {
			new Column("Check", true, true) {
				protected Object getWidget(final MObjectDTO data,
						final int currentRow) {
					return new MCheckBox(data);
				}
			},
			new Column(enttxt.Sample_alias(), SampleProperty.alias, true) {
				protected Object getWidget(final MObjectDTO data,
						final int currentRow) {
					return new MLink((String) data.mGet(SampleProperty.alias),
							TokenSpace.detailsOf((SampleDTO) data));
				}
			},
			new Column(enttxt.Sample_sesarNumber(), SampleProperty.sesarNumber,
					true) {
				protected Object getWidget(final MObjectDTO data,
						final int currentRow) {
					return new MLink((String) data
							.mGet(SampleProperty.sesarNumber), TokenSpace
							.detailsOf((SampleDTO) data));
				}
			},
			new Column(enttxt.Sample_owner(), SampleProperty.owner, true) {
				protected Object getWidget(final MObjectDTO data,
						final int currentRow) {
					return new MLink(
							((UserDTO) data.mGet(SampleProperty.owner))
									.getUsername(), TokenSpace
									.detailsOf((UserDTO) data
											.mGet(SampleProperty.owner)));
				}
			},
			new Column(enttxt.Sample_collector(), SampleProperty.collector,
					true) {
				protected Object getWidget(final MObjectDTO data,
						final int currentRow) {
					String text = ((String) data.mGet(SampleProperty.collector));
					if (text == null)
						text = "------";
					return new MText(text);

				}
			},
			new Column(enttxt.Sample_collectionDate(),
					SampleProperty.collectionDate, true) {
				protected Object getWidget(final MObjectDTO data,
						final int currentRow) {
					DateAttribute dateTemp = new DateAttribute(
							MpDb.doc.Sample_collectionDate,
							MpDb.doc.Sample_datePrecision);
					return ((MText) dateTemp.createDisplayWidget(data)[0])
							.getText();
				}
			},
			new Column(enttxt.Sample_rockType(), SampleProperty.rockType),
			new Column(enttxt.Sample_publicData(), SampleProperty.publicData),
			new Column("Location", SampleProperty.location, true) {
				protected Object getText(final MObjectDTO data,
						final int currentRow) {
					final Point location = (Point) data
							.mGet(SampleProperty.location);
					return "(" + format(location.x) + "," + format(location.y)
							+ ")";
				}
			},
			new Column(enttxt.Sample_country(), SampleProperty.country, true) {
				protected Object getWidget(final MObjectDTO data,
						final int currentRow) {
					String text = ((String) data.mGet(SampleProperty.country));
					if (text == null)
						text = "------";
					return new MText(text);

				}
			},
			new Column(enttxt.Sample_subsampleCount(),
					SampleProperty.subsampleCount),
	};

	public abstract void update(final PaginationParameters p,
			final AsyncCallback<Results<SampleDTO>> ac);

	public String getDefaultSortParameter() {
		return SampleProperty.alias.name();
	}

	public static String format(final double loc) {
		final String locStr = String.valueOf(loc);
		final int decPos = locStr.toString().indexOf(".");
		if (locStr.length() < decPos + 4 && decPos >= 0)
			return locStr;
		return locStr.substring(0, decPos + 4);
	}

}
