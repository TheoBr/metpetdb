package edu.rpi.metpetdb.client;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.UserDTO;
import edu.rpi.metpetdb.client.service.BeanManipulation;

public abstract class MpDbTestCase extends GWTTestCase {

	private UserDTO owner;

	public String getModuleName() {
		return "edu.rpi.metpetdb.MetPetDBApplication";
	}

	public abstract String getTestName();

	/**
	 * Sets up the test by logining in a valid user
	 */
	public void setUp() {
		owner = new UserDTO();
		owner.setId(1);
	}

	public UserDTO getUser() {
		return owner;
	}

	public void save(final BeanManipulation svc, final MObjectDTO bean,
			final AsyncCallback ac) {
		svc.save(bean, ac);
	}

	public void finish() {
		this.finishTest();
	}

	/**
	 * Loads the default Sample and Subsample that are used for parent object
	 */
	public void testLoadDefaults() {

	}

}
