package edu.rpi.metpetdb.server.bulk.upload.sample;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.MineralDTO;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.model.validation.DateStringConstraint;

public class SampleParser {

	private final InputStream is;
	private HSSFSheet sheet;
	private final List<SampleDTO> samples;
	private final Map<Integer, ValidationException> errors = new TreeMap<Integer, ValidationException>();

	/**
	 * sampleMethodMap[][0] === name in table sampleMethodMap[][1] === method to
	 * call on Sample to store data sampleMethodMap[][2] === parameter to the
	 * store method and return value of HSSFCell method
	 * 
	 * TODO: make this a Set of objects.
	 */
	private static final Object[][] sampleMethodMap = {
			{
					"present.+location", "setLocationText", String.class
			},
			{
					"sample", "setAlias", String.class
			},
			{
					"type", "setRockType", String.class
			},
			{
					"comment", "addComment", String.class
			},
			{
					"latitude", "setLatitude", double.class
			},
			{
					"longitude", "setLongitude", double.class
			},
			{
					"region", "addRegion", String.class
			},
			{
					"country", "setCountry", String.class
			},
			{
					"collector", "setCollector", String.class
			},
			{
					"(collected)|(collection.+date)", "setCollectionDate",
					Timestamp.class
			}, {
					"reference", "addReference", String.class
			}, {
					"grade", "addMetamorphicGrade", String.class
			}, {
					"mineral", "addMineral", String.class
			}

	};

	private final static List<MethodAssociation<SampleDTO>> methodAssociations = new LinkedList<MethodAssociation<SampleDTO>>();

	private static List<MineralDTO> minerals = null;

	/**
	 * relates columns to entries in map
	 */
	private final Map<Integer, Method> colMethods;
	private final Map<Integer, Object> colObjects;

	/**
	 * 
	 * @param is
	 *            the input stream that points to a spreadsheet
	 */
	public SampleParser(final InputStream is) {
		samples = new LinkedList<SampleDTO>();
		colMethods = new HashMap<Integer, Method>();
		colObjects = new HashMap<Integer, Object>();
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
				for (Object[] row : sampleMethodMap)
					methodAssociations.add(new MethodAssociation<SampleDTO>(
							(String) row[0], (String) row[1], (Class) row[2],
							new SampleDTO()));

			final POIFSFileSystem fs = new POIFSFileSystem(is);
			final HSSFWorkbook wb = new HSSFWorkbook(fs);
			sheet = wb.getSheetAt(0);
		} catch (IOException e) {
			throw new InvalidFormatException();
		}
	}
	public void parse() {
		int k = 0;

		// Skip empty rows at the start
		while (sheet.getRow(k) == null) {
			k++;
		}

		// First non-empty row is the header, want to associate what
		// we know how to parse with what is observed
		HSSFRow header = sheet.getRow(k);
		for (int i = 0; i < header.getPhysicalNumberOfCells(); ++i) {
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
			for (MethodAssociation<SampleDTO> sma : methodAssociations) {
				if (sma.matches(text)) {
					colMethods.put(new Integer(i), sma.getMethod());
					done = true;
					break;
				}
			}

			if (done)
				continue;

			// If we don't have an explicit match for the header, it could be a
			// mineral, check for that
			try {
				for (MineralDTO m : minerals) {
					if (m.getName().equalsIgnoreCase(text)) {
						colMethods.put(new Integer(i), SampleDTO.class
								.getMethod("addMineral", MineralDTO.class,
										Float.class));
						colObjects.put(new Integer(i), m);
						done = true;
						break;
					}
				}
			} catch (NoSuchMethodException e) {
				throw new IllegalStateException(
						"Programming Error -- Invalid Sample Method");
			}
		}

		// Loop through the remaining data rows, parsing based upon the column
		// determinations
		for (int i = k + 1; i < sheet.getPhysicalNumberOfRows(); ++i) {
			System.out.println("Parsing Row " + i);
			parseRow(i);
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

		final SampleDTO s = new SampleDTO();
		boolean sawDataInRow = false;

		for (Integer i = 0; i <= row.getPhysicalNumberOfCells(); ++i) {
			final HSSFCell cell = row.getCell((short) i.intValue());
			try {
				// Get the method we'll be using to parse this particular cell
				final Method storeMethod = colMethods.get(i);

				if (storeMethod == null)
					continue;

				System.out.println("\t Parsing Column " + i + ": "
						+ storeMethod.getName());

				// If this has an object then it isn't a normal header, handle
				// accordingly
				if (colObjects.get(i) != null) {
					final Object o = colObjects.get(i);

					// Created for Mineral Processing:
					// * If cell is empty, keep moving
					// * If there is a number, then that is the amount
					// * Anything else is taken as an "unknown quantity"
					if (cell.toString().length() > 0) {
						try {
							final Float data = Float
									.parseFloat(cell.toString());
							storeMethod.invoke(s, o, data);
						} catch (NumberFormatException e) {
							storeMethod.invoke(s, o, new Float(0));
						}
					}
					continue;
				}

				// Determine what class the method wants the content of the cell
				// to be so it can parse it
				final Class dataType = storeMethod.getParameterTypes()[0];

				if (dataType == String.class) {

					if (!storeMethod.getName().equals("addReference")
							&& !storeMethod.getName().equals("addComment")) {
						final String[] data = cell.toString()
								.split("\\s*,\\s*");
						for (String str : data)
							storeMethod.invoke(s, str);
					} else {
						final String data = cell.toString();
						storeMethod.invoke(s, data);
					}

				} else if (dataType == double.class) {

					final double data = cell.getNumericCellValue();
					storeMethod.invoke(s, data);

				} else if (dataType == Timestamp.class) {

					try {
						final Date data = cell.getDateCellValue();
						s.setCollectionDate(new Timestamp(data.getTime()));
						s.setDatePrecision((short) 1);
						// storeMethod.invoke(s, new Timestamp(data.getTime()));
					} catch (final NumberFormatException nfe) {
						System.out.println("parsing date");
						final String data = cell.toString();
						try {
							(new DateStringConstraint()).validateValue(data);
							parseDate(s, data);
						} catch (final ValidationException ife) {
							errors.put(new Integer(samples.size() + 2), ife);
						}
					}
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
			samples.add(s);
		}
	}
	public List<SampleDTO> getSamples() {
		return samples;
	}

	private void parseDate(final SampleDTO s, final String date) {
		Short precision = 365;
		String day, month, year;

		// Regexes for acceptable date formats
		final Pattern datepat_mmddyyyy = Pattern
				.compile("^((\\d{2})([-/]))?((\\d{2})([-/]))?(\\d{4})$");
		final Pattern datepat_yyyymmdd = Pattern
				.compile("^(\\d{4})(([-/])(\\d{2}))?(([-/])(\\d{2}))?$");

		// See what regular expression matches our input, and then parse
		Matcher datematch;
		if ((datematch = datepat_mmddyyyy.matcher(date)).find()) {
			// MM-DD-YYYY
			month = datematch.group(2);
			day = datematch.group(5);
			year = datematch.group(7);
		} else if ((datematch = datepat_yyyymmdd.matcher(date)).find()) {
			// YYYY-MM-DD
			year = datematch.group(1);
			month = datematch.group(4);
			day = datematch.group(7);
		} else {
			throw new IllegalStateException("Couldn't parse Date: " + date);
		}

		// Set precisions, etc according to what was observed
		if (month != null) {
			precision = 31;
		} else {
			month = "01";
		}

		if (day != null) {
			precision = 1;
		} else {
			day = "01";
		}

		Timestamp time = Timestamp.valueOf(year + "-" + month + "-" + day
				+ " 00:00:00.000000000");

		s.setCollectionDate(time);
		s.setDatePrecision(precision);
	}

	public Map<Integer, ValidationException> getErrors() {
		return errors;
	}

	public static boolean areMineralsSet() {
		return !(minerals == null);
	}

	public static void setMinerals(final List<MineralDTO> minerals) {
		SampleParser.minerals = minerals;
	}
}
