package com.vuzi.memorygame;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Card game
 * 
 * @author Vuzi
 *
 */
public class CardGame {

	// Variables members
	private int cards[][] = null;
	private boolean discovered[][] = null;
	
	private boolean isWon = false;
	private int score = 0;
	
	/**
	 * Constructor: generate a double array of size n, filled with couples of int datas
	 */
	public CardGame(int n, int maximumValue) {
		
		// Tests, n must be even
		if(n % 2 == 1)
			n++;
		
		// Prepare data
		cards = new int[n][n];
		discovered = new boolean[n][n];
		ArrayList<Integer> cardsRand = new ArrayList<Integer>();
		
		for(int i = ((n*n)/2) - 1; i >= 0; i--) {
			// Couple of values
			cardsRand.add(i % maximumValue);
			cardsRand.add(i % maximumValue);
		}
		
		Collections.shuffle(cardsRand);

		// Fill data
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				cards[i][j] = cardsRand.remove(0);
				discovered[i][j] = false;
			}
		}
	}
	
	/**
	 * Constructor : init game with exiting values
	 */
	public CardGame(int[][] cards, boolean[][] discovered) {
		// Arrays
		this.cards = cards;
		this.discovered = discovered;
	}

	/**
	 * Get the filled cards
	 * @return The cards
	 */
	public int[][] getCards() {
		return cards;
	}

	/**
	 * Get the filled cards
	 * @return The cards
	 */
	public boolean[][] getDiscovered() {
		return discovered;
	}
	
	/**
	 * Check if the game is won or not, forcing the test
	 * 
	 * @return True if won, false otherwise.
	 */
	public boolean testGameWon() {
		for(int i = 0; i < discovered.length; i++) {
			for(int j = 0; j < discovered.length; j++) {
				if(discovered[i][j] == false)
					return false;
			}
		}
		
		return true;
	}

	/**
	 * Check if the game is won or not
	 * 
	 * @return True if won, false otherwise
	 */
	public boolean isGameWon() {
		return isWon;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	/**
	 * Validate a pair of cards (or not)
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return True if validated, false otherwise
	 */
	public boolean validatePairs(int x1, int y1, int x2, int y2) {
		if(cards[x1][y1] == cards[x2][y2]) {
			discovered[x1][y1] = true;
			discovered[x2][y2] = true;
			
			isWon = testGameWon();
			
			return true;
		} else {
			return false;
		}
	}
	
	public int computeScore(int moves, long miliseconds) {
		this.score  = (int) ((1000F / ((moves/100F + miliseconds/100F) / (cards.length / 2))) * cards.length * cards.length * cards.length);
		return score;
	}
}
