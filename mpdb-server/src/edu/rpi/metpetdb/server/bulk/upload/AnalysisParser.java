package edu.rpi.metpetdb.server.bulk.upload;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFRow;

import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisElement;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisOxide;
import edu.rpi.metpetdb.client.model.Element;
import edu.rpi.metpetdb.client.model.Mineral;
import edu.rpi.metpetdb.client.model.Oxide;
import edu.rpi.metpetdb.client.model.Reference;
import edu.rpi.metpetdb.client.model.Subsample;

@Deprecated
public class AnalysisParser extends Parser<ChemicalAnalysis> {

	public static final int CAOXIDE = 2;
	public static final int CAELEMENT = 3;
	public static final int CAOXIDE_WITH_PRECISION = 4;
	public static final int CAELEMENT_WITH_PRECISION = 4;

	public static final int PERCISIONUNIT = 102;

	private final Map<Integer, ChemicalAnalysis> analyses;
	private String precisionUnit = null;
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
	public AnalysisParser(final InputStream is) {
		super(is);
		analyses = new HashMap<Integer, ChemicalAnalysis>();
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
		precisionUnit = null;
		parse(1);
	}

	protected void parseHeaderSpecialCase(final HSSFRow header,
			Integer cellNumber, final String cellText) {
		// special cases
		if (Pattern.compile("^(sample[ number| name|])|^(sample)$",
				Pattern.CASE_INSENSITIVE).matcher(cellText).find()) {
			colType.put(new Integer(cellNumber), SAMPLE);
			colName.put(new Integer(cellNumber), "ChemicalAnalysis_sample");
			return;
		} else if (Pattern.compile("precision unit", Pattern.CASE_INSENSITIVE)
				.matcher(cellText).find()) {
			colType.put(new Integer(cellNumber), PERCISIONUNIT);
			colName.put(new Integer(cellNumber),
					"ChemicalAnalysis_precisionUnit");
			return;
		} else if (Pattern.compile("subsample type", Pattern.CASE_INSENSITIVE)
				.matcher(cellText).find()) {
			colType.put(new Integer(cellNumber), SUBSAMPLE_TYPE);
			colName.put(new Integer(cellNumber), "Subsample_subsampleType");
			return;
		}

		// The string didn't match anything we explicitly check for, so
		// maybe it is an element or an oxide
		try {
			for (Element e : elements) {
				if (e.getName().equalsIgnoreCase(cellText)
						|| (e.getAlternateName() != null && e
								.getAlternateName().equalsIgnoreCase(cellText))
						|| (e.getSymbol().equalsIgnoreCase(cellText))) {
					// Get text of the next column
					boolean precision_next = false;
					try {
						String next_header = header.getCell(cellNumber + 1)
								.toString();
						precision_next = Pattern.compile("precision",
								Pattern.CASE_INSENSITIVE).matcher(next_header)
								.find();

					} catch (final NullPointerException npe) {
						// leave precision_next as false
					}

					if (precision_next) {
						colType.put(new Integer(cellNumber),
								CAELEMENT_WITH_PRECISION);
						colMethods.put(new Integer(cellNumber),
								ChemicalAnalysis.class
										.getMethod("addElement", Element.class,
												Float.class, Float.class));

						colName.put(new Integer(cellNumber + 1),
								"ChemicalAnalysis_precision");
					} else {
						colType.put(new Integer(cellNumber), CAELEMENT);
						colMethods.put(new Integer(cellNumber),
								ChemicalAnalysis.class.getMethod("addElement",
										Element.class, Float.class));
					}

					colObjects.put(new Integer(cellNumber), e);
					colName.put(new Integer(cellNumber),
							"ChemicalAnalysis_elements");

					if (precision_next)
						cellNumber++;
					break;
				}
			}

			for (Oxide o : oxides) {
				if (o.getSpecies().equalsIgnoreCase(cellText)) {
					// Get text of the next column
					boolean precision_next = false;
					try {
						String next_header = header.getCell(cellNumber + 1)
								.toString();
						precision_next = Pattern.compile("precision",
								Pattern.CASE_INSENSITIVE).matcher(next_header)
								.find();

					} catch (final NullPointerException npe) {
						// leave precision_next as false
					}

					if (precision_next) {
						colType.put(new Integer(cellNumber),
								CAOXIDE_WITH_PRECISION);
						colMethods.put(new Integer(cellNumber),
								ChemicalAnalysis.class.getMethod("addOxide",
										Oxide.class, Float.class, Float.class));
						colName.put(new Integer(cellNumber + 1),
								"ChemicalAnalysis_precision");
					} else {
						colType.put(new Integer(cellNumber), CAOXIDE);
						colMethods.put(new Integer(cellNumber),
								ChemicalAnalysis.class.getMethod("addOxide",
										Oxide.class, Float.class));
					}

					colObjects.put(new Integer(cellNumber), o);
					colName.put(new Integer(cellNumber),
							"ChemicalAnalysis_oxides");

					if (precision_next)
						cellNumber++;
					break;
				}
			}
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException(
					"Programming Error -- Invalid Chemical Analysis Method");
		}
	}
	/**
	 * 
	 * @param rownum
	 * 		the row number to parse
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvalidFormatException
	 * 		if the row isn't of the format designated by the headers
	 */
	protected boolean parseColumnSpecialCase(final HSSFRow row,
			Integer cellNumber, final String cellText, final Class<?> dataType,
			final ChemicalAnalysis currentObject)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, NumberFormatException {
		Integer type = colType.get(cellNumber);
		if (type == null)
			return false;
		if (type == PERCISIONUNIT) {
			precisionUnit = cellText;
		} else if (type == CAOXIDE || type == CAELEMENT) {
			final Method storeMethod = colMethods.get(new Integer(cellNumber));
			final Object o = colObjects.get(cellNumber);
			final Float data = Float.valueOf(sanitizeNumber(cellText));
			storeMethod.invoke(currentObject, o, data);
		} else if (type == CAOXIDE_WITH_PRECISION
				|| type == CAELEMENT_WITH_PRECISION) {
			final Method storeMethod = colMethods.get(new Integer(cellNumber));
			final Object o = colObjects.get(cellNumber);
			final Float data = Float.valueOf(sanitizeNumber(cellText));
			final Float precision = Float.valueOf(sanitizeNumber(row.getCell(
					(cellNumber + 1)).toString()));
			storeMethod.invoke(currentObject, o, data, precision);
			cellNumber++;
		} else if (type == METHOD) {
			// Determine which class the method wants the content of the
			// cell to be so it can parse it
			if (dataType == Mineral.class) {
				if (currentObject.getMineral() == null)
					currentObject.setMineral(new Mineral());
				currentObject.getMineral().setName(cellText);

			} else if (dataType == Reference.class) {
				if (currentObject.getReference() == null)
					currentObject.setReference(new Reference());
				currentObject.getReference().setName(cellText);
			} else 
				return false;
		} else
			return false;
		return true;
	}

	protected void addObject(final int index, final ChemicalAnalysis ca) {
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

		analyses.put(index, ca);
	}

	public Map<Integer, ChemicalAnalysis> getAnalyses() {
		return analyses;
	}

	public static void setElementsAndOxides(final List<Element> elements,
			final List<Oxide> oxides) {
		AnalysisParser.elements = elements;
		AnalysisParser.oxides = oxides;
	}

	@Override
	protected List<MethodAssociation<ChemicalAnalysis>> getMethodAssociations() {
		return methodAssociations;
	}

	@Override
	protected ChemicalAnalysis getNewObject() {
		return new ChemicalAnalysis();
	}
}
