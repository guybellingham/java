package com.gus.pattern.serviceprovider;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class UserRegistrationTest {
	
	private static final Logger logger = LogManager.getLogger(UserRegistrationTest.class);
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		org.apache.log4j.BasicConfigurator.configure();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testUserConstructor() {
		AbstractUser user1 = new WebUser("user1","passw0rd", "Juan","User","1966-06-23","user1@w.com");
		assertNotNull("FAILED to construct user1", user1);
		
		UserRegistration userRegistration = UserRegistrationService.getInstance().getUserRegistration(user1);
		boolean rc = userRegistration.isUserExists();
		
		try {
			userRegistration.registerNewUser();
		} catch (UserIdExistsException e) {
			logger.error(e);
			fail("User "+user1+" already exists?");
		}
		
		rc = userRegistration.isUserExists();
		
		userRegistration.deleteUser();
	}

	@Ignore
	public void testWriteExternal() {
		fail("Not yet implemented");
	}

	@Ignore
	public void testReadExternal() {
		fail("Not yet implemented");
	}

}
