package edu.rpi.metpetdb.server.bulk.upload;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;

import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.model.XrayImage;
import edu.rpi.metpetdb.client.model.bulk.upload.BulkUploadHeader;
import edu.rpi.metpetdb.client.model.validation.DatabaseObjectConstraints;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.server.DataStore;

public class ImageParser extends Parser<BulkUploadImage> {

	private final Map<Integer, BulkUploadImage> bulkUploadImages;
	private static List<ColumnMapping> columns;

	static {
		final DatabaseObjectConstraints doc = DataStore.getInstance()
				.getDatabaseObjectConstraints();
		columns = new ArrayList<ColumnMapping>();
		columns.add(new ColumnMapping(RegularExpressions.SAMPLE,
				doc.Sample_number));
		columns.add(new ColumnMapping(RegularExpressions.SUBSAMPLE,
				doc.Subsample_name));
		columns.add(new ColumnMapping(RegularExpressions.SUBSAMPLE_TYPE,
				doc.Subsample_subsampleType));
		columns.add(new ColumnMapping(RegularExpressions.FILENAME,
				doc.Image_filename));
		columns.add(new ColumnMapping(RegularExpressions.IMAGE_TYPE,
				doc.Image_imageType));
		columns.add(new ColumnMapping(RegularExpressions.DWELL_TIME,
				doc.XrayImage_dwelltime));
		columns.add(new ColumnMapping(RegularExpressions.CURRENT,
				doc.XrayImage_current));
		columns.add(new ColumnMapping(RegularExpressions.VOLTAGE,
				doc.XrayImage_voltage));
		columns.add(new ColumnMapping(RegularExpressions.ELEMENT,
				doc.XrayImage_element));
		columns.add(new ColumnMapping(RegularExpressions.COLLECTOR,
				doc.Image_collector));
		columns
				.add(new ColumnMapping(RegularExpressions.SCALE,
						doc.Image_scale));
		columns.add(new ColumnMapping(RegularExpressions.COMMENTS,
				doc.Image_description));
		columns.add(new ColumnMapping(RegularExpressions.PARENT_LOC_X,
				doc.ImageOnGrid_topLeftX));
		columns.add(new ColumnMapping(RegularExpressions.PARENT_LOC_Y,
				doc.ImageOnGrid_topLeftY));
		columns.add(new ColumnMapping(RegularExpressions.REFERENCES,
				doc.Image_references));
	}

	public List<ColumnMapping> getColumMappings() {
		return columns;
	}

	/**
	 * 
	 * @param is
	 *            the input stream that points to a spreadsheet
	 * @throws MpDbException
	 * @throws InvalidFormatException
	 */
	public ImageParser(final InputStream is) throws MpDbException {
		super(is);
		bulkUploadImages = new TreeMap<Integer, BulkUploadImage>();
	}

	protected boolean parseHeaderSpecialCase(final HSSFRow header,
			Integer cellNumber, final String cellText) {
		return false;
	}

	public Map<Integer, BulkUploadImage> getBulkUploadImages() {
		return bulkUploadImages;
	}

	@Override
	protected void addObject(int index, BulkUploadImage object) {
		// Save as a XrayImage only if we actually _are_ an XrayImage
		if (object.getImage() instanceof XrayImage) {
			object.getImageOnGrid().setImage(object.getImage());
		} else {
			object.getImageOnGrid().setImage(
					((XrayImage) object.getImage()).getImage());
			object.setImage(((XrayImage) object.getImage()).getImage());
		}
		bulkUploadImages.put(index, object);
	}

	@Override
	protected BulkUploadImage getNewObject() {
		return new BulkUploadImage();
	}
	@Override
	protected boolean parseColumnSpecialCase(HSSFRow row, HSSFCell cell,
			PropertyConstraint pc, BulkUploadImage currentObject, Integer cellNum)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		if (pc == doc.Image_imageType) {
			currentObject.getImage().setImageType(getImageType(cell.toString().trim()));
			return true;
		} else {
			return false;
		}
	}
	public Map<Integer, BulkUploadHeader> getHeaders() {
		return headers;
	}
}
