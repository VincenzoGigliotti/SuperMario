package supermario.model;

import supermario.config.BlockSettings;
import supermario.config.MapSettings;

public class Goomba {

	public int x;
	public int y;
	public final static int SPEED = 10;
	public int direction;
	public int status;
	public boolean smashed;
	
	public Goomba(int x) {
		this.x = x * MapSettings.BLOCK_SIZE;
		this.y = (MapSettings.ROW - 2) * MapSettings.BLOCK_SIZE;
		this.direction = 1;
		this.status = BlockSettings.RIGHT_GOOMBA;
		this.smashed = false;
	}
	
}