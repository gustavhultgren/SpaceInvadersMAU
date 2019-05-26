package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import main.GamePanel;

/**
 * This class represents a bomb that one enemy in the "squad" drops
 * every 2.5 seconds.
 * @author Gustav Hultgren
 *
 */
public class EnemyBomb extends Entity {

	private double dx;
	private double dy;
	private double rad;

	private Color bulletColor;

	/**
	 * Creates a EnemyBomb-object.
	 * @param angle - sets the angle that the bomb is heading.
	 * @param x - sets where the bomb starts on the x-axis.
	 * @param y - sets where the bomb starts on the y-axis.
	 */
	public EnemyBomb(double angle, int x, int y, int r, double speed) {
		super(x, y, r, speed);

		rad = Math.toRadians(angle);
		dx = Math.cos(rad) * speed;
		dy = Math.sin(rad) * speed;

		bulletColor = Color.GREEN;

	}
	//FUNCTIONS
	public int getX() { return x; }
	public int getY() { return y; }
	public int getR() { return r; }

	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, 2 * r, 6 * r);
	}
	
	//This method is called in class GamePanel. It makes the bomb move.
	public boolean update() {
		x -= dx;
		y -= dy;

		if(x < -r || x > GamePanel.WIDTH + r || y < -r || y > GamePanel.HEIGHT + r) {
			return true;
		}
		return false;

	}
	
	//This method is called in class GamePanel. It draws the bomb.
	public void draw(Graphics2D g) {
		g.setColor(bulletColor);
		g.fillOval((int)(x-r) + 10, (int)(y-r) + 10, 2 * r, 6 * r);
	}
}