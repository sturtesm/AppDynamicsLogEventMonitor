package com.appdynamics.monitor.log;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class InvalidStatusListener extends SonicLogListener {

	private final static String VALIDATION_ERROR_ATTR = "action=ValidationLogging";
	
	/** regex to extract the validation error from the log files */
	private final static String LOG_VALIDATE_REGEX_EXTRACT = "splunk\\[(.*)\\]";
	
	/** regex to extract the invoice number from the log files */
	private final static String LOG_INVOICE_EXTRACT = "invoiceNumber=(.*?),\\s*";
			
	/** validation log pattern */
	private Pattern validationLogSubstrPattern = null;
	private Pattern invoiceNumberPattern = null;
	
	private Logger logger = Logger.getLogger(InvalidStatusListener.class);

	private String invoiceNumber = null;
	
	public InvalidStatusListener() {
		super();
		
		validationLogSubstrPattern = Pattern.compile(LOG_VALIDATE_REGEX_EXTRACT);
		
		invoiceNumberPattern = Pattern.compile(LOG_INVOICE_EXTRACT);
	}

	protected Map<String, String> parseLogEventDetails(String line) {
		Map<String, String> details = new HashMap<String, String> ();
		String validationErrorDetails = null;
		
		try {
			Matcher m = validationLogSubstrPattern.matcher(line);
			
			/** for now, just extract a single string to generate the event, instead of a key/value pair list */
			if (!m.find() || m == null || m.group(1) == null) {
				logger.error("Failed to parse log validation substring from " + line);
				return null;
			}
			else {
				
				validationErrorDetails = m.group(1);
				
				details.put("Validation Error", validationErrorDetails);
				
				logger.debug("Parsed " + validationErrorDetails + " from " + line);
			}
			
			/** now try to parse the invoice id */
			Matcher m1 = invoiceNumberPattern.matcher(line);
			
			if (m1.find()) {
				invoiceNumber = m1.group(1);
				
				logger.debug("Parsed invoice number " + invoiceNumber + " from log line");
			}
			else {
				logger.info("Failed to parse invoice number from log line " + line);
				invoiceNumber = null;
			}
			
			return details;
			
			/**
			String subStr = m.group(1);
			
			String[] keyValProps = subStr.split(",");
			
			if (keyValProps == null || keyValProps.length <= 0) {
				logger.error("Failed to parse key val props from validation substring: " + subStr);
				
				return null;
			}
			
			for (int i = 0; i < keyValProps.length; i++) {
				String kv = keyValProps[i];
				kv = kv.trim();
				
				String[] keyVal = kv.split("=");
				
				if (keyVal == null || keyVal.length != 2) {
					logger.error("Failed to parse validation key=value property from: " + kv);
				}
				else {
					logger.debug("Parsed key=val pair: " + keyVal);
					
					pairs.add(new ImmutablePair<String, String> (keyVal[0], keyVal[1]));
				}
			}
			*/
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	protected boolean shouldProcessLogLine(String line) {
		return line.contains(VALIDATION_ERROR_ATTR);
	}

	@Override
	protected String getEventSummary() {
		
		if (invoiceNumber == null) {
			return "Invoice Validation Error";
		}
		else {
			return "Invoice Validation Error (Inv #" + invoiceNumber + ")";
		}
	}
}
