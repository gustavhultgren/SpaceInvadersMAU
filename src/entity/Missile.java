package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

/**
 * This class represents a missile that the player launches to shoot down
 * enemies.
 * 
 * @author Gustav Hultgren
 */
public class Missile extends Entity {

	private double dx;
	private double dy;
	private double rad;

	private BufferedImage missileImage;
	private Color bulletColor;

	private boolean firingRaygun = false;

	/**
	 * Creates a missile.
	 * 
	 * @param angle - sets the angle that the bomb is heading.
	 * @param x     - sets where the bomb starts on the x-axis.
	 * @param y     - sets where the bomb starts on the y-axis.
	 */
	public Missile(double angle, int x, int y, int r, double speed, boolean raygun, Color c) {
		super(x, y, r, speed);

		rad = Math.toRadians(angle);
		dx = Math.cos(rad) * speed;
		dy = Math.sin(rad) * speed;

		try {
			missileImage = ImageIO.read(new File("resources/images/player_Missile.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		bulletColor = c;

		firingRaygun = raygun;
	}

	public void setColor(Color c) {
		bulletColor = c;
	}

	// FUNCTIONS
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}

	public int getR() {
		return r;
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, 32, 40);
	}

	// This method is called in class GamePanel. It makes the missile move.
	public boolean update() {
		x += dx;
		y += dy;

		if (x < -r || x > GamePanel.WIDTH + r || y < -r || y > GamePanel.HEIGHT + r) {
			return true;
		}
		return false;

	}

	// This method is called in class GamePanel. It draws the missile.
	public void draw(Graphics2D g) {
		if (firingRaygun) {
			g.setColor(bulletColor);
			g.fillRect(x - r, y - 80, 5, 80);
		} else {
			g.drawImage(missileImage, x - r, y - r, 32, 40, null);
		}
	}
}
