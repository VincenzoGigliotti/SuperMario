package supermario.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JLabel;

public class StartMenuLabel extends JLabel {
	private static final long serialVersionUID = 7020254966159799815L;
	
	private Font superMarioFont;
	
	public StartMenuLabel(String text) {
		try {
			 InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("supermario/resources/startmenu/SuperMarioFont.ttf");
			 superMarioFont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(20f);
		} catch (IOException | FontFormatException e) {
			System.out.println("Font not found!");
		}
		this.setForeground(Color.WHITE);
		this.setFont(superMarioFont);
		this.setText(text);
	}
}