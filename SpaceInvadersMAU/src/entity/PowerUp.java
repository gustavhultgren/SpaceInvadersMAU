package entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * This class represents a Power Up.
 * A Power Up can be dropped by a dead enemy. If the player catches
 * the power up, then it gives the player specific benefits in the game 
 * such as more score or extra lives.
 * @author Gustav Hultgren
 */
public class PowerUp extends Entity {
	
	private int type;
	
	private BufferedImage powerUpImage;

	public PowerUp(int x, int y, int r, double speed, int type) {
		super(x, y, r, speed);
		this.type = type;
		
		init(type);
	}
	
	/**
	 * Type 1 -- +1 life
	 * Type 2 -- +50 score
	 */
	public void init(int type) {
		if(type == 1) {
			try {
				powerUpImage = ImageIO.read(new File("resources/images/heart.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if(type == 2) {
			try {
				powerUpImage = ImageIO.read(new File("resources/images/powerUp_Score.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public int getType() {
		return type;
	}
	
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, 2 * r, 2 * r);
	}
	
	public void update() {
		y += speed;
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(powerUpImage, x, y, 26, 26, null);
	}
	
}
