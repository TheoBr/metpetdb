package edu.rpi.metpetdb.server.dao.permissions;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses( {
	AnonymousLoadingPermissionsTest.class,
	ContributorLoadingPermissionsTest.class
})
public class AllPermissionTests {

}
