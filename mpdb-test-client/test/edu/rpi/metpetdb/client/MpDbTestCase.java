package edu.rpi.metpetdb.client;

import com.google.gwt.junit.client.GWTTestCase;

import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.model.SubsampleDTO;
import edu.rpi.metpetdb.client.model.UserDTO;

public abstract class MpDbTestCase extends GWTTestCase {

	private UserDTO owner;
	private SampleDTO sample;
	private SubsampleDTO subsample;

	public String getModuleName() {
		return "edu.rpi.metpetdb.MetPetDBApplication";
	}

	// public abstract String getTestName();

	/**
	 * Sets up the test by logining in a valid user
	 */
	public void gwtSetUp() {
		owner = new UserDTO();
		owner.setId(1);

		sample = new SampleDTO();
		sample.setId(1);
		sample.setOwner(owner);

		subsample = new SubsampleDTO();
		subsample.setId(1);
		subsample.setSample(sample);
	}

	public UserDTO getUser() {
		return owner;
	}

	public SampleDTO getSample() {
		return sample;
	}

	public SubsampleDTO getSubsample() {
		return subsample;
	}

	public void finish() {
		this.finishTest();
	}
}
