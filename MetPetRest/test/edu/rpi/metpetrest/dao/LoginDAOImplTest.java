package edu.rpi.metpetrest.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@ContextConfiguration(locations="../../../../test-services-servlet.xml")
public class LoginDAOImplTest extends
AbstractTransactionalJUnit4SpringContextTests  {

	@Test
	public void testLogin() {
		
		LoginDAOImpl loginDAO = (LoginDAOImpl)applicationContext.getBean("loginDAO");
		
		//String sessionId = loginDAO.login("halleb3@rpi.edu", "metpetdb");
		
	//	assertNotNull(sessionId);
	}

}
