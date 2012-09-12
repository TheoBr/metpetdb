import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractTransactionalJUnit38SpringContextTests;

import junit.framework.TestCase;
import edu.rpi.metpetdb.server.dao.UserDAO;
import edu.rpi.metpetdb.server.model.User;
import edu.rpi.metpetdb.server.security.Util;
import edu.rpi.metpetdb.server.security.PasswordEncrypter;

@ContextConfiguration
public class EncryptTest extends AbstractTransactionalJUnit38SpringContextTests {

	
	public void testFromHex()
	{
		byte [] encrypted_password = PasswordEncrypter.crypt("ejphc4pz");
		byte [] encrypted_password_2 = PasswordEncrypter.crypt("zxmfuvt2");
		
	//	assertEquals(Util.toHex(encrypted_password), "");
		assertEquals(Util.toHex(encrypted_password_2), "");
		
		assertTrue(PasswordEncrypter.verify(encrypted_password, ""));
		
		
	}
	
	public void testUserAuthentication()
	{
		UserDAO userDAO = (UserDAO)applicationContext.getBean("userDAO");
		
		//ByteArrayOutputStream baos = new ByteArrayOutputStream();
		//baos.
		//\004\363\235\014g_\206\035}\377\2333\346\026\322\255`\240Av\371\301 \231\324
		//
		
		
		User authenticatedUser = userDAO.authenticateUser("Stephen Ball", new byte[] {});
		
		byte [] passwd = authenticatedUser.getEncryptedPassword();
		byte [] encrypted_password = PasswordEncrypter.crypt("ejphc4pz");
		byte [] encrypted_password_2 = PasswordEncrypter.crypt("zxmfuvt2");
		
		System.out.println("passwd: " + Util.toHex(passwd));
		//assertTrue(PasswordEncrypter.verify(passwd, "seahorse"));
		//assertTrue(PasswordEncrypter.verify(passwd, "s3ahors3"));
		assertTrue(PasswordEncrypter.verify(passwd, "sw33tp3a"));
		
		String passwdHex = Util.toHex(encrypted_password_2);
		
		
		assertTrue(PasswordEncrypter.verify(encrypted_password_2, "zxmfuvt2"));
		assertEquals(passwdHex, "");
		
		
	//	assertEquals(Util.toHex(encrypted_password), "");
		assertEquals(Util.toHex(encrypted_password_2), "");
	}
}
