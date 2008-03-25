package edu.rpi.metpetdb.client.ui.objects.list;

import org.postgis.Point;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;

import edu.rpi.metpetdb.client.locale.LocaleEntity;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.model.UserDTO;
import edu.rpi.metpetdb.client.model.properties.SampleProperty;
import edu.rpi.metpetdb.client.paging.Column;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.ServerOp;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.widgets.MLink;

public abstract class SampleListEx extends ListEx {

	public SampleListEx() {
		super(columns);
	}

	private static final LocaleEntity enttxt = LocaleHandler.lc_entity;

	private static Column[] columns = {
			new Column(""), // Column for Checkboxes
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
			new Column(enttxt.Sample_collector(), SampleProperty.collector),
			new Column(enttxt.Sample_collectionDate(),
					SampleProperty.collectionDate),
			new Column(enttxt.Sample_rockType(), SampleProperty.rockType),
			new Column(enttxt.Sample_publicData(), SampleProperty.publicData),
			new Column("Location", SampleProperty.location, true) {
				protected Object getText(final MObjectDTO data,
						final int currentRow) {
					final Point location = (Point) data
							.mGet(SampleProperty.location);
					return "(" + location.x + "," + location.y + ")";
				}
			},
			new Column(enttxt.Sample_country(), SampleProperty.country),
			new Column(enttxt.Sample_subsampleCount(),
					SampleProperty.subsampleCount), };

	public abstract void update(final PaginationParameters p,
			final AsyncCallback<Results<SampleDTO>> ac);

	public void handleResults(final PaginationParameters p,
			final TableModel.Request request, final TableModel.Callback callback) {
		new ServerOp<Results<SampleDTO>>() {
			@Override
			public void begin() {
				update(p, this);
			}

			public void onSuccess(Results<SampleDTO> result) {
				SampleListEx.this.dataTable.setNumRows(result.getCount());
				final TableModel.SerializableResponse sr = new TableModel.SerializableResponse(
						getList(result.getList()), result.getList());
				if (result.getCount() == 0) {
					SampleListEx.this.clear();
					SampleListEx.this
							.add(new HTML(
									"nothing to see here...move along<br/><br/><br/><br/><br/>Seriously though, i did not find any samples that match your criteria"));
				} else {
					callback.onRowsReady(request, sr);
				}
			}

		}.begin();
	}

	public String getDefaultSortParameter() {
		return SampleProperty.alias.name();
	}
}
