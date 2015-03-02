package com.vuzi.memorygame;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnDismissListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class MemoryGameScoreScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_memory_game_score_screen);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		final MemoryGameApplication app = (MemoryGameApplication) getApplication();
		final TableLayout tlayout = (TableLayout) findViewById(R.id.score_table);
		
		// Clean the layout
		View header = tlayout.getChildAt(0);
		tlayout.removeAllViews();
		tlayout.addView(header);
		
		int i = 0;
		LayoutParams params;
		TableLayout.LayoutParams rowParams;
		
		// Data
		for(Score score : app.getScores()) {
			final TableRow row = new TableRow(getApplicationContext());
			
			final int finalI = i;
			final Score finalScore = score;
			
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
			
			row.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					row.setBackgroundColor(Color.rgb(255, 241, 184));

			        // Creating the pop-up menu
					PopupMenu popup = new PopupMenu(MemoryGameScoreScreen.this, row);
			        popup.getMenuInflater().inflate(R.menu.score_menu, popup.getMenu());
			        
			        // On dismiss, change back the color
			        popup.setOnDismissListener(new OnDismissListener() {
						@Override
						public void onDismiss(PopupMenu menu) {
							row.setBackgroundColor(Color.TRANSPARENT);
						}
					});			      
			        
			        // On select, if "remove" remove from model and reload view
			        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {  
						public boolean onMenuItemClick(MenuItem item) {
							if(item.getItemId() == R.id.remove) {
								Toast.makeText(MemoryGameScoreScreen.this, 
										String.format(getResources().getString(R.string.score_remove_msg), finalI), 
										Toast.LENGTH_SHORT).show();
								
								app.getScores().remove(finalScore);
								tlayout.removeView(row);
								
								app.saveScores();
							}
							
							row.setBackgroundColor(Color.TRANSPARENT);
							return true;  
						}  
			        });  
			        
			        popup.show();
				}
			});
			
			i++;
		}
	}
}
