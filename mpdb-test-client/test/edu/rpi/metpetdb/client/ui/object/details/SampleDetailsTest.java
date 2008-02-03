package edu.rpi.metpetdb.client.ui.object.details;

import java.util.Iterator;
import java.util.List;

import org.gwtwidgets.client.ui.pagination.PaginationParameters;
import org.gwtwidgets.client.ui.pagination.Results;

import com.google.gwt.junit.client.GWTTestCase;

import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;

public class SampleDetailsTest extends GWTTestCase {

	private User owner;

	public String getModuleName() {
		return "edu.rpi.metpetdb.MetPetDBApplication";
	}

	/**
	 * Sets up the test by logining in a valid user
	 */
	public void setUp() {
		owner = new User();
		owner.setId(1);
	}

	public void testSaveSample() {
		final Sample s = new Sample();
		s.setSesarNumber("000000006");
		s.setLatitude(1);
		s.setLongitude(1);
		s.setRockType("rockie rock");
		s.setOwner(owner);
		s.setAlias("6");
		new ServerOp() {
			public void begin() {
				MpDb.sample_svc.saveSample(s, this);
			}

			public void onSuccess(final Object result) {
				assertEquals(result, s);
				System.out.println(result.toString());
				finishTest();
			}
			public void onFailure(final Throwable e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		}.begin();
		delayTestFinish(5000);
	}

	/**
	 * Test loading a sample by verifying the id's are the same
	 */
	public void testLoadSample() {
		new ServerOp() {
			public void begin() {
				MpDb.sample_svc.details(1, this);
			}

			public void onSuccess(final Object result) {
				final Sample s = (Sample) result;
				assertTrue(s.getId() == 1);
				System.out.println(s.toString());
				finishTest();
			}
			
		}.begin();
		delayTestFinish(5000);
	}

	/**
	 * Test if a sample can be deleted
	 */
	public void testDeleteSample() {
		new ServerOp() {
			public void begin() {
				MpDb.sample_svc.delete(1, this);
			}

			public void onSuccess(final Object result) {
				finishTest();
			}
		}.begin();
		delayTestFinish(5000);
	}
	
	public void testAllSamples() {
		new ServerOp() {
			public void begin() {
				final PaginationParameters p = new PaginationParameters();
				p.setAscending(true);
				p.setMaxResults(10);
				p.setOffset(0);
				p.setParameter("alias");
				MpDb.sample_svc.all(p, this);
			}
			public void onSuccess(final Object result) {
				final Results results = (Results) result;
				final List l = results.getList();
				//verify the size
				assertTrue(results.getSize() <= 10);
				//Verify the order, also verifies we got the right ones
				final Iterator itr = l.iterator();
				//Start with 2 beause 1 was deleted
				int count = 2;
				while(itr.hasNext()) {
					final Sample s = (Sample) itr.next();
					System.out.println(s.toString());
					assertEquals(s.getAlias(), String.valueOf(count));
					++count;
				}
				finishTest();
			}
		}.begin();
		delayTestFinish(5000);
	}

}
