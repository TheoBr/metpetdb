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
import edu.rpi.metpetdb.client.model.ChemicalAnalysisElementDTO;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisOxideDTO;
import edu.rpi.metpetdb.client.model.ElementDTO;
import edu.rpi.metpetdb.client.model.MineralDTO;
import edu.rpi.metpetdb.client.model.OxideDTO;
import edu.rpi.metpetdb.client.model.ReferenceDTO;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.model.SubsampleDTO;

public class AnalysisParser {
	public static final int METHOD = 1;
	public static final int CAOXIDE = 2;
	public static final int CAELEMENT = 3;
	public static final int CAOXIDE_WITH_PRECISION = 4;
	public static final int CAELEMENT_WITH_PRECISION = 4;
	public static final int SAMPLE = 101;
	public static final int PERCISIONUNIT = 102;
	public static final int SUBSAMPLE_TYPE = 103;

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
					"^\\s*subsample\\s*$", "setSubsample", SubsampleDTO.class,
					"ChemicalAnalysis_subsample"
			},
			{
					"(mineral)|(material)", "setMineral", MineralDTO.class,
					"ChemicalAnalysis_mineral"
			},
			{
					"(method)|(analytical method)|(analysis method)|(^\\s*type\\s*$)",
					"setAnalysisMethod", String.class,
					"ChemicalAnalysis_analysisMethod"
			},
			{
					"(where done)|(analytical facility)", "setLocation",
					String.class, "ChemicalAnalysis_location"
			},
			{
					"(^\\s*reference\\s*$)|(^\\s*ref\\s*$)", "setReference",
					ReferenceDTO.class, "ChemicalAnalysis_reference"
			},
			{
					"(date)|(when analyzed)", "setAnalysisDate",
					Timestamp.class, "ChemicalAnalysis_analysisDate"
			},
			{
					"analyst", "setAnalyst", String.class,
					"ChemicalAnalysis_analyst"
			},
			// reference image (image)
			{
					"(point)|(spot)|(analysis location)", "setSpotId",
					String.class, "ChemicalAnalysis_spotId"
			},
			// precision (precision,error)
			{
					"(total)|(wt%tot)|(wt%total)", "setTotal", Float.class,
					"ChemicalAnalysis_total"
			},
			{
					"(comment)|(note)|(description)", "setDescription",
					String.class, "ChemicalAnalysis_comment"
			},
			{
					"(x position)|(x pos)|(x coordinate)|(x coord)",
					"setPointX", int.class, "ChemicalAnalysis_pointX"
			},
			{
					"(y position)|(y pos)|(y coordinate)|(y coord)",
					"setPointY", int.class, "ChemicalAnalysis_pointY"
			},

	};

	private final static List<MethodAssociation<ChemicalAnalysisDTO>> methodAssociations = new LinkedList<MethodAssociation<ChemicalAnalysisDTO>>();

	private static List<ElementDTO> elements = null;
	private static List<OxideDTO> oxides = null;

	/**
	 * relates columns to entries in map
	 */
	private final Map<Integer, Integer> colType;
	private final Map<Integer, Method> colMethods;
	private final Map<Integer, Object> colObjects;
	private final Map<Integer, String> colName;

	/**
	 * 
	 * @param is
	 *            the input stream that points to a spreadsheet
	 */
	public AnalysisParser(final InputStream is) {
		analyses = new LinkedList<ChemicalAnalysisDTO>();
		colType = new HashMap<Integer, Integer>();
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
				for (Object[] row : analysisMethodMap)
					methodAssociations
							.add(new MethodAssociation<ChemicalAnalysisDTO>(
									(String) row[0], (String) row[1],
									(Class) row[2], new ChemicalAnalysisDTO(),
									(String) row[3]));

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

	private void parseHeader(final int rownum) {
		// First non-empty row is the header, want to associate what
		// we know how to parse with what is observed
		HSSFRow header = sheet.getRow(rownum);
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
			for (MethodAssociation<ChemicalAnalysisDTO> sma : methodAssociations) {
				if (sma.matches(text)) {
					colType.put(new Integer(i), METHOD);
					colMethods.put(new Integer(i), sma.getMethod());
					colName.put(new Integer(i), sma.getName());
					done = true;
					break;
				}
			}

			if (done)
				continue;

			// special cases
			if (Pattern.compile("^\\s*sample\\s*$", Pattern.CASE_INSENSITIVE)
					.matcher(text).find()) {
				colType.put(new Integer(i), SAMPLE);
				colName.put(new Integer(i), "ChemicalAnalysis_sample");
				continue;
			} else if (Pattern.compile("precision unit",
					Pattern.CASE_INSENSITIVE).matcher(text).find()) {
				colType.put(new Integer(i), PERCISIONUNIT);
				colName.put(new Integer(i), "ChemicalAnalysis_precisionUnit");
				continue;
			} else if (Pattern.compile("subsample type",
					Pattern.CASE_INSENSITIVE).matcher(text).find()) {
				colType.put(new Integer(i), SUBSAMPLE_TYPE);
				colName.put(new Integer(i), "Subsample_type");
				continue;
			}

			// The string didn't match anything we explicitly check for, so
			// maybe it is an element or an oxide
			try {
				for (ElementDTO e : elements) {
					if (e.getName().equalsIgnoreCase(text)
							|| (e.getAlternateName() != null && e
									.getAlternateName().equalsIgnoreCase(text))
							|| (e.getSymbol().equalsIgnoreCase(text))) {
						// Get text of the next column
						boolean precision_next = false;
						try {
							String next_header = header
									.getCell((short) (i + 1)).toString();
							precision_next = Pattern.compile("precision",
									Pattern.CASE_INSENSITIVE).matcher(
									next_header).find();

						} catch (final NullPointerException npe) {
							// leave precision_next as false
						}

						if (precision_next) {
							colType.put(new Integer(i),
									CAELEMENT_WITH_PRECISION);
							colMethods.put(new Integer(i),
									ChemicalAnalysisDTO.class.getMethod(
											"addElement", ElementDTO.class,
											Float.class, Float.class));

							colName.put(new Integer(i + 1),
									"ChemicalAnalysis_precision");
						} else {
							colType.put(new Integer(i), CAELEMENT);
							colMethods.put(new Integer(i),
									ChemicalAnalysisDTO.class.getMethod(
											"addElement", ElementDTO.class,
											Float.class));
						}

						colObjects.put(new Integer(i), e);
						colName
								.put(new Integer(i),
										"ChemicalAnalysis_elements");
						done = true;

						if (precision_next)
							i++;
						break;
					}
				}

				if (done)
					continue;

				for (OxideDTO o : oxides) {
					if (o.getSpecies().equalsIgnoreCase(text)) {
						// Get text of the next column
						boolean precision_next = false;
						try {
							String next_header = header
									.getCell((short) (i + 1)).toString();
							precision_next = Pattern.compile("precision",
									Pattern.CASE_INSENSITIVE).matcher(
									next_header).find();

						} catch (final NullPointerException npe) {
							// leave precision_next as false
						}

						if (precision_next) {
							colType.put(new Integer(i), CAOXIDE_WITH_PRECISION);
							colMethods.put(new Integer(i),
									ChemicalAnalysisDTO.class.getMethod(
											"addOxide", OxideDTO.class,
											Float.class, Float.class));
							colName.put(new Integer(i + 1),
									"ChemicalAnalysis_precision");
						} else {
							colType.put(new Integer(i), CAOXIDE);
							colMethods.put(new Integer(i),
									ChemicalAnalysisDTO.class.getMethod(
											"addOxide", OxideDTO.class,
											Float.class));
						}

						colObjects.put(new Integer(i), o);
						colName.put(new Integer(i), "ChemicalAnalysis_oxides");

						if (precision_next)
							i++;
						break;
					}
				}
			} catch (NoSuchMethodException e) {
				throw new IllegalStateException(
						"Programming Error -- Invalid Chemical Analysis Method");
			}
		}
	}
	/**
	 * 
	 * @param rownum
	 *            the row number to parse
	 * @throws InvalidFormatException
	 *             if the row isn't of the format designated by the headers
	 */
	private void parseRow(final int rownum) {
		HSSFRow row = sheet.getRow(rownum);
		if (row == null)
			return;

		final ChemicalAnalysisDTO ca = new ChemicalAnalysisDTO();
		boolean sawDataInRow = false;

		String precisionUnit = null;
		for (Integer i = 0; i < row.getLastCellNum(); ++i) {
			final HSSFCell cell = row.getCell((short) i.intValue());
			try {
				Integer type = colType.get(i);
				if (type == null)
					continue;

				System.out.println("\t Parsing Column " + i + ": "
						+ colName.get(new Integer(i)));

				if (type == SAMPLE) {
					final String data = cell.toString();

					if (ca.getSubsample() == null)
						ca.setSubsample(new SubsampleDTO());
					if (ca.getSubsample().getSample() == null)
						ca.getSubsample().setSample(new SampleDTO());
					ca.getSubsample().getSample().setAlias(data);
				} else if (type == PERCISIONUNIT) {
					precisionUnit = cell.toString();
				} else if (type == CAOXIDE || type == CAELEMENT) {
					final Method storeMethod = colMethods.get(new Integer(i));
					final Object o = colObjects.get(i);
					final Float data = (float) cell.getNumericCellValue();

					storeMethod.invoke(ca, o, data);
				} else if (type == CAOXIDE_WITH_PRECISION
						|| type == CAELEMENT_WITH_PRECISION) {

					final Method storeMethod = colMethods.get(new Integer(i));
					final Object o = colObjects.get(i);
					final Float data = (float) cell.getNumericCellValue();
					final Float precision = (float) row
							.getCell((short) (i + 1)).getNumericCellValue();

					storeMethod.invoke(ca, o, data, precision);

					i++;
				} else if (type == SUBSAMPLE_TYPE) {
					final String data = cell.toString();

					if (ca.getSubsample() == null)
						ca.setSubsample(new SubsampleDTO());
					ca.getSubsample().addSubsampleType(data);
				} else if (type == METHOD) {
					final Method storeMethod = colMethods.get(new Integer(i));

					// Determine which class the method wants the content of the
					// cell to be so it can parse it
					final Class dataType = storeMethod.getParameterTypes()[0];

					if (dataType == String.class) {

						if (!storeMethod.getName().equals("addReference")
								&& !storeMethod.getName().equals(
										"setDescription")) {
							final String[] data = cell.toString().split(
									"\\s*,\\s*");
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

					} else if (dataType == Float.class) {

						final double data = cell.getNumericCellValue();
						storeMethod.invoke(ca, new Float(data));

					} else if (dataType == int.class) {

						final double data = cell.getNumericCellValue();
						storeMethod.invoke(ca, (int) data);

					} else if (dataType == Timestamp.class) {

						try {
							final Date data = cell.getDateCellValue();
							ca.setAnalysisDate(new Timestamp(data.getTime()));
							// storeMethod.invoke(s, new
							// Timestamp(data.getTime()));
						} catch (final NumberFormatException nfe) {
							System.out.println("parsing date");
							final String data = cell.toString();
							parseDate(ca, data);
						}

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
			if (precisionUnit != null) {
				for (ChemicalAnalysisElementDTO ele : ca.getElements())
					ele.setPrecisionUnit(precisionUnit);

				for (ChemicalAnalysisOxideDTO ox : ca.getOxides())
					ox.setPrecisionUnit(precisionUnit);
			}

			if (ca.getMineral() == null)
				ca.setLargeRock(true);
			else
				ca.setLargeRock(false);

			analyses.add(ca);
		}
	}
	public List<ChemicalAnalysisDTO> getAnalyses() {
		return analyses;
	}

	private void parseDate(final ChemicalAnalysisDTO ca, final String date) {
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

		ca.setAnalysisDate(time);
		ca.setDatePrecision(precision);
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
