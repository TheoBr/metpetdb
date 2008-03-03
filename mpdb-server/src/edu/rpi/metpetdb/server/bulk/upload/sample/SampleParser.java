package edu.rpi.metpetdb.server.bulk.upload.sample;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import edu.rpi.metpetdb.server.model.Sample;
import edu.rpi.metpetdb.server.model.User;

public class SampleParser {
	
	private final InputStream is;
	private final Set<Sample> samples;
	private final User user;
	/**
	 * methodMap[][0] === name in table
	 * methodMap[][1] === method to call on Sample to store data
	 * methodMap[][2] === parameter to the store method and return
	 * value of HSSFCell method
	 */
	private final Object[][] sampleMethodMap = {
			{ "sample", "setAlias", String.class },
			{ "rock type", "setRockType", String.class },
			{ "comment", "addComment", String.class },
			{ "latitude", "setLatitude", double.class },
			{ "longitude", "setLongitude", double.class },
			{ "region", "addRegion", String.class },
			{ "country", "setCountry", String.class },
			{ "collection", "setCollector", String.class },
			{ "when collected", "setCollectionDate", Timestamp.class },
			{ "present sample location", "setLocationText", String.class },
			{ "reference", null, null }, { "grade", null, null },
			{ "minerals present", null, null } };

	/**
	 * relates columns to entries in map
	 */
	private final Map<Integer, Method> colMethods;
	
	public SampleParser(final InputStream is, User u) {
		/*
		 * I moved the IO out of the constructor, since I think it's generally a
		 * bad idea to have constructors have any room for error.
		 */
		samples = new HashSet<Sample>();

		colMethods = new HashMap<Integer, Method>();
		this.is = is;
		this.user = u;
	}

	public void initialize() throws IOException, InvalidFormatException {
		
		final POIFSFileSystem fs = new POIFSFileSystem(is);
		final HSSFWorkbook wb = new HSSFWorkbook(fs);
		final HSSFSheet sheet = wb.getSheetAt(0);
		final HSSFRow header = sheet.getRow(0);
		for (int i = 0; i < header.getPhysicalNumberOfCells(); ++i) {
			final HSSFCell cell = header.getCell((short) i);
			final String text;
			try {
				text = cell.toString(); // getString();
			} catch (NullPointerException npe) {
				// blank column
				continue;
			}
			System.out.println("Parsing header " + i + ": " + text);
			// this seems inefficient, but since it's an ArrayList,
			// this is what a search would do anyway.
			for (int j = 0; j < sampleMethodMap.length; ++j) {
				String expectedName = (String) sampleMethodMap[j][0];
				if (expectedName.equalsIgnoreCase(text)){
					String methodName = (String) sampleMethodMap[j][1];
					Class dataType = (Class)sampleMethodMap[j][2];
					if(methodName == null || dataType == null){
						System.out.println("\tNo method for (" + text + ")");
						//TODO: throw InvalidFormatException?
						continue;
					}
					try{
						Method method = Sample.class.getMethod(methodName, dataType);
						colMethods.put(new Integer(i), method);
					} catch(NoSuchMethodException nsme){
						System.out.println("\tMethod not found (" + methodName + ")");
						//don't add anything.
					}
					break;
				}
			}
		}
		// Loop through the rows
		for (int i = 1; i < sheet.getPhysicalNumberOfRows(); ++i) {
			System.out.println("Parsing Row " + i);
			parseRow(sheet.getRow(i));
		}
	}

	
	private void parseRow(final HSSFRow row) throws InvalidFormatException{
		final Sample s = new Sample();
		for (Integer i = 0; i < row.getPhysicalNumberOfCells(); ++i) {
			final HSSFCell cell = row.getCell((short) i.intValue());
			try {
				Method storeMethod = colMethods.get(i);
				System.out.println("\t Parsing Column " + i + ": " + storeMethod.getName());
				//skip unknown columns TODO: pass along some sort of information.
				if(storeMethod == null)
					continue;
				
				Class dataType = storeMethod.getParameterTypes()[0];
				
				if(dataType == String.class){
					
					String data = cell.toString(); 
					storeMethod.invoke(s, data);
					
				} else if(dataType == double.class){
					
					double data = cell.getNumericCellValue();
					storeMethod.invoke(s, data);
					
				} else if(dataType == Timestamp.class){
					try{
					Date data = cell.getDateCellValue();
					storeMethod.invoke(s, new Timestamp(data.getTime()));
					} catch(NumberFormatException nfe){
						throw new InvalidFormatException();
					}
				}
					

			} catch (NullPointerException npe) {
				// empty cell
				continue;
			} catch (InvocationTargetException ie){
				throw new InvalidFormatException();
			} catch (IllegalAccessException iae){
				throw new InvalidFormatException();
			}
		}
		samples.add(s);
	}
	
	public Set<Sample> getSamples() {
		return samples;
	}

}