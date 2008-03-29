package edu.rpi.metpetdb.client;

import com.google.gwt.junit.client.GWTTestCase;

import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.model.UserDTO;

public abstract class MpDbTestCase extends GWTTestCase {

	private UserDTO owner;
	private SampleDTO sample;

	public String getModuleName() {
		return "edu.rpi.metpetdb.MetPetDBApplication";
	}

	// public abstract String getTestName();

	/**
	 * Sets up the test by logining in a valid user
	 */
	public void setUp() {
		owner = new UserDTO();
		owner.setId(1);

		sample = new SampleDTO();
		sample.setId(1);
		sample.setOwner(owner);
	}

	public UserDTO getUser() {
		return owner;
	}

	public SampleDTO getSample() {
		return sample;
	}

	public void finish() {
		this.finishTest();
	}
}
