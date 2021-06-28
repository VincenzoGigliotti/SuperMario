package supermario.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import supermario.config.GameSettings;
import supermario.config.StartMenuSettings;
import supermario.controller.StartMenuController;
import supermario.util.SpriteSheetCutter;


public class StartMenu extends JPanel {
	private static final long serialVersionUID = -566050395288357062L;
	
	private static StartMenu startMenu = null;
	private ArrayList<BufferedImage> coinsImage;
	private int coinsIndex;
	

	private StartMenu() {
		BorderLayout layout = new BorderLayout();
		this.setLayout(layout);
		this.add(setTopPanel(), BorderLayout.PAGE_START);
		this.add(setCenterPanel(), BorderLayout.CENTER);
		this.add(setDownPanel(), BorderLayout.PAGE_END);
    	this.addKeyListener(new StartMenuController(this));
    	this.setFocusable(true);
		this.coinsIndex = 0;
	}
	
	private JPanel setCenterPanel() {
		JPanel centerPanel = new JPanel();
		JLabel title = new JLabel();
		try {
			Image img = ImageIO.read(getClass().getResourceAsStream("/supermario/resources/startmenu/title.png"));	
			img = img.getScaledInstance(StartMenuSettings.TITLE_WIDTH, StartMenuSettings.TITLE_HEIGHT, Image.SCALE_SMOOTH);
			title.setIcon(new ImageIcon(img));
		} catch (IOException e) {
			System.out.println("Title image not found!");
		}
		centerPanel.setBackground(new Color(155, 145, 255));
		centerPanel.add(title);
		centerPanel.add(Box.createHorizontalStrut(5100));
		
		return centerPanel;
	}
	
	private JPanel setTopPanel() {
		JPanel topPanel = new JPanel();
		ArrayList<StartMenuLabel> labels = new ArrayList<StartMenuLabel>();
		StartMenuLabel mario = new StartMenuLabel("MARIO");
		labels.add(mario);
		StartMenuLabel coins = new StartMenuLabel("x00");
		labels.add(coins);
		StartMenuLabel world = new StartMenuLabel("WORLD " + GameSettings.world + "-" + GameSettings.level);
		labels.add(world);
		StartMenuLabel time = new StartMenuLabel("TIME");
		labels.add(time);
		for (int i = 0; i < labels.size(); i++) {
			topPanel.add(labels.get(i));
			if (i < labels.size()-1)
				topPanel.add(Box.createHorizontalStrut(100));
		}
		topPanel.setBackground(new Color(155, 145, 255));
		String coinsAnimationPath = "/supermario/resources/startmenu/coinsAnimation.png";
		SpriteSheetCutter coinsAnimation = new SpriteSheetCutter(coinsAnimationPath, 1, 3, 16, 16);
		coinsImage = coinsAnimation.getTiles();
		return topPanel;
	}
	
	private JLabel setDownPanel() {
		Image img;
		JLabel downLabel = null;
		try {
			img = ImageIO.read(getClass().getResourceAsStream("/supermario/resources/startmenu/downImage.png"));
			img = img.getScaledInstance(StartMenuSettings.DOWN_IMAGE_WIDTH, StartMenuSettings.DOWN_IMAGE_HEIGHT, Image.SCALE_SMOOTH);
			downLabel = new JLabel(new ImageIcon(img));

		} catch (IOException e) {
			System.out.println("Down image not found!");
		}
		return downLabel;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(coinsImage.get(coinsIndex), 290, 2, 35, 35, null);
	}
	
	public void update() {
		if(coinsIndex == 2)
			coinsIndex = 0;
		else
			coinsIndex++;
		repaint();
	}

	public static StartMenu getInstance() {
		if (startMenu == null) {
			startMenu = new StartMenu();
		}
		return startMenu;
	}
	
}