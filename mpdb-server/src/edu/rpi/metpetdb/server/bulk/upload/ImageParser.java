package edu.rpi.metpetdb.server.bulk.upload;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFRow;

import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.ImageOnGrid;
import edu.rpi.metpetdb.client.model.Reference;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.XrayImage;

@Deprecated
public class ImageParser extends Parser<XrayImage> {
	public static final int IMAGE_REFERENCE = 2;
	public static final int SUBSAMPLE_TYPE = 102;
	public static final int PARENT_LOC_X = 201;
	public static final int PARENT_LOC_Y = 202;

	private boolean partOfGrid = false;
	private int parent_loc_x = 0;
	private int parent_loc_y = 0;

	private final Map<Integer, Image> images;
	private final Map<Integer, ImageOnGrid> imagesOnGrid;
	// 0) Regex for header
	// 1) methodname to set in Sample
	// 2) datatype cell needs to be converted to for use with methodname
	// 3) id in LocaleEntity for humanreadable representation of this column
	private static final Object[][] imageMethodMap = {
			{
					"subsample", "setSubsample", Subsample.class, "Subsample"
			},
			{
					"sample", "setSample", Sample.class, "Sample"
			},
			{
					"(file)|(path)", "setFilename", String.class,
					"Image_filename"
			},
			// {
			// "format", "", String.class, "Image_format"
			// },
			{
					"image type", "addImageType", String.class,
					"Image_imageType"
			},
			{
					"dwell time", "setDwelltime", Integer.class,
					"XrayImage_dwelltime"
			},
			{
					"current", "setCurrent", Integer.class, "XrayImage_current"
			},
			{
					"voltage", "setVoltage", Integer.class, "XrayImage_voltage"

			},
			{
					"element", "setElement", String.class, "XrayImage_element"
			},
			{
					"(collector)|(collected by)", "setCollector", String.class,
					"Image_collector"
			},
			{
					"scale", "setScale", Integer.class, "Image_scale"
			},
			{
					"(comment)|(note)|(description)", "addComment",
					String.class, "Image_comments"
			},
	};

	private final static List<MethodAssociation<XrayImage>> methodAssociations = new LinkedList<MethodAssociation<XrayImage>>();

	/**
	 * 
	 * @param is
	 * 		the input stream that points to a spreadsheet
	 * @throws IOException
	 */
	public ImageParser(final InputStream is) {
		super(is);
		images = new HashMap<Integer, Image>();
		imagesOnGrid = new HashMap<Integer, ImageOnGrid>();
	}

	static {
		try {
			if (methodAssociations.isEmpty())
				for (Object[] row : imageMethodMap)
					methodAssociations
							.add(new MethodAssociation<XrayImage>(
									(String) row[0], (String) row[1],
									(Class<?>) row[2], new XrayImage(),
									(String) row[3]));
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void parseHeaderSpecialCase(final HSSFRow header,
			Integer cellNumber, final String cellText) {
		// special case for sample
		// TODO extract regular expressions
		if (cellText.matches("(sample[ number| name|])|(sample)")) {
			colType.put(new Integer(cellNumber), SAMPLE);
			colObjects.put(new Integer(cellNumber), new Sample());
		} else if (Pattern.compile("subsample type", Pattern.CASE_INSENSITIVE)
				.matcher(cellText).find()) {
			colType.put(new Integer(cellNumber), SUBSAMPLE_TYPE);
			colName.put(new Integer(cellNumber), "Subsample_subsampleType");
		} else if (Pattern.compile("^\\s*grid location x\\s*$",
				Pattern.CASE_INSENSITIVE).matcher(cellText).find()) {
			colType.put(new Integer(cellNumber), PARENT_LOC_X);
			colName.put(new Integer(cellNumber), "ImageOnGrid_xpos");
		} else if (Pattern.compile("^\\s*grid location y\\s*$",
				Pattern.CASE_INSENSITIVE).matcher(cellText).find()) {
			colType.put(new Integer(cellNumber), PARENT_LOC_Y);
			colName.put(new Integer(cellNumber), "ImageOnGrid_ypos");
		} else if (Pattern.compile("(^\\s*reference\\s*$)|(^\\s*ref\\s*$)",
				Pattern.CASE_INSENSITIVE).matcher(cellText).find()) {
			colType.put(new Integer(cellNumber), IMAGE_REFERENCE);
			colName.put(new Integer(cellNumber), "Image_reference");
		}
	}

	/**
	 * 
	 * @param row
	 * 		the row to parse
	 * @throws InvalidFormatException
	 * 		if the row isn't of the format designated by the headers
	 */
	protected boolean parseColumnSpecialCase(final HSSFRow row,
			Integer cellNumber, final String cellText, final Class<?> dataType,
			final XrayImage currentObject) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		Integer type = colType.get(cellNumber);
		if (type == null)
			return false;
		// If the object is a sample, we want the image
		// to be related to a subsample of that sample
		if (colObjects.get(cellNumber) instanceof Sample) {
			if (currentObject.getSample() == null)
				currentObject.setSample(new Sample());
			currentObject.getSample().setAlias(cellText);

			if (currentObject.getSubsample() == null)
				currentObject.setSubsample(new Subsample());
			if (currentObject.getSubsample().getSample() == null)
				currentObject.getSubsample().setSample(
						currentObject.getSample());
		} else if (type == PARENT_LOC_X || type == PARENT_LOC_Y) {
			final Double data = Double.parseDouble(sanitizeNumber(cellText));
			partOfGrid = true;
			if (type == PARENT_LOC_X)
				parent_loc_x = data.intValue();
			else if (type == PARENT_LOC_Y)
				parent_loc_y = data.intValue();
		} else if (type == IMAGE_REFERENCE) {
			if (currentObject.getReferences() == null)
				currentObject.setReferences(new HashSet<Reference>());
			Reference ref = new Reference();
			ref.setName(cellText);
			currentObject.getReferences().add(ref);
		} else
			return false;
		return true;
	}

	public Map<Integer, Image> getImages() {
		return images;
	}

	public Map<Integer, ImageOnGrid> getImagesOnGrid() {
		return imagesOnGrid;
	}

	@Override
	protected void addObject(int index, XrayImage object) {
		if (partOfGrid) {
			// Parent specified, so part of a grid
			ImageOnGrid iog = new ImageOnGrid();
			iog.setTopLeftX(parent_loc_x);
			iog.setTopLeftY(parent_loc_y);

			// Sensible defaults (we want to see the img)
			iog.setResizeRatio(1);
			iog.setOpacity(100);

			// Save as a XrayImage only if we actually _are_ an XrayImage
			if (object.getElement() == null)
				iog.setImage(object.getImage());
			else
				iog.setImage(object);

			imagesOnGrid.put(index, iog);
		} else if (object.getElement() == null)
			images.put(index, object.getImage());
		else
			images.put(index, object);
	}

	@Override
	protected List<MethodAssociation<XrayImage>> getMethodAssociations() {
		return methodAssociations;
	}

	@Override
	protected XrayImage getNewObject() {
		partOfGrid = false;
		parent_loc_x = 0;
		parent_loc_y = 0;
		return new XrayImage();
	}
}
