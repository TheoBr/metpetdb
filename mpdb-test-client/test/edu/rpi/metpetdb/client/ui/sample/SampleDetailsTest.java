package edu.rpi.metpetdb.client.ui.sample;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.rpi.metpetdb.client.MpDbTestCase;
import edu.rpi.metpetdb.client.TestServerOp;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.SampleAlias;
import edu.rpi.metpetdb.client.paging.PaginationParameters;
import edu.rpi.metpetdb.client.paging.Results;
import edu.rpi.metpetdb.client.ui.MpDb;

public class SampleDetailsTest extends MpDbTestCase {

	/**
	 * Test loading all samples and verfies the correct amount, and the correct
	 * order is returned
	 */
	public void testAllSamples() {
		final PaginationParameters p = new PaginationParameters();
		p.setAscending(true);
		p.setMaxResults(10);
		p.setFirstResult(0);
		p.setParameter("alias");

		new TestServerOp<Results<Sample>>(this) {
			public void begin() {
				MpDb.sample_svc.all(p, this);
			}

			public void onSuccess(final Results<Sample> results) {
				final List<Sample> l = results.getList();
				SampleAlias alias[] = {
						new SampleAlias("1"), new SampleAlias("3"),
						new SampleAlias("4"), new SampleAlias("5"),
						new SampleAlias("6"),
				};
				final Set<SampleAlias> aliases = new HashSet<SampleAlias>(
						Arrays.asList(alias));
				// verify the size
				assertEquals(5, results.getCount());
				// Verify the order, also verifies we got the right ones
				for (int i = 0; i < l.size(); ++i) {
					final Sample s = l.get(i);
					assertEquals(aliases, s.getAliases());
					/*
					 * if (i + 1 < l.size()) {
					 * assertTrue(s.getAliases().compareTo( l.get(i +
					 * 1).getAliases()) < 0); }
					 */
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
		p.setFirstResult(0);
		p.setParameter("alias");
		new TestServerOp<Results<Sample>>(this) {
			public void begin() {
				MpDb.sample_svc.allPublicSamples(p, this);
			}

			public void onSuccess(final Results<Sample> results) {
				final List<Sample> l = results.getList();
				// verify they are all public
				final Iterator<Sample> itr = l.iterator();
				while (itr.hasNext()) {
					final Sample s = (Sample) itr.next();
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
		p.setFirstResult(0);
		p.setParameter("alias");
		new TestServerOp<Results<Sample>>(this) {
			public void begin() {
				MpDb.sample_svc.allSamplesForUser(p, getUser().getId(), this);
			}

			public void onSuccess(final Results<Sample> results) {
				final List<Sample> l = results.getList();
				// verify they are all public
				final Iterator<Sample> itr = l.iterator();
				while (itr.hasNext()) {
					final Sample s = (Sample) itr.next();
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
