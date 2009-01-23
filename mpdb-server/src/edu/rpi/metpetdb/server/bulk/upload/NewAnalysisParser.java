package edu.rpi.metpetdb.server.bulk.upload;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;

import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisElement;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisOxide;
import edu.rpi.metpetdb.client.model.Element;
import edu.rpi.metpetdb.client.model.Oxide;
import edu.rpi.metpetdb.client.model.bulk.upload.BulkUploadHeader;
import edu.rpi.metpetdb.client.model.validation.DatabaseObjectConstraints;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.server.DataStore;

public class NewAnalysisParser extends NewParser<ChemicalAnalysis> {

	private final Map<Integer, ChemicalAnalysis> chemicalAnalyses;
	private static List<ColumnMapping> columns;
	// Maps an oxide/element name to their measurement unit
	private final Map<String, String> measurementUnits;
	// Maps an oxide/element name to their precision unit
	private final Map<String, String> precisionUnits;
	//Oxides/Elements that are in this bulk upload process
	private final Map<String, Element> uploadElements;
	private final Map<String, Oxide> uploadOxides;

	static {
		final DatabaseObjectConstraints doc = DataStore.getInstance()
				.getDatabaseObjectConstraints();
		columns = new ArrayList<ColumnMapping>();
		columns.add(new ColumnMapping(RegularExpressions.SAMPLE,
				doc.ChemicalAnalysis_sampleName));
		columns.add(new ColumnMapping(RegularExpressions.SUBSAMPLE,
				doc.Subsample_name));
		columns.add(new ColumnMapping(RegularExpressions.SUBSAMPLE_TYPE,
				doc.Subsample_subsampleType));
		columns.add(new ColumnMapping(RegularExpressions.MINERALS,
				doc.ChemicalAnalysis_mineral));
		columns.add(new ColumnMapping(RegularExpressions.ANALYSIS_METHOD,
				doc.ChemicalAnalysis_analysisMethod));
		columns.add(new ColumnMapping(RegularExpressions.LOCATION,
				doc.ChemicalAnalysis_location));
		columns.add(new ColumnMapping(RegularExpressions.REFERENCES,
				doc.ChemicalAnalysis_reference));
		columns.add(new ColumnMapping(RegularExpressions.COLLECTION_DATE,
				doc.ChemicalAnalysis_analysisDate));
		columns.add(new ColumnMapping(RegularExpressions.ANALYST,
				doc.ChemicalAnalysis_analyst));
		columns.add(new ColumnMapping(RegularExpressions.SPOT_ID,
				doc.ChemicalAnalysis_spotId));
		columns.add(new ColumnMapping(RegularExpressions.TOTAL,
				doc.ChemicalAnalysis_total));
		columns.add(new ColumnMapping(RegularExpressions.COMMENTS,
				doc.ChemicalAnalysis_description));
		columns.add(new ColumnMapping(RegularExpressions.X_COORDINATE,
				doc.ChemicalAnalysis_referenceX));
		columns.add(new ColumnMapping(RegularExpressions.Y_COORDINATE,
				doc.ChemicalAnalysis_referenceY));
	}

	public List<ColumnMapping> getColumMappings() {
		return columns;
	}

	/**
	 * 
	 * @param is
	 * 		the input stream that points to a spreadsheet
	 * @throws MpDbException
	 * @throws InvalidFormatException
	 */
	public NewAnalysisParser(final InputStream is) throws MpDbException {
		super(is);
		chemicalAnalyses = new TreeMap<Integer, ChemicalAnalysis>();
		precisionUnits = new HashMap<String, String>();
		measurementUnits = new HashMap<String, String>();
		uploadElements = new HashMap<String, Element>();
		uploadOxides = new HashMap<String, Oxide>();
	}
	
	public void parse() {
		//data starts on row 1 for chemical analyses
		parse(1);
	}

	private void handleSpecial(final HSSFRow header, Integer cellNumber,
			final String cellText, final PropertyConstraint primary,
			final PropertyConstraint secondary) {
		// add it in to the columns
		spreadSheetColumnMapping.put(cellNumber, primary);
		headers.put(cellNumber, new BulkUploadHeader(cellText,
				primary.propertyName));
		// we have an element, check the next row for the
		// measurement unit
		final HSSFCell measurementUnitCell = sheet.getRow(
				header.getRowNum() + 1).getCell(cellNumber);
		measurementUnits.put(cellText, measurementUnitCell.toString());
		// Get text of the next column
		if (header.getLastCellNum() > cellNumber + 1) {
			final String nextHeader = header.getCell(cellNumber + 1).toString();
			if (Pattern.compile(RegularExpressions.PRECISION,
					Pattern.CASE_INSENSITIVE).matcher(nextHeader).find()) {
				// if the next column is a precion mark it is for
				// elements
				spreadSheetColumnMapping.put(cellNumber + 1, secondary);
				// also store the unit
				final HSSFCell precision = sheet.getRow(header.getRowNum() + 1)
						.getCell(cellNumber + 1);
				precisionUnits.put(cellText, precision.toString());
				++cellNumber;
				headers.put(cellNumber, new BulkUploadHeader(cellText,
						secondary.propertyName));
			}
		}
	}

	protected void parseHeaderSpecialCase(final HSSFRow header,
			Integer cellNumber, final String cellText) {
		// The string didn't match anything we explicitly check for, so
		// maybe it is an element or an oxide
		for (Element e : elements) {
			if (e.getName().equalsIgnoreCase(cellText)
					|| (e.getAlternateName() != null && e.getAlternateName()
							.equalsIgnoreCase(cellText))
					|| (e.getSymbol().equalsIgnoreCase(cellText))) {
				uploadElements.put(cellText, e);
				handleSpecial(
						header,
						cellNumber,
						cellText,
						doc.ChemicalAnalysis_elements,
						doc.ChemicalAnalysisElement_ChemicalAnalysis_elements_precision);
			}
		}
		for (Oxide o : oxides) {
			if (o.getSpecies().equalsIgnoreCase(cellText)) {
				uploadOxides.put(cellText, o);
				handleSpecial(
						header,
						cellNumber,
						cellText,
						doc.ChemicalAnalysis_oxides,
						doc.ChemicalAnalysisOxide_ChemicalAnalysis_oxides_precision);
			}
		}
	}

	public Map<Integer, ChemicalAnalysis> getChemicalAnalyses() {
		return chemicalAnalyses;
	}

	@Override
	protected void addObject(int index, ChemicalAnalysis object) {
		if (object.getMineral() == null)
			object.setLargeRock(true);
		else
			object.setLargeRock(false);
		chemicalAnalyses.put(index, object);
	}

	@Override
	protected ChemicalAnalysis getNewObject() {
		return new ChemicalAnalysis();
	}
	@Override
	protected boolean parseColumnSpecialCase(HSSFRow row, HSSFCell cell,
			PropertyConstraint pc, ChemicalAnalysis currentObject)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		final String headerText = headers.get(cell.getColumnIndex())
				.getHeaderText();
		if (pc == doc.ChemicalAnalysis_elements) {
			final ChemicalAnalysisElement element = new ChemicalAnalysisElement();
			element.setElement(uploadElements.get(headerText));
			element.setAmount(Float.parseFloat(sanitizeNumber(cell.toString())));
			element.setMeasurementUnit(measurementUnits.get(headerText));
			// see if our next column is our precision
			if (spreadSheetColumnMapping.get(cell.getColumnIndex() + 1) == doc.ChemicalAnalysisElement_ChemicalAnalysis_elements_precision) {
				element.setPrecision(Float.parseFloat(sanitizeNumber(row
						.getCell(cell.getColumnIndex() + 1).toString())));
				element.setPrecisionUnit(precisionUnits.get(headers.get(
						cell.getColumnIndex()).getHeaderText()));
			}
			element.setMinMax();
			currentObject.addElement(element);
			return true;
		} else if (pc == doc.ChemicalAnalysis_oxides) {
			final ChemicalAnalysisOxide oxide = new ChemicalAnalysisOxide();
			oxide.setOxide(uploadOxides.get(headerText));
			oxide.setAmount(Float.parseFloat(sanitizeNumber(cell.toString())));
			oxide.setMeasurementUnit(measurementUnits.get(headerText).toUpperCase());
			// see if our next column is our precision
			if (spreadSheetColumnMapping.get(cell.getColumnIndex() + 1) == doc.ChemicalAnalysisOxide_ChemicalAnalysis_oxides_precision) {
				oxide.setPrecision(Float.parseFloat(sanitizeNumber(row.getCell(
						cell.getColumnIndex() + 1).toString())));
				oxide.setPrecisionUnit(precisionUnits.get(headers.get(
						cell.getColumnIndex()).getHeaderText()).toUpperCase());
			}
			oxide.setMinMax();
			currentObject.addOxide(oxide);
			return true;
		} else {
			return false;
		}
	}
	public Map<Integer, BulkUploadHeader> getHeaders() {
		return headers;
	}

}
