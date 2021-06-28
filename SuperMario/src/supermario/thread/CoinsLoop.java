package supermario.thread;

import supermario.view.StartMenu;

public class CoinsLoop implements Runnable {
	
	// thread usato per animazione della moneta nello start menu
	@Override
	public void run() {
		while(!Thread.currentThread().isInterrupted()) {
			StartMenu.getInstance().update();
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				return;
			}
		}
	}

}