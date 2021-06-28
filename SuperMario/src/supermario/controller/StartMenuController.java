package supermario.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import supermario.view.MainFrame;
import supermario.view.StartMenu;

public class StartMenuController implements KeyListener {

	private StartMenu startMenu;
	
	public StartMenuController(StartMenu startMenu) {
		this.startMenu = startMenu;
	}
	

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			MainFrame.getInstance().setLevelTransitionScene();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}
	@Override
	public void keyTyped(KeyEvent e) {}
}