package entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class PowerUpText extends Entity {
	
	private long time;
	private String string;
	
	private long start;
	
	private Color textColor;

	public PowerUpText(int x, int y, int r, double speed, long time, String string) {
		super(x, y, r, speed);
		this.time = time;
		this.string = string;
		start = System.nanoTime();
		
		textColor = Color.WHITE;
	}
	
	public boolean update() {
		long elapsedTime = (System.nanoTime() - start) / 1000000;
		if(elapsedTime > time) {
			return true;
		}
		return false;
	}
	
	public void draw(Graphics2D g) {
		g.setFont(new Font("Century Gothic", Font.BOLD, 18));
		g.setColor(textColor);
		g.drawString(string, (int)x, (int)y);
		
	}

}
