package edu.rpi.metpetdb.client.ui.subsample;

import java.util.Iterator;
import java.util.List;

import edu.rpi.metpetdb.client.MpDbTestCase;
import edu.rpi.metpetdb.client.TestServerOp;
import edu.rpi.metpetdb.client.VoidTestServerOp;
import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.SubsampleType;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.MpDb;

public class SubsampleDetailsTest extends MpDbTestCase {

	private static final long SUBSAMPLE_ID = 2;
	private static final String NAME = "loller skater";
	private static SubsampleType TYPE;

	@Override
	public void gwtSetUp() {
		super.gwtSetUp();
		TYPE = new SubsampleType();
		TYPE.setSubsampleType("linux");

	}

	/**
	 * Test saving a sample to the database, which in turns test that
	 * authentication works and the object constraints
	 */
	public void testSaveSubsample() {
		final Subsample s = new Subsample();
		s.setSample(getSample());
		s.setName(NAME);
		s.setSubsampleType(TYPE);
		new TestServerOp<Subsample>(this) {
			public void begin() {
				MpDb.subsample_svc.save(s, this);
			}

			public void onSuccess(final Subsample subsample) {
				assertEquals(subsample.getName(), NAME);
				assertEquals(subsample.getSubsampleType(), TYPE);
				finishTest();
			}
		}.begin();
		delayTestFinish(10000);
	}

	/**
	 * Test the name constraint of a subsample
	 */
	public void testSaveSubsampleFailName() {
		final Subsample s = new Subsample();
		s.setSample(getSample());
		s.setSubsampleType(TYPE);
		new TestServerOp<Subsample>(this) {
			public void begin() {
				MpDb.subsample_svc.save(s, this);
			}

			public void onSuccess(final Subsample subsample) {
				fail("failed verifying constraints");
				finishTest();
			}

			public void onFailure(final Throwable e) {
				if (e instanceof ValidationException) {
					finishTest();
				} else {
					fail("failed with wrong exception");
				}
			}
		}.begin();
		delayTestFinish(5000);
	}

	/**
	 * Test loading a sample by verifying the id's are the same
	 */
	public void testLoadSubsample() {
		new TestServerOp<Subsample>(this) {
			public void begin() {
				MpDb.subsample_svc.details(SUBSAMPLE_ID, this);
			}

			public void onSuccess(final Subsample subsample) {
				assertEquals(SUBSAMPLE_ID, subsample.getId());
				finishTest();
			}
		}.begin();
		delayTestFinish(5000);
	}

	/**
	 * Test if a sample can be deleted
	 */
	public void testDeleteSubsample() {
		new VoidTestServerOp(this) {
			public void begin() {
				MpDb.subsample_svc.delete(SUBSAMPLE_ID, this);
			}
		}.begin();
		delayTestFinish(5000);
	}

	/**
	 * Test loading all samples and verfies the correct amount, and the correct
	 * order is returned
	 */
	public void testAllSubsamples() {
		final PaginationParameters p = new PaginationParameters();
		p.setAscending(true);
		p.setMaxResults(4);
		p.setFirstResult(0);
		p.setParameter("name");

		new TestServerOp<Results<Subsample>>(this) {
			public void begin() {
				MpDb.subsample_svc.all(p, getSample().getId(), this);
			}

			public void onSuccess(final Results<Subsample> results) {
				final List<Subsample> l = results.getList();
				final String[] names = {
						"1", "3", "4", "5"
				};
				// verify the size
				assertEquals(5, results.getCount());
				// Verify the order, also verifies we got the right ones
				final Iterator<Subsample> itr = l.iterator();
				// Start with 2 beause 1 was deleted
				int count = 0;
				while (itr.hasNext()) {
					final Subsample s = itr.next();
					assertEquals(names[count], s.getName());
					++count;
				}
				finishTest();
			}
		}.begin();
		delayTestFinish(5000);
	}

	public String getTestName() {
		return "SubsampleDetailsTest";
	}

}
