package com.vuzi.memorygame;

import java.io.Serializable;
import android.annotation.SuppressLint;
import android.content.res.Resources;

public class Score implements Serializable, Comparable<Score> {

	/**
	 * UID
	 */
	private static final long serialVersionUID = -8708233332463278294L;
	
	private int value;
	private int size;
	private int moves;
	private long miliseconds;
	
	/**
	 * Constructor 
	 * @param value
	 * @param moves
	 * @param miliseconds
	 */
	public Score(int value, int size, int moves, long miliseconds) {
		super();
		this.value = value;
		this.size = size;
		this.moves = moves;
		this.miliseconds = miliseconds;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public int getMoves() {
		return moves;
	}
	
	public void setMoves(int moves) {
		this.moves = moves;
	}
	
	public long getMiliseconds() {
		return miliseconds;
	}
	
	public void setMiliseconds(long miliseconds) {
		this.miliseconds = miliseconds;
	}
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	@SuppressLint("DefaultLocale")
	public String getFormatedMiliseconds() {
		int seconds = (int) (miliseconds / 1000);
		int ms = (int) (miliseconds - (seconds * 1000));
		return String.format("%d:%03d", seconds, ms);
	}

	public String getFormatedScore(Resources ressource) {
		String score;
		
		score = ressource.getString(R.string.game_score) + " ";
		score += value + "  ";
		score += ressource.getString(R.string.game_moves) + " ";
		score += moves + " ";
		score += ressource.getString(R.string.game_size2) + " ";
		score += size + " ";
		score += ressource.getString(R.string.game_timer) + " ";
		score += getFormatedMiliseconds() + "s";
		
		return score;
	}

	@Override
	public int compareTo(Score another) {
		if(value > another.value)
			return -1;
		else if(value < another.value)
			return 1;
		else
			return 0;
	}	
}
