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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.Mineral;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.validation.DateStringConstraint;

public class SampleParser extends Parser {

	private final List<Sample> samples;
	private final Map<Integer, ValidationException> errors = new HashMap<Integer, ValidationException>();

	/**
	 * TODO: make this a Set of objects.
	 */
	// 0) Regex for header
	// 1) methodname to set in Sample
	// 2) datatype cell needs to be converted to for use with methodname
	// 3) id in LocaleEntity for humanreadable representation of this column
	private static final Object[][] sampleMethodMap = {
			{
					"(type)|(rock)", "addRockType", String.class,
					"Sample_rockType"
			},
			{
					"(sesar)|(isgn)", "setSesarNumber", String.class,
					"Sample_sesarNumber"
			},
			{
					"(latitude error)|(lat error)", "setLatitudeError",
					Float.class, "Sample_latitudeError"
			},
			{
					"(latitude)|(lat\\s*)", "setLatitude", double.class,
					"Sample_latitude"
			},
			{
					"(longitude error)|(lon error)", "setLongitudeError",
					Float.class, "Sample_longitudeError"
			},
			{
					"(longitude)|(^lon\\s*)", "setLongitude", double.class,
					"Sample_longitude"
			},
			{
					"region", "addRegion", String.class, "Sample_regions"
			},
			{
					"country", "setCountry", String.class, "Sample_country"
			},
			{
					"(collector)|(collected by)", "setCollector", String.class,
					"Sample_collector"
			},
			{
					"(date of collection)|(collected)|(collection.+date)",
					"setCollectionDate", Timestamp.class,
					"Sample_collectionDate"
			},
			{
					"(present.+location)|(current.+location)",
					"setLocationText", String.class, "Sample_locationText"
			},
			{
					"(grade)|(facies)", "addMetamorphicGrade", String.class,
					"Sample_metamorphicGrades"
			},
			{
					"(comment)|(note)|(description)", "addComment",
					String.class, "Sample_comments"
			},
			{
					"(reference)|(ref)", "addReference", String.class,
					"Sample_references"
			},
			{
					"(sample[ number| name|])|(sample)", "setAlias", String.class,
					"Sample_alias"
			}, {
					"minerals", "addMineral", String.class, "Sample_minerals"
			},

	};

	private final static List<MethodAssociation<Sample>> methodAssociations = new ArrayList<MethodAssociation<Sample>>();

	private static Collection<Mineral> minerals;

	/**
	 * 
	 * @param is
	 * 		the input stream that points to a spreadsheet
	 * @throws InvalidFormatException
	 */
	public SampleParser(final InputStream is)  {
		super();
		samples = new LinkedList<Sample>();
		try {
			final POIFSFileSystem fs = new POIFSFileSystem(is);
			final HSSFWorkbook wb = new HSSFWorkbook(fs);
			sheet = wb.getSheetAt(0);
		} catch (IOException e) {

		}
	}

	static {
		try {
			if (methodAssociations.isEmpty())
				for (Object[] row : sampleMethodMap)
					methodAssociations.add(new MethodAssociation<Sample>(
							(String) row[0], (String) row[1], (Class<?>) row[2],
							new Sample(), (String) row[3]));
		} catch (NoSuchMethodException e) {

		}
	}

	

	protected void parseHeader(final int rowindex) {
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
			//System.out.println("Parsing header " + i + ": " + text);

			// Determine method to be used for data in this column
			for (MethodAssociation<Sample> sma : methodAssociations) {
				if (sma.matches(text)) {
					colMethods.put(new Integer(i), sma.getMethod());
					colName.put(new Integer(i), sma.getName());
					done = true;
					break;
				}
			}

			if (done)
				continue;

			// If we don't have an explicit match for the header, it could be a
			// mineral, check for that
			try {
				for (Mineral m : minerals) {
					if (m.getName().equalsIgnoreCase(text)) {
						colMethods.put(new Integer(i), Sample.class.getMethod(
								"addMineral", Mineral.class, Float.class));
						colObjects.put(new Integer(i), getRealMineral(m));
						colName.put(new Integer(i), "Sample_minerals");
						done = true;
						break;
					}
				}
			} catch (NoSuchMethodException e) {
				throw new IllegalStateException(
						"Programming Error -- Invalid Sample Method");
			}
		}
	}
	
	/**
	 * Replaces the alternate minerals with their correct counterparts
	 * @param alternate
	 * @return
	 */
	private Mineral getRealMineral(final Mineral alternate) {
		if (alternate.getId() == alternate.getRealMineralId())
			return alternate;
		final int realMineralId = alternate.getRealMineralId();
		for(Mineral m : minerals) {
			if (m.getId() == realMineralId)
				return m;
		}
		return alternate;
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

		final Sample s = new Sample();
		boolean sawDataInRow = false;

		for (Integer i = 0; i <= row.getLastCellNum(); ++i) {
			final HSSFCell cell = row.getCell((short) i.intValue());
			try {
				// Get the method we'll be using to parse this particular cell
				final Method storeMethod = colMethods.get(i);

				if (storeMethod == null)
					continue;

				//System.out.println("\t Parsing Column " + i + ": "
						//+ storeMethod.getName());

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
						String sanatizedData = cell.toString();
						if (storeMethod.getName().equals("setAlias") || storeMethod.getName().equals("addRockType")) {
							sanatizedData = sanatizedData.replace(" ", "");
						}
						sanatizedData = sanatizedData.replaceAll(" +", " ");
						final String[] data = sanatizedData
								.split("\\s*" + DATA_SEPARATOR +"\\s*");
						for (String str : data) {
							if (!"".equals(str))
								storeMethod.invoke(s, str);
						}
					} else {
						final String data = cell.toString();
						if (!"".equals(data))
							storeMethod.invoke(s, data);
					}

				} else if (dataType == Float.class || dataType == double.class) {
					double data;
					try {
						data = Double.parseDouble(cell.toString());
						if (!cell.toString().equals(String.valueOf(data))) {
							throw new NullPointerException();
						}
					} catch(NumberFormatException nfe) {
						//most likely this cell is suppose to be a number put the person put non-numeric things in it
						//so parse out the number of possible
						final String tempData = cell.toString();
						try {
							data = Double.parseDouble(tempData.replaceAll("[^-\\.0-9]", ""));
						} catch (Exception e) {
							data = 0;
						}
					}
					
					if (dataType == Float.class)
						storeMethod.invoke(s,new Float(data));
					else
						storeMethod.invoke(s,data);

				} else if (dataType == Timestamp.class) {

					try {
						final Date data = cell.getDateCellValue();
						s.setCollectionDate(new Timestamp(data.getTime()));
						s.setDatePrecision((short) 1);
						// storeMethod.invoke(s, new Timestamp(data.getTime()));
					} catch (final IllegalStateException nfe) {
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

	public List<Sample> getSamples() {
		return samples;
	}

	public Map<Integer, ValidationException> getErrors() {
		return errors;
	}

	public static void setMinerals(final Collection<Mineral> minerals) {
		SampleParser.minerals = minerals;
	}
}
