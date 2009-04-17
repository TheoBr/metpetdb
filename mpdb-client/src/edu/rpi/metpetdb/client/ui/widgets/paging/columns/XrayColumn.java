package edu.rpi.metpetdb.client.ui.widgets.paging.columns;

import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.XrayImage;
import edu.rpi.metpetdb.client.model.properties.Property;

/**
 * Specifcally used for Xray Images ONLY
 * @author anthony
 *
 */
public class XrayColumn extends Column<Image, String> {

	private Property<XrayImage> xrayProperty;

	public XrayColumn(String title, Property<XrayImage> xrayProperty) {
		super(title);
		this.xrayProperty = xrayProperty;
	}

	@Override
	public String getCellValue(Image rowValue) {
		if (rowValue.getImageType().getImageType().equals("X-ray Map")) {
			return xrayProperty.get((XrayImage) rowValue) == null ? "----"
					: xrayProperty.get((XrayImage) rowValue).toString();
		} else
			return "----";
	}

}