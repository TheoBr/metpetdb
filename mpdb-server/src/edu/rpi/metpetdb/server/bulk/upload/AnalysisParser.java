package edu.rpi.metpetdb.server.bulk.upload;

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
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisElement;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisOxide;
import edu.rpi.metpetdb.client.model.Element;
import edu.rpi.metpetdb.client.model.Mineral;
import edu.rpi.metpetdb.client.model.Oxide;
import edu.rpi.metpetdb.client.model.Reference;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;

public class AnalysisParser extends Parser {
	public static final int METHOD = 1;
	public static final int CAOXIDE = 2;
	public static final int CAELEMENT = 3;
	public static final int CAOXIDE_WITH_PRECISION = 4;
	public static final int CAELEMENT_WITH_PRECISION = 4;
	public static final int SAMPLE = 101;
	public static final int PERCISIONUNIT = 102;
	public static final int SUBSAMPLE_TYPE = 103;

	private final Map<Integer, ChemicalAnalysis> analyses;
	/**
	 * sampleMethodMap[][0] === name in table sampleMethodMap[][1] === method to
	 * call on Sample to store data sampleMethodMap[][2] === parameter to the
	 * store method and return value of HSSFCell method
	 */
	private static final Object[][] analysisMethodMap = {
			{
					"^\\s*subsample\\s*$", "setSubsample", Subsample.class,
					"ChemicalAnalysis_subsample"
			},
			{
					"(mineral)|(material)", "setMineral", Mineral.class,
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
					Reference.class, "ChemicalAnalysis_reference"
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

	private final static List<MethodAssociation<ChemicalAnalysis>> methodAssociations = new LinkedList<MethodAssociation<ChemicalAnalysis>>();

	private static List<Element> elements = null;
	private static List<Oxide> oxides = null;

	/**
	 * 
	 * @param is
	 * 		the input stream that points to a spreadsheet
	 * @throws IOException
	 */
	public AnalysisParser(final InputStream is) throws IOException {
		super();
		analyses = new HashMap<Integer, ChemicalAnalysis>();
		final POIFSFileSystem fs = new POIFSFileSystem(is);
		final HSSFWorkbook wb = new HSSFWorkbook(fs);
		sheet = wb.getSheetAt(0);
	}

	static {
		try {
			if (methodAssociations.isEmpty())
				for (Object[] row : analysisMethodMap)
					methodAssociations
							.add(new MethodAssociation<ChemicalAnalysis>(
									(String) row[0], (String) row[1],
									(Class<?>) row[2], new ChemicalAnalysis(),
									(String) row[3]));
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void parse() {
		parse(1);
	}

	protected void parseHeader(final int rownum) {
		// First non-empty row is the header, want to associate what
		// we know how to parse with what is observed
		HSSFRow header = sheet.getRow(rownum);
		for (int i = 0; i < header.getLastCellNum(); ++i) {
			// Convert header title to String
			final HSSFCell cell = header.getCell(i);
			final String text;
			boolean done = false;
			try {
				text = cell.toString(); // getString();
			} catch (final NullPointerException npe) {
				// blank column
				continue;
			}
			// System.out.println("Parsing header " + i + ": " + text);

			// Determine method to be used for data in this column
			for (MethodAssociation<ChemicalAnalysis> sma : methodAssociations) {
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
			if (Pattern.compile("^(sample[ number| name|])|(sample)$", Pattern.CASE_INSENSITIVE)
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
				colName.put(new Integer(i), "Subsample_subsampleType");
				continue;
			}

			// The string didn't match anything we explicitly check for, so
			// maybe it is an element or an oxide
			try {
				for (Element e : elements) {
					if (e.getName().equalsIgnoreCase(text)
							|| (e.getAlternateName() != null && e
									.getAlternateName().equalsIgnoreCase(text))
							|| (e.getSymbol().equalsIgnoreCase(text))) {
						// Get text of the next column
						boolean precision_next = false;
						try {
							String next_header = header.getCell((i + 1))
									.toString();
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
									ChemicalAnalysis.class.getMethod(
											"addElement", Element.class,
											Float.class, Float.class));

							colName.put(new Integer(i + 1),
									"ChemicalAnalysis_precision");
						} else {
							colType.put(new Integer(i), CAELEMENT);
							colMethods.put(new Integer(i),
									ChemicalAnalysis.class.getMethod(
											"addElement", Element.class,
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

				for (Oxide o : oxides) {
					if (o.getSpecies().equalsIgnoreCase(text)) {
						// Get text of the next column
						boolean precision_next = false;
						try {
							String next_header = header.getCell((i + 1))
									.toString();
							precision_next = Pattern.compile("precision",
									Pattern.CASE_INSENSITIVE).matcher(
									next_header).find();

						} catch (final NullPointerException npe) {
							// leave precision_next as false
						}

						if (precision_next) {
							colType.put(new Integer(i), CAOXIDE_WITH_PRECISION);
							colMethods.put(new Integer(i),
									ChemicalAnalysis.class.getMethod(
											"addOxide", Oxide.class,
											Float.class, Float.class));
							colName.put(new Integer(i + 1),
									"ChemicalAnalysis_precision");
						} else {
							colType.put(new Integer(i), CAOXIDE);
							colMethods.put(new Integer(i),
									ChemicalAnalysis.class.getMethod(
											"addOxide", Oxide.class,
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
	 * 		the row number to parse
	 * @throws InvalidFormatException
	 * 		if the row isn't of the format designated by the headers
	 */
	protected void parseRow(final int rownum) {
		HSSFRow row = sheet.getRow(rownum);
		if (row == null)
			return;

		final ChemicalAnalysis ca = new ChemicalAnalysis();
		boolean sawDataInRow = false;

		String precisionUnit = null;
		for (int i = 0; i < row.getLastCellNum(); ++i) {
			final HSSFCell cell = row.getCell(i);
			try {
				Integer type = colType.get(i);
				if (type == null)
					continue;

				// System.out.println("\t Parsing Column " + i + ": "
				// + colName.get(new Integer(i)));

				if (type == SAMPLE) {
					final String data = cell.toString();

					if (ca.getSubsample() == null)
						ca.setSubsample(new Subsample());
					if (ca.getSubsample().getSample() == null)
						ca.getSubsample().setSample(new Sample());
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
					final Float data = Float.valueOf(sanitizeNumber(cell.toString()));
					final Float precision = Float.valueOf(sanitizeNumber(row.getCell((i + 1))
							.toString()));

					storeMethod.invoke(ca, o, data, precision);

					i++;
				} else if (type == SUBSAMPLE_TYPE) {
					final String data = cell.toString();

					if (ca.getSubsample() == null)
						ca.setSubsample(new Subsample());
					ca.getSubsample().addSubsampleType(data);
				} else if (type == METHOD) {
					final Method storeMethod = colMethods.get(new Integer(i));

					// Determine which class the method wants the content of the
					// cell to be so it can parse it
					final Class<?> dataType = storeMethod.getParameterTypes()[0];

					if (handleData(dataType, storeMethod, cell, ca)) {

					} else if (dataType == Subsample.class) {

						final String data = cell.toString();
						if (ca.getSubsample() == null)
							ca.setSubsample(new Subsample());
						ca.getSubsample().setName(data);

					} else if (dataType == Mineral.class) {

						final String data = cell.toString();
						if (ca.getMineral() == null)
							ca.setMineral(new Mineral());
						ca.getMineral().setName(data);

					} else if (dataType == Reference.class) {

						final String data = cell.toString();
						if (ca.getReference() == null)
							ca.setReference(new Reference());
						ca.getReference().setName(data);
					} else if (dataType == Timestamp.class) {

						try {
							final Date data = cell.getDateCellValue();
							ca.setAnalysisDate(new Timestamp(data.getTime()));
							// storeMethod.invoke(s, new
							// Timestamp(data.getTime()));
						} catch (final IllegalStateException nfe) {
							// System.out.println("parsing date");
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
				for (ChemicalAnalysisElement ele : ca.getElements())
					ele.setPrecisionUnit(precisionUnit);

				for (ChemicalAnalysisOxide ox : ca.getOxides())
					ox.setPrecisionUnit(precisionUnit);
			}

			if (ca.getMineral() == null)
				ca.setLargeRock(true);
			else
				ca.setLargeRock(false);

			analyses.put(rownum + 1, ca);
		}
	}
	public Map<Integer, ChemicalAnalysis> getAnalyses() {
		return analyses;
	}

	public static void setElementsAndOxides(final List<Element> elements,
			final List<Oxide> oxides) {
		AnalysisParser.elements = elements;
		AnalysisParser.oxides = oxides;
	}
}
