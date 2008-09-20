package edu.rpi.metpetdb.client;

import com.google.gwt.junit.client.GWTTestCase;

import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.User;

public abstract class MpDbTestCase extends GWTTestCase {

	private User owner;
	private Sample sample;
	private Subsample subsample;

	public String getModuleName() {
		return "edu.rpi.metpetdb.MetPetDBApplication";
	}

	// public abstract String getTestName();

	/**
	 * Sets up the test by logining in a valid user
	 */
	public void gwtSetUp() {
		owner = new User();
		owner.setId(1);

		sample = new Sample();
		sample.setId(1);
		sample.setOwner(owner);

		subsample = new Subsample();
		subsample.setId(1);
		subsample.setSample(sample);
	}

	public User getUser() {
		return owner;
	}

	public Sample getSample() {
		return sample;
	}

	public Subsample getSubsample() {
		return subsample;
	}

	public void finish() {
		this.finishTest();
	}
}
