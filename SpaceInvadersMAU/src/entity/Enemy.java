package entity;


import java.awt.Rectangle;

import main.GamePanel;
import states.PlayingState;

/**
 * This class represents a enemy. 
 * A enemy moves left and right. Each time one enemy hits a "wall" 
 * all the enemies moves down 15 pixels.
 * @author Gustav Hultgren
 */
public class Enemy {

	//FIELDS
	private int x;
	private int y;
	private int r;

	private double dx;
	private double speed;

	private boolean dead;

	//This variables is used for enemy firing.
	private boolean firing;
	private long firingTimer;
	private long firingDelay;

	/**
	 * When a Enemy-object is created it gets a x-value and y-value. 
	 * The firing delay is also set.
	 * @param x - where the Enemy is created on x-axis.
	 * @param y - where the Enemy is created on x-axis.
	 */
	public Enemy(int x, int y, int firingDelay) {
		this.x = x;
		this.y = y;
		r = 15;

		dx = speed;

		dead = false;

		firing = true;
		firingTimer = System.nanoTime();
		this.firingDelay = firingDelay;
	}

	//FUNCTIONS
	public int getX() { return x; }
	public int getY() { return y; }
	public int getR() { return r;	 }

	public void setY(int y) { this.y = y; }

	public boolean isDead() { return dead; }

	public void hit() { dead = true; }

	//Used to handle player - enemy collision.
	public Rectangle getBounds() {
		return new Rectangle(x, y, 20, 20);
	}

	/**
	 * This method is called in class GamePanel to update the enemy.
	 * @param direction - sets the direction of enemy. + for right and - for left.
	 * @param isShooter
	 */
	public void act(int direction, boolean isShooter) {
		this.x += direction;

		if(firing) {
			long elapsed = (System.nanoTime() - firingTimer) / 1000000;
			if(elapsed > firingDelay) {

				firingTimer = System.nanoTime();
				if (isShooter) {
					//Adding enemy bombs to list which is then  onto the panel.

					PlayingState.bombs.add(new EnemyBomb(270, x, y));
				}
			}
		}
	}
}