package edu.rpi.metpetdb.server.test;

import org.junit.After;
import org.junit.Before;

import junit.framework.TestCase;

/**
 * The testcase that all database test should extend, it is responsible for
 * creating the initial hibernate conenction
 * 
 * @author anthony
 * 
 */
public class BaseDatabaseTest extends TestCase {
	
	@Before
	public void setUp() throws Exception {
	}
	
	@After
	public void tearDown() throws Exception {
	}

}
