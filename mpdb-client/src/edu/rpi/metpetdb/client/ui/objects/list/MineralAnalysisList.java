package edu.rpi.metpetdb.client.ui.objects.list;

import org.gwtwidgets.client.ui.pagination.Column;
import org.gwtwidgets.client.ui.pagination.DataProvider;
import org.gwtwidgets.client.ui.pagination.DefaultPaginationBehavior;
import org.gwtwidgets.client.ui.pagination.PaginationBehavior;
import org.gwtwidgets.client.ui.pagination.RowRenderer;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.rpi.metpetdb.client.locale.LocaleEntity;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.MineralAnalysisDTO;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.model.SubsampleDTO;
import edu.rpi.metpetdb.client.ui.Styles;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.input.attributes.DateAttribute;
import edu.rpi.metpetdb.client.ui.widgets.MLink;

public class MineralAnalysisList extends VerticalPanel {
	public MineralAnalysisList(final DataProvider prov) {
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
				new Column(enttxt.MineralAnalysis_spotId(), "spotId"), 
				new Column(enttxt.MineralAnalysis_sample(), "spotId"), // not ordered properly
				new Column(enttxt.MineralAnalysis_subsample(), "spotId"), // not ordered properly
				new Column(enttxt.MineralAnalysis_pointX(), "pointX"),
				new Column(enttxt.MineralAnalysis_pointY(), "pointY"),
				new Column(enttxt.MineralAnalysis_method(), "method"),
				new Column(enttxt.MineralAnalysis_location(), "location"),
				new Column(enttxt.MineralAnalysis_analyst(), "analyst"),
				new Column(enttxt.MineralAnalysis_analysisDate(),
						"analysisDate"),
		};

		public void populateRow(final PaginationBehavior pagination,
				final int row, final Object object) {
			final MineralAnalysisDTO ma = (MineralAnalysisDTO) object;
			final SampleDTO s = ma.getSubsample().getSample();
			final SubsampleDTO ss = ma.getSubsample();
			
			setCell(row, 0, new MLink(String.valueOf(ma.getSpotId()),
					TokenSpace.detailsOf(ma)));
			setCell(row, 1, new MLink(s.getAlias(), TokenSpace.detailsOf(s)));
			setCell(row, 2, new MLink(ss.getName(), TokenSpace.detailsOf(ss)));
			setCell(row, 3, new Label(String.valueOf(ma.getPointX())));
			setCell(row, 4, new Label(String.valueOf(ma.getPointY())));
			setCell(row, 5, new Label(ma.getAnalysisMethod()));
			setCell(row, 6, new Label(ma.getLocation()));
			setCell(row, 7, new Label(ma.getAnalyst()));
			setCell(row, 8, new Label(ma.getAnalysisDate() == null
					? ""
					: DateAttribute.dateToString(ma.getAnalysisDate())));
		}

		private final DataProvider dataProvider;

		public MyBehavior(final FlexTable paging, final FlexTable table,
				final int rowsPerPage, final DataProvider prov) {
			super(paging, table, rowsPerPage);
			dataProvider = prov;
			showPage(1, getColumns()[0].getParameter(), true);
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