package edu.rpi.metpetdb.client.ui.objects.list;

import java.util.ArrayList;

import com.google.gwt.gen2.table.client.SelectionGrid.SelectionPolicy;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleEntity;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.properties.ImageProperty;
import edu.rpi.metpetdb.client.model.properties.XrayImageProperty;
import edu.rpi.metpetdb.client.ui.dialogs.ViewImagePopup;
import edu.rpi.metpetdb.client.ui.image.browser.ImageBrowserImageList;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.paging.Column;
import edu.rpi.metpetdb.client.ui.widgets.paging.StringColumn;
import edu.rpi.metpetdb.client.ui.widgets.paging.XrayColumn;

/**
 * Typically used for subsamples, it extends ImageBrowserImageList because this
 * one adds more columns and disables selection
 * 
 * @author anthony
 * 
 */
public abstract class ImageListViewer extends List<Image> {

	private static final LocaleEntity enttxt = LocaleHandler.lc_entity;
	private static ArrayList<Column<Image, ?>> columns;
	static {
		// copy the columns from the image browser
		columns = new ArrayList<Column<Image, ?>>(ImageBrowserImageList.columns);
		columns.add(new StringColumn<Image>(enttxt.Image_collector(),
				ImageProperty.collector));
		columns.add(new XrayColumn(enttxt.XrayImage_current(),
				XrayImageProperty.current));
		columns.add(new XrayColumn(enttxt.XrayImage_voltage(),
				XrayImageProperty.voltage));
		columns.add(new XrayColumn(enttxt.XrayImage_dwelltime(),
				XrayImageProperty.dwelltime));
		columns.add(new XrayColumn(enttxt.XrayImage_element(),
				XrayImageProperty.element));
		columns.add(new Column<Image, MLink>("") {

			@Override
			public MLink getCellValue(final Image rowValue) {
				return new MLink("View Larger", new ClickListener() {
					public void onClick(final Widget sender) {
						// FIXME hack to work with view image popup
						final ArrayList<Image> lol = new ArrayList<Image>();
						lol.add(rowValue);
						new ViewImagePopup(lol,
								new com.google.gwt.user.client.ui.Image(
										rowValue.getHalfServerPath()), 0)
								.show();
					}
				});
			}

		});
	}

	public ImageListViewer() {
		super(columns);
		// we have to first change the selection policy in order to prevent
		// an error when their are checkboxes/radio buttons
		dataTable.setSelectionPolicy(SelectionPolicy.ONE_ROW);
		dataTable.setSelectionEnabled(false);
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
