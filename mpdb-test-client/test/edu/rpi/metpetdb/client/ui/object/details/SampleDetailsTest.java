package edu.rpi.metpetdb.client.ui.object.details;

import java.util.Iterator;
import java.util.List;

import edu.rpi.metpetdb.client.model.UserDTO;
import edu.rpi.metpetdb.client.model.SampleDTO;
import org.gwtwidgets.client.ui.pagination.PaginationParameters;
import org.gwtwidgets.client.ui.pagination.Results;

import com.google.gwt.junit.client.GWTTestCase;

import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.ServerOp;

//TODO put in checks to make sure the exact amount comes back
public class SampleDetailsTest extends GWTTestCase {

	private UserDTO owner;

	public String getModuleName() {
		return "edu.rpi.metpetdb.MetPetDBApplication";
	}

	/**
	 * Sets up the test by logining in a valid user
	 */
	public void setUp() {
		owner = new UserDTO();
		owner.setId(1);
	}

	/**
	 * Test saving a sample to the database, which in turns test that
	 * authentication works and the object constraints
	 */
	public void testSaveSample() {
		final SampleDTO s = new SampleDTO();
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
		delayTestFinish(10000);
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
				final SampleDTO s = (SampleDTO) result;
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

	/**
	 * Test loading all samples and verfies the correct amount, and the correct
	 * order is returned
	 */
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
				// verify the size
				assertTrue(results.getSize() <= 10);
				// Verify the order, also verifies we got the right ones
				final Iterator itr = l.iterator();
				// Start with 2 beause 1 was deleted
				int count = 2;
				while (itr.hasNext()) {
					final SampleDTO s = (SampleDTO) itr.next();
					System.out.println(s.toString());
					assertEquals(s.getAlias(), String.valueOf(count));
					++count;
				}
				finishTest();
			}
		}.begin();
		delayTestFinish(5000);
	}

	/**
	 * Basically the same as testAllSamples except it verifies each sample is
	 * public
	 */
	public void testAllPublicSamples() {
		new ServerOp() {
			public void begin() {
				final PaginationParameters p = new PaginationParameters();
				p.setAscending(true);
				p.setMaxResults(10);
				p.setOffset(0);
				p.setParameter("alias");
				MpDb.sample_svc.allPublicSamples(p, this);
			}

			public void onSuccess(final Object result) {
				final Results results = (Results) result;
				final List l = results.getList();
				// verify they are all public
				final Iterator itr = l.iterator();
				while (itr.hasNext()) {
					final SampleDTO s = (SampleDTO) itr.next();
					System.out.println(s.toString());
					assertTrue(s.isPublicData());
				}
				finishTest();
			}
		}.begin();
		delayTestFinish(5000);
	}

	/**
	 * Basically the same as testAllSamples except it verifies that the user
	 * owns the samples
	 */
	public void testAllSamplesForUser() {
		new ServerOp() {
			public void begin() {
				final PaginationParameters p = new PaginationParameters();
				p.setAscending(true);
				p.setMaxResults(10);
				p.setOffset(0);
				p.setParameter("alias");
				MpDb.sample_svc.allSamplesForUser(p, owner.getId(), this);
			}

			public void onSuccess(final Object result) {
				final Results results = (Results) result;
				final List l = results.getList();
				// verify they are all public
				final Iterator itr = l.iterator();
				while (itr.hasNext()) {
					final SampleDTO s = (SampleDTO) itr.next();
					System.out.println(s.toString());
					assertEquals(s.getOwner(), owner);
				}
				finishTest();
			}
		}.begin();
		delayTestFinish(5000);
	}

}
