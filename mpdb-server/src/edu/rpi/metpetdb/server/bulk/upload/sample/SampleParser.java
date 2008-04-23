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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.model.SampleDTO;

public class SampleParser {

	private final InputStream is;
	private HSSFSheet sheet;
	private final List<SampleDTO> samples;
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

	/**
	 * relates columns to entries in map
	 */
	private final Map<Integer, Method> colMethods;

	/**
	 * 
	 * @param is
	 *            the input stream that points to a spreadsheet
	 */
	public SampleParser(final InputStream is) {
		samples = new LinkedList<SampleDTO>();
		colMethods = new HashMap<Integer, Method>();
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
		while (sheet.getRow(k) == null) {
			k++;
		}
		HSSFRow header = sheet.getRow(k);
		for (int i = 0; i < header.getPhysicalNumberOfCells(); ++i) {
			final HSSFCell cell = header.getCell((short) i);
			final String text;
			try {
				text = cell.toString(); // getString();
			} catch (final NullPointerException npe) {
				// blank column
				continue;
			}
			System.out.println("Parsing header " + i + ": " + text);
			for (MethodAssociation<SampleDTO> sma : methodAssociations) {
				if (sma.matches(text)) {
					colMethods.put(new Integer(i), sma.getMethod());
					break;
				}
			}
		}
		// Loop through the rows
		for (int i = k + 1; i < sheet.getPhysicalNumberOfRows(); ++i) {
			System.out.println("Parsing Row " + i);
			parseRow(sheet.getRow(i));
		}
	}

	/**
	 * 
	 * @param row
	 *            the row to parse
	 * @throws InvalidFormatException
	 *             if the row isn't of the format designated by the headers
	 */
	private void parseRow(final HSSFRow row) {
		final SampleDTO s = new SampleDTO();

		for (Integer i = 0; i < row.getPhysicalNumberOfCells(); ++i) {
			final HSSFCell cell = row.getCell((short) i.intValue());
			try {
				final Method storeMethod = colMethods.get(i);
				System.out.println("\t Parsing Column " + i + ": "
						+ storeMethod.getName());

				if (storeMethod == null)
					continue;

				final Class dataType = storeMethod.getParameterTypes()[0];

				if (dataType == String.class) {

					if (storeMethod.getName().equals("addReference")
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
						parseDate(s, data);
					}
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
		samples.add(s);
	}
	public List<SampleDTO> getSamples() {
		return samples;
	}

	private void parseDate(final SampleDTO s, final String date) {
		Short precision = 365;
		String day, month, year;
		// DD-MM-YYYY
		final Pattern datepat = Pattern
				.compile("^((\\d{2})([-/]))?((\\d{2})([-/]))?(\\d{4})$");
		final Matcher datematch = datepat.matcher(date);

		if (!datematch.find()) {
			throw new IllegalStateException();
		}

		// do we have a month?
		if (datematch.group(2) != null) {
			month = datematch.group(2);
			precision = 31;
		} else
			month = "01";
		// do we have a day?
		if ((day = datematch.group(5)) != null) {
			precision = 1;
		} else
			day = "01";

		year = datematch.group(7);
		Timestamp time = Timestamp.valueOf(year + "-" + month + "-" + day
				+ " 00:00:00.000000000");

		s.setCollectionDate(time);
		s.setDatePrecision(precision);
	}
}
