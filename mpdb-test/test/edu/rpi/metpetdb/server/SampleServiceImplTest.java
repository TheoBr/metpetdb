package edu.rpi.metpetdb.server;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;

import edu.rpi.metpetdb.client.error.LoginRequiredException;
import edu.rpi.metpetdb.client.error.SampleAlreadyExistsException;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.server.SampleServiceImpl;

public class SampleServiceImplTest extends TestCase {

	@Before
	public void setUp() throws Exception {
	}

	public void testCreate() throws SampleAlreadyExistsException,
			ValidationException, LoginRequiredException {
		final Sample s = new Sample();
		final SampleServiceImpl sample_svc = new SampleServiceImpl();
		sample_svc.saveSample(s);
	}
	
	

	@After
	public void tearDown() throws Exception {
	}

}
