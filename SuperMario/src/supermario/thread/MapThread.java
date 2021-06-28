package supermario.thread;

import supermario.model.Game;
import supermario.view.MapView;

public class MapThread extends Thread {
	
	// thread che si occupa della gestione della mappa, repaint, animazioni dei blocchi, goomba ecc..
	
	private boolean interrotto;
	private int cont;
	
	public MapThread() {
		this.interrotto = false;
		this.cont = 0;
	}
	
	@Override
	public void run() {
		super.run();
		while(true) {
			if(!interrotto) {
				MapView.getInstance().repaint();
				if(cont >= 150) {
					cont = 0;
					MapView.getInstance().updateBlocks();
					if (Game.getInstance().brickBroken) {
						MapView.getInstance().breakBlocks(Game.getInstance().brickToBreak.getX(), Game.getInstance().brickToBreak.getY());
						Game.getInstance().brickBroken = false;
					}
					Game.getInstance().moveGoomba();
					MapView.getInstance().updateGoomba();
					for (int i = 0; i < Game.getInstance().getMushroom().size(); i++) {
						if (Game.getInstance().getMushroom().get(i).visible)
							Game.getInstance().moveMushroom(i);
					}
				}
				cont++;
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				System.out.println("Error in MapThread!");
			}
		}
	}
	
	public void interrompi() {
		this.interrotto = true;
	}
	
	public void riprendi() {
		this.interrotto = false;
	}

}