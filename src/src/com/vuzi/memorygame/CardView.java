package com.vuzi.memorygame;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

/**
 * Graphical card displayed
 * 
 * @author Vuzi
 *
 */
public class CardView extends ImageSwitcher implements OnClickListener {

	private CardGameView parent;
	
	private Drawable cardImage;
	private Drawable backImage;
	
	private int x;
	private int y;
	
	/**
	 * Constructor of the graphical card
	 * @param parent The parent class
	 * @param cardImage The card drawable image
	 * @param backImage The card back drawable image
	 * @param x The x position of the card
	 * @param y The y position of the card
	 */
	public CardView(CardGameView parent, Drawable cardImage, Drawable backImage, int x, int y) {
		super(parent.getContext());
		
		this.cardImage = cardImage;
		this.backImage = backImage;
		this.parent = parent;
		
		this.x = x;
		this.y = y;
		
		this.init();
	}

	/**
	 * Init the graphical card
	 */
	private void init() {
		
		setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.from_middle));
		setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.to_middle));
		
		setFactory(new ViewFactory() {
			
			@Override
			public View makeView() {
				
				// Prepare image
				ImageView imageView = new ImageView(getContext());
				
				// Set layout
				LayoutParams params = new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				imageView.setLayoutParams(params);
                
                return imageView;
			}
		});
		
		setImageDrawable(backImage);
		setOnClickListener(this);
	}
	
	/**
	 * Get the x position of the card
	 * @return The x position of the card
	 */
	public int getXpos() {
		return x;
	}
	
	/**
	 * Get the Y position of the card
	 * @return The y position of the card
	 */
	public int getYpos() {
		return y;
	}

	@Override
	public void onClick(View v) {
		parent.setClicked(x, y);
	}
	
	/**
	 * Launch the display of the card
	 */
	protected void showCard() {
		setImageDrawable(cardImage);
	}
	
	/**
	 * Launch the hidding of the card
	 */
	protected void hideCard() {
		setImageDrawable(backImage);
	}
	
}
