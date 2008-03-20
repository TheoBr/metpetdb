package edu.rpi.metpetdb.client.ui.objects.list;

import com.google.gwt.user.client.ui.VerticalPanel;

public class ProjectSamplesList extends VerticalPanel {
	// public ProjectSamplesList(final DataProvider prov) {
	// final FlexTable data = new FlexTable();
	// final FlexTable page = new FlexTable();
	// data.setStyleName(Styles.DATATABLE);
	// page.setStyleName(Styles.PAGETABLE);
	// new MyBehavior(page, data, 20, prov);
	// add(data);
	// add(page);
	// }
	//
	// static class MyBehavior extends DefaultPaginationBehavior
	// implements
	// RowRenderer {
	// private static final LocaleEntity enttxt = LocaleHandler.lc_entity;
	//
	// private static Column[] columnModel = {
	// new Column(enttxt.Sample_alias(), "alias"),
	// new Column(enttxt.Sample_sesarNumber(), "sesarNumber"),
	// new Column(enttxt.Sample_owner(), "owner"),
	// new Column(enttxt.Sample_collectionDate(), "collectionDate"),
	// new Column(enttxt.Sample_rockType(), "rockType"),
	// new Column(enttxt.Sample_publicData(), "publicData"),
	// new Column(enttxt.Sample_latitude(), "latitude"),
	// new Column(enttxt.Sample_longitude(), "longitude"),
	// new Column(enttxt.Sample_country(), "country"),
	// new Column(enttxt.Sample_description(), "description"),
	// new Column(enttxt.Sample_collector(), "collector"),
	// new Column(enttxt.Sample_locationText(), "locationText"),};
	//
	// public void populateRow(final PaginationBehavior pagination,
	// final int row, final Object object) {
	// final SampleDTO s = (SampleDTO) object;
	//
	// setCell(row, 0, new MLink((s.getAlias() == null) ? " " : s
	// .getAlias(), TokenSpace.detailsOf(s)));
	// setCell(row, 1, new MLink(s.getSesarNumber(),TokenSpace.detailsOf(s)));
	// setCell(row, 2, new MLink(s.getOwner().getUsername(), TokenSpace
	// .detailsOf(s.getOwner())));
	// setCell(row, 3, new Label(s.getCollectionDate() == null ? "" : s
	// .getCollectionDate().toString()));
	// setCell(row, 4, new Label(s.getRockType().toString()));
	// setCell(row, 5, new Label(s.isPublicData() ? "Yes" : "No"));
	// setCell(row, 6, new Label(s.getLocation() == null
	// ? ""
	// : new Double(((Point) (s.getLocation())).x).toString()));
	// setCell(row, 7, new Label(s.getLocation() == null
	// ? ""
	// : new Double(((Point) (s.getLocation())).y).toString()));
	// setCell(row, 9, new Label((s.getCountry() == null) ? " " : s
	// .getCountry()));
	// setCell(row, 10, new Label((s.getDescription() == null) ? " " : s
	// .getDescription()));
	// setCell(row, 11, new Label((s.getCollector() == null) ? " " : s
	// .getCollector()));
	// setCell(row, 12, new Label((s.getLocationText() == null) ? " " : s
	// .getLocationText()));
	//
	// }
	//
	// private final DataProvider dataProvider;
	//
	// public MyBehavior(final FlexTable paging, final FlexTable table,
	// final int rowsPerPage, final DataProvider prov) {
	// super(paging, table, rowsPerPage);
	// dataProvider = prov;
	// showPage(1, getColumns()[0].getParameter(), true);
	// }
	//
	// protected Column[] getColumns() {
	// return columnModel;
	// }
	//
	// protected DataProvider getDataProvider() {
	// return dataProvider;
	// }
	//
	// protected RowRenderer getRowRenderer() {
	// return this;
	// }
	// }
}