package entity;

import java.awt.Graphics2D;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import states.BossState;

/**
 * 
 * @author Joakim Thiman
 */

public class BossRolf extends Entity {

	private boolean dead = true;
	private boolean firing;

	private int health = 2;
	private int frameCounter = 0;

	private long firingTimer;
	private long firingDelay;

	private BufferedImage boss1Image;
	private Rectangle rectangle;

	/**
	 * When a Boss1-object is created it gets a x-value and y-value. The firing
	 * delay is also set.
	 * 
	 * @param x - where the Boss1 is created on x-axis.
	 * @param y - where the Boss1 is created on y-axis.
	 */
	public BossRolf(int x, int y, int r, double speed, int firingDelay) {
		super(x, y, r, speed);

		dead = false;
		firing = true;
		firingTimer = System.nanoTime();
		this.firingDelay = firingDelay;

		try {
			boss1Image = ImageIO.read(getClass().getResourceAsStream("/images/rolfskepp.png"));

		} catch (Exception e) {

		}
		rectangle = new Rectangle(x, y, boss1Image.getWidth() - 80, 165);
	}

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
		this.firingDelay = y;
	}

	public int getLives() {
		return health;
	}

	
	public boolean isDead() {
		if(health < 1 ||health == 0 );
		return dead;
		}
		


	public void hit() {
		health--;
	}

	// Handles Player-Enemy Collision
	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, 200, 200);
	}

	/**
	 * This method is called in class GamePanel to update the Boss.
	 * 
	 * @param direction - sets the direction of the Boss. + for right and - for
	 *                  left.
	 * @param isShooter
	 */
	public void update(int direction, boolean isShooter, Player player) {
		this.x += direction;
		rectangle.setBounds(x, y, boss1Image.getWidth() - 80, 165);



		if (firing) {
			long elapsed = (System.nanoTime() - firingTimer) / 1000000;
			if (elapsed > firingDelay) {

				firingTimer = System.nanoTime();
				if (isShooter) {

					// Bomb aimed against player. Written by Gustav Hultgren and Tom Eriksson.
					if (frameCounter >= 150) {
						double diffX = x - player.getX();
						double diffY = y - player.getY();
						double result = diffX / diffY;

						double angle = 360 - (90 + Math.toDegrees(Math.atan(result)));

						BossState.bombs.add(new EnemyBomb(angle, (int) rectangle.getCenterX(), y + 150, 3, 6));
						BossState.bombs.add(new EnemyBomb(angle + 5, (int) rectangle.getCenterX(), y + 150, 3, 6));
						BossState.bombs.add(new EnemyBomb(angle - 5, (int) rectangle.getCenterX(), y + 150, 3, 6));
						frameCounter = 0;
					}
				}
			}
		}
		frameCounter++;
	}

	public void draw(Graphics2D g) {
		if (!isDead())
			g.drawImage(boss1Image, null, (int) rectangle.getX() - 20, 10);
	}
}
