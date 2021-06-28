package supermario.view;

import java.awt.image.BufferedImage;


import supermario.model.Mario;
import supermario.util.SpriteSheetCutter;

public class MarioView {
	
	private static MarioView marioView = null;
	private BufferedImage [][] mario;
	public static final int IDX_LITTLE_MARIO = 0;
	public static final int IDX_BIG_MARIO = 1;
	public static final int IDX_DEAD_MARIO = 2;
	private int index;
	
	
	private MarioView() {
		mario = new BufferedImage[3][10];
		this.index = 3;
		SpriteSheetCutter marioAnimation = new SpriteSheetCutter("/supermario/resources/sprites/little_mario.png", 5, 2, 51, 48);
		for (int i = 0; i < 10; i++) {
			mario[IDX_LITTLE_MARIO][i] = marioAnimation.getTiles().get(i);
		}
		marioAnimation = new SpriteSheetCutter("/supermario/resources/sprites/big_mario.png", 5, 2, 49, 96);
		for (int i = 0; i < 10; i++) {
			mario[IDX_BIG_MARIO][i] = marioAnimation.getTiles().get(i);
		}
		marioAnimation = new SpriteSheetCutter("/supermario/resources/sprites/dead_mario.png", 1, 1, 15, 14);
		mario[IDX_DEAD_MARIO][0] = marioAnimation.getTiles().get(0);
	}
	
	public void update() {
		index += 2;
		if (index > 9)
			index = 3;
		if (Mario.getInstance().direction == 1 && index %2 == 0) {
			index = 3;
		}
		if (Mario.getInstance().direction == -1 && index %2 != 0)
			index = 2;
	}
	
	public void jump() {
		if (Mario.getInstance().direction == 1)
			index = 1;
		
		if (Mario.getInstance().direction == -1)
			index = 0;
	}
	
	public void stop() {
		if (Mario.getInstance().direction == 1) 
			index = 3;
		else if (Mario.getInstance().direction == -1) 
			index = 2;
	}
	
	public BufferedImage getCurrentMarioImage() {
		if (Mario.getInstance().status == IDX_DEAD_MARIO)
			return mario[Mario.getInstance().status][0];
		return mario[Mario.getInstance().status][index];
	}
	
	public static MarioView getInstance() {
		if (marioView == null) {
			marioView = new MarioView();
		}
		return marioView;
	}
}
