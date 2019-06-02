package tileMap;

/**
 *  Class that draws the moving background in all
 *  states. Speed, position and angle can be adjusted.
 *
 * @author Gustav Georgson
 */

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import main.GamePanel;
import java.awt.*;

public class MenuBackground {

	private BufferedImage image;
	private double x;
	private double y;
	private double dx;
	private double dy;
	private double moveScale;

	
	/**
	 *  Constructor that takes a string and a double as 
	 *  parameters. The string is a resources location.
	 *  And the ms is the update rate/delay.
	 *  
	 * @param s
	 * @param ms
	 */
	
	public MenuBackground(String s, double ms) {
		try {

			image = ImageIO.read(getClass().getResourceAsStream(s));
			moveScale = ms;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets position of image.
	 * 
	 * @param x
	 * @param y
	 */

	public void setPosition(double x, double y) {
		this.x = (x * moveScale) % GamePanel.WIDTH;

		this.y = (y * moveScale) % GamePanel.HEIGHT;

	}
	/**
	 * sets the direction of the image.
	 * 
	 * @param dx
	 * @param dy
	 */
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}

	
	/**
	 * Updates the position of the image.
	 * 
	 */
	
	public void update() {
		x += dx;
		y += dy;
		setPosition(x, y);
	}
	
	/**
	 * Draws the image within the limits of the panel.
	 * 
	 */
	
	public void draw(Graphics2D g) {
		g.drawImage(image, (int) x, (int) y, null);
		
		if (x < GamePanel.WIDTH) {

			g.drawImage(image, (int) x + GamePanel.WIDTH, (int) y, null);
		}
		if (x > GamePanel.WIDTH) {

			g.drawImage(image, (int) x - GamePanel.WIDTH, (int) y, null);
		}
	}
}