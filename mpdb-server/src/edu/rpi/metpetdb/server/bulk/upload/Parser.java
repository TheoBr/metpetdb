package edu.rpi.metpetdb.server.bulk.upload;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.dao.GenericDAOException;
import edu.rpi.metpetdb.client.error.validation.InvalidDateStringException;
import edu.rpi.metpetdb.client.model.Mineral;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.interfaces.HasDate;
import edu.rpi.metpetdb.client.model.interfaces.HasSample;
import edu.rpi.metpetdb.client.model.interfaces.HasSubsample;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.properties.Property;
import edu.rpi.metpetdb.client.model.validation.DateStringConstraint;

/**
 * 
 * @deprecated to be replaced by NewParser once AnalysisParser and ImageParser have been updated
 * @param <T>
 */

@Deprecated
public abstract class Parser<T extends MObject> {

	/** the character that is used to separate multiple data in one column */
	protected final static String DATA_SEPARATOR = ";";
	protected static final int METHOD = 1;
	protected static final int SAMPLE = 101;
	protected static final int SUBSAMPLE_TYPE = 103;

	protected HSSFSheet sheet;

	protected final Map<Integer, MpDbException> errors = new HashMap<Integer, MpDbException>();

	protected static Collection<Mineral> minerals;

	/**
	 * relates columns to entries in map
	 */
	
	protected final Map<Integer, Integer> colType;
	protected final Map<Integer, Method> colMethods;
	protected final Map<Integer, Object> colObjects;
	protected final Map<Integer, String> colName;

	protected Parser(final InputStream is) {
		colType = new HashMap<Integer, Integer>();
		colMethods = new HashMap<Integer, Method>();
		colObjects = new HashMap<Integer, Object>();
		colName = new HashMap<Integer, String>();
		try {
			final POIFSFileSystem fs = new POIFSFileSystem(is);
			final HSSFWorkbook wb = new HSSFWorkbook(fs);
			sheet = wb.getSheetAt(0);
		} catch (IOException e) {
			// TODO handle this better
			e.printStackTrace();
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
			final HSSFCell cell = header.getCell(i);
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

	/**
	 * Parses a date from a string, valid formats are MM-DD-YYYY and YYYY-MM-DD
	 * 
	 * @param hasDate
	 * @param date
	 * @throws InvalidDateStringException
	 */
	protected void parseDate(final HasDate hasDate, final String date)
			throws InvalidDateStringException {
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
			throw new InvalidDateStringException("Couldn't parse Date: " + date);
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

		hasDate.setDate(time);
		hasDate.setDatePrecision(precision);
	}

	public void parse() {
		parse(0);
	}

	/**
	 * Data offset is the number of offset rows until real data is started
	 * 
	 * @param dataOffset
	 */
	public void parse(int dataOffset) {
		int k = 0;

		// Skip empty rows at the start
		while (sheet.getRow(k) == null) {
			k++;
		}

		// First non-empty row is the header, want to associate what
		// we know how to parse with what is observed
		parseHeader(k);

		k += dataOffset;

		// Loop through the remaining data rows, parsing based upon the column
		// determinations
		for (int i = k + 1; i <= sheet.getLastRowNum(); ++i) {
			// System.out.println("Parsing Row " + i);
			parseRow(i);
		}
	}

	protected String sanitizeNumber(final String number) {
		return number.replaceAll("[^-\\.0-9]", "");
	}

	protected boolean handleData(final Class<?> dataType,
			final Method storeMethod, final HSSFCell cell, final Object o)
			throws InvocationTargetException, IllegalArgumentException,
			IllegalAccessException {
		if (cell.toString().equals("")) {
			return false;
		} else if (dataType == String.class) {
			if (!storeMethod.getName().equals("addReference")
					&& !storeMethod.getName().equals("addComment")) {
				String sanatizedData = cell.toString();
				if (storeMethod.getName().equals("setAlias")
						|| storeMethod.getName().equals("addRockType")) {
					sanatizedData = sanatizedData.replace(" ", "");
				}
				sanatizedData = sanatizedData.replaceAll(" +", " ");
				final String[] data = sanatizedData.split("\\s*"
						+ DATA_SEPARATOR + "\\s*");
				for (String str : data) {
					if (!"".equals(str))
						storeMethod.invoke(o, str);
				}
			} else {
				final String data = cell.toString();
				if (!"".equals(data))
					storeMethod.invoke(o, data);
			}
		} else if (dataType == Float.class || dataType == double.class
				|| dataType == Integer.class || dataType == int.class) {
			double data;
			try {
				data = Double.parseDouble(cell.toString());
				if (!cell.toString().equals(String.valueOf(data))) {
					throw new NullPointerException();
				}
			} catch (NumberFormatException nfe) {
				// most likely this cell is suppose to be a number put the
				// person put non-numeric things in it
				// so parse out the number of possible
				final String tempData = cell.toString();
				try {
					data = Double.parseDouble(tempData.replaceAll("[^-\\.0-9]",
							""));
				} catch (Exception e) {
					data = 0;
				}

			}
			

			if (dataType == Float.class)
				storeMethod.invoke(o, new Float(data));
			else if (dataType == Integer.class || dataType == int.class)
				storeMethod.invoke(o, new Double(data).intValue());
			else
				storeMethod.invoke(o, data);
		} else if (dataType == Subsample.class) {
			if (o instanceof HasSubsample) {
				final String data = cell.toString();
				if (((HasSubsample) o).getSubsample() == null)
					((HasSubsample) o).setSubsample(new Subsample());
				((HasSubsample) o).getSubsample().setName(data);
			}
		} else {
			return false;
		}
		return true;
	}

	/**
	 * Retrieves an exception that should be sent back to the client
	 * 
	 * @return
	 */
	public Map<Integer, MpDbException> getErrors() {
		return errors;
	}

	/**
	 * Replaces the alternate minerals with their correct counterparts
	 * 
	 * @param alternate
	 * @return
	 */
	protected Mineral getRealMineral(final Mineral alternate) {
		if (alternate.getId() == alternate.getRealMineralId())
			return alternate;
		final int realMineralId = alternate.getRealMineralId();
		for (Mineral m : minerals) {
			if (m.getId() == realMineralId)
				return m;
		}
		return alternate;
	}

	public static void setMinerals(final Collection<Mineral> minerals) {
		Parser.minerals = minerals;
	}

	protected final void parseRow(final int rowindex) {
		final HSSFRow row = sheet.getRow(rowindex);
		if (row == null) {
			return;
		}
		final T newObject = getNewObject();
		boolean sawDataInRow = false;

		for (Integer i = 0; i <= row.getLastCellNum(); ++i) {
			final HSSFCell cell = row.getCell(i.intValue());
			try {
				// Get the method we'll be using to parse this particular cell
				final Method storeMethod = colMethods.get(i);
				Integer type = colType.get(i);
				if (type == null)
					continue;

				if (type == SAMPLE) {
					if (newObject instanceof HasSample) {
						if (((HasSample) newObject).getSample() == null)
							((HasSample) newObject).setSample(new Sample());
						((HasSample) newObject).getSample().setAlias(
								cell.toString());
					}
				} else if (type == SUBSAMPLE_TYPE) {
					if (newObject instanceof HasSubsample) {
						if (((HasSubsample) newObject).getSubsample() == null)
							((HasSubsample) newObject)
									.setSubsample(new Subsample());
						((HasSubsample) newObject).getSubsample()
								.addSubsampleType(cell.toString());
					}
				}

				if (storeMethod == null)
					continue;
				

				// Determine what class the method wants the content of the cell
				// to be so it can parse it
				final Class<?> dataType = storeMethod.getParameterTypes()[0];
				
				if (parseColumnSpecialCase(row, i, cell.toString(), dataType, newObject)) {
					continue;
				}

				// System.out.println("\t Parsing Column " + i + ": "
				// + storeMethod.getName());

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
							storeMethod.invoke(newObject, o, data);
						} catch (NumberFormatException e) {
							storeMethod.invoke(newObject, o, new Float(0));
						}
					}
					continue;
				}

				if (handleData(dataType, storeMethod, cell, newObject)) {
					sawDataInRow = true;
				} else {
					if (dataType == Timestamp.class) {
						if (newObject instanceof HasDate) {
							final HasDate objectWithDate = (HasDate) newObject;
							try {
								final Date data = cell.getDateCellValue();
								objectWithDate.setDate(new Timestamp(data
										.getTime()));
								objectWithDate.setDatePrecision((short) 1);
								// storeMethod.invoke(s, new Timestamp(data.
								// getTime(
								// )));
							} catch (final IllegalStateException nfe) {
								// System.out.println("parsing date");
								final String data = cell.toString();
								try {
									(new DateStringConstraint())
											.validateValue(data);
									parseDate(objectWithDate, data);
									sawDataInRow = true;
								} catch (final ValidationException ve) {
									errors.put(rowindex + 1, ve);
								}
							}
						}
					}
				}
			} catch (final NullPointerException npe) {
				// empty cell
				continue;
			} catch (final InvocationTargetException ie) {
				// this indicates a bug.
				ie.getTargetException().printStackTrace();
				errors.put(rowindex, new GenericDAOException(ie.getMessage()));
			} catch (final IllegalAccessException iae) {
				// I believe this is when a method is private and we don't have
				// access. It should never get here.
				iae.printStackTrace();
				errors.put(rowindex, new GenericDAOException(iae.getMessage()));
			} catch (Exception e) {
				errors.put(rowindex, new GenericDAOException(e.getMessage()));
			}
		}

		if (sawDataInRow) {
			addObject(rowindex + 1, newObject);
		}
	}

	protected abstract void addObject(final int index, T object);

	protected abstract T getNewObject();

	protected abstract boolean parseColumnSpecialCase(final HSSFRow row,
			Integer cellNumber, final String cellText, final Class<?> dataType,
			final T currentObject) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException;

	protected void parseHeader(final int rownum) {
		// First non-empty row is the header, want to associate what
		// we know how to parse with what is observed
		HSSFRow header = sheet.getRow(rownum);
		for (int i = 0; i < header.getLastCellNum(); ++i) {
			// Convert header title to String
			final HSSFCell cell = header.getCell(i);
			final String text;
			try {
				text = cell.toString(); // getString();
			} catch (final NullPointerException npe) {
				// blank column
				continue;
			}
			// System.out.println("Parsing header " + i + ": " + text);

			// Determine method to be used for data in this column
			for (MethodAssociation<T> sma : getMethodAssociations()) {
				if (sma.matches(text)) {
					colType.put(new Integer(i), METHOD);
					colMethods.put(new Integer(i), sma.getMethod());
					colName.put(new Integer(i), sma.getName());
					break;
				}
			}
			
			parseHeaderSpecialCase(header, i, text);
		}
	}

	/**
	 * 
	 * @param header
	 * @param cellNumber
	 * 		can be modified
	 * @param cellText
	 */
	protected abstract void parseHeaderSpecialCase(final HSSFRow header,
			Integer cellNumber, final String cellText);

	protected abstract List<MethodAssociation<T>> getMethodAssociations();
}
