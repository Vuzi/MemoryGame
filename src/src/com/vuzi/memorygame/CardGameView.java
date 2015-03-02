package com.vuzi.memorygame;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.widget.GridLayout;
import android.widget.TextView;

/**
 * Controller for the card game. Handle all the cards view, the layout and the events
 * 
 * @author Vuzi
 *
 */
public class CardGameView extends GridLayout {

	// View handling & construction
	private CardGame cardGame = null;
	private ArrayList<Drawable> drawableCards = null;
	private CardView[][] cards = null;
	private Drawable drawableBack = null;
	private CardView lastCard;

	private MemoryGameGameScreen parent;
	private MiliChronometer chrono;
	private TextView moveConter;
	
	int moves;
	long timerValue;
	
	int inAction = 0;
	
	/**
	 * Constructor
	 *  
	 * @param context The context.
	 * @param cardGame The cardGame.
	 */
	public CardGameView(MemoryGameGameScreen parent, CardGame cardGame) {
		super(parent.getApplicationContext());
		
		this.parent = parent;
		this.cardGame = cardGame;
	}
	
	/**
	 * Set the chronometer to use
	 * @param chrono
	 */
	public void setChronometer(MiliChronometer chrono) {
		this.chrono = chrono;
	}

	/**
	 * Set the textView counter
	 * @param textView
	 */
	public void setMovesView(TextView textView) {
		moveConter = textView;
	}
	
	private void counterInc() {
		moves++;
		moveConter.setText(moves+"");
	}
	
	/**
	 * Return the number of moves for the current game
	 * @return
	 */
	public int getMoves() {
		return moves;
	}
	
	/**
	 * Return the contained card game used for the game construction
	 * @return
	 */
	public CardGame getCardGame() {
		return cardGame;
	}
	
	
	/**
	 * Set the moves number to the given value
	 * @param moves
	 */
	public void setInitialMoves(int moves) {
		this.moves = moves;
		moveConter.setText(moves+"");
	}
	
	/**
	 * Set the initial timer value
	 * @param time
	 */
	public void setInitialTimer(long time) {
		this.timerValue = time;
	}
	
	/**
	 * Init the view.
	 */
	public void initView(int cardsByRown, int cardSize) {
		if(cardGame == null || drawableBack == null || drawableCards == null) {
			Log.i("CardGameView", "Can't display game !");
			return; // Can't init the view
		}
		
		cards = new CardView[cardsByRown][cardsByRown];

		// Prepare layout
		removeAllViews();
		setColumnCount(cardsByRown);
		
		// Add the cards
		for(int i = 0; i < cardsByRown; i++) {
			for(int j = 0; j < cardsByRown; j++) {
				
				// Layout
				LayoutParams params = new GridLayout.LayoutParams();
				params.width = cardSize;
				params.height = cardSize;
				params.setMargins(5, 5, 5, 5);
				
				// Add the cards views + bind events
				CardView gc = new CardView(this, drawableCards.get(cardGame.getCards()[i][j]), drawableBack, i, j);	
						
				// Add to layout & save
				cards[i][j] = gc;
				gc.setLayoutParams(params);
				this.addView(gc);
				
				// If seleted, display
				if(cardGame.getDiscovered()[i][j])
					gc.showCard();
			}
		}
		
		// Chrono
		chrono.start(timerValue);
	}
	
	/**
	 * Set a card clicked
	 * @param x
	 * @param y
	 */
	public void setClicked(int x, int y) {
		CardView card = cards[x][y];
		
		// Not more than two action at the same time
		if(inAction < 2) {
			inAction++;
			
			// Is this card can be selected ?
			if(!cardGame.getDiscovered()[x][y]) {

				// Other card selected ?
				if(lastCard != null) {
					final CardView clicked = card;
					final CardView other = lastCard;

					// Not the same card
					if(other != clicked) {
						counterInc();
						card.showCard();
						
						Handler handler = new Handler();
						handler.postDelayed(new Runnable() {
							
							@Override
							public void run() {
								// Same value ?
								if(cardGame.validatePairs(clicked.getXpos(), clicked.getYpos(), other.getXpos(), other.getYpos())) {
									// Yes
									cardGame.getDiscovered()[clicked.getXpos()][clicked.getYpos()] = true;
									cardGame.getDiscovered()[other.getXpos()][other.getYpos()] = true;
									
									if(cardGame.isGameWon()) {
										// Won
										gameWon();
									}
								} else {
									// Node
									clicked.hideCard();
									other.hideCard();
								}
								
								lastCard = null;
								inAction = 0;
							}
						}, 400);
					} else
						inAction--;
				} else {
					lastCard = card;
					card.showCard();
					counterInc();
				}
			} else
				inAction--;
		}
	}
	
	private void gameWon() {
		// Stop chrono
		chrono.stop();
		cardGame.computeScore(moves, chrono.getMiliseconds());
		
		// Score
		Score score = new Score(cardGame.getScore(), cards.length, moves, chrono.getMiliseconds());
		
		// Save the score
		MemoryGameApplication app = (MemoryGameApplication) parent.getApplication();
		app.addScore(score);
		app.saveScores(); // Force the save
		
		// Winning message
		parent.displayWonMessage(score);
	}
	
	/**
	 * Create the back of the cards.
	 * @param cardBack
	 */
	public void prepareBackDrawable(Bitmap cardBack) {
		drawableBack = new BitmapDrawable(getResources(), cardBack);
	}

	/**
	 * Prepare the card for display.
	 * @param cardsImage
	 * @param cardsByRown
	 * @param cardSize
	 * @return
	 */
	public void prepareCardsDrawables(Bitmap cardsImage, int cardsByRown, int cardsByColumn, int cardSize) {

		// Get the card images
		drawableCards = new ArrayList<Drawable>();
		
		for(int i = 0; i < cardsByRown; i++) {
			for(int j = 0; j < cardsByColumn; j++) {
				drawableCards.add(new BitmapDrawable(getResources(), Bitmap.createBitmap(cardsImage, j * cardSize, i * cardSize, cardSize, cardSize)));
			}
		}
	}
	
}
