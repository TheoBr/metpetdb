package edu.rpi.metpetdb.server.bulk.upload;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;

import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.model.Mineral;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.properties.SampleProperty;

public class NewSampleParser  {
//
//	private final Map<Integer, Sample> samples;
//	private static List<ColumnMapping> columns;
//
//	static {
//		columns
//				.add(new ColumnMapping("(type)|(rock)", SampleProperty.rockType));
//		columns.add(new ColumnMapping("(sesar)|(isgn)",
//				SampleProperty.sesarNumber));
//		columns.add(new ColumnMapping("(latitude error)|(lat error)",
//				SampleProperty.latitudeError));
//		columns.add(new ColumnMapping("(latitude)|(lat\\s*)",
//				SampleProperty.latitude));
//		columns.add(new ColumnMapping("(longitude error)|(lon error)",
//				SampleProperty.longitudeError));
//		columns.add(new ColumnMapping("(longitude)|(^lon\\s*)",
//				SampleProperty.longitude));
//		columns.add(new ColumnMapping("region", SampleProperty.regions));
//		columns.add(new ColumnMapping("country", SampleProperty.country));
//		columns.add(new ColumnMapping("(collector)|(collected by)",
//				SampleProperty.collector));
//		columns.add(new ColumnMapping(
//				"(date of collection)|(collected)|(collection.+date)",
//				SampleProperty.collectionDate));
//		columns.add(new ColumnMapping(
//				"(present.+location)|(current.+location)",
//				SampleProperty.locationText));
//		columns.add(new ColumnMapping("(grade)|(facies)",
//				SampleProperty.metamorphicGrades));
//		columns.add(new ColumnMapping("(comment)|(note)|(description)",
//				SampleProperty.comments));
//		columns.add(new ColumnMapping("(reference)|(ref)",
//				SampleProperty.references));
//		columns.add(new ColumnMapping("(sample[ number| name|])|(sample)",
//				SampleProperty.alias));
//		columns.add(new ColumnMapping("minerals", SampleProperty.minerals));
//	}
//
//	public List<ColumnMapping> getColumMappings() {
//		return columns;
//	}
//
//	/**
//	 * 
//	 * @param is
//	 * 		the input stream that points to a spreadsheet
//	 * @throws InvalidFormatException
//	 */
//	public SampleParser(final InputStream is) {
//		super(is);
//		samples = new HashMap<Integer, Sample>();
//	}
//
//	protected void parseHeaderSpecialCase(final HSSFRow header,
//			Integer cellNumber, final String cellText) {
//		// If we don't have an explicit match for the header, it could be a
//		// mineral, check for that
//		for (Mineral m : minerals) {
//			if (m.getName().equalsIgnoreCase(cellText)) {
//				columnProperties.add(SampleProperty.minerals);
//				break;
//			}
//		}
//	}
//
//	public Map<Integer, Sample> getSamples() {
//		return samples;
//	}
//
//	@Override
//	protected void addObject(int index, Sample object) {
//		samples.put(index, object);
//	}
//
//	@Override
//	protected Sample getNewObject() {
//		return new Sample();
//	}
//	@Override
//	protected boolean parseColumnSpecialCase(HSSFRow row, Integer cellNumber,
//			String cellText, Class<?> dataType, Sample currentObject)
//			throws IllegalArgumentException, IllegalAccessException,
//			InvocationTargetException {
//		// samples don't have any special cases
//		return true;
//	}
}