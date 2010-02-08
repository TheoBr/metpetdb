package edu.rpi.metpetdb.server.bulk.upload;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;

import edu.rpi.metpetdb.client.error.MpDbException;
import edu.rpi.metpetdb.client.model.GeoReference;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.bulk.upload.BulkUploadHeader;
import edu.rpi.metpetdb.client.model.validation.DatabaseObjectConstraints;
import edu.rpi.metpetdb.client.model.validation.ObjectConstraints;
import edu.rpi.metpetdb.client.model.validation.PropertyConstraint;
import edu.rpi.metpetdb.server.DataStore;

public class ReferenceParser extends Parser<GeoReference>{
	
	private final Map<Integer, GeoReference> references;
	private static List<ColumnMapping> columns;
	
	static {
		final DatabaseObjectConstraints doc = DataStore.getInstance()
				.getDatabaseObjectConstraints();
		final ObjectConstraints oc = DataStore.getInstance()
				.getObjectConstraints();
		columns = new ArrayList<ColumnMapping>();
		columns.add(new ColumnMapping(RegularExpressions.SAMPLE,
				doc.Sample_number));
		columns.add(new ColumnMapping(RegularExpressions.REFERENCE_FILENAME,
				doc.GeoReference_filename));
	}

	public ReferenceParser(InputStream is) throws MpDbException {
		super(is);
		references = new TreeMap<Integer, GeoReference>();
	}

	@Override
	protected void addObject(int index, GeoReference object) {
		references.put(index, object);
	}

	public List<ColumnMapping> getColumMappings() {
		return columns;
	}

	@Override
	protected GeoReference getNewObject() {
		return new GeoReference();
	}

	@Override
	protected boolean parseColumnSpecialCase(HSSFRow row, HSSFCell cell,
			PropertyConstraint pc, GeoReference currentObject, Integer cellNum)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		return false;
	}

	@Override
	protected boolean parseHeaderSpecialCase(HSSFRow header,
			Integer cellNumber, String cellText) {
		return false;
	}

	public Map<Integer, GeoReference> getReferences() {
		return references;
	}

	public Map<Integer, BulkUploadHeader> getHeaders() {
		return headers;
	}

}
