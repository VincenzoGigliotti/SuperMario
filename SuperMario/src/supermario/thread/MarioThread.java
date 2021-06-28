package supermario.thread;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import supermario.config.BlockSettings;
import supermario.config.MapSettings;
import supermario.model.Game;
import supermario.model.Mario;
import supermario.sound.SoundManager;
import supermario.view.MarioView;

public class MarioThread extends Thread {
	
	// thread che si occupa della gestione di mario, delle sue animazioni e delle collisioni con i mushroom e i goomba
	
	private int counter;
	private int delay;
	private Timer timer;
	private boolean interrotto;
	private boolean canDead;
	private int cont;
	
	public MarioThread() {
		this.counter = 2;
		this.delay = 1000;
		this.interrotto = false;
		this.canDead = true;
		this.cont = 0;
	}
	
	@Override
	public void run() {
		super.run();
		while(true) {
			if(!interrotto) {
				if (Mario.getInstance().x / MapSettings.BLOCK_SIZE >= 197) {
					SoundManager.getInstance().stopMusic();
					while (Mario.getInstance().x / MapSettings.BLOCK_SIZE < 206) {
						Mario.getInstance().x += 10;
						MarioView.getInstance().update();
						try {
							Thread.sleep(80);
						} catch (InterruptedException e) {
							System.out.println("Error in MarioThread!");
						}
						SoundManager.getInstance().newLevel();
					}
					Game.getInstance().updateLevel();
				}
				if (Mario.getInstance().jumping) {
					MarioView.getInstance().jump();
					Game.getInstance().jump();
					MarioView.getInstance().stop();
				}
				if (Mario.getInstance().moving) {
					if (cont == 100) {
						MarioView.getInstance().update();
						cont = 0;
					}
					cont++;
				}
				Mario.getInstance().jumping = false;
				
				if (Game.getInstance().getGoombas().size() > 0) {
					for (int i = 0; i < Game.getInstance().getGoombas().size(); i++) {
						if (!Game.getInstance().getGoombas().get(i).smashed && (Game.getInstance().getGoombas().get(i).x + 30  == Mario.getInstance().x || Game.getInstance().getGoombas().get(i).x - 30  == Mario.getInstance().x) && Mario.getInstance().y == (MapSettings.ROW - 2) * MapSettings.BLOCK_SIZE)
							if (Mario.getInstance().status == MarioView.getInstance().IDX_BIG_MARIO) {
								SoundManager.getInstance().powerDown();
								Mario.getInstance().status = MarioView.getInstance().IDX_LITTLE_MARIO;
								canDead = false;
								// parte un timer per far si che mario da grande a piccolo non muoia appena tocca un nemico, dato che controlla le collisioni sempre e non si ha tempo di spostarsi altrimenti
								ActionListener action = new ActionListener() {
						            @Override
						            public void actionPerformed(ActionEvent event) {
						                if(counter == 0) {
						                    timer.stop();
						                    canDead = true;
						                }
						                else
						                	counter--;
						                }
						        };
						        counter = 2;
						        timer = new Timer(delay, action);
						        timer.setInitialDelay(0);
						        timer.start();
							}
							else if (Mario.getInstance().status == MarioView.getInstance().IDX_LITTLE_MARIO && canDead == true && !Mario.getInstance().falling) {
								Mario.getInstance().status = MarioView.IDX_DEAD_MARIO;
								Mario.getInstance().dead = true;
								for (int j = 0; j < 200; j++) {
									Mario.getInstance().y--;
									try {
										Thread.sleep(1);
									} catch (InterruptedException e) {
										System.out.println("Error in EntityCollisionThread!");
									}
								}
								for (int j = 0; j < 200; j++) {
									Mario.getInstance().y++;
									try {
										Thread.sleep(1);
									} catch (InterruptedException e) {
										System.out.println("Error in EntityCollisionThread!");
									}
								}
								Game.getInstance().deadMario();
							}
						if (Game.getInstance().getBlocks()[Game.getInstance().getGoombas().get(i).y / MapSettings.BLOCK_SIZE + 1][Game.getInstance().getGoombas().get(i).x / MapSettings.BLOCK_SIZE + 1].getType() == BlockSettings.VOID) {
							Game.getInstance().getGoombas().get(i).status = BlockSettings.DEAD_GOOMBA;
							Game.getInstance().getGoombas().get(i).smashed = true;
						}
					}
				}
				for (int i = 0; i < Game.getInstance().getMushroom().size(); i++) {
					if (Game.getInstance().getMushroom().get(i).visible && Game.getInstance().getMushroom().get(i).y == Mario.getInstance().y && (Game.getInstance().getMushroom().get(i).x + 30  == Mario.getInstance().x || Game.getInstance().getMushroom().get(i).x == Mario.getInstance().x || Game.getInstance().getMushroom().get(i).x - 30  == Mario.getInstance().x)  && !Game.getInstance().getMushroom().get(i).used) {
						Mario.getInstance().status = MarioView.getInstance().IDX_BIG_MARIO;
						SoundManager.getInstance().powerUp();
						Game.getInstance().getMushroom().get(i).used = true;
						Game.getInstance().getMushroom().get(i).visible = false;
					}
				}
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
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