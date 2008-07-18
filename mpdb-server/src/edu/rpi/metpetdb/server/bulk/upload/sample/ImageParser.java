package edu.rpi.metpetdb.server.bulk.upload.sample;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.ElementDTO;
import edu.rpi.metpetdb.client.model.ImageDTO;
import edu.rpi.metpetdb.client.model.MineralDTO;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.model.SubsampleDTO;
import edu.rpi.metpetdb.client.model.XrayImageDTO;

public class ImageParser {
	private final InputStream is;
	private HSSFSheet sheet;
	private final List<ImageDTO> images;
	private final Map<Integer, ValidationException> errors = new TreeMap<Integer, ValidationException>();

	// 0) Regex for header
	// 1) methodname to set in SampleDTO
	// 2) datatype cell needs to be converted to for use with methodname
	// 3) id in LocaleEntity for humanreadable representation of this column
	private static final Object[][] imageMethodMap = {
			{
					"subsample", "setSubsample", SubsampleDTO.class,
					"Subsample"
			},
			{
					"sample", "setSample", SampleDTO.class, "Sample"
			},
			{
					"file", "setFilename", String.class, "Image_filename"
			},
			// {
			// "format", "", String.class, "Image_format"
			// },
			{
					"image type", "setImageType", String.class,
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
					"element", "setElement", ElementDTO.class,
					"XrayImage_element"
			}
	};

	private final static List<MethodAssociation<XrayImageDTO>> methodAssociations = new LinkedList<MethodAssociation<XrayImageDTO>>();

	private static List<MineralDTO> minerals = null;

	/**
	 * relates columns to entries in map
	 */
	private final Map<Integer, Method> colMethods;
	private final Map<Integer, Object> colObjects;
	private final Map<Integer, String> colName;

	/**
	 * 
	 * @param is
	 *            the input stream that points to a spreadsheet
	 */
	public ImageParser(final InputStream is) {
		images = new LinkedList<ImageDTO>();
		colMethods = new HashMap<Integer, Method>();
		colObjects = new HashMap<Integer, Object>();
		colName = new HashMap<Integer, String>();
		this.is = is;

	}

	/**
	 * 
	 * 
	 * @throws IOException
	 *             if the file could not be read.
	 */
	public void initialize() throws InvalidFormatException,
			NoSuchMethodException {

		try {
			if (methodAssociations.isEmpty())
				for (Object[] row : imageMethodMap)
					methodAssociations.add(new MethodAssociation<XrayImageDTO>(
							(String) row[0], (String) row[1], (Class) row[2],
							new XrayImageDTO(), (String) row[3]));

			final POIFSFileSystem fs = new POIFSFileSystem(is);
			final HSSFWorkbook wb = new HSSFWorkbook(fs);
			sheet = wb.getSheetAt(0);
		} catch (IOException e) {
			throw new InvalidFormatException();
		}
	}

	public void parse() {
		int k = 0;
		while (sheet.getRow(k) == null) {
			k++;
		}

		// First non-empty row is the header, want to associate what
		// we know how to parse with what is observed
		parseHeader(k);

		// Loop through the remaining data rows, parsing based upon the column
		// determination
		for (int i = k + 1; i <= sheet.getLastRowNum(); ++i) {
			System.out.println("Parsing Row " + i);
			parseRow(i);
		}
	}

	public Map<Integer, String[]> getHeaders() {
		int k = 0;
		Map<Integer, String[]> headers = new HashMap<Integer, String[]>();

		// Skip empty rows at the start
		while (sheet.getRow(k) == null) {
			k++;
		}

		// First non-empty row is the header, want to associate what
		// we know how to parse with what is observed
		parseHeader(k);

		// Now that we've assigned columns to methods, create the column text to
		// data mapping
		HSSFRow header = sheet.getRow(k);
		for (int i = 0; i < header.getLastCellNum(); ++i) {
			final HSSFCell cell = header.getCell((short) i);
			final String text;

			try {
				text = cell.toString();
			} catch (final NullPointerException npe) {
				continue;
			}

			String[] this_header = {
					text, colName.get(new Integer(i))
			};
			if (this_header[1] == null) {
				this_header[1] = "";
			}

			headers.put(new Integer(i), this_header);
		}

		return headers;
	}

	private void parseHeader(final int rowindex) {
		HSSFRow header = sheet.getRow(rowindex);
		for (int i = 0; i < header.getLastCellNum(); ++i) {
			// Convert header title to String
			final HSSFCell cell = header.getCell((short) i);
			final String text;
			boolean done = false;

			try {
				text = cell.toString(); // getString();
			} catch (final NullPointerException npe) {
				// blank column
				continue;
			}
			System.out.println("Parsing header " + i + ": " + text);

			// Determine method to be used for data in this column
			for (MethodAssociation<XrayImageDTO> sma : methodAssociations) {
				// special case for sample
				if (text.matches("[Ss][Aa][Mm][Pp][Ll][Ee]"))
					colObjects.put(new Integer(i), new SampleDTO());

				if (sma.matches(text)) {
					colMethods.put(new Integer(i), sma.getMethod());
					colName.put(new Integer(i), sma.getName());
					done = true;
					break;
				}
			}
		}
	}

	/**
	 * 
	 * @param row
	 *            the row to parse
	 * @throws InvalidFormatException
	 *             if the row isn't of the format designated by the headers
	 */
	private void parseRow(final int rowindex) {
		final HSSFRow row = sheet.getRow(rowindex);

		if (row == null) {
			return;
		}

		final XrayImageDTO img = new XrayImageDTO();
		boolean sawDataInRow = false;

		for (Integer i = 0; i <= row.getLastCellNum(); ++i) {
			final HSSFCell cell = row.getCell((short) i.intValue());
			try {
				// Get the method we'll be using to parse this particular cell
				final Method storeMethod = colMethods.get(i);

				if (storeMethod == null)
					continue;

				System.out.println("\t Parsing Column " + i + ": "
						+ storeMethod.getName());

				// If an object for the column exists then handle accordingly
				if (colObjects.get(i) != null) {

					// If the object is a sample, we want the chemical analysis
					// to be related to a subsample of that sample
					if (colObjects.get(i) instanceof SampleDTO) {
						final String data = cell.toString();
						System.out.println("\t\t(Sample)");

						if (img.getSubsample() == null)
							img.setSubsample(new SubsampleDTO());
						if (img.getSubsample().getSample() == null)
							img.getSubsample().setSample(new SampleDTO());
						img.getSubsample().getSample().setAlias(data);
						continue;
					}
				}

				// Determine what class the method wants the content of the cell
				// to be so it can parse it
				final Class dataType = storeMethod.getParameterTypes()[0];

				if (dataType == String.class) {

					final String data = cell.toString();
					storeMethod.invoke(img, data);

				} else if (dataType == SampleDTO.class) {

					final String data = cell.toString();
					if (img.getSample() == null)
						img.setSample(new SampleDTO());
					img.getSample().setAlias(data);

				} else if (dataType == SubsampleDTO.class) {

					final String data = cell.toString();
					if (img.getSubsample() == null)
						img.setSubsample(new SubsampleDTO());
					img.getSubsample().setName(data);

				} else if (dataType == ElementDTO.class) {

					final String data = cell.toString();
					if (img.getElement() == null)
						img.setElement(new ElementDTO());
					img.getElement().setName(data);

				} else if (dataType == Integer.class) {

					final Double data = new Double(cell.getNumericCellValue());
					storeMethod.invoke(img, data.intValue());

				} else {
					throw new IllegalStateException(
							"Don't know how to convert to datatype: "
									+ dataType.toString());
				}
				sawDataInRow = true;
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
			if (img.getElement() == null)
				images.add(img.getImage());
			else
				images.add(img);
		}
	}

	public List<ImageDTO> getImages() {
		return images;
	}
}
