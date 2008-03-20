package edu.rpi.metpetdb.server.bulk.upload.sample;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.server.model.Sample;

public class SampleParser {

	public final class Index {
		public final int row, col;

		public Index(final int r, final int c) {
			row = r;
			col = c;
		}
	}

	private final InputStream is;
	private HSSFSheet sheet;
	private final Set<SampleDTO> samples;
	/**
	 * sampleMethodMap[][0] === name in table sampleMethodMap[][1] === method to
	 * call on Sample to store data sampleMethodMap[][2] === parameter to the
	 * store method and return value of HSSFCell method
	 * 
	 * TODO: make this a Set of objects.
	 */
	private final Object[][] sampleMethodMap = {
			{ "sample", "setAlias", String.class },
			{ "rock type", "setRockType", String.class },
			{ "comment", "addComment", String.class },
			{ "latitude", "setLatitude", double.class },
			{ "longitude", "setLongitude", double.class },
			{ "region", "addRegion", String.class },
			{ "country", "setCountry", String.class },
			{ "collection", "setCollector", String.class },
			{ "when collected", "setCollectionDate", Timestamp.class },
			{ "present sample location", "setLocationText", String.class },
			{ "reference", null, null }, { "grade", null, null },
			{ "minerals present", null, null } };

	/**
	 * relates columns to entries in map
	 */
	private final Map<Integer, Method> colMethods;

	/**
	 * 
	 * @param is
	 *            the input stream that points to a spreadsheet
	 * @param u
	 *            the user to whom to attribute the samples created
	 */
	public SampleParser(final InputStream is) {
		samples = new HashSet<SampleDTO>();
		colMethods = new HashMap<Integer, Method>();
		this.is = is;
	}

	/**
	 * Does the bulk of the bulk upload work.
	 * 
	 * @throws IOException
	 *             if the file could not be read.
	 */
	public void initialize() throws IOException {

		final POIFSFileSystem fs = new POIFSFileSystem(is);
		final HSSFWorkbook wb = new HSSFWorkbook(fs);
		sheet = wb.getSheetAt(0);
	}

	/**
	 * validate validates the spreadsheet, setting errors as necessary.
	 * 
	 * @param cell_errors
	 *            a set to be populated with the indices of cell errors
	 * @param col_errors
	 *            a set to be populated with the indices of column errors
	 * @param row_errors
	 *            a set to be populated with the indices of row errors
	 * @return a list of lists of strings that represent the excel sheet.
	 */
	public List<List<String>> validate(final Set<Index> cell_errors,
			final Set<Integer> col_errors, final Set<Integer> row_errors) {
		final List<List<String>> rows = new ArrayList<List<String>>();
		final HSSFRow header = sheet.getRow(0);
		for (int i = 0; i < header.getPhysicalNumberOfCells(); ++i) {
			final HSSFCell cell = header.getCell((short) i);
			final String text;
			try {
				text = cell.toString(); // getString();
			} catch (final NullPointerException npe) {
				// blank column
				continue;
			}
			System.out.println("Validating header " + i + ": " + text);
			// this seems inefficient, but since it's an array,
			// this is what a search would do anyway.
			for (int j = 0; j < sampleMethodMap.length; ++j) {
				final String expectedName = (String) sampleMethodMap[j][0];
				if (expectedName.equalsIgnoreCase(text)) {
					final String methodName = (String) sampleMethodMap[j][1];
					final Class dataType = (Class) sampleMethodMap[j][2];
					if (methodName == null || dataType == null) {
						System.out.println("\tNo method for (" + text + ")");
						col_errors.add(new Integer(j));
						continue;
					}
					try {
						final Method method = Sample.class.getMethod(
								methodName, dataType);
						colMethods.put(new Integer(i), method);
					} catch (final NoSuchMethodException nsme) {
						System.out.println("\tMethod not found (" + methodName
								+ ")");
						// don't add anything.
					}
					break;
				}
			}
		}
		// Loop through the rows
		final Set<Index> new_errors = new HashSet<Index>();
		for (int i = 1; i < sheet.getPhysicalNumberOfRows(); ++i) {
			new_errors.clear();
			System.out.println("Parsing Row " + i);
			rows.add(validateRow(sheet.getRow(i), new_errors));
			if (!new_errors.isEmpty()) {
				cell_errors.addAll(new_errors);
				row_errors.add(new Integer(i));
			}
		}

		return rows;
	}

	public void parse() throws InvalidFormatException {
		final HSSFRow header = sheet.getRow(0);
		if (header == null) {
			throw new InvalidFormatException();
		}
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
			// this seems inefficient, but since it's an array,
			// this is what a search would do anyway.
			for (int j = 0; j < sampleMethodMap.length; ++j) {
				final String expectedName = (String) sampleMethodMap[j][0];
				if (expectedName.equalsIgnoreCase(text)) {
					final String methodName = (String) sampleMethodMap[j][1];
					final Class dataType = (Class) sampleMethodMap[j][2];
					if (methodName == null || dataType == null) {
						System.out.println("\tNo method for (" + text + ")");
						// throw new InvalidFormatException();
						continue;
					}
					try {
						final Method method = SampleDTO.class.getMethod(
								methodName, dataType);
						colMethods.put(new Integer(i), method);
					} catch (final NoSuchMethodException nsme) {
						System.out.println("\tMethod not found (" + methodName
								+ ")");
						// don't add anything.
					}
					break;
				}
			}
		}
		// Loop through the rows
		for (int i = 1; i < sheet.getPhysicalNumberOfRows(); ++i) {
			System.out.println("Parsing Row " + i);
			parseRow(sheet.getRow(i));
		}
	}

	private List<String> validateRow(final HSSFRow row, final Set<Index> errors) {
		final List<String> output = new ArrayList<String>();
		for (Integer i = 0; i < row.getPhysicalNumberOfCells(); ++i) {
			final HSSFCell cell = row.getCell((short) i.intValue());
			try {
				final Method storeMethod = colMethods.get(i);
				System.out.println("\t Validating Column " + i + ": "
						+ storeMethod.getName());
				if (storeMethod == null)
					continue;

				final Class dataType = storeMethod.getParameterTypes()[0];

				if (dataType == String.class) {

					cell.toString();

				} else if (dataType == double.class) {

					cell.getNumericCellValue();

				} else if (dataType == Timestamp.class) {
					try {
						cell.getDateCellValue();
					} catch (final NumberFormatException nfe) {
						errors.add(new Index(row.getRowNum(), i));
					}
				}

			} catch (final NullPointerException npe) {
				// empty cell
				// continue;
			} catch (final NumberFormatException nfe) {
				errors.add(new Index(row.getRowNum(), i));
			} finally {
				try {
					output.add(cell.toString());
				} catch (final NullPointerException npe) {
					output.add("");
				}
			}
		}
		return output;
	}

	/**
	 * 
	 * @param row
	 *            the row to parse
	 * @throws InvalidFormatException
	 *             if the row isn't of the format designated by the headers
	 */
	private void parseRow(final HSSFRow row) throws InvalidFormatException {
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

					final String data = cell.toString();
					storeMethod.invoke(s, data);

				} else if (dataType == double.class) {

					final double data = cell.getNumericCellValue();
					storeMethod.invoke(s, data);

				} else if (dataType == Timestamp.class) {
					try {
						final Date data = cell.getDateCellValue();
						storeMethod.invoke(s, new Timestamp(data.getTime()));
					} catch (final NumberFormatException nfe) {
						// throw new InvalidFormatException();
					}
				}

			} catch (final NullPointerException npe) {
				// empty cell
				continue;
			} catch (final InvocationTargetException ie) {
				// hopefully it will have been thrown already.
				throw new InvalidFormatException();
			} catch (final IllegalAccessException iae) {
				// I believe this is when a method is private and we don't have
				// access. It should never get here.
				throw new InvalidFormatException(/*
													 * "Internal System Error:
													 * Illegal Method Access"
													 */);
			}
		}
		samples.add(s);
	}

	public Set<SampleDTO> getSamples() {
		return samples;
	}

}