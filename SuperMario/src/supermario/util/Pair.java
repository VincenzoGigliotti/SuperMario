package supermario.util;

// classe creata per facilitare la gestione dei vari oggetti di cui abbiamo bisogno di sapere sia la x che la y contemporaneamente 

public class Pair {
	private int x;
	private int y;
	
	public Pair(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
}
