package edu.rpi.metpetdb.server;

import java.io.FileInputStream;

import junit.framework.TestCase;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;

public class DatabaseTestCase extends TestCase {

	private final String xmlFile;
	
	private IDataSet loadedDataSet;

	public DatabaseTestCase(final String xmlFile) {
		this.xmlFile = xmlFile;
	}

	@Override
	public void setUp() {
		try {
			loadedDataSet = new FlatXmlDataSet(
					new FileInputStream(xmlFile));
			//Insert test data
			DatabaseOperation.INSERT.execute(InitDatabase.getConnection(), loadedDataSet);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void tearDown() {
		try {
			//Delete test data
			DatabaseOperation.DELETE_ALL.execute(InitDatabase.getConnection(),loadedDataSet );
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

}
