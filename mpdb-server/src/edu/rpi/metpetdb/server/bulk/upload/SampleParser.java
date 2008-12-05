package edu.rpi.metpetdb.server.bulk.upload;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;

import edu.rpi.metpetdb.client.error.InvalidFormatException;
import edu.rpi.metpetdb.client.model.Mineral;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.properties.SampleProperty;

public class SampleParser extends Parser<Sample> {

	private final Map<Integer, Sample> samples;
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
					"(sample[ number| name|])|(sample)", "setAlias",
					String.class, "Sample_alias"
			}, {
					"minerals", "addMineral", String.class, "Sample_minerals"
			},

	};

	private final static List<MethodAssociation<Sample>> methodAssociations = new ArrayList<MethodAssociation<Sample>>();

	/**
	 * 
	 * @param is
	 * 		the input stream that points to a spreadsheet
	 * @throws InvalidFormatException
	 */
	public SampleParser(final InputStream is) {
		super(is);
		samples = new HashMap<Integer, Sample>();
	}

static {
		try {
			if (methodAssociations.isEmpty())
				for (Object[] row : sampleMethodMap)
					methodAssociations.add(new MethodAssociation<Sample>(
							(String) row[0], (String) row[1],
							(Class<?>) row[2], new Sample(), (String) row[3]));
		} catch (NoSuchMethodException e) {

		}
	}

	protected void parseHeaderSpecialCase(final HSSFRow header,Integer cellNumber,
			final String cellText) {
		// If we don't have an explicit match for the header, it could be a
		// mineral, check for that
		try {
			for (Mineral m : minerals) {
				if (m.getName().equalsIgnoreCase(cellText)) {
					colMethods.put(new Integer(cellNumber),
							Sample.class.getMethod("addMineral", Mineral.class,
									Float.class));
					colObjects.put(new Integer(cellNumber), getRealMineral(m));
					colName.put(new Integer(cellNumber), "Sample_minerals");
					break;
				}
			}
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException(
					"Programming Error -- Invalid Sample Method");
		}
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
	protected boolean parseColumnSpecialCase(HSSFRow row, Integer cellNumber,
			String cellText, Class<?> dataType, Sample currentObject)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		// samples don't have any special cases
		return false;
	}
	@Override
	protected List<MethodAssociation<Sample>> getMethodAssociations() {
		return methodAssociations;
	}
}
