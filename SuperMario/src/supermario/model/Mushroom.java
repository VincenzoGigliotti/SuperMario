package supermario.model;

import supermario.config.MapSettings;

public class Mushroom {
	
	public int x;
	public int y;
	public int direction;
	public boolean used;
	public boolean visible;
	
	public Mushroom(int x, int y) {
		this.x = x * MapSettings.BLOCK_SIZE;
		this.y = y * MapSettings.BLOCK_SIZE;
		this.direction = 1;
		this.used = false;
		this.visible = false;
	}

}
