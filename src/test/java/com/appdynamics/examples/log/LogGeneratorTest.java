package com.appdynamics.examples.log;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

public class LogGeneratorTest {

	Logger logger = Logger.getLogger(LogGeneratorTest.class);


	public LogGeneratorTest() {
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	public String getValidationError() {
		RandomString randomString = new RandomString(10);

		String randomInvoiceNumber = randomString.nextString();

		return new String("action=ValidationLogging, invoiceId=null, invoiceNumber=" + randomInvoiceNumber + ", storeNumber=3366, "
				+ "dciName=\"BEK-Amarillo\" " 
				+ "msg=\"Encountered validation error or warning for invoice; error was FATAL : "
				+ "Invoice number must be unique per Distribution Center during past 65 days.\"");
	}
	
	

	@Test
	public void testInvalidStatusError() {

		for (int i = 0; i < 100; i++) {

			String s = getInvalidStatusError();

			logger.info(s);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	private String getInvalidStatusError() {
		//String s = 
		return null;
	}

	@Test
	public void testValidationError() {

		for (int i = 0; i < 100; i++) {

			String s = getValidationError();

			logger.info(s);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
