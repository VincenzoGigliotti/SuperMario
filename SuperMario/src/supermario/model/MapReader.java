package supermario.model;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import supermario.config.MapSettings;

public class MapReader {
	
	private Integer[][] blocks = new Integer[MapSettings.ROW][MapSettings.COL];
	private String path;
	
	public MapReader(String path) {
		this.path = path;
		int rowIndex = 0;
		try {
			BufferedReader in = new BufferedReader(new FileReader(getClass().getResource(path).getFile()));
			while(in.ready()) {
				String line = in.readLine();
				String[] elements = line.split(",");
				for (int j = 0; j < MapSettings.COL; j++) {
					blocks[rowIndex][j] = Integer.parseInt(elements[j]) - 1;
				}
				rowIndex++;
			}
			if (in != null) in.close();
		} catch (IOException e) {
			System.out.println("Map file not found!");
		}
	}
	
	public Integer[][] getBlocks() {
		return blocks;
	}
}