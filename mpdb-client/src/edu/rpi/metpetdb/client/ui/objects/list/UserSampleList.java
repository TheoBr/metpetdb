package edu.rpi.metpetdb.client.ui.objects.list;

import com.google.gwt.user.client.ui.FlowPanel;

public class UserSampleList extends FlowPanel {
	//
	// private static boolean selectAllSamples = false;
	//
	// private final MLink uploadSample;
	//
	// private final MLink bulkUpload;
	//
	// public UserSampleList(final DataProvider prov) {
	// final Element header = DOM.createElement("h1");
	// DOM.setInnerText(header, "My Samples");
	// DOM.appendChild(this.getElement(), header);
	//
	// final MUnorderedList ulistOptions = new MUnorderedList();
	// uploadSample = new MLink("Enter Sample", TokenSpace.enterSample);
	// bulkUpload = new MLink("Bulk Upload", this);
	// uploadSample.addStyleName("addlink");
	// bulkUpload.addStyleName("addlink");
	// ulistOptions.add(uploadSample);
	// ulistOptions.add(bulkUpload);
	// ulistOptions.setStyleName("options");
	// add(ulistOptions);
	//
	// final HorizontalPanel filterView = new HorizontalPanel();
	// filterView.setSpacing(5);
	// filterView.add(new Label("Quick Filters:"));
	// filterView.add(new MLink("Recently added", new ClickListener() {
	// public void onClick(Widget sender) {
	// }
	// }));
	// filterView.add(new MLink("In MyProj", new ClickListener() {
	// public void onClick(Widget sender) {
	// }
	// }));
	// filterView.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
	//
	// final HorizontalPanel changeView = new HorizontalPanel();
	// changeView.setSpacing(5);
	// changeView.add(new Label("Change View:"));
	// changeView.add(new MLink("Simple", new ClickListener() {
	// public void onClick(Widget sender) {
	// }
	// }));
	// changeView.add(new MLink("Detailed", new ClickListener() {
	// public void onClick(Widget sender) {
	// }
	// }));
	// changeView.add(new Label("|"));
	// changeView.add(new MLink("Create New View", new ClickListener() {
	// public void onClick(Widget sender) {
	// }
	// }));
	// changeView.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
	//
	// final HorizontalPanel filterAndChange = new HorizontalPanel();
	// filterAndChange.add(filterView);
	// filterAndChange.add(changeView);
	// add(filterAndChange);
	//
	// final FlexTable data = new FlexTable();
	// final FlexTable page = new FlexTable();
	// data.setStyleName(Styles.DATATABLE);
	// page.setStyleName(Styles.PAGETABLE);
	// final MyBehavior table = new MyBehavior(page, data, 10, prov);
	// add(data);
	//
	// final HorizontalPanel actions = new HorizontalPanel();
	// actions.setSpacing(5);
	//
	// // Checkbox to select all rows
	// CheckBox selectAll = new CheckBox();
	// if (selectAll.isChecked()) {
	// selectAllSamples = true;
	// }
	// actions.add(selectAll);
	// actions.add(new Label("Select All"));
	//
	// // Listbox for adding to project or removing a sample
	// final ListBox addRemove = new ListBox();
	// addRemove.addItem("-------", "-------");
	// addRemove.addItem("Add to", "Add to");
	// addRemove.addItem("Remove from my samples", "Remove from my samples");
	// addRemove.setVisibleItemCount(1);
	// addRemove.setSelectedIndex(0);
	// actions.add(addRemove);
	//
	// // Listbox of projects
	// final ListBox projectList = new ListBox();
	// projectList.setVisibleItemCount(1);
	// actions.add(projectList);
	//
	// addRemove.addClickListener(new ClickListener() {
	// public void onClick(Widget sender) {
	// new ServerOp() {
	// public void begin() {
	// MpDb.user_svc.details(MpDb.currentUser().getUsername(),
	// this);
	// }
	//
	// public void onSuccess(Object result) {
	// UserDTO user = (UserDTO) result;
	// Set projects = user.getProjects();
	// Iterator it = projects.iterator();
	// while (it.hasNext()) {
	// ProjectDTO project = (ProjectDTO) it.next();
	// projectList.addItem(project.getName(), new Integer(
	// project.getId()).toString());
	// }
	// }
	// }.begin();
	// }
	// });
	//
	// /*
	// * projectList.addChangeListener(new ChangeListener(){ public void
	// * onChange(final Widget sender){ new ServerOp(){ public void begin(){
	// * MpDb.user_svc.details(MpDb.currentUser().getUsername(), this); }
	// * public void onSuccess(Object result){ User user = (User) result;
	// * ListBox list = (ListBox) sender; list.clear(); Set projects =
	// * user.getProjects(); Iterator it = projects.iterator(); while
	// * (it.hasNext()) { Project project = (Project) it.next();
	// * list.addItem(project.getName(), new
	// * Integer(project.getId()).toString()); } } }.begin(); } });
	// */
	//
	// /*
	// * projectList.addClickListener(new ClickListener(){ public void
	// * onClick(Widget sender){ new ServerOp(){ public void begin(){
	// * projectList.clear(); Set projects = MpDb.currentUser().getProjects();
	// * Iterator iter = projects.iterator(); while(iter.hasNext()){ Project
	// * project = (Project) iter.next();
	// * projectList.addItem(project.getName(), new
	// * Integer(project.getId()).toString()); } } public void
	// * onSuccess(Object result){ } }.begin(); } });
	// */
	//
	// // Add button
	// final Button adder = new Button("Add", new ClickListener() {
	// // Add selected samples to the selected project
	// public void onClick(Widget sender) {
	//
	// // Get project with specified project name
	// // String projectName = projectList.getItemText(projectList
	// // .getSelectedIndex());
	// final int projectId = Integer.parseInt(projectList
	// .getValue(projectList.getSelectedIndex()));
	//
	// // Retrieve project using projectId
	// new ServerOp() {
	// public void begin() {
	// MpDb.project_svc.details(projectId, this);
	// }
	//
	// public void onSuccess(Object result) {
	// final ProjectDTO project = (ProjectDTO) result;
	//
	// // Add each selected sample to project
	// Set selectedSamples = table.getSelectedSamples();
	// Iterator it = selectedSamples.iterator();
	// while (it.hasNext()) {
	// final SampleDTO sample = (SampleDTO) it.next();
	//
	// // Sample is unique, project is unique now.
	// // Add sample to project and project to sample
	// project.getSamples().add(sample);
	// sample.getProjects().add(project);
	// }
	//
	// // project is saved after adding samples
	// new ServerOp() {
	// public void begin() {
	// MpDb.project_svc.saveProject(project, this);
	// }
	//
	// public void onSuccess(Object result) {
	//
	// }
	// }.begin();
	//
	// }
	// }.begin();
	// }
	// });
	// actions.add(adder);
	//
	// // Remove button
	// final Button remover = new Button("Remove", new ClickListener() {
	// public void onClick(Widget sender) {
	// // TODO Remove selected samples from Samples table
	// }
	// });
	// actions.add(remover);
	//
	// // Adding action
	// if (addRemove.getSelectedIndex() == 1) {
	// projectList.setEnabled(true);
	// adder.setEnabled(true);
	// remover.setEnabled(false);
	// } else if (addRemove.getSelectedIndex() == 2) {
	// projectList.setEnabled(false);
	// adder.setEnabled(false);
	// remover.setEnabled(true);
	// }
	// actions.setStyleName("mpdb-tableFooter");
	// add(actions);
	// add(page);
	// }
	//
	// static class MyBehavior extends DefaultPaginationBehavior
	// implements
	// RowRenderer {
	// private static final LocaleEntity enttxt = LocaleHandler.lc_entity;
	//
	// private static Set selectedSamples = new HashSet();
	//
	// private static Column[] columnModel = {
	// new Column("", "alias"), // Column for Checkboxes
	// new Column(enttxt.Sample_alias(), "alias"),
	// new Column(enttxt.Sample_sesarNumber(), "sesarNumber"),
	// new Column(enttxt.Sample_owner(), "owner"),
	// new Column(enttxt.Sample_collectionDate(), "collectionDate"),
	// new Column(enttxt.Sample_rockType(), "rockType"),
	// new Column(enttxt.Sample_publicData(), "publicData"),
	// new Column(enttxt.Sample_latitude(), "alias"),
	// new Column(enttxt.Sample_longitude(), "alias"),
	// new Column(enttxt.Sample_country(), "country"),
	// new Column(enttxt.Sample_description(), "description"),
	// new Column(enttxt.Sample_collector(), "collector"),
	// new Column(enttxt.Sample_locationText(), "locationText"),
	// new Column(enttxt.Sample_latitudeError(), "latitudeError"),
	// new Column(enttxt.Sample_longitudeError(), "longitudeError"),
	// new Column(enttxt.Sample_subsampleCount()),};
	//
	// public void populateRow(final PaginationBehavior pagination,
	// final int row, final Object object) {
	// final SampleDTO s = (SampleDTO) object;
	//
	// CheckBox checker = new CheckBox();
	// if (selectAllSamples)
	// checker.setChecked(true);
	//
	// setCell(row, 0, checker);
	// checker.addClickListener(new ClickListener() {
	// public void onClick(Widget sender) {
	// if (((CheckBox) sender).isChecked()
	// && !selectedSamples.contains(s)) {
	// selectedSamples.add(s);
	// } else if (!((CheckBox) sender).isChecked()
	// && selectedSamples.contains(s)) {
	// selectedSamples.remove(s);
	// }
	// }
	// });
	//
	// setCell(row, 1, new MLink((s.getAlias() == null) ? " " : s
	// .getAlias(), TokenSpace.detailsOf(s)));
	// setCell(row, 2, new MLink(s.getSesarNumber(),TokenSpace.detailsOf(s)));
	// setCell(row, 3, new MLink(s.getOwner().getUsername(), TokenSpace
	// .detailsOf(s.getOwner())));
	// setCell(row, 4, new Label(s.getCollectionDate() == null
	// ? ""
	// : DateAttribute.dateToString(s.getCollectionDate())));
	// setCell(row, 5, new Label(s.getRockType().toString()));
	// setCell(row, 6, new Label(s.isPublicData() ? "Yes" : "No"));
	// setCell(row, 7, new Label(s.getLocation() == null
	// ? ""
	// : new Double(((Point) (s.getLocation())).x).toString()));
	// setCell(row, 8, new Label(s.getLocation() == null
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
	// setCell(row, 13, new Label(s.getLatitudeError() == null ? " " : (s
	// .getLatitudeError()).toString()));
	// setCell(row, 14, new Label(s.getLongitudeError() == null ? " " : (s
	// .getLongitudeError()).toString()));
	// setCell(row, 15, new Label(String.valueOf(s.getSubsampleCount())));
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
	// public Set getSelectedSamples() {
	// if (selectedSamples == null)
	// selectedSamples = new HashSet();
	// return selectedSamples;
	// }
	//
	// // not implemented yet
	// public void setSelectedSamples() {
	// ;
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
	//
	// public void onClick(final Widget sender) {
	// if (sender == uploadSample) {
	//
	// } else if (sender == bulkUpload) {
	//
	// }
	// }
}