package supermario.util;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class SpriteSheetCutter {
	
	// classe che serve per tagliare i tiles da un tilesheet
	
	private BufferedImage img;
	private String path;
	private int row;
	private int col;
	private ArrayList<BufferedImage> tiles;
	private int tileWidth; // dimensione in pixel di ogni tile del thilesheet
	private int tileHeight;
	
	
	public SpriteSheetCutter(String path, int row, int col, int tileWidth, int tileHeight) {
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.row = row;
		this.col = col;
		this.path = path;
		tiles = new ArrayList<BufferedImage>();
		
		try {
			img = ImageIO.read(getClass().getResourceAsStream(path));
		} catch (IOException e) {
			System.out.println("Tilesheet image not found!");
		}
		
		int width = img.getWidth() / col;
		int height = img.getHeight() / row;
		
		for (int y = 0; y < img.getHeight(); y += height) {
			for (int x = 0; x < img.getWidth(); x += width) {
				BufferedImage subImage = img.getSubimage(x, y, tileWidth, tileHeight);
				tiles.add(subImage);
			}
		}
	}
	
	
	
	public ArrayList<BufferedImage> getTiles() {
		return tiles;
	}
	
}