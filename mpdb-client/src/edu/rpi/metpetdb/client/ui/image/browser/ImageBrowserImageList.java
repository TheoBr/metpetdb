package edu.rpi.metpetdb.client.ui.image.browser;

import java.util.ArrayList;

import com.google.gwt.gen2.table.client.SelectionGrid.SelectionPolicy;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleEntity;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.properties.ImageProperty;
import edu.rpi.metpetdb.client.ui.objects.list.List;
import edu.rpi.metpetdb.client.ui.widgets.paging.Column;
import edu.rpi.metpetdb.client.ui.widgets.paging.StringColumn;

public abstract class ImageBrowserImageList extends List<Image> {

	private static final LocaleEntity enttxt = LocaleHandler.lc_entity;

	protected static ArrayList<Column<Image, ?>> columns;
	static {
		columns = new ArrayList<Column<Image, ?>>();
		
		columns.add(new Column<Image, com.google.gwt.user.client.ui.Image>(
				"") {
			@Override
			public com.google.gwt.user.client.ui.Image getCellValue(
					Image rowValue) {
				return new com.google.gwt.user.client.ui.Image(rowValue
						.get64x64ServerPath());
			}
		});
		columns.add(new StringColumn<Image>(enttxt.Image_filename(),
				ImageProperty.filename));
		columns.add(new StringColumn<Image>(enttxt.Image_imageType(),
				ImageProperty.imageType));
	}

	public ImageBrowserImageList() {
		super(columns);
		dataTable.setSelectionEnabled(true);
		dataTable.setSelectionPolicy(SelectionPolicy.CHECKBOX);
	}

	@Override
	public String getDefaultSortParameter() {
		return "filename";
	}

	@Override
	protected Widget getNoResultsWidget() {
		return new Label("No Image Results");
	}

}
