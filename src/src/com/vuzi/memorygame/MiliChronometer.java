package com.vuzi.memorygame;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;

/**
 * Chronometer displaying seconds and miliseconds
 * @author Vuzi
 *
 */
public class MiliChronometer extends android.widget.TextView {

	private long init, time;
	
    private final Handler handler = new Handler();
    private boolean paused = false, running = false;
    private Runnable updater;
    
    public MiliChronometer(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		setText("00:00");
	}
    
	public MiliChronometer(Context context) {
		super(context);
		
		setText("00:00");
	}
	
	/**
	 * Get the miliseconds
	 * @return The miliseconds
	 */
	public long getMiliseconds() {
		return time;
	}

	public void start(long passedTime) {
		
		// Init timer
		init = System.currentTimeMillis() - passedTime;
		running = true;
		
		// Update every 30ms
		updater = new Runnable() {
            @Override
            public void run() {
            	if(running) {
	            	if(!paused) {
	            		updateTimer();
	            	}
	                handler.postDelayed(this, 30);
            	}
            }
        };
        handler.postDelayed(updater, 30);
        
        // First update
        updateTimer();
	}
	
	/**
	 * Start the chronometer
	 */
	public void start() {
		start(0);
	}
	
	/**
	 * Pause the chronometer
	 */
	public void pause() {
		if(!paused && running) {
			paused = true;
		}
	}
	
	/**
	 * Resume the chronometer
	 */
	public void resume() {
		if(paused && running) {
			init = System.currentTimeMillis() - time;
			paused = false;
		}
	}
	
	/**
	 * Stop the chronometer
	 */
	public void stop() {
		if(running) {
			handler.removeCallbacks(updater);
			time = System.currentTimeMillis() - init;
			updateTimer();
			running = false;
		}
	}
	
	private void updateTimer() {
		time = System.currentTimeMillis() - init;
		
		int seconds = (int) (time / 1000);
		int ms = (int) (time - (seconds * 1000));
		
		setText(String.format("%d:%03d", seconds, ms));
	}
	
}
