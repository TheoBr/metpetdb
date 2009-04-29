package edu.rpi.metpetdb.server.util;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * Class to parse "Elements and Oxides.xls" and convert data into relevant SQL
 * statements.
 * 
 * @author gramm
 * @debug fyffem
 * @debug gramm
 */

public class ElementParser {

	// members
	private String inFile;
	private String outFile;
	private int element_id, oxide_id; // primary keys

	// macros
	private final short ELEMENT = 5; // columns with info about element
	private final short NUM_COLUMNS = 11; // " "" 1st oxide species
	private final short NUM_COLUMNS2 = 16; // 2nd species
	private final short NUM_COLUMNS3 = 20; // 3rd species

	/**
	 * simple constructor
	 */
	public ElementParser(String in, String out) {
		inFile = in;
		outFile = out;
		element_id = 1;
		oxide_id = 0;
	}

	/**
	 * Use POI HSSF to open the file and sends each row to be parsed. This
	 * function almost entirely a copy from SampleParser.java
	 * 
	 * @param inFile
	 * 		path to the input file
	 * @param outFile
	 * 		path to the output file
	 */
	public void parse() {
		try { // attempt to access the spreadsheet
			final POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(
					inFile));
			final HSSFWorkbook wb = new HSSFWorkbook(fs);
			final HSSFSheet sheet = wb.getSheetAt(0);
			FileWriter ostr = new FileWriter(outFile, false);

			// Loop through the rows and parse
			for (int i = 5; i < sheet.getPhysicalNumberOfRows(); element_id++, ++i)
				parseRow(/* headerText, */sheet.getRow(i), ostr);

			ostr.close();
		} catch (IOException ioe) { /* TODO */
		}
	}

	/**
	 * Convert a row from the spreadsheet into SQL table insertions.
	 * 
	 * @param row
	 * 		the row from the spreadsheet being parsed
	 * @param ostr
	 * 		file stream to the output file
	 */
	private void parseRow(final HSSFRow row, FileWriter ostr) {

		double atomic_number = 0, oxidation_state = 0, weight = 0, cations_per_oxide = 0, conversion_factor = 0;
		String species = new String();
		String name = new String();
		String alternate_name = null;
		String symbol = new String();

		HSSFCell current = null;

		// parse data about the element
		for (short i = 0; i < ELEMENT; i++) {
			current = row.getCell(i);
			switch (i) {
			case 0: // name and alternate name
				String raw = current.getRichStringCellValue().toString();
				int j;
				for (j = 0; j < raw.length() && raw.charAt(j) != ' '; j++)
					name += raw.charAt(j);
				if (j < raw.length())
					alternate_name = new String();
				for (; j < raw.length() && raw.charAt(j) != ')'; j++)
					if (raw.charAt(j) != ' ' && raw.charAt(j) != '(')
						alternate_name += raw.charAt(j);
				break;
			case 2: // symbol
				symbol = current.getRichStringCellValue().toString();
				break;
			case 3: // atomic number
				atomic_number = current.getNumericCellValue();
			case 4: // element weight
				weight = current.getNumericCellValue();
			}
		}

		try { // write the element data
			if (null == alternate_name) {
				ostr.write("INSERT INTO elements ( element_id, name, "
						+ "symbol, atomic_number, weight ) " + "VALUES ("
						+ element_id + ", '" + name + "', '" + symbol + "', "
						+ atomic_number + ", " + weight + " );\r\n");
			} else {
				ostr.write("INSERT INTO elements ( element_id, name, "
						+ "alternate_name, symbol, atomic_number, weight ) "
						+ "VALUES (" + element_id + ", '" + name + "', '"
						+ alternate_name + "', '" + symbol + "', "
						+ atomic_number + ", " + weight + " );\r\n");
			}
		} catch (IOException ioe) {/* TODO */
		}

		// Column 5 == Oxidation State, will be NON-EMPTY for an oxide
		current = row.getCell(ELEMENT);
		if (null == current)
			return;

		for (short i = 0; i < NUM_COLUMNS; i++) {
			current = row.getCell(i);
			switch (i) {
			case 3: // atomic number
				atomic_number = current.getNumericCellValue();
				break;

			case 5: // 1st oxidation state
				oxidation_state = current.getNumericCellValue();
				break;

			case 6: // 1st species
				species = current.getRichStringCellValue().toString();
				break;

			case 7: // 1st oxide weight
				weight = current.getNumericCellValue();
				break;

			case 8: // 1st cations per oxide
				cations_per_oxide = current.getNumericCellValue();
				break;

			case 9: // 1st element to oxide conversion factor
				conversion_factor = current.getNumericCellValue();
				break;

			default:
				break;
			}
		}
		try { // write data for the 1st species
			ostr.write("INSERT INTO oxides ( element_id, oxide_id, "
					+ "oxidation_state, species, weight, cations_per_oxide,"
					+ "conversion_factor ) " + "VALUES ( " + element_id + ", "
					+ (++oxide_id) + ", " + (int) oxidation_state + ", '"
					+ species + "', " + weight + ", " + (int) cations_per_oxide
					+ ", " + conversion_factor + " );\r\n");
		} catch (IOException ioe) { /* TODO */
		}

		// Check if a second oxide species exists
		// Column 11 == 2nd Oxidation State, will be NON-empty for 2nd species
		current = row.getCell(NUM_COLUMNS);
		if (null == current)
			return;

		for (short i = NUM_COLUMNS; i < NUM_COLUMNS2; i++) {
			current = row.getCell(i);
			switch (i) {
			case 11: // 2nd oxidation state
				oxidation_state = current.getNumericCellValue();
				break;

			case 12: // 2nd species
				species = current.getRichStringCellValue().toString();
				break;

			case 13: // 2nd oxide weight
				weight = current.getNumericCellValue();
				break;

			case 14: // 2nd cations per oxide
				cations_per_oxide = current.getNumericCellValue();
				break;

			case 15: // 2nd element to oxide conversion factor
				conversion_factor = current.getNumericCellValue();
				break;

			default:
				break;
			}
		}

		try { // write data for the 2nd species
			ostr.write("INSERT INTO oxides ( element_id, oxide_id,"
					+ "oxidation_state, species, weight, cations_per_oxide,"
					+ "conversion_factor ) " + "VALUES ( " + element_id + ", "
					+ (++oxide_id) + ", " + (int) oxidation_state + ", '"
					+ species + "', " + weight + ", " + (int) cations_per_oxide
					+ ", " + conversion_factor + " );\r\n");
		} catch (IOException ioe) { /* TODO */
		}

		// Check if a third oxide species exists
		// Column 16 == 3nd Oxidation State, will be NON-empty for 3rd species
		current = row.getCell(NUM_COLUMNS2);
		if (null == current)
			return;

		for (short i = NUM_COLUMNS2; i < NUM_COLUMNS3; i++) {
			current = row.getCell(i);
			switch (i) {
			case 16: // 3rd oxidation state
				oxidation_state = current.getNumericCellValue();
				break;

			case 17: // 3rd species
				species = current.getRichStringCellValue().toString();
				break;

			case 18: // 3rd oxide weight
				weight = current.getNumericCellValue();
				break;

			case 19: // 3rd cations per oxide
				cations_per_oxide = current.getNumericCellValue();
				break;

			case 20: // 3rd element to oxide conversion factor
				conversion_factor = current.getNumericCellValue();
				break;

			default:
				break;
			}
		}

		try { // write data for the 3rd species
			ostr.write("INSERT INTO oxides ( element_id, oxide_id,"
					+ "oxidation_state, species, weight, cations_per_oxide,"
					+ "conversion_factor ) " + "VALUES ( " + element_id + ", "
					+ (++oxide_id) + ", " + (int) oxidation_state + ", '"
					+ species + "', " + weight + ", " + (int) cations_per_oxide
					+ ", " + conversion_factor + " );\r\n");
		} catch (IOException ioe) { /* TODO */
		}
	}

	public static void main(String[] args) {
		ElementParser ep = new ElementParser(args[0], args[1]);
		ep.parse();
	}

}
