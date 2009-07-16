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
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.error.bulk.upload.ExpectedStringColumnTypeException;
import edu.rpi.metpetdb.client.error.bulk.upload.InvalidSpreadSheetException;
import edu.rpi.metpetdb.client.error.dao.GenericDAOException;
import edu.rpi.metpetdb.client.error.validation.InvalidDateStringException;
import edu.rpi.metpetdb.client.model.Element;
import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.ImageType;
import edu.rpi.metpetdb.client.model.Mineral;
import edu.rpi.metpetdb.client.model.Oxide;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.bulk.upload.BulkUploadError;
import edu.rpi.metpetdb.client.model.bulk.upload.BulkUploadHeader;
import edu.rpi.metpetdb.client.model.interfaces.HasDate;
import edu.rpi.metpetdb.client.model.interfaces.HasImage;
import edu.rpi.metpetdb.client.model.interfaces.HasSample;
import edu.rpi.metpetdb.client.model.interfaces.HasSubsample;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.validation.DatabaseObjectConstraints;
import edu.rpi.metpetdb.client.model.validation.DateStringConstraint;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.client.model.validation.TimestampConstraint;
import edu.rpi.metpetdb.client.model.validation.interfaces.NumberConstraint;
import edu.rpi.metpetdb.server.DataStore;

public abstract class Parser<T extends MObject> {

	/** the character that is used to separate multiple data in one column */
	protected final static String DATA_SEPARATOR = ";";
	protected HSSFSheet sheet;
	protected final Map<Integer, BulkUploadError> errors = new HashMap<Integer, BulkUploadError>();
	/** even though it it says bulk upload error, these are for warnings */
	protected final Map<Integer, BulkUploadError> warnings = new HashMap<Integer, BulkUploadError>();
	protected static Collection<Mineral> minerals;
	protected static Collection<Element> elements;
	protected static Collection<Oxide> oxides;
	protected static Collection<ImageType> imageTypes;
	protected final Map<Integer, BulkUploadHeader> headers = new HashMap<Integer, BulkUploadHeader>();
	protected static DatabaseObjectConstraints doc = DataStore.getInstance()
			.getDatabaseObjectConstraints();
	/**
	 * relates columns to entries in map
	 */
	protected final Map<Integer, PropertyConstraint> spreadSheetColumnMapping;

	protected Parser(final InputStream is) throws MpDbException {
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
	 * Returns any warnings encounterd during processing, i.e. of the cell type
	 * is wrong
	 * 
	 * @return
	 */
	public Map<Integer, BulkUploadError> getWarnings() {
		return warnings;
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

	/**
	 * Replaces abbreviations of image types with the correct ones
	 * 
	 * @param imageType
	 * @return
	 */
	protected ImageType getImageType(final String imageType) {
		for (ImageType it : imageTypes) {
			if (it.getImageType().equals(imageType))
				return it;
			if (it.getAbbreviation().equals(imageType))
				return it;
		}
		final ImageType it = new ImageType();
		it.setImageType(imageType);
		return it;
	}

	public static void setMinerals(final Collection<Mineral> minerals) {
		Parser.minerals = minerals;
	}

	public static void setElements(final Collection<Element> elements) {
		Parser.elements = elements;
	}

	public static void setOxides(final Collection<Oxide> oxides) {
		Parser.oxides = oxides;
	}

	public static void setImageTypes(final Collection<ImageType> imageTypes) {
		Parser.imageTypes = imageTypes;
	}

	protected final void parseRow(final int rowindex) {
		final HSSFRow row = sheet.getRow(rowindex);
		if (row == null) {
			return;
		}
		final T newObject = getNewObject();
		boolean sawDataInRow = false;

		for (Integer i = 0; i <= row.getLastCellNum(); ++i) {
			HSSFCell cell = row.getCell(i.intValue());
			final String cellName = new CellReference(rowindex, i)
					.formatAsString();
			try {
				// get the constraint for this cell
				final PropertyConstraint pc = spreadSheetColumnMapping.get(i);

				if (pc != null && cell != null) {
					// verify cell type
					if (pc == doc.Sample_number
							|| pc == doc.ChemicalAnalysis_spotId
							|| pc == doc.ChemicalAnalysis_sampleName
							|| pc == doc.ChemicalAnalysis_subsampleName
							|| pc == doc.Subsample_name
							|| pc == doc.Subsample_sampleName
							|| pc == doc.Image_filename)
						if (cell.getCellType() != HSSFCell.CELL_TYPE_STRING) {
							warnings.put(rowindex + 1, new BulkUploadError(
									rowindex + 1, cellName,
									new ExpectedStringColumnTypeException(),
									cell.toString()));
						}
				}

				if (pc != null && cell != null && !cell.toString().matches("^\\s*$")
						&& !parseColumnSpecialCase(row, cell, pc, newObject, i)) {

					if (pc == doc.Subsample_subsampleType) {
						// We have to invoke the method on a subsample
						if (newObject instanceof HasSubsample) {
							if (((HasSubsample) newObject).getSubsample() == null) {
								final Subsample s = new Subsample();
								((HasSubsample) newObject).setSubsample(s);
							}
							((HasSubsample) newObject).getSubsample().mSet(
									pc.property, cell.toString());
						}
					} else if (pc == doc.Subsample_name) {
						// We have to invoke the method on a subsample
						if (newObject instanceof HasSubsample) {
							if (((HasSubsample) newObject).getSubsample() == null) {
								final Subsample s = new Subsample();
								((HasSubsample) newObject).setSubsample(s);
							}
							((HasSubsample) newObject).getSubsample().mSet(
									pc.property, cell.toString());
						}
					} else if (pc == doc.Sample_number
							&& !pc.entityName.equals(newObject.getClass()
									.getSimpleName())) {
						// only do sample number if we are not working on a
						// sample
						if (newObject instanceof HasSample) {
							if (((HasSample) newObject).getSample() == null) {
								final Sample s = new Sample();
								((HasSample) newObject).setSample(s);
							}
							((HasSample) newObject).getSample().mSet(
									pc.property, cell.toString());
						}
					} else if (pc == doc.Image_filename && newObject instanceof HasImage) {
						if (((HasImage) newObject).getImage() == null) {
							final Image im = new Image();
							((HasImage) newObject).setImage(im);
						}
						((HasImage) newObject).getImage().mSet(
								pc.property, cell.toString());
					} else if (pc.entityName.equals(newObject.getClass()
							.getSimpleName())
							|| ((pc.entityName.equals("Image")
									|| pc.entityName.equals("ImageOnGrid") || pc.entityName
									.equals("XrayImage")) && newObject
									.getClass().getSimpleName().equals(
											("BulkUploadImage")))) {
						if (pc instanceof NumberConstraint<?>) {
							newObject.mSet(pc.property, getDoubleValue(cell));
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
										errors.put(rowindex,
												new BulkUploadError(
														rowindex + 1,
														new CellReference(
																rowindex, i)
																.toString(),
														ve, data));
									}
								}
							}
						} else if (pc == doc.Sample_description
								|| pc == doc.ChemicalAnalysis_description
								|| pc == doc.Image_description) {
							// we want to append the data to the field
							final String currentData = (String) newObject
									.mGet(pc.property);
							if (currentData != null)
								newObject.mSet(pc.property, currentData + "\n"
										+ cell.toString());
							else
								newObject.mSet(pc.property, cell.toString());
						}  else {
							final String data = cell.toString();
							final String[] mulitpartData = data.split("\\s*"
									+ DATA_SEPARATOR + "\\s*");
							for (String str : mulitpartData) {
								newObject.mSet(pc.property, str.trim());
							}
						}
					}
				}
				if (pc != null && cell != null && !cell.equals("")
						&& !cell.toString().matches("\\s*")) {
					if (pc.entityName.equals(newObject.getClass()
							.getSimpleName()))
						pc.validateEntity(newObject);
					sawDataInRow = true;
				}
			} catch (MpDbException e) {
				errors.put(rowindex + 1, new BulkUploadError(rowindex + 1,
						cellName, e, cell.toString()));
			} catch (NumberFormatException e) {
				// TODO maybe handle specially?
				errors.put(rowindex + 1, new BulkUploadError(rowindex + 1,
						cellName, new GenericDAOException(e.getMessage()), cell
								.toString()));
			} catch (Exception e) {
				e.printStackTrace();
				errors.put(rowindex + 1, new BulkUploadError(rowindex + 1,
						cellName, new GenericDAOException(e.getMessage()), cell
								.toString()));
			}
		}

		if (sawDataInRow) {
			addObject(rowindex + 1, newObject);
		}
	}

	protected abstract void addObject(final int index, T object);

	protected abstract T getNewObject();

	protected Double getDoubleValue(final HSSFCell cell) {
		// first try to get the numeric value, if that fails try to parse it out
		// of a string
		try {
			return new Double(cell.getNumericCellValue());
		} catch (Exception e) {
			final String number = cell.toString();
			final String data = sanitizeNumber(number);
			if (data.equals("")) {
				throw new NumberFormatException("Unable to convert '" + number
						+ "' to a number");
			}
			return Double.parseDouble(data);
		}
	}

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
	protected abstract boolean parseColumnSpecialCase(HSSFRow row,
			HSSFCell cell, final PropertyConstraint pc, final T currentObject,
			Integer cellNum) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException;

	protected void parseHeader(final int rownum) {
		// First non-empty row is the header, want to associate what
		// we know how to parse with what is observed
		HSSFRow header = sheet.getRow(rownum);
		for (int i = header.getFirstCellNum(); i < header.getLastCellNum(); ++i) {
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
							cm.getProperty().entityName + "_"
									+ cm.getProperty().propertyName));
					break;
				}
			}
			if (!parseHeaderSpecialCase(header, i, text)) {
				// unmatched column
				if (!headers.containsKey(i))
					headers.put(i, new BulkUploadHeader(text,
							"Not matched by MetPetDB"));
			}
		}
	}

	/**
	 * 
	 * @param header
	 * @param cellNumber
	 *            can be modified
	 * @param cellText
	 * 
	 * @Return true if the header is used
	 */
	protected abstract boolean parseHeaderSpecialCase(final HSSFRow header,
			Integer cellNumber, final String cellText);

	public abstract List<ColumnMapping> getColumMappings();
}
