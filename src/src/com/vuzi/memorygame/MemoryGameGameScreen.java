package com.vuzi.memorygame;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Spinner;

/**
 * Main activity
 * 
 * @author Vuzi
 *
 */
public class MemoryGameGameScreen extends Activity {
	
	private CardGameView cardView;
	private CardGame cardGame;
	
	private RelativeLayout gameLayout;
	private MiliChronometer chrono;
	private TextView movesText;
	
	private Bundle savedData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Init
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_memory_game_game_screen);

		// Get references
		gameLayout = (RelativeLayout) findViewById(R.id.gameLayout);
		chrono = (MiliChronometer) findViewById(R.id.chronometer);
		movesText = (TextView) findViewById(R.id.moves);
		savedData = savedInstanceState;
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		chrono.pause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		chrono.resume();
	}
	
	/**
	 * Reload a card game
	 * @param savedInstanceState The bundle data
	 */
	private void createCardGame(Bundle savedInstanceState) {
		
		// Load data
		int gameSize = savedInstanceState.getInt("game_size");
		int cards[][] = new int[gameSize][gameSize];
		boolean discovered[][] = new boolean[gameSize][gameSize];
		
		// Load the card matrix
		for(int i = 0; i < cards.length; i++)
			cards[i] = savedInstanceState.getIntArray("card"+i);
		
		// Load the discovered cards
		for(int i = 0; i < cards.length; i++)
			discovered[i] = savedInstanceState.getBooleanArray("discovered"+i);

		// Card model
		cardGame = new CardGame(cards, discovered);
		cardGame.setScore(savedInstanceState.getInt("score"));

		// Card view
		createCardView(gameSize, savedInstanceState.getInt("moves"), savedInstanceState.getLong("chrono"));
		
		// Hide or show winning message
		if(cardGame.testGameWon())
			displayWonMessage(new Score(cardGame.getScore(), gameSize, cardView.getMoves(), cardGame.getScore()));
		else
			hideWonMessage();
	}
	
	/**
	 * Create a new card game
	 * @param gameSize The game size (column)
	 */
	public void createCardGame(int gameSize) {
		
		// Hide winning message
		hideWonMessage();
		
		// Card model
		cardGame = new CardGame(gameSize, getResources().getInteger(R.integer.cardMaxValue));

		// Card view
		createCardView(gameSize);
	}
	
	@Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
		if(cardGame != null) {
			int cards[][] = cardGame.getCards();
			boolean discovered[][] = cardGame.getDiscovered();
 			
			// Save the card matrix
			savedInstanceState.putInt("game_size", cards.length);
			
			for(int i = 0; i < cards.length; i++)
				savedInstanceState.putIntArray("card"+i, cards[i]);
			
			// Save the discovered card matrix
			for(int i = 0; i < discovered.length; i++)
				savedInstanceState.putBooleanArray("discovered"+i, discovered[i]);
			
			savedInstanceState.putLong("chrono", chrono.getMiliseconds());
			savedInstanceState.putInt("moves", cardView.getMoves());
			savedInstanceState.putInt("score", cardGame.getScore());
		}
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Button & spinner
		final Spinner gameSize = (Spinner) findViewById(R.id.s_game_size_);
		Button loadButton = (Button) findViewById(R.id.b_game_new);
		loadButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View source) {
				createCardGame(Integer.parseInt(gameSize.getSelectedItem().toString()));
				chrono.start();
			}
			
		});
		
		// Create first game
		if(savedData != null && savedData.containsKey("game_size"))
			createCardGame(savedData);
		else
			createCardGame(4);
		
		return true;
	}
	
	/**
	 * Compute the optimal gamecard size based on the screen size
	 * @param gameSize The number of cards to display
	 * @return
	 */
	public int guessCardSize(int gameSize) {
		int width = (gameLayout.getWidth() / (1 + gameSize)) - gameSize * 2;
		int height = (gameLayout.getHeight() / (1 + gameSize)) - gameSize * 2;

		return width > height ? height : width;
	}
	
	/**
	 * Create the view, and init with the existing game
	 */
	private void createCardView(int gameSize) {
		createCardView(gameSize, 0, 0L);
	}
	
	/**
	 * Create the game view with the provided values
	 */
	private void createCardView(int gameSize, int moves, long timer) {

		if(cardView != null)
			gameLayout.removeView(cardView);
		
		int cardBitmapSize = getResources().getInteger(R.integer.cardImageSize); // pixels
		int cardNumberX = getResources().getInteger(R.integer.cardPerRow);
		int cardNumberY = getResources().getInteger(R.integer.cardPerColumn);
		
		// Card size
		int cardSize = guessCardSize(gameSize);
		
		// Card view/controller
		cardView = new CardGameView(this, cardGame);
		
		cardView.setChronometer(chrono);
		cardView.setMovesView(movesText);
		cardView.setInitialMoves(moves);
		cardView.setInitialTimer(timer);
		
		cardView.prepareBackDrawable(BitmapFactory.decodeResource(getResources(), R.drawable.back));
		cardView.prepareCardsDrawables(BitmapFactory.decodeResource(getResources(), R.drawable.cards), cardNumberX, cardNumberY, cardBitmapSize);
		
		cardView.initView(gameSize, cardSize);

		// Layout
		LayoutParams p = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
		p.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		p.setMargins(0, cardSize/2, 0, 0);

		// Add to the view
		gameLayout.addView(cardView, p);
	}
	
	/**
	 * Hide the won message
	 */
	public void hideWonMessage() {

		// Hide won message
		LinearLayout wonLayout = (LinearLayout) findViewById(R.id.gameWonMessage);
		wonLayout.setVisibility(LinearLayout.INVISIBLE);
	}
	
	/**
	 * Display the winning message
	 */
	public void displayWonMessage(Score score) {

		// Display won message
		LinearLayout wonLayout = (LinearLayout) findViewById(R.id.gameWonMessage);
		wonLayout.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.won_message));
		wonLayout.setVisibility(LinearLayout.VISIBLE);
		wonLayout.bringToFront();
		
		TextView wonMessage = (TextView) findViewById(R.id.t_game_won);
		wonMessage.setText(getResources().getString(R.string.game_moves) + " " + score.getMoves() +
				"    " + getResources().getString(R.string.game_timer) + " " + score.getFormatedMiliseconds());
		
		TextView scoreMessage = (TextView) findViewById(R.id.t_game_score);
		scoreMessage.setText(getResources().getString(R.string.game_score) + " " + score.getValue());
	}
}
