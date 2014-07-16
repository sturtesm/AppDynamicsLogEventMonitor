package com.appdynamics.monitor.log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

public class LogEventMonitor {

	public static void main(String[] args) throws IOException {
		Logger logger = Logger.getLogger(LogEventMonitor.class);
		Properties p = new Properties();
		
		if (args.length <= 0) {
			logger.fatal("Error starting, expecting Log File to tail as an input....");
			
			System.exit(100);
		}
		else {
			logger.info("Tailing " + args[0] + "....");
		}
		
		
		/** try to load the properties file from within the JAR, or file system */
		InputStream stream = LogEventMonitor.class.getResourceAsStream("/logTailer.properties");

		if (stream == null) {
			logger.warn("Failed to read resource as a stream /logTailer.properties, trying file input stream...");
			try {
				stream = new FileInputStream("logTailer.properties");
			} catch (FileNotFoundException e) {
				stream = null;
			}
			
			if (stream == null) {
				logger.error("Failed to read logTailer.properties as a file input stream. Exiting....");
				
				System.exit(200);
			}
			else {
				logger.debug("Read logTailer.properties as a file input stream.");
			}
		}
		else {
			logger.debug("Read logTailer.properties as a resource stream.");
		}
		
		p.load(stream);
		
		
		File logPath = new File(args[0]);
		List<SonicLogListener> listeners = new ArrayList<SonicLogListener> (); 
		
		listeners.add(new ValidationLoggerListener());
		listeners.add(new InvalidStatusListener());
		
		LogFileTailer tailer = 
			new LogFileTailer(logPath, new Long(5000), listeners);
		
		/** start tailing the log files */
		tailer.start();
		
		while (true) {
			try {
				Thread.sleep(1000);
			}
			catch (Exception e) {
				
			}
		}
	}

}
