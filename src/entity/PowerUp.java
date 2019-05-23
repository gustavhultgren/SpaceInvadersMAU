package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * This class represents a Power Up. A Power Up can be dropped by a dead enemy.
 * If the player catches the power up, then it gives the player specific
 * benefits in the game such as more score or extra lives.
 * 
 * @author Gustav Hultgren
 */
public class PowerUp extends Entity {

	private int type;

	private BufferedImage powerUpImage;
	public static int HEART = 1;
	public static int SCORE = 2;
	public static int RAYGUN = 3;
	public static int SHIELD = 4;

	public PowerUp(int x, int y, int r, double speed, int type) {
		super(x, y, r, speed);
		this.type = type;

		init(type);
	}

	/**
	 * Type HEART = +1 life 
	 * Type SCORE = +50 score
	 * Type RAYGUN = Super weapon
	 * Type SHIELD = shield that protects player
	 */
	public void init(int type) {
		if (type == HEART) {
			try {
				powerUpImage = ImageIO.read(new File("resources/images/heart.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (type == SCORE) {
			try {
				powerUpImage = ImageIO.read(new File("resources/images/powerUp_Score.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if(type == RAYGUN) {
			try {
				powerUpImage = ImageIO.read(new File("resources/images/raygun.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if(type == SHIELD) {
			try {
				powerUpImage = ImageIO.read(new File("resources/images/powerUp_Shield_drop.png"));
			} catch (IOException e) {
				// TODO: handle exception
			}
		}
	}

	public int getType() {
		return type;
	}

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, 2 * r, 2 * r);
	}
	public void update() {
		y += speed;
	}

	//Draw method for different PowerUps.
	public void draw(Graphics2D g) {
			g.drawImage(powerUpImage, x, y, 50, 50, null);
	}
	
	//Draw method for RayGun and Shield.
	public void draw(Graphics2D g, int x, int y, int width, int height) {
			g.drawImage(powerUpImage, x, y, 50, 50, null);
	}

}
