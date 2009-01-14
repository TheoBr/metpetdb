package edu.rpi.metpetdb.server.bulk.upload;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
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
import edu.rpi.metpetdb.client.error.bulk.upload.InvalidSpreadSheetException;
import edu.rpi.metpetdb.client.error.dao.GenericDAOException;
import edu.rpi.metpetdb.client.error.validation.InvalidDateStringException;
import edu.rpi.metpetdb.client.model.Element;
import edu.rpi.metpetdb.client.model.Mineral;
import edu.rpi.metpetdb.client.model.Oxide;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.bulk.upload.BulkUploadError;
import edu.rpi.metpetdb.client.model.bulk.upload.BulkUploadHeader;
import edu.rpi.metpetdb.client.model.interfaces.HasDate;
import edu.rpi.metpetdb.client.model.interfaces.HasSubsample;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.DatabaseObjectConstraints;
import edu.rpi.metpetdb.client.model.validation.DateStringConstraint;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.TimestampConstraint;
import edu.rpi.metpetdb.client.model.validation.interfaces.NumberConstraint;
import edu.rpi.metpetdb.server.DataStore;

public abstract class NewParser<T extends MObject> {

	/** the character that is used to separate multiple data in one column */
	protected final static String DATA_SEPARATOR = ";";
	protected HSSFSheet sheet;
	protected final Map<Integer, BulkUploadError> errors = new HashMap<Integer, BulkUploadError>();
	protected static Collection<Mineral> minerals;
	protected static Collection<Element> elements;
	protected static Collection<Oxide> oxides;
	protected final Map<Integer, BulkUploadHeader> headers = new HashMap<Integer, BulkUploadHeader>();
	protected static DatabaseObjectConstraints doc = DataStore.getInstance()
			.getDatabaseObjectConstraints();
	/**
	 * relates columns to entries in map
	 */
	protected final Map<Integer, PropertyConstraint> spreadSheetColumnMapping;

	protected NewParser(final InputStream is) throws MpDbException {
		spreadSheetColumnMapping = new HashMap<Integer, PropertyConstraint>();
		try {
			final POIFSFileSystem fs = new POIFSFileSystem(is);
			final HSSFWorkbook wb = new HSSFWorkbook(fs);
			sheet = wb.getSheetAt(0);
		} catch (IOException e) {
			throw new InvalidSpreadSheetException(e.getMessage());
		}
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

	/**
	 * Retrieves an exception that should be sent back to the client
	 * 
	 * @return
	 */
	public Map<Integer, BulkUploadError> getErrors() {
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
		NewParser.minerals = minerals;
	}

	public static void setElements(final Collection<Element> elements) {
		NewParser.elements = elements;
	}

	public static void setOxides(final Collection<Oxide> oxides) {
		NewParser.oxides = oxides;
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
				// get the constraint for this cell
				final PropertyConstraint pc = spreadSheetColumnMapping.get(i);

				if (pc != null && cell != null && !cell.toString().equals("")
						&& !parseColumnSpecialCase(row, cell, pc, newObject)) {

					if (pc == doc.Subsample_subsampleType) {
						// We have to invoke the method on a subsample
						if (newObject instanceof HasSubsample) {
							if (((HasSubsample) newObject).getSubsample() != null) {
								((HasSubsample) newObject).getSubsample().mSet(
										pc.property, cell.toString());
							} else {
								final Subsample s = new Subsample();
								s.mSet(pc.property, cell.toString());
								((HasSubsample) newObject).setSubsample(s);
							}
						}
					} else if (pc.entityName.equals(newObject.getClass()
							.getSimpleName())) {
						if (pc instanceof NumberConstraint<?>) {
							// Santize numbers before setting them on the object
							final String data = sanitizeNumber(cell.toString());
							newObject.mSet(pc.property, data);
						} else if (pc instanceof TimestampConstraint) {
							// handle dates differently
							if (newObject instanceof HasDate) {
								final HasDate objectWithDate = (HasDate) newObject;
								try {
									final Date data = cell.getDateCellValue();
									objectWithDate.setDate(new Timestamp(data
											.getTime()));
									objectWithDate.setDatePrecision((short) 1);
								} catch (final IllegalStateException nfe) {
									final String data = cell.toString();
									try {
										(new DateStringConstraint())
												.validateValue(data);
										parseDate(objectWithDate, data);
									} catch (final ValidationException ve) {
										errors
												.put(rowindex,
														new BulkUploadError(
																rowindex + 1,
																i + 1, ve));
									}
								}
							}
						} else if (pc == doc.Sample_description || pc == doc.ChemicalAnalysis_description) {
							//we want to append the data to the field
							final String currentData = (String) newObject.mGet(pc.property);
							newObject.mSet(pc.property, currentData + "\n" + cell.toString());
						} else {
							final String data = cell.toString();
							final String[] mulitpartData = data.split("\\s*"
									+ DATA_SEPARATOR + "\\s*");
							for (String str : mulitpartData) {
								newObject.mSet(pc.property, str.trim());
							}
						}
					}
				}
				if (pc != null && cell != null && !cell.equals("") && !cell.toString().matches("\\s*")) {
					if (pc.entityName.equals(newObject.getClass()
							.getSimpleName()))
						pc.validateEntity(newObject);
					sawDataInRow = true;
				}
			} catch (MpDbException e) {
				errors.put(rowindex + 1, new BulkUploadError(rowindex + 1,
						i + 1, e));
			} catch (NumberFormatException e) {
				// TODO maybe handle specially?
				errors.put(rowindex + 1, new BulkUploadError(rowindex + 1,
						i + 1, new GenericDAOException(e.getMessage())));
			} catch (Exception e) {
				errors.put(rowindex + 1, new BulkUploadError(rowindex + 1,
						i + 1, new GenericDAOException(e.getMessage())));
			}
		}

		if (sawDataInRow) {
			addObject(rowindex + 1, newObject);
		}
	}

	protected abstract void addObject(final int index, T object);

	protected abstract T getNewObject();

	/**
	 * Return true if this column has been handled specially else false
	 * 
	 * @param row
	 * @param cell
	 * @param pc
	 * @param currentObject
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	protected abstract boolean parseColumnSpecialCase(final HSSFRow row,
			final HSSFCell cell, final PropertyConstraint pc,
			final T currentObject) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException;

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

			// Determine method to be used for data in this column
			for (ColumnMapping cm : getColumMappings()) {
				if (cm.matches(text)) {
					spreadSheetColumnMapping.put(i, cm.getProperty());
					headers.put(i, new BulkUploadHeader(text,
							cm.getProperty().propertyName));
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

	public abstract List<ColumnMapping> getColumMappings();
}
