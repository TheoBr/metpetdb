package edu.rpi.metpetdb.client.ui.objects.list;

import com.google.gwt.gen2.table.client.AbstractScrollTable.ScrollPolicy;
import com.google.gwt.gen2.table.client.SelectionGrid.SelectionPolicy;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleEntity;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.Project;
import edu.rpi.metpetdb.client.model.properties.ImageProperty;
import edu.rpi.metpetdb.client.model.properties.XrayImageProperty;
import edu.rpi.metpetdb.client.ui.image.browser.ImageBrowserImageList;
import edu.rpi.metpetdb.client.ui.widgets.paging.DataList;
import edu.rpi.metpetdb.client.ui.widgets.paging.columns.Column;
import edu.rpi.metpetdb.client.ui.widgets.paging.columns.ColumnDefinition;
import edu.rpi.metpetdb.client.ui.widgets.paging.columns.StringColumn;
import edu.rpi.metpetdb.client.ui.widgets.paging.columns.XrayColumn;

/**
 * Typically used for subsamples, it extends ImageBrowserImageList because this
 * one adds more columns and disables selection
 * 
 * @author anthony
 * 
 */
public abstract class ImageListViewer extends DataList<Image> {

	private static final LocaleEntity enttxt = LocaleHandler.lc_entity;
	private static ColumnDefinition<Image> columns;
	
	static {
		// copy the columns from the image browser
		columns = new ColumnDefinition<Image>();
		for(Column<Image,?> c: ImageBrowserImageList.getColumns()) {
			columns.addColumn(c);
		}
		
		columns.addColumn(new StringColumn<Image>(enttxt.Image_collector(),
				ImageProperty.collector).setOptional(true));
		columns.addColumn(new XrayColumn(enttxt.XrayImage_current(),
				XrayImageProperty.current).setOptional(true));
		columns.addColumn(new XrayColumn(enttxt.XrayImage_voltage(),
				XrayImageProperty.voltage).setOptional(true));
		columns.addColumn(new XrayColumn(enttxt.XrayImage_dwelltime(),
				XrayImageProperty.dwelltime).setOptional(true));
		columns.addColumn(new XrayColumn(enttxt.XrayImage_element(),
				XrayImageProperty.element).setOptional(true));
	}
	
	@Override
	public String getListName() {
		return "imageList";
	}

	public ImageListViewer() {
		super(columns);
		// we have to first change the selection policy in order to prevent
		// an error when their are checkboxes/radio buttons
		getDataTable().setSelectionPolicy(SelectionPolicy.ONE_ROW);
		getDataTable().setSelectionEnabled(false);
		initialize();
	}

	@Override
	public String getDefaultSortParameter() {
		return "filename";
	}

	@Override
	protected Widget getNoResultsWidget() {
		return new Label("No Image Results");
	}
	
	protected ColumnDefinition<Image> getDefaultColumns() {
		return columns;
	}
	
	@Override
	protected Object getId(Image i){
		return i.getId();
	}

}
