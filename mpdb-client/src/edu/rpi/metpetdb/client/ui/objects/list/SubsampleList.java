package edu.rpi.metpetdb.client.ui.objects.list;

import com.google.gwt.user.client.ui.VerticalPanel;

public class SubsampleList extends VerticalPanel {
	// public final static ArrayList subsamples = new ArrayList();
	// public SubsampleList(final DataProvider prov, final ServerOp r) {
	// final FlexTable data = new FlexTable();
	// final FlexTable page = new FlexTable();
	// data.setStyleName(Styles.DATATABLE);
	// page.setStyleName(Styles.PAGETABLE);
	// new MyBehavior(page, data, 25, prov);
	// add(data);
	// add(page);
	// SubsampleList.subsamples.clear();
	// }
	//
	// static class MyBehavior extends DefaultPaginationBehavior
	// implements
	// RowRenderer {
	//
	// private static final LocaleEntity enttxt = LocaleHandler.lc_entity;
	//
	// private static Column[] columnModel = {
	// new Column("Image", "name"),
	// new Column("Name", "name"),
	// new Column(enttxt.Subsample_type(), "name"),
	// new Column("Map", "name"),
	// new Column(enttxt.Subsample_imageCount()),
	// new Column(enttxt.Subsample_analysisCount()),};
	//
	// public void populateRow(final PaginationBehavior pagination,
	// final int row, final Object object) {
	// final Subsample s = (Subsample) object;
	// final ImageList imageList = new ImageList(s.getId(), subsamples,
	// false);
	// setCell(row, 0, imageList);
	// setCell(row, 1, new MLink(s.getName(), TokenSpace.detailsOf(s)));
	// setCell(row, 2, new Label(s.getType()));
	// if (s.getGrid() == null) {
	// setCell(row, 3, new MLink("Create Map", new ClickListener() {
	// public void onClick(final Widget sender) {
	// MetPetDBApplication.show(new ImageBrowserDetails()
	// .createNew(s.getId()));
	// }
	// }));
	// } else {
	// setCell(row, 3, new MLink("View Map", TokenSpace.detailsOf(s
	// .getGrid())));
	// }
	//
	//
	// setCell(row, 4, new Label((new Integer(s.getImageCount()))
	// .toString()));
	// setCell(row, 5, new Label((new Integer(s.getAnalysisCount()))
	// .toString()));
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
