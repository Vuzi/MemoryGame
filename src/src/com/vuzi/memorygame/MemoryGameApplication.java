package com.vuzi.memorygame;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Application;

public class MemoryGameApplication extends Application {

	private ArrayList<Score> scores;

    @Override
    public void onCreate() {
    	super.onCreate();
    	
    	// Load scores
    	scores = loadScores();
    }

    /**
     * Get all the game scores
     * @return A list of all the game scores
     */
    public List<Score> getScores() {
    	return scores;
    }
    
    /**
     * Sort the score list
     */
    public void sortScores() {
    	Collections.sort(scores);
    }
    
    /**
     * Save the score in the filesystem
     */
    public void saveScores() {
    	saveScores(scores);
    }
    
	/**
	 * Save the first 100 given scores
	 * @param scores The score to save
	 */
	private void saveScores(ArrayList<Score> scores) {
		FileOutputStream outputStream;
		ObjectOutputStream objectOutputStream;
		String filename = "scores.save";
		
		try {
			// Open the writter
			outputStream = openFileOutput(filename, MODE_PRIVATE);
			objectOutputStream = new ObjectOutputStream(outputStream);
			
			// Write the scores
			if(scores.size() > 50)
				objectOutputStream.writeObject(scores.subList(0, 50));
			else
				objectOutputStream.writeObject(scores);
			
			// Close the stream
			objectOutputStream.close();
		} catch (Exception e) {
			  e.printStackTrace();
		}
	}
	
	/**
	 * Load the saved score
	 * @return The list of loaded scores
	 */
	private ArrayList<Score> loadScores() {
		FileInputStream inputStream;
		ObjectInputStream objectInputStream;
		String filename = "scores.save";
		
		try {
			// Open the save file
			inputStream = openFileInput(filename);
			objectInputStream = new ObjectInputStream(inputStream);
			
			// Read the array list of scores
			@SuppressWarnings("unchecked")
			ArrayList<Score> scores = (ArrayList<Score>) objectInputStream.readObject();
			
			// Close the steam
			objectInputStream.close();
			
			if(scores != null)
				return scores;
			else
				return new ArrayList<Score>();
				
		} catch (Exception e) {
			  e.printStackTrace();
			  
			  // Return empty list
			  return new ArrayList<Score>();
		}
	}

	/**
	 * Add a new score and sort the scores
	 * @param score The new score
	 */
	public void addScore(Score score) {
		scores.add(score);
		sortScores();
	}
}
