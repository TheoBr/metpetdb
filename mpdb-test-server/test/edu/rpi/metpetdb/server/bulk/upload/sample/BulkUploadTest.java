package edu.rpi.metpetdb.server.bulk.upload.sample;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import edu.rpi.metpetdb.server.DatabaseTestCase;

public class BulkUploadTest extends DatabaseTestCase {

	private SampleParser sp;

	public BulkUploadTest() {
		super("test-data/test-sample-data.xml");

	}

	/**
	 * load up the valhalla data file. This will only fail if the file is in the
	 * wrong place
	 */
	@Test
	public void load_file() {

		try {
			sp = new SampleParser(new FileInputStream(
					"../mpdb-common/sample-data/Valhalla_samples_upload.xls"));
		} catch (FileNotFoundException fnfe) {
			fail("File Not Found");
		}

	}

	/**
	 * the "bulk" of the bulk upload process.
	 */
	@Test
	public void parse_file() {
		try {
			sp.initialize();
		} catch (IOException ioe) {
			fail("IO Exception");
		}
	}

	@Test
	public void parser_count() {
		assertEquals(sp.getSamples().size(), 29);
		// should be 29 samples in the spreadsheet.
	}

	@Test
	public void saved_count() {
		fail("Not written yet.");
		// 29 + the five from the sample data
	}
}
