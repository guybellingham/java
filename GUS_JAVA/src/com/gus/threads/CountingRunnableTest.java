package com.gus.threads;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CountingRunnableTest {

	private static final CountingRunnable counter = new CountingRunnable(10);
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testRun() {
		Thread t1 = new Thread(counter);
		System.out.println("Created thread name="+t1.getName()+" priority="+t1.getPriority());
		
	}

}
