package edu.rpi.metpetdb.server.bulk.upload.sample;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import edu.rpi.metpetdb.server.model.Sample;

public class SampleParser {

	private final InputStream is;
	private final Set<Sample> samples;
	/*
	 * methodMap[][0] = name in table methodMap[][1] = method to call on
	 * HSSFCell to retrieve data methodMap[][2] = method to call on Sample to
	 * store data methodMap[][3] = parameter to the store method
	 */
	private final Object[][] methodMap = {
			{ "sample", "getStringCellValue", "setAlias", String.class },
			{ "rock type", "getStringCellValue", "setRockType", String.class },
			{ "comment", "getStringCellValue", "addComment", String.class },
			{ "latitude", "getNumericCellValue", "setLatitude", double.class },
			{ "longitude", "getNumericCellValue", "setLongitude", double.class },
			{ "region", "getStringCellValue", "addRegion", String.class },
			{ "country", "getStringCellValue", "setCountry", String.class },
			{ "collection", "getStringCellValue", "setCollector", String.class },
			{ "when collected", "getDateCellValue", "setCollectionDate",
					Date.class },
			{ "present sample location", "getStringCellValue",
					"setLocationText", String.class },
			{ "reference", null, null, null }, { "grade", null, null, null },
			{ "minerals present", null, null, null } };
	// relates columns to entries in map
	private final ArrayList<Integer> colMethods;

	public SampleParser(final InputStream is) {
		/*
		 * I moved the IO out of the constructor, since I think it's generally a
		 * bad idea to have constructors have any room for error.
		 */
		samples = new HashSet<Sample>();
		colMethods = new ArrayList<Integer>();
		this.is = is;
	}

	public void initialize() throws IOException {
		final POIFSFileSystem fs = new POIFSFileSystem(is);
		final HSSFWorkbook wb = new HSSFWorkbook(fs);
		final HSSFSheet sheet = wb.getSheetAt(0);
		final HSSFRow header = sheet.getRow(0);
		for (int i = 0; i < header.getPhysicalNumberOfCells(); ++i) {
			final HSSFCell cell = header.getCell((short) i);
			System.err.println("Parsing header " + i);
			//final HSSFRichTextString richText = cell.getRichStringCellValue();
			final String text;
			try {
				text = cell.toString(); // getString();
			} catch (NullPointerException npe) {
				colMethods.add(new Integer(-1));
				continue;
			}
			// this seems inefficient, but since it's an ArrayList,
			// this is what a search would do anyway.
			for (int j = 0; j < methodMap.length; ++j) {
				if (((String) methodMap[j][0]).equalsIgnoreCase(text))
					colMethods.add(new Integer(j));
			}
		}
		// Loop through the rows
		for (int i = 1; i < sheet.getPhysicalNumberOfRows(); ++i) {
			parseRow(sheet.getRow(i));
		}
	}

	@SuppressWarnings("unchecked")
	private void parseRow(final HSSFRow row) {
		final Sample s = new Sample();
		for (int i = 0; i < row.getPhysicalNumberOfCells(); ++i) {
			final HSSFCell cell = row.getCell((short) i);
			try {
				int methodNumber = colMethods.get(i).intValue();
				if (methodNumber == -1)
					continue;
				final Method readMethod = (Method) HSSFCell.class.getMethod(
						(String) methodMap[methodNumber][1], new Class[] {});
				final Method storeMethod = (Method) Sample.class.getMethod(
						(String) methodMap[methodNumber][2],
						new Class[] { (Class) methodMap[methodNumber][3] });
				storeMethod.invoke(s, readMethod.invoke(cell, (Object[]) null));

			} catch (Exception e) {
				System.err.println("Bulk Upload Parse Error " + e.toString());
			}
		}
		samples.add(s);
	}

	/*
	 * @SuppressWarnings("unchecked") private Class getColumnType(final String
	 * headerText) { if (headerText.equals("Latitude")) return double.class; if
	 * (headerText.equals("Longitude")) return double.class; if
	 * (headerText.equals("When collected")) return Date.class; else return
	 * String.class; }
	 * 
	 * private Method getMethodWithName(final String name) { for (int i = 0; i <
	 * Sample.class.getMethods().length; ++i) { final Method m =
	 * Sample.class.getMethods()[i]; if (m.getName().equals(name)) return m; }
	 * return null; }
	 */
	public Set<Sample> getSamples() {
		return samples;
	}

}