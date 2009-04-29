package edu.rpi.metpetdb.server.util;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class MineralParser {

	/** Row the data for the minerals start on */
	private static final int STARTING_ROW = 10;
	private static final int STARTING_COLUMN = 0;
	private static final String mineralSQL = "insert into minerals (mineral_id, real_mineral_id, name) "
			+ " VALUES (nextval('mineral_seq'), currval('mineral_seq'), '%NAME%');";
	private static final String relationshipSQL = "insert into mineral_relationships "
			+ "(parent_mineral_id, child_mineral_id) VALUES"
			+ "((select mineral_id from minerals where name='%PARENT%'),"
			+ "(select mineral_id from minerals where name='%CHILD%'));";
	/** Stores what minerals we put in already so we don't have duplicates */
	private final Collection<String> minerals;
	/** Number of empty rows we need to see before we call it quits */
	private static final int EMPTY_ROW_LIMIT = 20;
	private Integer numEmtpyRows;

	public MineralParser(final String spreadsheet, final String outputFile)
			throws FileNotFoundException, IOException {
		minerals = new HashSet<String>();
		final POIFSFileSystem fs = new POIFSFileSystem(MineralParser.class
				.getResourceAsStream(spreadsheet));
		final HSSFWorkbook wb = new HSSFWorkbook(fs);
		final HSSFSheet sheet = wb.getSheetAt(0);
		final FileWriter output = new FileWriter(outputFile);
		parse(output, sheet, STARTING_ROW, STARTING_COLUMN,
				new LinkedList<String>());
		output.close();
	}

	private void parse(final FileWriter output, final HSSFSheet sheet,
			final Integer rowIdx, final Integer columnIdx,
			final LinkedList<String> parents) throws IOException {
		final HSSFRow row = sheet.getRow(rowIdx);
		if (row != null) {
			numEmtpyRows = 0;
			final HSSFCell cell = row.getCell(columnIdx);
			if (cell != null) {
				final String mineralName = cell.toString();
				if (!minerals.contains(mineralName)) {
					output.write(mineralSQL.replace("%NAME%", mineralName));
					output.write("\n");
					minerals.add(mineralName);
				}
				if (parents.peek() != null) {
					// add a relationship
					output.write(relationshipSQL.replace("%PARENT%",
							parents.peek()).replace("%CHILD%", mineralName));
					output.write("\n");
				}
				// children
				parents.addFirst(mineralName);
				parse(output, sheet, rowIdx + 1, columnIdx + 1, parents);
				// siblings
				parents.poll();
				parse(output, sheet, rowIdx + 1, columnIdx, parents);
				parents.poll();
				// back to parent
				for (int idx = columnIdx - 1; idx >= 0; --idx)
					parse(output, sheet, rowIdx + 1, idx, parents);
			}
		} else {
			++numEmtpyRows;
			if (numEmtpyRows < EMPTY_ROW_LIMIT) {
				if (columnIdx == 0) {
					parents.poll();
					parse(output, sheet, rowIdx + 1, columnIdx, parents);
				}
			}
		}
	}

	public static void main(final String[] args) throws FileNotFoundException,
			IOException {
		new MineralParser(args[0], args[1]);
	}

}
