package supermario.view;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.Timer;

import supermario.SuperMarioMain;
import supermario.config.WindowSettings;
import supermario.model.Game;
import supermario.sound.SoundManager;
import supermario.thread.CoinsLoop;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = -3775309771448663213L;
	
	private static MainFrame mainFrame = null;
	private Container container;
	private CardLayout layout;
	private Thread thread;
	private int counter;
	private Timer timer;
	private int delay;
	
	private MainFrame() {
		super("Super Mario");
		this.counter = 3;
		this.delay = 1000;
		this.container = getContentPane();
		this.layout = new CardLayout();
		this.container.setLayout(layout);
		try {
            URL resource = SuperMarioMain.class.getResource("/supermario/resources/startmenu/frameIcon.png");
            BufferedImage image = ImageIO.read(resource);
            this.setIconImage(image);
        } catch (IOException e) {
            System.out.println("Frame icon not found!");
        }
		this.setSize(WindowSettings.WIDTH_WINDOW_SIZE, WindowSettings.HEIGHT_WINDOW_SIZE);
		this.setResizable(false);
		this.setLocation(WindowSettings.START_WIDTH_LOCATION, WindowSettings.START_HEIGHT_LOCATION);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	// Metodi per la corretta gestione del thread
	public void run() {
		if(thread != null)
			thread.start();
	}
		
	public void stop() {
		if(thread != null) {
			thread.interrupt();
			thread = null;
		}
	}
	
	public void setStartScene() {
		stop();
		StartMenu.getInstance().setFocusable(true);
		container.add("start", StartMenu.getInstance());
		layout.show(container, "start");
		StartMenu.getInstance().requestFocusInWindow();
		this.revalidate();
		thread = new Thread(new CoinsLoop());
		run();
	}
	
	public void setLevelTransitionScene() {
		stop();
		if (!container.isAncestorOf(LevelTransition.getInstance())) {
			container.add("levelTransition", LevelTransition.getInstance());
		}
		else {
			container.remove(LevelTransition.getInstance());
			LevelTransition.getInstance().reset();
			container.add("levelTransition", LevelTransition.getInstance());
		}
		layout.show(container, "levelTransition");
		this.revalidate();

		ActionListener action = new ActionListener() {   
            @Override
            public void actionPerformed(ActionEvent event) {
                if(counter == 0) {
                    timer.stop();  
                    setMapScene();
                }
                else
                	counter--;
                }
        };
        counter = 3;
        timer = new Timer(delay, action);
        timer.setInitialDelay(0);
        timer.start();
	}
	
	public void setMapScene() {
		stop();
		JScrollPane map = new JScrollPane(MapView.getInstance());
		map.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		map.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		if (!container.isAncestorOf(map)) {
			container.add("map", map);
		}
		layout.show(container, "map");
		MapView.getInstance().requestFocusInWindow();
		this.revalidate();
		Game.getInstance();
		MapView.getInstance().riprendiThreads();
		SoundManager.getInstance().startMusic();
	}
	
	public static MainFrame getInstance() {
		if (mainFrame == null)
			mainFrame = new MainFrame();
		return mainFrame;
	}

}