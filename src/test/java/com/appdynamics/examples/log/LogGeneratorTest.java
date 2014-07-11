package com.appdynamics.examples.log;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

public class LogGeneratorTest {

	Logger logger = Logger.getLogger(LogGeneratorTest.class);
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void test() {
		
		RandomString randomString = new RandomString(10);
		
		for (int i = 0; i < 100; i++) {
			
			String randomInvoiceNumber = randomString.nextString();
			
			logger.info("action=ValidationLogging, invoiceId=null, invoiceNumber=" + randomInvoiceNumber + ", storeNumber=3366, "
						+ "dciName=\"BEK-Amarillo\" " 
						+ "msg=\"Encountered validation error or warning for invoice; error was FATAL : "
						+ "Invoice number must be unique per Distribution Center during past 65 days.\"");
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
