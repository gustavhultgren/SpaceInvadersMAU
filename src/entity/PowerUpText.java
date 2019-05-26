package entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

/**
 * This class represents a PowerUp text that pops up and shows what kind of
 * power up the player have catched / activated.
 * 
 * @author Gustav Hultgren
 */
public class PowerUpText extends Entity {

	private long time;
	private String string;

	private long start;

	private Font font;
	private Color textColor;

	public PowerUpText(int x, int y, int r, double speed, long time, String string) {
		super(x, y, r, speed);
		this.time = time;
		this.string = string;
		start = System.nanoTime();

		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/ARCADE_I.TTF")).deriveFont(18f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/ARCADE_I.TTF")));
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		textColor = Color.WHITE;
	}

	public boolean update() {
		long elapsedTime = (System.nanoTime() - start) / 1000000;
		if (elapsedTime > time) {
			return true;
		}
		return false;
	}

	public void draw(Graphics2D g) {
		g.setFont(font);
		g.setColor(textColor);
		g.drawString(string, (int) x, (int) y);

	}

}
