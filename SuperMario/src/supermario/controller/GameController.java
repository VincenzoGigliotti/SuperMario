package supermario.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


import supermario.model.Game;
import supermario.model.Mario;
import supermario.thread.MapThread;
import supermario.thread.MarioThread;
import supermario.view.MapView;
import supermario.view.MarioView;

public class GameController implements KeyListener {
	
	private static GameController gameController = null;
	private MapView level1Map;
	private MarioThread marioThread;
	private MapThread mapThread;
	
	
	private GameController(MapView level1Map) {
		this.level1Map = level1Map;
		this.marioThread = new MarioThread();
		this.mapThread = new MapThread();
		
		marioThread.start();
		mapThread.start();
	}

		
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (!Game.getInstance().collision(e)) {
				if (Mario.getInstance().jumping && Mario.getInstance().direction == 1)
					MarioView.getInstance().jump();
				Game.getInstance().moveRight();
			}
		}
		
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			if (!Game.getInstance().collision(e) && Game.getInstance().canMoveLeft()) {
				if (Mario.getInstance().jumping && Mario.getInstance().direction == -1)
					MarioView.getInstance().jump();
				Game.getInstance().moveLeft();
			}
		}
		
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			if (Game.getInstance().collision(e) && !Mario.getInstance().jumping) { 
				Mario.getInstance().jumping = true;
			}
			else {
				if (!Mario.getInstance().jumping) {
					Mario.getInstance().jumping = true;
				}
			}
		}
	}
	
	public void riprendiThreads() {
		mapThread.riprendi();
		marioThread.riprendi();
	}
	
	public void interrompiThreads() {
		marioThread.interrompi();
		mapThread.interrompi();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() != KeyEvent.VK_SPACE && !Mario.getInstance().jumping) {
			MarioView.getInstance().stop();
		}
		Mario.getInstance().moving = false;
	}
	
	public static GameController getInstance(MapView level1Map) {
		if (gameController == null)
			gameController = new GameController(level1Map);
		return gameController;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}

}
