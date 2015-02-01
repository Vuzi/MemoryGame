package com.vuzi.memorygame;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class MemoryGameScoreScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_memory_game_score_screen);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		MemoryGameApplication app = (MemoryGameApplication) getApplication();
		TableLayout tlayout = (TableLayout) findViewById(R.id.score_table);
		
		int i = 0;
		LayoutParams params;
		TableLayout.LayoutParams rowParams;
		
		// Data
		for(Score score : app.getScores()) {
			TableRow row = new TableRow(getApplicationContext());
			
			// Order
			params = new LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
			params.rightMargin = 10;
			TextView order = new TextView(getApplicationContext());
			order.setText("#"+i);
			order.setTextColor(Color.BLACK);
			row.addView(order, params);

			// Score
			params = new LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
			params.rightMargin = 10;
			TextView scoreText = new TextView(getApplicationContext());
			scoreText.setText(score.getValue()+"");
			scoreText.setTextColor(Color.BLACK);
			row.addView(scoreText, params);
			
			// Size
			params = new LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
			params.rightMargin = 10;
			TextView size = new TextView(getApplicationContext());
			size.setText(score.getSize()+"");
			size.setTextColor(Color.BLACK);
			row.addView(size, params);
			
			// Moves
			params = new LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
			params.rightMargin = 10;
			TextView moves = new TextView(getApplicationContext());
			moves.setText(score.getMoves()+"");
			moves.setTextColor(Color.BLACK);
			row.addView(moves, params);
			
			// Timer
			params = new LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
			params.rightMargin = 10;
			TextView time = new TextView(getApplicationContext());
			time.setText(score.getFormatedMiliseconds());
			time.setTextColor(Color.BLACK);
			row.addView(time, params);
			
			rowParams = new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT, 1.0f);
			row.setLayoutParams(rowParams);
			tlayout.addView(row);
			i++;
		}
	}
}
