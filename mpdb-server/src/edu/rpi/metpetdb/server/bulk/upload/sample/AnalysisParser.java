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
import edu.rpi.metpetdb.client.model.ChemicalAnalysisDTO;
import edu.rpi.metpetdb.client.model.ElementDTO;
import edu.rpi.metpetdb.client.model.MineralDTO;
import edu.rpi.metpetdb.client.model.OxideDTO;
import edu.rpi.metpetdb.client.model.ReferenceDTO;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.model.SubsampleDTO;

public class AnalysisParser {

	private final InputStream is;
	private HSSFSheet sheet;
	private final List<ChemicalAnalysisDTO> analyses;
	/**
	 * sampleMethodMap[][0] === name in table sampleMethodMap[][1] === method to
	 * call on Sample to store data sampleMethodMap[][2] === parameter to the
	 * store method and return value of HSSFCell method
	 */
	private static final Object[][] analysisMethodMap = {
			{
					"subsample", "setSubsample", SubsampleDTO.class
			}, {
					"sample", "setSubsample", SubsampleDTO.class
			}, {
					"mineral", "setMineral", MineralDTO.class
			}, {
					"analyst", "setAnalyst", String.class
			}, {
					"date", "setAnalysisDate", Timestamp.class
			}, {
					// "(comment)|(description)", "setDescription", String.class
					// }, {
					"location", "setLocation", String.class
			}, {
					"reference", "setReference", ReferenceDTO.class
			}, {
					"(point)|(spot)", "setSpotId", String.class
			}, {
					"rock", "setLargeRock", Boolean.class
			}

	};

	private final static List<MethodAssociation<ChemicalAnalysisDTO>> methodAssociations = new LinkedList<MethodAssociation<ChemicalAnalysisDTO>>();

	private static List<ElementDTO> elements = null;
	private static List<OxideDTO> oxides = null;

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
	public AnalysisParser(final InputStream is) {
		analyses = new LinkedList<ChemicalAnalysisDTO>();
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
				for (Object[] row : analysisMethodMap)
					methodAssociations
							.add(new MethodAssociation<ChemicalAnalysisDTO>(
									(String) row[0], (String) row[1],
									(Class) row[2], new ChemicalAnalysisDTO()));

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
			for (MethodAssociation<ChemicalAnalysisDTO> sma : methodAssociations) {
				// special case for sample
				if (text.matches("[Ss][Aa][Mm][Pp][Ll][Ee]"))
					colObjects.put(new Integer(i), new SampleDTO());

				// Generic title => method check
				if (sma.matches(text)) {
					colMethods.put(new Integer(i), sma.getMethod());
					done = true;
					break;
				}
			}

			if (done)
				continue;

			// The string didn't match anything we explicitly check for, so
			// maybe it is an element or an oxide
			try {
				for (ElementDTO e : elements) {
					if (e.getName().equalsIgnoreCase(text)
							|| (e.getAlternateName() != null && e
									.getAlternateName().equalsIgnoreCase(text))) {
						colMethods.put(new Integer(i),
								ChemicalAnalysisDTO.class.getMethod(
										"addElement", ElementDTO.class,
										Float.class));
						colObjects.put(new Integer(i), e);
						done = true;
						break;
					}
				}

				if (done)
					continue;

				for (OxideDTO o : oxides) {
					if (o.getSpecies().equalsIgnoreCase(text)) {
						colMethods.put(new Integer(i),
								ChemicalAnalysisDTO.class.getMethod("addOxide",
										OxideDTO.class, Float.class));
						colObjects.put(new Integer(i), o);
						break;
					}
				}
			} catch (NoSuchMethodException e) {
				throw new IllegalStateException(
						"Programming Error -- Invalid Chemical Analysis Method");
			}
		}

		// Loop through the remaining data rows, parsing based upon the column
		// determination
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
		if (row == null)
			return;

		final ChemicalAnalysisDTO ca = new ChemicalAnalysisDTO();

		for (Integer i = 0; i < row.getPhysicalNumberOfCells(); ++i) {
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

						if (ca.getSubsample() == null)
							ca.setSubsample(new SubsampleDTO());
						if (ca.getSubsample().getSample() == null)
							ca.getSubsample().setSample(new SampleDTO());
						ca.getSubsample().getSample().setAlias(data);
						continue;
					}

					// Otherwise it is an Element or Oxide, so just handle
					// create one
					final Object o = colObjects.get(i);
					final Float data = (float) cell.getNumericCellValue();
					storeMethod.invoke(ca, o, data);
					continue;
				}

				// Determine which class the method wants the content of the
				// cell to be so it can parse it
				final Class dataType = storeMethod.getParameterTypes()[0];

				if (dataType == String.class) {

					if (!storeMethod.getName().equals("addReference")
							&& !storeMethod.getName().equals("setDescription")) {
						final String[] data = cell.toString()
								.split("\\s*,\\s*");
						for (String str : data)
							storeMethod.invoke(ca, str);
					} else {
						final String data = cell.toString();
						storeMethod.invoke(ca, data);
					}

				} else if (dataType == SubsampleDTO.class) {

					final String data = cell.toString();
					if (ca.getSubsample() == null)
						ca.setSubsample(new SubsampleDTO());
					ca.getSubsample().setName(data);

				} else if (dataType == MineralDTO.class) {

					final String data = cell.toString();
					if (ca.getMineral() == null)
						ca.setMineral(new MineralDTO());
					ca.getMineral().setName(data);

				} else if (dataType == ReferenceDTO.class) {

					final String data = cell.toString();
					if (ca.getReference() == null)
						ca.setReference(new ReferenceDTO());
					ca.getReference().setName(data);

				} else if (dataType == double.class) {

					final double data = cell.getNumericCellValue();
					storeMethod.invoke(ca, data);

				} else if (dataType == Timestamp.class) {

					try {
						final Date data = cell.getDateCellValue();
						ca.setAnalysisDate(new Timestamp(data.getTime()));
						// storeMethod.invoke(s, new Timestamp(data.getTime()));
					} catch (final NumberFormatException nfe) {
						System.out.println("parsing date");
						final String data = cell.toString();
						Integer precision = new Integer(0);
						Timestamp date = parseDate(data, precision);
						ca.setAnalysisDate(date);
					}

				} else if (dataType == Boolean.class) {

					final String data = cell.toString();
					ca
							.setLargeRock(data.matches("yes")
									|| data.matches("true"));
				} else {
					throw new IllegalStateException(
							"Don't know how to convert to datatype: "
									+ dataType.toString());
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
		analyses.add(ca);
	}
	public List<ChemicalAnalysisDTO> getAnalyses() {
		return analyses;
	}

	private Timestamp parseDate(final String date, Integer precision) {
		precision = 0;
		String day = "01", month = "01", year = "1900";
		// DD-MM-YYYY
		final Pattern datepat = Pattern
				.compile("((\\d{2})([-/]))?((\\d{2})([-/]))?(\\d{4})");
		final Matcher datematch = datepat.matcher(date);

		if (!datematch.find()) {
			return null;
		}

		// do we have a month?
		if (datematch.group(2) != null) {
			month = datematch.group(2);
			precision = 365;
		}
		// do we have a day?
		if (datematch.group(5) != null) {
			day = datematch.group(5);
			precision = 31;
		}

		year = datematch.group(7);
		System.out.println(year + "-" + month + "-" + day
				+ " 00:00:00.000000000");
		return Timestamp.valueOf(year + "-" + month + "-" + day
				+ " 00:00:00.000000000");
	}
	public static boolean areElementsAndOxidesSet() {
		return elements == null || oxides == null;
	}

	public static void setElementsAndOxides(final List<ElementDTO> elements,
			final List<OxideDTO> oxides) {
		AnalysisParser.elements = elements;
		AnalysisParser.oxides = oxides;
	}
}
