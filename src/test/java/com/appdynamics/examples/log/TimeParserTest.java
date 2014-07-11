package com.appdynamics.examples.log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class TimeParserTest {

	@Test
	public void test() {

		/** [07-09 12:18:52] */
		Pattern p = Pattern.compile("\\[(\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})\\]");

		Matcher m = p.matcher("[07-09 12:18:52] WARN  SupplyChainServiceImpl");

		if (m.find()) {
			System.out.println(m.group(1));
		}
		else {
			System.err.println("Failed to parse timestamp from pattern");
		}
	}

	@Test
	public void simpleDateFormatTest() {
		SimpleDateFormat sdfParser = new SimpleDateFormat("MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);

		try {
			Date ts = sdfParser.parse("07-11 12:47:52");

			cal.setTime(ts);
			cal.set(Calendar.YEAR, year);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}

		System.out.println(cal.getTime());
	}
	
	@Test
	public void validationLogParseTest() {
		String log = "splunk[action=ValidationLogging, invoiceId=null, invoiceNumber=41654062A540189, storeNumber=3366, "
				+ "dciName=\"BEK-Amarillo\" msg=\"Encountered validation error or warning for invoice; error was FATAL : "
				+ "Invoice number must be unique per Distribution Center during past 65 days.]" ;
		
		Pattern validationLogSubstrPattern = Pattern.compile("splunk\\[(.*)\\]");
		
		Matcher m = validationLogSubstrPattern.matcher(log);
		
		if (m.find()) {
			System.out.println(m.group(1));
		}
		
		assert(m.find());
	}
}
