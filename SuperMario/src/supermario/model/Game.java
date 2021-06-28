package supermario.model;

import java.awt.event.KeyEvent;
import java.util.ArrayList;


import supermario.config.BlockSettings;
import supermario.config.GameSettings;
import supermario.config.MapSettings;
import supermario.sound.SoundManager;
import supermario.util.Pair;
import supermario.view.MainFrame;
import supermario.view.MapView;

public class Game {
	private static Game game = null;
	private MapReader reader;
	private Block[][] blocks;
	private Integer[][] map;
	public boolean brickBroken;
	public Pair brickToBreak;
	private ArrayList<Goomba> goombas;
	private ArrayList<Mushroom> mushrooms;
	
	private Game() {
		this.goombas = new ArrayList<Goomba>();
		this.mushrooms = new ArrayList<Mushroom>();
		reader = new MapReader("/supermario/resources/map/Map" + GameSettings.level + ".txt");
		map = reader.getBlocks();
		blocks = new Block[MapSettings.ROW][MapSettings.COL];
		initializeBlocks();
		this.brickBroken = false;
	}
	
	// reset del game per avere l'istanza aggiornata
	public void reset() {
		this.game = new Game();
	}
	
	private void initializeBlocks() {
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[i].length; j++) {
				switch (map[i][j]) {
				case BlockSettings.GROUND:
					blocks[i][j] = new Block(BlockSettings.GENERAL);
					break;
				case BlockSettings.STAIR:
					blocks[i][j] = new Block(BlockSettings.GENERAL);
					break;
				case BlockSettings.UPPER_PIPE_SX:
					blocks[i][j] = new Block(BlockSettings.GENERAL);
					break;
				case BlockSettings.UPPER_PIPE_DX:
					blocks[i][j] = new Block(BlockSettings.GENERAL);
					break;
				case BlockSettings.LOWER_PIPE_SX:
					blocks[i][j] = new Block(BlockSettings.GENERAL);
					break;
				case BlockSettings.LOWER_PIPE_DX:
					blocks[i][j] = new Block(BlockSettings.GENERAL);
					break;
				case BlockSettings.UPPER_FLAG:
					blocks[i][j] = new Block(BlockSettings.GENERAL);
					break;
				case BlockSettings.LOWER_FLAG:
					blocks[i][j] = new Block(BlockSettings.GENERAL);
					break;
				case BlockSettings.MYSTERY_BOX:
					blocks[i][j] = new Block(BlockSettings.MYSTERY_BOX);
					break;
				case BlockSettings.BRICK:
					blocks[i][j] = new Block(BlockSettings.BRICK);
					break;
				case BlockSettings.LEFT_GOOMBA:
					goombas.add(new Goomba(j));
					blocks[i][j] = new Block(BlockSettings.EMPTY);
					break;
				case BlockSettings.MUSHROOM:
					mushrooms.add(new Mushroom(j, i));
					blocks[i][j] = new Block(BlockSettings.MUSHROOM);
					break;
				default:
					if (i == map.length-1 && map[i][j] == BlockSettings.SKY) {
						blocks[i][j] = new Block(BlockSettings.VOID);
					}
					else 
						blocks[i][j] = new Block(BlockSettings.EMPTY);							
					break;
				}
			}
		}
	}
	
	
	public ArrayList<Pair> getCollisionBlock() {
		ArrayList<Pair> collisionBlocks = new ArrayList<Pair>();
		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks[i].length; j++) {
				if (blocks[i][j].getType() == BlockSettings.MYSTERY_BOX || blocks[i][j].getType() == BlockSettings.BRICK) {
					collisionBlocks.add(new Pair(i, j));
				}
			}
		}
		return collisionBlocks;
	}
	
	
	public boolean collision(KeyEvent e) {
		if (blocks[Mario.getInstance().y / MapSettings.BLOCK_SIZE][Mario.getInstance().x / MapSettings.BLOCK_SIZE + 1].getType() !=  BlockSettings.EMPTY && blocks[Mario.getInstance().y / MapSettings.BLOCK_SIZE][Mario.getInstance().x / MapSettings.BLOCK_SIZE + 1].getType() !=  BlockSettings.MUSHROOM && e.getKeyCode() == KeyEvent.VK_RIGHT || Mario.getInstance().dead) {
			return true;
		}
		if (Mario.getInstance().x > MapSettings.BLOCK_SIZE && blocks[Mario.getInstance().y / MapSettings.BLOCK_SIZE][Mario.getInstance().x / MapSettings.BLOCK_SIZE].getType() != BlockSettings.EMPTY && blocks[Mario.getInstance().y / MapSettings.BLOCK_SIZE][Mario.getInstance().x / MapSettings.BLOCK_SIZE].getType() !=  BlockSettings.MUSHROOM && e.getKeyCode() == KeyEvent.VK_LEFT || Mario.getInstance().dead) {
			return true;
		}
		return false;
	}
	
	public boolean canMoveLeft() {
		if(Mario.getInstance().x - Mario.SPEED >= 0 && Mario.getInstance().x - Mario.SPEED > MapView.getInstance().getLeftBound() && Mario.getInstance().x != 9950) {
			return true;
		}
		return false;
	}
	
	public void moveRight() {
		if(Mario.getInstance().x + Mario.SPEED <= 199 * MapSettings.BLOCK_SIZE) {
			Mario.getInstance().x += Mario.SPEED;
			Mario.getInstance().direction = 1;
			Mario.getInstance().moving = true;
		}
		if (Mario.getInstance().y != (MapSettings.ROW - 2) * MapSettings.BLOCK_SIZE && blocks[Mario.getInstance().y / MapSettings.BLOCK_SIZE + 1][Mario.getInstance().x / MapSettings.BLOCK_SIZE].getType() == BlockSettings.EMPTY && !Mario.getInstance().jumping) {
			Mario.getInstance().direction = 1;
			Mario.getInstance().moving = true;
			Mario.getInstance().falling = true;
			for(int i = 0; i < MapSettings.ROW * MapSettings.BLOCK_SIZE; i++) {
				if (blocks[Mario.getInstance().y / MapSettings.BLOCK_SIZE + 1][Mario.getInstance().x / MapSettings.BLOCK_SIZE].getType() != BlockSettings.EMPTY) {
					break;
				}
				Mario.getInstance().y += 5;
			}
			Mario.getInstance().falling = false;
		}
		if (blocks[Mario.getInstance().y / MapSettings.BLOCK_SIZE + 1][Mario.getInstance().x / MapSettings.BLOCK_SIZE].getType() == BlockSettings.VOID) {
			Game.getInstance().deadMario();
		}
	}
	
	public void moveLeft() {
		Mario.getInstance().x -= Mario.SPEED;
		Mario.getInstance().direction = -1;
		Mario.getInstance().moving = true;
		if (Mario.getInstance().y != (MapSettings.ROW - 2) * MapSettings.BLOCK_SIZE && blocks[Mario.getInstance().y / MapSettings.BLOCK_SIZE + 1][Mario.getInstance().x / MapSettings.BLOCK_SIZE].getType() == BlockSettings.EMPTY && !Mario.getInstance().jumping) {
			Mario.getInstance().falling = true;
			for(int i = 0; i < MapSettings.ROW * MapSettings.BLOCK_SIZE; i++) {
				if (blocks[Mario.getInstance().y / MapSettings.BLOCK_SIZE + 1][Mario.getInstance().x / MapSettings.BLOCK_SIZE].getType() != BlockSettings.EMPTY) {
					break;
				}
				Mario.getInstance().y += 5;
			}
			Mario.getInstance().falling = false;
		}
		if (blocks[Mario.getInstance().y / MapSettings.BLOCK_SIZE + 1][Mario.getInstance().x / MapSettings.BLOCK_SIZE + 1].getType() == BlockSettings.VOID) {
			Game.getInstance().deadMario();
		}
	}
	
	public void moveGoomba() {
		for (int i = 0; i < goombas.size(); i++) {
			if(goombas.get(i).direction == 1 && blocks[12][goombas.get(i).x / MapSettings.BLOCK_SIZE + 1].getType() != BlockSettings.EMPTY)
				goombas.get(i).direction = -1;
			else if(goombas.get(i).direction == 1 && blocks[12][goombas.get(i).x / MapSettings.BLOCK_SIZE + 1].getType() == BlockSettings.EMPTY)
				goombas.get(i).x += 10;
			else if(goombas.get(i).x > MapSettings.BLOCK_SIZE && goombas.get(i).direction == -1 && blocks[12][goombas.get(i).x / MapSettings.BLOCK_SIZE -1].getType() != BlockSettings.EMPTY)
				goombas.get(i).direction = 1;
			else if(goombas.get(i).x > MapSettings.BLOCK_SIZE && goombas.get(i).direction == -1 && blocks[12][goombas.get(i).x / MapSettings.BLOCK_SIZE -1].getType() == BlockSettings.EMPTY && goombas.get(i).x > 10)
				goombas.get(i).x -= 10;		
		}
	}
	
	public  void moveMushroom(int i) {
		if (mushrooms.get(i).direction == 1 && blocks[mushrooms.get(i).y / MapSettings.BLOCK_SIZE + 1][mushrooms.get(i).x / MapSettings.BLOCK_SIZE].getType() == BlockSettings.EMPTY) {
			mushrooms.get(i).y = (MapSettings.ROW - 2) * MapSettings.BLOCK_SIZE;
		}
		else if(mushrooms.get(i).direction == 1 && blocks[mushrooms.get(i).y / MapSettings.BLOCK_SIZE][mushrooms.get(i).x / MapSettings.BLOCK_SIZE + 1].getType() != BlockSettings.EMPTY)
			mushrooms.get(i).direction = -1;
		else if(mushrooms.get(i).direction == 1 && blocks[mushrooms.get(i).y / MapSettings.BLOCK_SIZE][mushrooms.get(i).x / MapSettings.BLOCK_SIZE + 1].getType() == BlockSettings.EMPTY)
			mushrooms.get(i).x += Goomba.SPEED;
		else if(mushrooms.get(i).x > MapSettings.BLOCK_SIZE && mushrooms.get(i).direction == -1 && blocks[mushrooms.get(i).y / MapSettings.BLOCK_SIZE][mushrooms.get(i).x / MapSettings.BLOCK_SIZE -1].getType() != BlockSettings.EMPTY)
			mushrooms.get(i).direction = 1;
		else if(mushrooms.get(i).x > MapSettings.BLOCK_SIZE && mushrooms.get(i).direction == -1 && blocks[mushrooms.get(i).y / MapSettings.BLOCK_SIZE][mushrooms.get(i).x / MapSettings.BLOCK_SIZE -1].getType() == BlockSettings.EMPTY && mushrooms.get(i).x > 10) {
			mushrooms.get(i).x -= Goomba.SPEED;
			
		}
	}
	
	public  void jump() {
		SoundManager.getInstance().jumpSound();
		for(int i = 0; i < 300; i++) {
			if (Mario.getInstance().y > 0)
				Mario.getInstance().y--;
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (blocks[Mario.getInstance().y / MapSettings.BLOCK_SIZE][Mario.getInstance().x / MapSettings.BLOCK_SIZE].getType() != BlockSettings.EMPTY || blocks[Mario.getInstance().y / MapSettings.BLOCK_SIZE][(Mario.getInstance().x + 30) / MapSettings.BLOCK_SIZE].getType() != BlockSettings.EMPTY) {
				if (blocks[Mario.getInstance().y / MapSettings.BLOCK_SIZE][Mario.getInstance().x / MapSettings.BLOCK_SIZE].getType() == BlockSettings.BRICK) {
					blocks[Mario.getInstance().y / MapSettings.BLOCK_SIZE][Mario.getInstance().x / MapSettings.BLOCK_SIZE].setType(BlockSettings.EMPTY);
					brickToBreak = new Pair(Mario.getInstance().y / MapSettings.BLOCK_SIZE, Mario.getInstance().x / MapSettings.BLOCK_SIZE);
					SoundManager.getInstance().brick();
					brickBroken = true;
				}
				else if (blocks[Mario.getInstance().y / MapSettings.BLOCK_SIZE][(Mario.getInstance().x + 30) / MapSettings.BLOCK_SIZE].getType() == BlockSettings.BRICK) {
					blocks[Mario.getInstance().y / MapSettings.BLOCK_SIZE][(Mario.getInstance().x + 30) / MapSettings.BLOCK_SIZE].setType(BlockSettings.EMPTY);
					brickToBreak = new Pair(Mario.getInstance().y / MapSettings.BLOCK_SIZE, Mario.getInstance().x / MapSettings.BLOCK_SIZE);
					SoundManager.getInstance().brick();
					brickBroken = true;
				}
				if (blocks[Mario.getInstance().y / MapSettings.BLOCK_SIZE][Mario.getInstance().x / MapSettings.BLOCK_SIZE].getType() == BlockSettings.MYSTERY_BOX) {
					blocks[Mario.getInstance().y / MapSettings.BLOCK_SIZE][Mario.getInstance().x / MapSettings.BLOCK_SIZE].setType(BlockSettings.BROKEN_MYSTERY);
					map[Mario.getInstance().y / MapSettings.BLOCK_SIZE][Mario.getInstance().x / MapSettings.BLOCK_SIZE] = BlockSettings.BROKEN_MYSTERY;
					if (blocks[Mario.getInstance().y / MapSettings.BLOCK_SIZE - 1][Mario.getInstance().x / MapSettings.BLOCK_SIZE].getType() == BlockSettings.MUSHROOM) {
						for (int index = 0; index < mushrooms.size(); index++) {
							if (mushrooms.get(index).x / MapSettings.BLOCK_SIZE == Mario.getInstance().x / MapSettings.BLOCK_SIZE) {
								mushrooms.get(index).visible = true;
							}
						}
					}
				}
				else if (blocks[Mario.getInstance().y / MapSettings.BLOCK_SIZE][(Mario.getInstance().x + 30)/ MapSettings.BLOCK_SIZE].getType() == BlockSettings.MYSTERY_BOX) {
					blocks[Mario.getInstance().y / MapSettings.BLOCK_SIZE][(Mario.getInstance().x + 30) / MapSettings.BLOCK_SIZE].setType(BlockSettings.BROKEN_MYSTERY);
					map[Mario.getInstance().y / MapSettings.BLOCK_SIZE][(Mario.getInstance().x + 30) / MapSettings.BLOCK_SIZE] = BlockSettings.BROKEN_MYSTERY;
					if (blocks[Mario.getInstance().y / MapSettings.BLOCK_SIZE - 1][(Mario.getInstance().x + 30) / MapSettings.BLOCK_SIZE].getType() == BlockSettings.MUSHROOM) {
						for (int index = 0; index < mushrooms.size(); index++) {
							if (mushrooms.get(index).x / MapSettings.BLOCK_SIZE == (Mario.getInstance().x + 30) / MapSettings.BLOCK_SIZE) {
								mushrooms.get(index).visible = true;
							}
						}
					}
				}
				break;
			}
		}
		for(int i = 0; i < MapSettings.ROW * MapSettings.BLOCK_SIZE; i++) {
			Mario.getInstance().y++;
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (blocks[Mario.getInstance().y / MapSettings.BLOCK_SIZE + 1][Mario.getInstance().x / MapSettings.BLOCK_SIZE].getType() != BlockSettings.EMPTY && blocks[Mario.getInstance().y / MapSettings.BLOCK_SIZE + 1][Mario.getInstance().x / MapSettings.BLOCK_SIZE].getType() != BlockSettings.MUSHROOM) {
				break;
			}
 			if (blocks[Mario.getInstance().y / MapSettings.BLOCK_SIZE + 1][(Mario.getInstance().x + 30) / MapSettings.BLOCK_SIZE].getType() != BlockSettings.EMPTY && blocks[Mario.getInstance().y / MapSettings.BLOCK_SIZE + 1][(Mario.getInstance().x + 30) / MapSettings.BLOCK_SIZE].getType() != BlockSettings.MUSHROOM) {
 				break;
 			}
 			for (int goombaIndex = 0; goombaIndex < goombas.size(); goombaIndex++) {
 				if ((goombas.get(goombaIndex).x >= Mario.getInstance().x && goombas.get(goombaIndex).x - Mario.getInstance().x <= MapSettings.BLOCK_SIZE) && goombas.get(goombaIndex).y == Mario.getInstance().y + (MapSettings.BLOCK_SIZE / 2)) {
 					SoundManager.getInstance().smashedGoomba();
 					goombas.get(goombaIndex).smashed = true;
 				}
 				else if ((Mario.getInstance().x > goombas.get(goombaIndex).x && Mario.getInstance().x - goombas.get(goombaIndex).x <= MapSettings.BLOCK_SIZE) && goombas.get(goombaIndex).y == Mario.getInstance().y + (MapSettings.BLOCK_SIZE / 2)) {
 					SoundManager.getInstance().smashedGoomba();
 					goombas.get(goombaIndex).smashed = true;
 				}
 			}
 			
		}
		if (blocks[Mario.getInstance().y / MapSettings.BLOCK_SIZE + 1][Mario.getInstance().x / MapSettings.BLOCK_SIZE].getType() == BlockSettings.VOID) {
			Game.getInstance().deadMario();
		}
	 }
	
	public void deadMario() {
		MapView.getInstance().interrompiThreads();
		SoundManager.getInstance().die();
		GameSettings.life--;
		if (GameSettings.life == 0) {
			GameSettings.life = 3;
			GameSettings.level = 1;
			Game.getInstance().reset();
			Mario.getInstance().reset();
			MapView.getInstance().reset();
			MainFrame.getInstance().setStartScene();
		}
		else {
			Game.getInstance().reset();
			Mario.getInstance().reset();
			MapView.getInstance().reset();
			MainFrame.getInstance().setLevelTransitionScene();
		}
	}
	
	public void updateLevel() {
		if (GameSettings.level == 1) {
			GameSettings.level++;
			Game.getInstance().reset();
			Mario.getInstance().reset();
			MapView.getInstance().reset();
			MainFrame.getInstance().setLevelTransitionScene();
		}
		else {
			GameSettings.level = 1;
			GameSettings.life = 3;
			Game.getInstance().reset();
			Mario.getInstance().reset();
			MapView.getInstance().reset();
			MainFrame.getInstance().setStartScene();
		}
	}
	
	public Block[][] getBlocks() {
		return blocks;
	}
	
	public Integer[][] getMap() {
		return map;
	}
	
	public static Game getInstance() {
		if(game == null) {
			game = new Game();
		}
		return game;
	}
	
	public ArrayList<Goomba> getGoombas() {
		return goombas;
	}
	
	public ArrayList<Mushroom> getMushroom() {
		return mushrooms;
	}
	
}