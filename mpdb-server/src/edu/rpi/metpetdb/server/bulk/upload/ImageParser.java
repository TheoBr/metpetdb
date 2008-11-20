package edu.rpi.metpetdb.server.bulk.upload;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.ImageOnGrid;
import edu.rpi.metpetdb.client.model.Reference;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.XrayImage;

public class ImageParser extends Parser {
	public static final int METHOD = 1;
	public static final int IMAGE_REFERENCE = 2;
	public static final int SAMPLE = 101;
	public static final int SUBSAMPLE_TYPE = 102;
	public static final int PARENT_LOC_X = 201;
	public static final int PARENT_LOC_Y = 202;

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
	public ImageParser(final InputStream is) throws IOException {
		super();
		images = new HashMap<Integer, Image>();
		imagesOnGrid = new HashMap<Integer, ImageOnGrid>();
		final POIFSFileSystem fs = new POIFSFileSystem(is);
		final HSSFWorkbook wb = new HSSFWorkbook(fs);
		sheet = wb.getSheetAt(0);
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

	protected void parseHeader(final int rowindex) {
		HSSFRow header = sheet.getRow(rowindex);
		for (int i = 0; i < header.getLastCellNum(); ++i) {
			// Convert header title to String
			final HSSFCell cell = header.getCell(i);
			final String text;
			boolean done = false;

			try {
				text = cell.toString(); // getString();
			} catch (final NullPointerException npe) {
				// blank column
				continue;
			}
			// System.out.println("Parsing header " + i + ": " + text);

			// Determine method to be used for data in this column
			for (MethodAssociation<XrayImage> sma : methodAssociations) {
				// special case for sample
				if (text.matches("[Ss][Aa][Mm][Pp][Ll][Ee]")) {
					colType.put(new Integer(i), SAMPLE);
					colObjects.put(new Integer(i), new Sample());
				} else if (Pattern.compile("subsample type",
						Pattern.CASE_INSENSITIVE).matcher(text).find()) {
					colType.put(new Integer(i), SUBSAMPLE_TYPE);
					colName.put(new Integer(i), "Subsample_subsampleType");
					continue;
				}

				if (sma.matches(text)) {
					if (colType.get(new Integer(i)) == null)
						colType.put(new Integer(i), METHOD);
					colMethods.put(new Integer(i), sma.getMethod());
					colName.put(new Integer(i), sma.getName());
					done = true;
					break;
				}
			}

			if (done)
				continue;

			if (Pattern.compile("^\\s*grid location x\\s*$",
					Pattern.CASE_INSENSITIVE).matcher(text).find()) {
				colType.put(new Integer(i), PARENT_LOC_X);
				colName.put(new Integer(i), "ImageOnGrid_xpos");
				done = true;
			} else if (Pattern.compile("^\\s*grid location y\\s*$",
					Pattern.CASE_INSENSITIVE).matcher(text).find()) {
				colType.put(new Integer(i), PARENT_LOC_Y);
				colName.put(new Integer(i), "ImageOnGrid_ypos");
				done = true;
			} else if (Pattern.compile("(^\\s*reference\\s*$)|(^\\s*ref\\s*$)",
					Pattern.CASE_INSENSITIVE).matcher(text).find()) {
				colType.put(new Integer(i), IMAGE_REFERENCE);
				colName.put(new Integer(i), "Image_reference");
			}
		}
	}

	/**
	 * 
	 * @param row
	 * 		the row to parse
	 * @throws InvalidFormatException
	 * 		if the row isn't of the format designated by the headers
	 */
	protected void parseRow(final int rowindex) {
		final HSSFRow row = sheet.getRow(rowindex);

		if (row == null) {
			return;
		}

		final XrayImage img = new XrayImage();
		boolean sawDataInRow = false;

		boolean partOfGrid = false;
		int parent_loc_x = 0;
		int parent_loc_y = 0;

		for (int i = 0; i <= row.getLastCellNum(); ++i) {
			final HSSFCell cell = row.getCell(i);
			try {
				// Get the method we'll be using to parse this particular cell
				Integer type = colType.get(i);
				if (type == null)
					continue;

				// System.out.println("\t Parsing Column " + i + ": "
				// + colName.get(new Integer(i)));

				// If an object for the column exists then handle accordingly
				final int columnType = colType.get(new Integer(i));
				if (columnType == SAMPLE) {

					// If the object is a sample, we want the image
					// to be related to a subsample of that sample
					if (colObjects.get(i) instanceof Sample) {
						final String data = cell.toString();
						// System.out.println("\t\t(Sample)");

						if (img.getSample() == null)
							img.setSample(new Sample());
						img.getSample().setAlias(data);

						if (img.getSubsample() == null)
							img.setSubsample(new Subsample());
						if (img.getSubsample().getSample() == null)
							img.getSubsample().setSample(img.getSample());
						continue;
					}
				} else if (columnType == SUBSAMPLE_TYPE) {
					if (img.getSubsample() == null)
						img.setSubsample(new Subsample());
					if (img.getSubsample().getSample() == null)
						img.getSubsample().setSample(img.getSample());
					final String data = cell.toString();
					img.getSubsample().addSubsampleType(data);
				} else if (columnType == PARENT_LOC_X
						|| columnType == PARENT_LOC_Y) {
					final Double data = new Double(cell.getNumericCellValue());
					partOfGrid = true;
					if (columnType == PARENT_LOC_X)
						parent_loc_x = data.intValue();
					else if (columnType == PARENT_LOC_Y)
						parent_loc_y = data.intValue();
				} else if (columnType == IMAGE_REFERENCE) {
					final String data = cell.toString();
					if (img.getReferences() == null)
						img.setReferences(new HashSet<Reference>());
					Reference ref = new Reference();
					ref.setName(data);
					img.getReferences().add(ref);
				} else if (columnType == METHOD) {
					final Method storeMethod = colMethods.get(i);

					// Determine what class the method wants the content of the
					// cell
					// to be so it can parse it
					final Class<?> dataType = storeMethod.getParameterTypes()[0];

					if (handleData(dataType, storeMethod, cell, img)) {
						
					} else if (dataType == Sample.class) {

						final String data = cell.toString();
						if (img.getSample() == null)
							img.setSample(new Sample());
						img.getSample().setAlias(data);

					} else if (dataType == Subsample.class) {

						final String data = cell.toString();
						if (img.getSubsample() == null)
							img.setSubsample(new Subsample());
						img.getSubsample().setName(data);

						if (img.getSubsample().getSample() == null)
							img.getSubsample().setSample(img.getSample());
					} else {
						throw new IllegalStateException(
								"Don't know how to convert to datatype: "
										+ dataType.toString());
					}
					sawDataInRow = true;
				}
			} catch (final NullPointerException npe) {
				// empty cell
				continue;
			} catch (final InvocationTargetException ie) {
				// this indicates a bug.
				ie.getTargetException().printStackTrace();
				// ie.printStackTrace();
				// throw new IllegalStateException(ie.getMessage());
			} catch (final IllegalAccessException iae) {
				// I believe this is when a method is private and we don't have
				// access. It should never get here.
				iae.printStackTrace();
				throw new IllegalStateException(iae.getMessage());
			}
		}

		if (sawDataInRow) {
			if (partOfGrid) {
				// Parent specified, so part of a grid
				ImageOnGrid iog = new ImageOnGrid();
				iog.setTopLeftX(parent_loc_x);
				iog.setTopLeftY(parent_loc_y);

				// Sensible defaults (we want to see the img)
				iog.setResizeRatio(1);
				iog.setOpacity(100);

				// Save as a XrayImage only if we actually _are_ an XrayImage
				if (img.getElement() == null)
					iog.setImage(img.getImage());
				else
					iog.setImage(img);

				imagesOnGrid.put(rowindex, iog);
			} else if (img.getElement() == null)
				images.put(rowindex + 1, img.getImage());
			else
				images.put(rowindex + 1, img);
		}
	}

	public Map<Integer, Image> getImages() {
		return images;
	}

	public Map<Integer, ImageOnGrid> getImagesOnGrid() {
		return imagesOnGrid;
	}
}
