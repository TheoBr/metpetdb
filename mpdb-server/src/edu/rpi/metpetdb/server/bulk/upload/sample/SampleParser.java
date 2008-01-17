package edu.rpi.metpetdb.server.bulk.upload.sample;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import edu.rpi.metpetdb.client.model.Sample;

public class SampleParser {

	private Set samples;

	private Map mapping;

	public SampleParser(final String filename) {
		samples = new HashSet();
		mapping = new HashMap();
		mapping.put("Sample", getMethodWithName("setAlias"));
		mapping.put("Rock Type", getMethodWithName("setRockType"));
		mapping.put("Comment", null); // not yet
		mapping.put("Latitude", getMethodWithName("setLatitude"));
		mapping.put("Longitude", getMethodWithName("setLongitude"));
		mapping.put("Region", getMethodWithName("addRegion"));
		mapping.put("Country", getMethodWithName("setCountry"));
		mapping.put("Collection", getMethodWithName("setCollector"));
		mapping.put("When Collected", getMethodWithName("setCollectionDate"));
		mapping.put("Present Sample Location",
				getMethodWithName("setLocationText"));
		mapping.put("Reference", null); // not yet
		mapping.put("Grade", null);
		mapping.put("Minerals present", null);
		try {
			final POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(
					filename));
			final HSSFWorkbook wb = new HSSFWorkbook(fs);
			final HSSFSheet sheet = wb.getSheetAt(0);
			final HSSFRow header = sheet.getRow(0);
			final ArrayList headerText = new ArrayList();
			for (int i = 0; i < header.getPhysicalNumberOfCells(); ++i) {
				final HSSFCell cell = header.getCell((short) i);
				final HSSFRichTextString richText = cell
						.getRichStringCellValue();
				final String text = richText.getString();
				headerText.add(text);
			}
			// Loop through the rows
			for (int i = 1; i < sheet.getPhysicalNumberOfRows(); ++i) {
				parseRow(headerText, sheet.getRow(i));
			}
		} catch (IOException ioe) {
			// TODO:
		}
	}

	private void parseRow(final ArrayList headerText, final HSSFRow row) {
		final Sample s = new Sample();
		for(int i = 0;i<row.getPhysicalNumberOfCells();++i) {
			//final HSSFCell cell = row.getCell((short)i);
			final Class columnType = getColumnType((String)headerText.get(i));
			//Object value;
			if (columnType.equals(double.class)){
				//value = cell.getNumericCellValue();
			}
			else if (columnType.equals(Date.class)) {
				//value = cell.getDateCellValue();
			}
		}
		samples.add(s);
	}
	
	private Class getColumnType(final String headerText) {
		if (headerText.equals("Latitude"))
			return double.class;
		if (headerText.equals("Longitude"))
			return double.class;
		if (headerText.equals("When collected"))
			return Date.class;
		else
			return String.class;
	}
	
	private Method getMethodWithName(final String name) {
		for (int i = 0; i < Sample.class.getMethods().length; ++i) {
			final Method m = Sample.class.getMethods()[i];
			if (m.getName().equals(name))
				return m;
		}
		return null;
	}

}