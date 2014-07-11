package com.appdynamics.monitor.log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.input.TailerListenerAdapter;
import org.apache.log4j.Logger;

import com.appdynamics.apm.appagent.api.AgentDelegate;
import com.appdynamics.apm.appagent.api.IMetricAndEventReporter;

public abstract class SonicLogListener extends TailerListenerAdapter{

	/** format to parse - 07-09 12:18:52 */
	private final static String LOG_TIME_FORMAT = "MM-dd HH:mm:ss";

	/** regex to extract the timestamp from the log files */
	private final static String LOG_TIME_REGEX_EXTRACT = "\\[(\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})\\]";

	/** the pattern we'll use to parse the event time from the log line */
	private Pattern eventTimePattern = null;

	/** the simple date pattern to convert the time to a date object */
	SimpleDateFormat simpleDateFormat = null;

	/** current calendar year */
	private int calendarYear;

	/** our calendar */
	private Calendar cal;

	private Logger logger = Logger.getLogger(SonicLogListener.class);


	public SonicLogListener() {
		cal = Calendar.getInstance();
		calendarYear = cal.get(Calendar.YEAR);

		/** matches 07-09 12:18:52 */
		eventTimePattern = Pattern.compile(LOG_TIME_REGEX_EXTRACT);

		simpleDateFormat = new SimpleDateFormat(LOG_TIME_FORMAT);
	}

	/** 
	 * Generate an error using the AppDynamics agent
	 * 
	 * @param timestamp
	 * @param validateError
	 */
	protected void generateAppDynamicsEvent(String eventSummary, Map<String, String> details, boolean isCritical) {
		IMetricAndEventReporter metricEventSender = AgentDelegate.getMetricAndEventPublisher();
		
		logger.info("Publishing new AppDynamics Event, Critical (" + isCritical + "): " + eventSummary);

		metricEventSender.publishErrorEvent(eventSummary, details, isCritical);
	}

	/**
	 * Parse the timestamp from the log file line
	 * 
	 * @param line
	 * @return
	 */
	protected Date parseTimestamp(String line) {
		Date timestamp = new Date();


		try {
			Matcher m = eventTimePattern.matcher(line);

			if (m.find()) {
				String ts = m.group(1);
				ts = ts.trim();

				Date sdf = null;

				try {
					SimpleDateFormat sdfParser = new SimpleDateFormat(LOG_TIME_FORMAT);

					sdf = sdfParser.parse(ts);

					cal.setTime(sdf);
					cal.set(Calendar.YEAR, calendarYear);

					sdf = cal.getTime();

					logger.debug("Parsed " + sdf + " from " + ts);

				} catch (ParseException e) {
					logger.error("Caught error message processing time from " + ts + ": " + e.getMessage());
				}

				timestamp = (sdf == null) ? timestamp : sdf; 
			}
			else {
				logger.warn("Failed to parse log validation error timestamp from " + line);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}

		return timestamp;
	}

	@Override
	public void handle(String line) {

		if (shouldProcessLogLine(line)) {
			processLogLine(line);
		}
		else {
			logger.debug("Won't process " + line + " as a validation error");
		}

	}

	/** provide a summary description of the event */
	protected abstract String getEventSummary();
	
	/** process the details of the log event */
	protected abstract Map<String, String> parseLogEventDetails(String line);
	
	/** returns true when a log listener wants to process a log file line */
	protected abstract boolean shouldProcessLogLine(String line);

	/** process a log file line */
	protected void processLogLine(String line) {

		logger.debug("Processing log event: " + line);

		Date timestamp = parseTimestamp(line);
		Map<String, String> details = parseLogEventDetails(line);

		if (details != null) {

			String eventSummary = getEventSummary();
			
			details.put("Event Time", timestamp.toString());

			generateAppDynamicsEvent(eventSummary, details, true);
		}
		else {
			logger.warn("Won't generate event, failed to parse context data from " + line);
		}

	}
}
