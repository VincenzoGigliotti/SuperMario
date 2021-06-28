package supermario.model;

import supermario.config.MapSettings;
import supermario.view.MarioView;

public class Mario {
	
	private static Mario mario = null;

	public int x;
	public int y;
	public static final int SPEED = 10;
	public int direction;
	public int status;
	public boolean jumping;
	public boolean falling;
	public boolean moving;
	public boolean dead;
	public int life;
	
	private Mario() {
		this.moving = false;
		this.status = MarioView.IDX_LITTLE_MARIO;
		this.y = (MapSettings.ROW - 2) * MapSettings.BLOCK_SIZE;
		this.x = 0;
		this.jumping = false;
		this.falling = false;
		this.dead = false;
		this.direction = 1;
	}
	
	public void reset() {
		this.mario = new Mario();
	}
	
	public static Mario getInstance() {
		if(mario == null) {
 			mario = new Mario();
		}
		return mario;
	}
	
}