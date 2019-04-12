package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import main.GamePanel;

/**
 * This class represents a missile that the player launches to shoot down
 * enemies. 
 * @author Gustav Hultgren
 *
 */
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

	/**
	 * Creates a missile.
	 * @param angle - sets the angle that the bomb is heading.
	 * @param x - sets where the bomb starts on the x-axis.
	 * @param y - sets where the bomb starts on the y-axis.
	 */
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
	
	//This method is called in class GamePanel. It makes the missile move.
	public boolean update() {
		x += dx;
		y += dy;

		if(x < -r || x > Game.WIDTH + r || y < -r || y > Game.HEIGHT + r) {
			return true;
		}
		return false;

	}
	
	//This method is called in class GamePanel. It draws the missile.
	public void draw(Graphics2D g) {
		g.setColor(bulletColor);
		g.fillOval((int)(x-r), (int)(y-r), 2 * r, 6 * r);

	}
}
