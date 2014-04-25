package com.cagst.swkroa.integration;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cagst.swkroa.utils.UserUtilities;

/**
 * Test class for UserUtilities class.
 * 
 * @author Craig Gaskill
 * 
 * @version 1.0.0
 * 
 */
@RunWith(JUnit4.class)
public class UserUtilitiesTest {
	public UserUtilities utils;

	@Before
	public void setUp() {
		ApplicationContext appCtx = new ClassPathXmlApplicationContext("classpath*:/test-appCtx/**/*.xml");

		utils = (UserUtilities) appCtx.getBean("userUtils");
	}

	/**
	 * Test the createUser method by actually inserting the user into the database.
	 */
	@Test
	@Ignore
	public void testCreateUser() {
		utils.createUser("dyunker", "dyunker", "Darcy", "Yunker");
	}
}
