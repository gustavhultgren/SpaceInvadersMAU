package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import states.PlayingState;

/**
 * This class represents a enemy. A enemy moves left and right. Each time one
 * enemy hits a "wall" all the enemies moves down 15 pixels.
 * 
 * @author Gustav Hultgren
 */
public class Enemy extends Entity {

	private boolean dead;

	// This variables is used for enemy firing.
	private boolean firing;
	private long firingTimer;
	private long firingDelay;

	private boolean slow;

	private BufferedImage spritesheet;
	private BufferedImage[] sprites;

	/**
	 * When a Enemy-object is created it gets a x-value and y-value. The firing
	 * delay is also set.
	 * 
	 * @param x - where the Enemy is created on x-axis.
	 * @param y - where the Enemy is created on x-axis.
	 */
	public Enemy(int x, int y, int r, double speed, int firingDelay) {
		super(x, y, r, speed);

		dead = false;

		firing = true;
		firingTimer = System.nanoTime();
		this.firingDelay = firingDelay;

		try {

			spritesheet = ImageIO.read(getClass().getResourceAsStream("/images/enemySprite.gif"));

			sprites = new BufferedImage[6];
			for (int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(i * 50, 0, 50, 32);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

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

	public void setY(int y) {
		this.y = y;
	}

	public boolean isDead() {
		return dead;
	}

	public void killed() {
		dead = true;
	}

	public void setSlow(boolean b) {
		slow = b;
	}

	// Used to handle player - enemy collision.
	public Rectangle getBounds() {
		return new Rectangle(x, y, 50, 32);
	}

	/**
	 * This method is called in class PlayingState to update the enemy.
	 * 
	 * @param direction - sets the direction of enemy. + for right and - for left.
	 * @param isShooter
	 */
	public void update(int direction, boolean isShooter) {
		if (slow) {
			this.x += direction * 0.3;
		} else {
			this.x += direction;
			if (firing) {
				long elapsed = (System.nanoTime() - firingTimer) / 1000000;
				if (elapsed > firingDelay) {

					firingTimer = System.nanoTime();
					if (isShooter) {
						// Adding enemy bombs to list which is then drawn onto the panel.

						PlayingState.bombs.add(new EnemyBomb(270, x, y, 3, 6));
					}
				}
			}
		}
	}

	public void draw(Graphics2D g, int frame, int enemyType) {

		if (frame <= 30) {
			g.drawImage(sprites[enemyType], null, x, y);
		} else if (frame > 30 && frame <= 60) {
			g.drawImage(sprites[enemyType + 1], null, x, y);

		}
	}
}