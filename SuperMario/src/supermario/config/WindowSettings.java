package supermario.config;

import java.awt.Dimension;
import java.awt.Toolkit;

public class WindowSettings {
	public final static int WIDTH_WINDOW_SIZE = 1000;
	public final static int HEIGHT_WINDOW_SIZE = 730;
	public final static Dimension DIM = Toolkit.getDefaultToolkit().getScreenSize();
	public final static int START_WIDTH_LOCATION = DIM.width / 2 - WIDTH_WINDOW_SIZE / 2;
	public final static int START_HEIGHT_LOCATION = DIM.height / 2 - HEIGHT_WINDOW_SIZE / 2;
	
}