package com.appdynamics.monitor.log;

import java.io.File;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.apache.commons.io.input.TailerListenerAdapter;


public class LogFileTailer extends TailerListenerAdapter {

	private Hashtable<String, Tailer> logTailers = null;
	private long tailFrequencyMillis;

	/** the listeners that will respond to file changes */
	private List<SonicLogListener> tailerListeners = null;
	
	private File file;
	
	/** a pointer to this log tailer */
	private Tailer tailer;


	/**
	 * Construct a new log file tailer
	 * 
	 * @param fileName Tail this log file
	 * @param tailFrequencyMillis check for updates every <N> milliseconds
	 */
	public LogFileTailer(File file, Long tailFrequencyMillis, List<SonicLogListener> listeners) {

		if (file == null || file.getName() == null) {
			throw new IllegalArgumentException("File to tail cannot be null");
		}
		else if (tailFrequencyMillis == null || tailFrequencyMillis.longValue() <= 0) {
			throw new IllegalArgumentException("Tail frequency cannot be NULL and must be > 0");
		}
		else if (listeners == null || listeners.size() <= 0) {
			throw new IllegalArgumentException("Listeners cannot be NULL and must be > 0");
		}

		this.file = file;
		this.logTailers = new Hashtable<String, Tailer> ();
		this.tailFrequencyMillis = tailFrequencyMillis;
		this.tailerListeners = listeners;
	}

	public void start() {
		tailer  = Tailer.create(file, this, tailFrequencyMillis);
	}

	public void stop() {
		if (tailer != null) {
			tailer.stop();
		}
	}

	@Override
	public void handle(String line) {

		for (TailerListener tl : tailerListeners) {
			tl.handle(line);
		}
	}
}
