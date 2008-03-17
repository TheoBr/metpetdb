package edu.rpi.metpetdb.client.ui.object.details;

import java.util.Iterator;
import java.util.List;

import org.gwtwidgets.client.ui.pagination.PaginationParameters;
import org.gwtwidgets.client.ui.pagination.Results;

import edu.rpi.metpetdb.client.MpDbTestCase;
import edu.rpi.metpetdb.client.TestServerOp;
import edu.rpi.metpetdb.client.VoidTestServerOp;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.model.SubsampleDTO;
import edu.rpi.metpetdb.client.ui.MpDb;

public class SubsampleDetailsTest extends MpDbTestCase {
	
	private SampleDTO sample;
	
	public void setUp() {
		super.setUp();
		sample = new SampleDTO();
		
	}
	/**
	 * Test saving a sample to the database, which in turns test that
	 * authentication works and the object constraints
	 */
	public void testSaveSubsample() {
		final SubsampleDTO s = new SubsampleDTO();
		
		new TestServerOp<SampleDTO>(this) {
			public void begin() {
				//MpDb.sample_svc.save(s, this);
			}

			public void onSuccess(final SampleDTO sample) {
				assertEquals(sample, s);
				finishTest();
			}
		}.begin();
		delayTestFinish(10000);
	}

	/**
	 * Test loading a sample by verifying the id's are the same
	 */
	public void testLoadSample() {
		new TestServerOp<SampleDTO>(this) {
			public void begin() {
				MpDb.sample_svc.details(1, this);
			}

			public void onSuccess(final SampleDTO sample) {
				assertEquals(1, sample.getId());
				finishTest();
			}
		}.begin();
		delayTestFinish(5000);
	}

	/**
	 * Test if a sample can be deleted
	 */
	public void testDeleteSample() {
		new VoidTestServerOp(this) {
			public void begin() {
				MpDb.sample_svc.delete(1, this);
			}
		}.begin();
		delayTestFinish(5000);
	}

	/**
	 * Test loading all samples and verfies the correct amount, and the correct
	 * order is returned
	 */
	public void testAllSamples() {
		final PaginationParameters p = new PaginationParameters();
		p.setAscending(true);
		p.setMaxResults(10);
		p.setOffset(0);
		p.setParameter("alias");

		new TestServerOp<Results>(this) {
			public void begin() {
				MpDb.sample_svc.all(p, this);
			}

			public void onSuccess(final Results result) {
				final Results results = (Results) result;
				final List l = results.getList();
				// verify the size
				assertEquals(5, results.getSize());
				// Verify the order, also verifies we got the right ones
				final Iterator itr = l.iterator();
				// Start with 2 beause 1 was deleted
				int count = 2;
				while (itr.hasNext()) {
					final SampleDTO s = (SampleDTO) itr.next();
					assertEquals(count, Integer.parseInt(s.getAlias()));
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
		final PaginationParameters p = new PaginationParameters();
		p.setAscending(true);
		p.setMaxResults(10);
		p.setOffset(0);
		p.setParameter("alias");
		new TestServerOp<Results>(this) {
			public void begin() {
				MpDb.sample_svc.allPublicSamples(p, this);
			}

			public void onSuccess(final Results result) {
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
		final PaginationParameters p = new PaginationParameters();
		p.setAscending(true);
		p.setMaxResults(10);
		p.setOffset(0);
		p.setParameter("alias");
		new TestServerOp<Results>(this) {
			public void begin() {
				MpDb.sample_svc.allSamplesForUser(p, getUser().getId(), this);
			}

			public void onSuccess(final Results result) {
				final Results results = (Results) result;
				final List l = results.getList();
				// verify they are all public
				final Iterator itr = l.iterator();
				while (itr.hasNext()) {
					final SampleDTO s = (SampleDTO) itr.next();
					System.out.println(s.toString());
					assertEquals(s.getOwner(), getUser());
				}
				finishTest();
			}
		}.begin();
		delayTestFinish(5000);
	}

	public String getTestName() {
		return "SampleDetailsTest";
	}

}
