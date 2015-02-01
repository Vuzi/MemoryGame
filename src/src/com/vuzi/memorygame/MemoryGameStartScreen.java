package com.vuzi.memorygame;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MemoryGameStartScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_memory_game_start_screen);
		
		Button launchGame = (Button) findViewById(R.id.b_new_game);
		Button viewScore = (Button) findViewById(R.id.b_view_score);
		
		launchGame.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startGame();
			}
			
		});
		
		viewScore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				showScores();
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		// Set the maximum scores (if any)
		TextView[] scoreLabels = { (TextView) findViewById(R.id.t_score1),
								   (TextView) findViewById(R.id.t_score2),
								   (TextView) findViewById(R.id.t_score3) };
		
		List<Score> scores = ((MemoryGameApplication) getApplication()).getScores();

		for(int i = 0; i < 3; i++) {
			if(scores.size() > i) {
				scoreLabels[i].setText("#" + i + ": " + scores.get(i).getFormatedScore(getResources()));
			} else {
				scoreLabels[i].setText("");
			}
		}
	}
	
	/**
	 * Start the game with the default size
	 */
	private void startGame() {
		Intent intent = new Intent(this, MemoryGameGameScreen.class);
		startActivity(intent);
	}
	
	/**
	 * Show all the saved scores
	 */
	private void showScores() {
		Intent intent = new Intent(this, MemoryGameScoreScreen.class);
		startActivity(intent);
	}
}
