package edu.rpi.metpetdb.server.chemical.analysis;

import java.io.FileInputStream;
import java.io.FileWriter;
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

/**
 * Class to parse "Elements and Oxides.xls" and convert data
 * relating to chemical oxides into relevant SQL statements.
 * TODO: Implement element_id correctly
 * @author gramm
 * @debug fyffem
 * @debug gramm
 */

public class OxideParser {
	
	/**
	 * Use POI HSSF to open the file and sends each row to be parsed.
	 * This function almost entirely a copy from SampleParser.java
	 * @param inFile path to the input file
	 * @param outFile path to the output file
	 */
	public OxideParser( final String inFile, final String outFile ) {
		try { //attempt to access the spreadsheet
			final POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(
					inFile));
			final HSSFWorkbook wb = new HSSFWorkbook(fs);
			final HSSFSheet sheet = wb.getSheetAt(0);
			/*final HSSFRow header = sheet.getRow(0);
			final ArrayList<String> headerText = new ArrayList<String>();
			for (int i = 0; i < header.getPhysicalNumberOfCells(); ++i) {
				final HSSFCell cell = header.getCell((short) i);
				final HSSFRichTextString richText = cell
						.getRichStringCellValue();
				final String text = richText.getString();
				headerText.add(text);
			}*/
			
			FileWriter ostr = new FileWriter(outFile,false);
			
			// Loop through the rows
			for (int i = 5; i < sheet.getPhysicalNumberOfRows(); ++i)
				parseRow(/*headerText,*/ sheet.getRow(i), ostr);
			ostr.close();
		} catch ( IOException ioe ) { /*TODO*/ }
	}
	
	
	/**
	 * Convert a row from the spreadsheet into SQL table insertions.
//	 * param headerText ??? not sure this is needed
	 * @param row the row from the spreadsheet being parsed
	 * @param ostr file stream to the output file
	 */
	private void parseRow(/*final ArrayList<String> headerText,*/
			final HSSFRow row, FileWriter ostr) {
		final short NUM_COLUMNS = 10; //columns with info about 1st species
		final short NUM_COLUMNS2 = 16; //columns with info about 2nd species
		final short NUM_COLUMNS3 = 21; //columns with info about 3rd species
		
		double element_id=0, oxidation_state=0,weight=0,
			cations_per_oxide=0,conversion_factor=0;
		//String oxidation_state = new String();
		//char[] species = new char[20];
		String species = new String();
		//String weight = new String();
		//String cations_per_oxide = new String();
		//String conversion_factor = new String();
		//char[] mineral_type = new char[12]; Not sure how to evaluate this

		//Before doing anything, check if the element is actually an oxide
		//Column 5 == Oxidation State, will be NON-EMPTY for an oxide
		HSSFCell current = row.getCell((short)5);
		if (null == current)
			return;
		
		//current.setCellType(1);
		for ( short i = 0; i < NUM_COLUMNS; i++ ) {
			current = row.getCell(i);
			switch(i) {
			case 3: //atomic number
				element_id = current.getNumericCellValue();
				break;
				
			case 5: //1st oxidation state
				oxidation_state = current.getNumericCellValue();
				break;
				
			case 6: //1st species
				species = current.getRichStringCellValue().toString();
				break;
				
			case 7: //1st oxide weight
				weight = current.getNumericCellValue();
				break;
				
			case 8: //1st cations per oxide
				cations_per_oxide = current.getNumericCellValue();
				break;
				
			case 9: //1st element to oxide conversion factor
				conversion_factor = current.getNumericCellValue();
				break;
				
			default:
				break;
			}
		}
		try { //write data for the 1st species
			ostr.write( 
					 "INSERT INTO oxides ( element_id, oxidation_state, species,"
					+" weight, cations_per_oxide, conversion_factor )\r\n"
					+"VALUES ( '"+(int)element_id+"' '"+(int)oxidation_state
					+"' '"+species+"' '"+weight+"' '"
					+(int)cations_per_oxide+"' '"+conversion_factor+" )\r\n"
			);	
		} catch (IOException ioe) { /*TODO*/ }
		
		//Check if a second oxide species exists
		//Column 11 == 2nd Oxidation State, will be NON-empty for 2nd species
		current = row.getCell((short)11);
		if ( null == current )
			return;
		
		for ( short i = NUM_COLUMNS+1; i < NUM_COLUMNS2; i++ ) {
			current = row.getCell(i);
			switch(i) {
			case 11: //2nd oxidation state
				oxidation_state = current.getNumericCellValue();
				break;
				
			case 12: //2nd species
				species = current.getRichStringCellValue().toString();
				break;
				
			case 13: //2nd oxide weight
				weight = current.getNumericCellValue();
				break;
				
			case 14: //2nd cations per oxide
				cations_per_oxide = current.getNumericCellValue();
				break;
				
			case 15: //2nd element to oxide conversion factor
				conversion_factor = current.getNumericCellValue();
				break;
				
			default:
				break;
			}
		}
		
		try { //write data for the 2nd species
			ostr.write( 
					 "INSERT INTO oxides ( element_id, oxidation_state, species,"
					+" weight, cations_per_oxide, conversion_factor )\r\n"
					+"VALUES ( '"+(int)element_id+"' '"+(int)oxidation_state
					+"' '"+species+"' '"+weight+"' '"
					+(int)cations_per_oxide+"' '"+conversion_factor+" )\r\n"
			);	
		} catch (IOException ioe) { /*TODO*/ } 
		
		//Check if a third oxide species exists
		//Column 16 == 3nd Oxidation State, will be NON-empty for 2nd species
		current = row.getCell((short)16);
		if ( null == current )
			return;
		
		for ( short i = NUM_COLUMNS2+1; i < NUM_COLUMNS3; i++ ) {
			current = row.getCell(i);
			switch(i) {
			case 16: //2nd oxidation state
				oxidation_state = current.getNumericCellValue();
				break;
				
			case 17: //2nd species
				species = current.getRichStringCellValue().toString();
				break;
				
			case 18: //2nd oxide weight
				weight = current.getNumericCellValue();
				break;
				
			case 19: //2nd cations per oxide
				cations_per_oxide = current.getNumericCellValue();
				break;
				
			case 20: //2nd element to oxide conversion factor
				conversion_factor = current.getNumericCellValue();
				break;
				
			default:
				break;
			}
		}
		
		try { //write data for the 3nd species
			ostr.write( 
					 "INSERT INTO oxides ( element_id, oxidation_state, species,"
					+" weight, cations_per_oxide, conversion_factor )\r\n"
					+"VALUES ( '"+(int)element_id+"' '"+(int)oxidation_state
					+"' '"+species+"' '"+weight+"' '"
					+(int)cations_per_oxide+"' '"+conversion_factor+" )\r\n"
			);	
		} catch (IOException ioe) { /*TODO*/ } 
	}


	public static void main( String[] args ) {
		OxideParser op = new OxideParser( args[0], args[1] );	
	}
	
}


/** SQL oxides table for reference
CREATE TABLE oxides
(
   oxide_id INT2 NOT NULL,
   element_id INT2 NOT NULL,
   oxidation_state INT2,
   species  VARCHAR(20),
   weight FLOAT4,
   cations_per_oxide INT2,
   conversion_factor  FLOAT8 NOT NULL,
   mineral_type  VARCHAR(12),
   CONSTRAINT oxides_sk PRIMARY KEY (oxide_id),
   CONSTRAINT oxides_fk FOREIGN KEY (element_id)
        REFERENCES elements(element_id),
   CONSTRAINT oxides_nk UNIQUE (species),
   CONSTRAINT element_type_ck	
     CHECK (mineral_type IN ('Silicates','Oxides', 'Carbonates','Phosphates','Other'))
) WITHOUT OIDS ;
*/