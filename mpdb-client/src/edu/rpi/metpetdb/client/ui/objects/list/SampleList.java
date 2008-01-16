package edu.rpi.metpetdb.client.ui.objects.list;

import org.gwtwidgets.client.ui.pagination.Column;
import org.gwtwidgets.client.ui.pagination.DataProvider;
import org.gwtwidgets.client.ui.pagination.DefaultPaginationBehavior;
import org.gwtwidgets.client.ui.pagination.PaginationBehavior;
import org.gwtwidgets.client.ui.pagination.RowRenderer;
import org.postgis.Point;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.ui.LocaleEntity;
import edu.rpi.metpetdb.client.ui.LocaleHandler;
import edu.rpi.metpetdb.client.ui.Styles;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.input.attributes.DateAttribute;
import edu.rpi.metpetdb.client.ui.widgets.MLink;

public class SampleList extends FlowPanel {
	public SampleList(final DataProvider prov, final String label) {
		final Element header = DOM.createElement("h1");
		DOM.setInnerText(header, label);
		DOM.appendChild(this.getElement(), header);
		final FlexTable data = new FlexTable();
		final FlexTable page = new FlexTable();
		data.setStyleName(Styles.DATATABLE);
		page.setStyleName(Styles.PAGETABLE);
		new MyBehavior(page, data, 20, prov);
		add(data);
		add(page);
	}

	static class MyBehavior extends DefaultPaginationBehavior
			implements
				RowRenderer {
		private static final LocaleEntity enttxt = LocaleHandler.lc_entity;

		private static Column[] columnModel = {
				new Column(""), // Column for Checkboxes
				new Column(enttxt.Sample_alias(), "alias"),
				new Column(enttxt.Sample_sesarNumber(), "sesarNumber"),
				new Column(enttxt.Sample_owner(), "owner"),
				new Column(enttxt.Sample_collectionDate(), "collectionDate"),
				new Column(enttxt.Sample_rockType(), "rockType"),
				new Column(enttxt.Sample_publicData(), "publicData"),
				new Column(enttxt.Sample_latitude(), "latitude"),
				new Column(enttxt.Sample_longitude(), "longitude"),
				new Column(enttxt.Sample_country(), "country"),
				new Column(enttxt.Sample_description(), "description"),
				new Column(enttxt.Sample_collector(), "collector"),
				new Column(enttxt.Sample_locationText(), "locationText"),
				new Column(enttxt.Sample_latitudeError(), "latitudeError"),
				new Column(enttxt.Sample_longitudeError(), "longitudeError"),
				new Column(enttxt.Sample_subsampleCount()),};

		public void populateRow(final PaginationBehavior pagination,
				final int row, final Object object) {
			final Sample s = (Sample) object;

			final CheckBox checker = new CheckBox();
			checker.addClickListener(new ClickListener() {
				public void onClick(Widget sender) {
					if (((CheckBox) sender).isChecked()) {
						;
					}
				}
			});

			setCell(row, 0, checker);
			setCell(row, 1, new MLink((s.getAlias() == null) ? " " : s
					.getAlias(), TokenSpace.detailsOf(s)));
			setCell(row, 2, new MLink(s.getSesarNumber(),TokenSpace.detailsOf(s)));
			setCell(row, 3, new MLink(s.getOwner().getUsername(), TokenSpace
					.detailsOf(s.getOwner())));
			setCell(row, 4, new Label(s.getCollectionDate() == null
					? ""
					: DateAttribute.dateToString(s.getCollectionDate())));
			setCell(row, 5, new Label(s.getRockType().toString()));
			setCell(row, 6, new Label(s.isPublicData() ? "Yes" : "No"));
			setCell(row, 7, new Label(s.getLocation() == null
					? ""
					: new Double(((Point) (s.getLocation())).x).toString()));
			setCell(row, 8, new Label(s.getLocation() == null
					? ""
					: new Double(((Point) (s.getLocation())).y).toString()));
			setCell(row, 9, new Label((s.getCountry() == null) ? " " : s
					.getCountry()));
			setCell(row, 10, new Label((s.getDescription() == null) ? " " : s
					.getDescription()));
			setCell(row, 11, new Label((s.getCollector() == null) ? " " : s
					.getCollector()));
			setCell(row, 12, new Label((s.getLocationText() == null) ? " " : s
					.getLocationText()));
			setCell(row, 13, new Label(s.getLatitudeError() == null ? " " : (s
					.getLatitudeError()).toString()));
			setCell(row, 14, new Label(s.getLongitudeError() == null ? " " : (s
					.getLongitudeError()).toString()));
			setCell(row, 15, new Label((new Integer(s.getSubsamples().size()))
					.toString()));

		}

		private final DataProvider dataProvider;

		public MyBehavior(final FlexTable paging, final FlexTable table,
				final int rowsPerPage, final DataProvider prov) {
			super(paging, table, rowsPerPage);
			dataProvider = prov;
			showPage(1, getColumns()[1].getParameter(), true);
		}

		protected Column[] getColumns() {
			return columnModel;
		}

		protected DataProvider getDataProvider() {
			return dataProvider;
		}

		protected RowRenderer getRowRenderer() {
			return this;
		}
	}
}