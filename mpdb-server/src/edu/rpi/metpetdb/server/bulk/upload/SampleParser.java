package edu.rpi.metpetdb.server.bulk.upload;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;

import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.model.Mineral;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.bulk.upload.BulkUploadHeader;
import edu.rpi.metpetdb.client.model.validation.DatabaseObjectConstraints;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraints;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.server.DataStore;

public class SampleParser extends Parser<Sample> {

	private final Map<Integer, Sample> samples;
	private static List<ColumnMapping> columns;

	static {
		final DatabaseObjectConstraints doc = DataStore.getInstance()
				.getDatabaseObjectConstraints();
		final ObjectConstraints oc = DataStore.getInstance()
				.getObjectConstraints();
		columns = new ArrayList<ColumnMapping>();
		columns.add(new ColumnMapping(RegularExpressions.ROCK_TYPE,
				doc.Sample_rockType));
		columns.add(new ColumnMapping(RegularExpressions.SESAR_NUMBER,
				doc.Sample_sesarNumber));
		columns.add(new ColumnMapping(RegularExpressions.LATITUDE,
				oc.Sample_latitude));
		columns.add(new ColumnMapping(RegularExpressions.LONGITUDE,
				oc.Sample_longitude));
		columns.add(new ColumnMapping(RegularExpressions.LOCATION_ERROR,
				doc.Sample_locationError));
		columns.add(new ColumnMapping(RegularExpressions.REGION,
				doc.Sample_regions));
		columns.add(new ColumnMapping(RegularExpressions.COUNTRY,
				doc.Sample_country));
		columns.add(new ColumnMapping(RegularExpressions.COLLECTOR,
				doc.Sample_collector));
		columns.add(new ColumnMapping(RegularExpressions.COLLECTION_DATE,
				doc.Sample_collectionDate));
		columns.add(new ColumnMapping(RegularExpressions.LOCATION,
				doc.Sample_locationText));
		columns.add(new ColumnMapping(RegularExpressions.METAMORPHIC_GRADES,
				doc.Sample_metamorphicGrades));
		columns.add(new ColumnMapping(RegularExpressions.COMMENTS,
				doc.Sample_description));
		columns.add(new ColumnMapping(RegularExpressions.REFERENCES,
				doc.Sample_references));
		columns.add(new ColumnMapping(RegularExpressions.SAMPLE,
				doc.Sample_number));
		columns.add(new ColumnMapping(RegularExpressions.MINERALS,
				doc.Sample_minerals));
	}

	public List<ColumnMapping> getColumMappings() {
		return columns;
	}

	/**
	 * 
	 * @param is
	 *            the input stream that points to a spreadsheet
	 * @throws MpDbException
	 * @throws InvalidFormatException
	 */
	public SampleParser(final InputStream is) throws MpDbException {
		super(is);
		samples = new TreeMap<Integer, Sample>();
	}

	protected boolean parseHeaderSpecialCase(final HSSFRow header,
			Integer cellNumber, final String cellText) {
		// If we don't have an explicit match for the header, it could be a
		// mineral, check for that
		for (Mineral m : minerals) {
			if (m.getName().equalsIgnoreCase(cellText)) {
				spreadSheetColumnMapping.put(cellNumber, doc.Sample_minerals);
				headers.put(cellNumber, new BulkUploadHeader(getRealMineral(m)
						.getName(), doc.Sample_minerals.entityName + "_"
						+ doc.Sample_minerals.propertyName));
				return true;
			}
		}
		return false;
	}

	public Map<Integer, Sample> getSamples() {
		return samples;
	}

	@Override
	protected void addObject(int index, Sample object) {
		samples.put(index, object);
	}

	@Override
	protected Sample getNewObject() {
		return new Sample();
	}
	@Override
	protected boolean parseColumnSpecialCase(HSSFRow row, HSSFCell cell,
			PropertyConstraint pc, Sample currentObject, Integer cellNum)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		if (pc == doc.Sample_minerals) {
			// Created for Mineral Processing:
			// * If cell is empty, keep moving
			// * If there is a number, then that is the amount
			// * Anything else is taken as an "unknown quantity"
			if (cell.toString().length() > 0) {
				final Mineral m = new Mineral();
				m.setName(headers.get(cell.getColumnIndex()).getHeaderText());
				try {
					final Float data = Float.parseFloat(sanitizeNumber(cell
							.toString()));
					currentObject.addMineral(m, data);
				} catch (NumberFormatException e) {
					// check if the actual column is a column of minerals
					// search for the correct mineral (so alternate minerals
					// work)
					if (cell.toString().length() > 0) {
						for (Mineral mineral : minerals) {
							if (mineral.getName().equalsIgnoreCase(cell.toString())) {
								currentObject.addMineral(getRealMineral(mineral));
								return true;
							}
						}
						//if we got here the cell name doesn't equal a mineral so take the one formed
						//above from the header
						currentObject.addMineral(m);
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}
	public Map<Integer, BulkUploadHeader> getHeaders() {
		return headers;
	}
}
