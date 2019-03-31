package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import main.GamePanel;

public class Missile {

	//FIELDS
	private double x;
	private double y;
	private int r;

	private double dx;
	private double dy;
	private double rad;
	private double speed;

	private Color bulletColor;

	public Missile(double angle, int x, int y) {
		this.x = x;
		this.y = y;
		r = 3;

		rad = Math.toRadians(angle);
		speed = 7;
		dx = Math.cos(rad) * speed;
		dy = Math.sin(rad) * speed;

		bulletColor = Color.GRAY;

	}
	//FUNCTIONS
	public double getX() { return x; }
	public double getY() { return y; }
	public double getR() { return r; }
	
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, 6, 18);
	}
	
	public boolean update() {
		x += dx;
		y += dy;

		if(x < -r || x > GamePanel.WIDTH + r || y < -r || y > GamePanel.HEIGHT + r) {
			return true;
		}
		return false;

	}

	public void draw(Graphics2D g) {
		g.setColor(bulletColor);
		g.fillOval((int)(x-r), (int)(y-r), 2 * r, 6 * r);

	}
}
