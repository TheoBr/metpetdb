package edu.rpi.metpetdb.client.ui.image.browser;

import com.google.gwt.gen2.table.client.SelectionGrid.SelectionPolicy;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleEntity;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.properties.ImageProperty;
import edu.rpi.metpetdb.client.ui.widgets.paging.DataList;
import edu.rpi.metpetdb.client.ui.widgets.paging.columns.Column;
import edu.rpi.metpetdb.client.ui.widgets.paging.columns.ColumnDefinition;
import edu.rpi.metpetdb.client.ui.widgets.paging.columns.StringColumn;

public abstract class ImageBrowserImageList extends DataList<Image> {

	private static final LocaleEntity enttxt = LocaleHandler.lc_entity;

	@Override
	public String getListName() {
		return "imageBrowserList";
	}
	
	public static ColumnDefinition<Image> getColumns() {
		return columns;
	}

	public static ColumnDefinition<Image> columns;
	static {
		columns = new ColumnDefinition<Image>();

		columns
				.addColumn(new Column<Image, com.google.gwt.user.client.ui.Image>(
						"") {
					@Override
					public com.google.gwt.user.client.ui.Image getCellValue(
							Image rowValue) {
						return new com.google.gwt.user.client.ui.Image(rowValue
								.get64x64ServerPath());
					}
				}.setName("image"));
		columns.addColumn(new StringColumn<Image>(enttxt.Image_filename(),
				ImageProperty.filename));
		columns.addColumn(new StringColumn<Image>(enttxt.Image_imageType(),
				ImageProperty.imageType));
	}

	public ImageBrowserImageList() {
		super(columns);
		getDataTable().setSelectionEnabled(true);
		getDataTable().setSelectionPolicy(SelectionPolicy.CHECKBOX);
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

}
