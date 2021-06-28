package supermario.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Scrollable;

import supermario.config.BlockSettings;
import supermario.config.MapSettings;
import supermario.controller.GameController;
import supermario.model.Game;
import supermario.model.Mario;
import supermario.util.Pair;
import supermario.util.SpriteSheetCutter;


public class MapView extends JPanel implements Scrollable {
	private static final long serialVersionUID = -1890012796100197407L;
	private static MapView level1 = null;
	
	private Integer[][] blocks;
	private ArrayList<BufferedImage> tiles;
	private int i;
	private int j;
	private int leftBound;
	private int index;
	private ArrayList<Pair> collisionBlocks;
	private Integer[][] map;
	boolean enemySmashed = false;

	private MapView() {
		this.index = BlockSettings.MYSTERY_BOX;
		this.collisionBlocks = Game.getInstance().getCollisionBlock();
		this.map = Game.getInstance().getMap();
		blocks = Game.getInstance().getMap(); // leggiamo la mappa dal game cosï¿½ possiamo modificarla quando avvengono collisioni	
		
		SpriteSheetCutter cutter = new SpriteSheetCutter("/supermario/resources/map/MapTilesheet.png", 28, 33, 16, 16);
		tiles = cutter.getTiles();
		cutter = new SpriteSheetCutter("/supermario/resources/sprites/brickAnimation.png", 1, 4, 105, 105);
		tiles.addAll(cutter.getTiles());
		cutter = new SpriteSheetCutter("/supermario/resources/sprites/goomba.png", 1, 3, 16, 16);
		tiles.addAll(cutter.getTiles());
		cutter = new SpriteSheetCutter("/supermario/resources/sprites/mushroom.png", 1, 1, 16, 16);
		tiles.addAll(cutter.getTiles());
		this.setPreferredSize(new Dimension(12500, 700));
		this.setBackground(new Color(99, 173, 255));
		this.addKeyListener(GameController.getInstance(this));
		this.setFocusable(true);
		this.leftBound = 0;
	}
	
	public void reset() {
		this.level1 = new MapView();
	}
	
	public synchronized void riprendiThreads() {
		GameController.getInstance(this).riprendiThreads();
	}
	
	public void interrompiThreads() {
		GameController.getInstance(this).interrompiThreads();
	}
	
	public static MapView getInstance() {
		if (level1 == null) {
			level1 = new MapView();
		}
		return level1;
	}
	
	public void breakBlocks(int x, int y) {
		if (blocks[x][y] != BlockSettings.BRICK)
			y++;
		for (int i = BlockSettings.FIRST_BROKEN_BRICK; i < BlockSettings.LAST_FINAL_BRICK; i++) {
			blocks[x][y] = i;
			try {
				Thread.sleep(80);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			MapView.getInstance().repaint();
		}
		blocks[x][y] = BlockSettings.SKY;
	}
	
	public void updateBlocks() {
		for (int i = 0; i < collisionBlocks.size(); i++) {
			if (map[collisionBlocks.get(i).getX()][collisionBlocks.get(i).getY()] >= BlockSettings.MYSTERY_BOX && map[collisionBlocks.get(i).getX()][collisionBlocks.get(i).getY()] < BlockSettings.BROKEN_MYSTERY) {
				blocks[collisionBlocks.get(i).getX()][collisionBlocks.get(i).getY()] = index;
			}
		}
		index++;
		if (index == BlockSettings.BROKEN_MYSTERY)
			index = BlockSettings.MYSTERY_BOX;
	}

	public void updateGoomba() {
		for (int i = 0; i < Game.getInstance().getGoombas().size(); i++) {
			if (!Game.getInstance().getGoombas().get(i).smashed)
				Game.getInstance().getGoombas().get(i).status++;
			if (Game.getInstance().getGoombas().get(i).status == BlockSettings.DEAD_GOOMBA && !Game.getInstance().getGoombas().get(i).smashed)
				Game.getInstance().getGoombas().get(i).status = BlockSettings.RIGHT_GOOMBA;
			
		}
	}
	
	@Override
	protected synchronized void paintComponent(Graphics g) {
		super.paintComponent(g);
		// serve per disegnare un nemico dopo che e' stato schiacciato e non farlo sparire subito
		if (enemySmashed == true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			enemySmashed = false;
		}
		i = 0;
		for(int y = 0; y < MapSettings.ROW * MapSettings.BLOCK_SIZE; y += MapSettings.BLOCK_SIZE) { // righe
			for(int x = 0; x < MapSettings.COL * MapSettings.BLOCK_SIZE; x += MapSettings.BLOCK_SIZE) { // colonne
				if (map[i][j] < BlockSettings.LEFT_GOOMBA) {
					g.drawImage(tiles.get(blocks[i][j]), x, y, MapSettings.BLOCK_SIZE, MapSettings.BLOCK_SIZE, null);
				}
				j++;
				if(j == MapSettings.COL)
					j = 0;
			}
			i++;
		}
		g.drawImage(MarioView.getInstance().getCurrentMarioImage(), Mario.getInstance().x, Mario.getInstance().y, MapSettings.BLOCK_SIZE, MapSettings.BLOCK_SIZE, null);
		for (int i = 0; i < Game.getInstance().getGoombas().size(); i++) {
			if (Game.getInstance().getGoombas().get(i).status != BlockSettings.DEAD_GOOMBA) {
				if (Game.getInstance().getGoombas().get(i).smashed) {
					Game.getInstance().getGoombas().get(i).status = BlockSettings.DEAD_GOOMBA;
					enemySmashed = true;
				}
				g.drawImage(tiles.get(Game.getInstance().getGoombas().get(i).status), Game.getInstance().getGoombas().get(i).x, Game.getInstance().getGoombas().get(i).y, MapSettings.BLOCK_SIZE, MapSettings.BLOCK_SIZE, null);
			}
		}
		for (int i = 0; i < Game.getInstance().getMushroom().size(); i++) {
			if (Game.getInstance().getMushroom().get(i).visible) {
				g.drawImage(tiles.get(BlockSettings.MUSHROOM), Game.getInstance().getMushroom().get(i).x, Game.getInstance().getMushroom().get(i).y, MapSettings.BLOCK_SIZE, MapSettings.BLOCK_SIZE, null);
			}
		}
	}

	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) { // ci sono più visibleRect per adattare il tutto alla finestra di ogni sistema operativo.
		if((Mario.getInstance().x == (int) visibleRect.getCenterX()-1 || Mario.getInstance().x == (int) visibleRect.getCenterX() || Mario.getInstance().x == (int) visibleRect.getCenterX()+2 | Mario.getInstance().x == (int) visibleRect.getCenterX()+3) && direction > 0) {
			leftBound = (int) visibleRect.getBounds().x;
			return Mario.SPEED;
		}
		return 0;
	}
	
	@Override
	public boolean getScrollableTracksViewportWidth() { return false; }

	@Override
	public boolean getScrollableTracksViewportHeight() { return true; }
	
	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) { return 0; }
	@Override
	public Dimension getPreferredScrollableViewportSize() { return null; }

	public int getLeftBound() {
		return leftBound;
	}
}