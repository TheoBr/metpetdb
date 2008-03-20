package edu.rpi.metpetdb.client.ui.objects.list;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.widgetideas.table.client.FixedWidthFlexTable;
import com.google.gwt.widgetideas.table.client.PagingGrid;
import com.google.gwt.widgetideas.table.client.PagingScrollTable;
import com.google.gwt.widgetideas.table.client.ReadOnlyTableModel;

import edu.rpi.metpetdb.client.locale.LocaleEntity;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.model.properties.SampleProperty;
import edu.rpi.metpetdb.client.paging.Column;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;

public class SampleListEx extends FlowPanel {

	private static final String DEFAULT_PARAMETER = "alias";
	private static final boolean DEFAULT_SORT_ORDER = true;

	private final PagingGrid dataTable;

	private static final LocaleEntity enttxt = LocaleHandler.lc_entity;

	private static Column[] columns = {
			new Column(""), // Column for Checkboxes
			new Column(enttxt.Sample_alias(), SampleProperty.alias),
			new Column(enttxt.Sample_sesarNumber(), SampleProperty.sesarNumber),
			new Column(enttxt.Sample_owner(), SampleProperty.owner),
			new Column(enttxt.Sample_collectionDate(),
					SampleProperty.collectionDate),
			new Column(enttxt.Sample_rockType(), SampleProperty.rockType),
			new Column(enttxt.Sample_publicData(), SampleProperty.publicData),
			new Column("Location", SampleProperty.location),
			new Column(enttxt.Sample_country(), SampleProperty.country),
			new Column(enttxt.Sample_description(), SampleProperty.description),
			new Column(enttxt.Sample_collector(), SampleProperty.collector),
			new Column(enttxt.Sample_locationText(),
					SampleProperty.locationText),
			new Column(enttxt.Sample_latitudeError(),
					SampleProperty.latitudeError),
			new Column(enttxt.Sample_longitudeError(),
					SampleProperty.longitudeError),
			new Column(enttxt.Sample_subsampleCount(),
					SampleProperty.subsampleCount), };

	private class TableModel extends ReadOnlyTableModel {
		public void requestRows(final Request request, final Callback callback) {
			final int startRow = request.getStartRow();
			final int numRows = request.getNumRows();

			final ColumnSortList sortList = request.getColumnSortList();
			// Get column to sort
			final ColumnSortInfo sortInfo = sortList.getPrimaryColumnSortInfo();

			new ServerOp<Results<SampleDTO>>() {
				public void begin() {
					final PaginationParameters p = new PaginationParameters();
					if (sortInfo != null) {
						p.setParameter(columns[sortInfo.getColumn()]
								.getProperty().name());
						p.setAscending(sortInfo.isAscending());
					} else {
						p.setAscending(DEFAULT_SORT_ORDER);
						p.setParameter(DEFAULT_PARAMETER);
					}
					p.setFirstResult(startRow);
					p.setMaxResults(numRows);
					MpDb.sample_svc.all(p, this);
				}

				public void onSuccess(final Results<SampleDTO> result) {
					final List<SampleDTO> samples = result.getList();
					final Iterator<SampleDTO> itr = samples.iterator();
					final List<List<Object>> rows = new ArrayList<List<Object>>();
					int currentRow = 0;
					while (itr.hasNext()) {
						final List<Object> data = new ArrayList<Object>();
						final SampleDTO sample = (SampleDTO) itr.next();
						for (int i = 0; i < columns.length; ++i) {
							if (columns[i].getWidget(sample, currentRow) != null) {
								data.add(columns[i].getWidget(sample,
										currentRow));
							} else if (columns[i].getProperty() != null) {
								data.add(columns[i].getProperty().get(sample));
							} else
								data.add(columns[i].getTitle());
						}
						rows.add(data);
						++currentRow;
					}
					dataTable.setNumRows(result.getCount());
					Response response = new SerializableResponse(rows, samples);
					callback.onRowsReady(request, response);
				}
			}.begin();
		}
	}

	public SampleListEx() {
		TableModel tableModel = new TableModel();
		dataTable = new PagingGrid(tableModel);

		dataTable.setPageSize(2);
		dataTable.setNumRows(100);
		FixedWidthFlexTable headerTable = new FixedWidthFlexTable();

		for (int i = 0; i < columns.length; ++i) {
			headerTable.setText(0, i, columns[i].getTitle());
		}

		PagingScrollTable scrollTable = new PagingScrollTable(dataTable,
				headerTable);

		// Setup sortable/unsortable columns
		for (int i = 0; i < columns.length; ++i) {
			scrollTable.setColumnSortable(i, columns[i].isSortable());
		}

		dataTable.addTableListener(new TableListener() {

			public void onCellClicked(SourcesTableEvents sender, int row,
					int cell) {
				columns[cell].handleClickEvent(dataTable.getRowValue(row - 1),
						row - 1);

			}

		});

		scrollTable.setHeight("100%");
		scrollTable.setWidth("100%");
		dataTable.setWidth("100%");
		dataTable.setHeight("100%");
		headerTable.setWidth("100%");
		this.add(scrollTable);
		this.setHeight("400px");
		this.setWidth("100%");
	}

}
