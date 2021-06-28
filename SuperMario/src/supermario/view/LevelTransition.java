package supermario.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import supermario.config.GameSettings;

public class LevelTransition extends JPanel{
	private static final long serialVersionUID = 493182643832674007L;
	
	private static LevelTransition levelTransition = null;

	private LevelTransition() {
		BorderLayout layout = new BorderLayout();
		this.setLayout(layout);
		this.add(setTopPanel(), BorderLayout.PAGE_START);
		this.add(setCenterPanel(), BorderLayout.CENTER);
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
		topPanel.setBackground(Color.BLACK);
		return topPanel;
	}
	
	private JPanel setCenterPanel() {
		JPanel centerPanel = new JPanel();
		JLabel marioLabel = new JLabel();
		StartMenuLabel world = new StartMenuLabel("WORLD "+ GameSettings.world + "-" + GameSettings.level);
		centerPanel.add(world, BorderLayout.PAGE_START);
		try {
			Image img = ImageIO.read(getClass().getResourceAsStream("/supermario/resources/startmenu/FrameIcon.png"));	
			img = img.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
			marioLabel.setIcon(new ImageIcon(img));
		} catch (Exception e) {
			e.getStackTrace();
		}
		add(centerPanel, marioLabel);
		centerPanel.setBackground(Color.BLACK);
		return centerPanel;
	}
	
	public void add(JPanel centerPanel, JLabel marioLabel) {
		centerPanel.add(Box.createVerticalStrut(300));
		centerPanel.add(Box.createHorizontalStrut(6000));
		centerPanel.add(marioLabel);
		StartMenuLabel life = new StartMenuLabel("X  \t" + GameSettings.life);
		centerPanel.add(life);
	}
	
	public void reset() {
		levelTransition = new LevelTransition();
	}
	
	public static LevelTransition getInstance() {
		if(levelTransition == null)
			levelTransition = new LevelTransition();
		return levelTransition;
	}
}
