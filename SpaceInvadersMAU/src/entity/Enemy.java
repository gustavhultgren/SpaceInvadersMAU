package entity;

import java.awt.Rectangle;
import java.util.Random;

import main.GamePanel;

public class Enemy {

	//FIELDS
	private int x;
	private int y;
	private int r;

	private double dx;
	//	private double rad;
	private double speed;

	private boolean dead;

	private boolean firing;
	private long firingTimer;
	private long firingDelay;

	public Enemy(int x, int y) {
		this.x = x;
		this.y = y;
		r = 15;

		dx = speed;

		dead = false;

		firing = true;
		firingTimer = System.nanoTime();
		firingDelay = 2500;
	}

	//FUNCTIONS
	public int getX() { return x; }
	public int getY() { return y; }
	public int getR() { return r;	 }

	public void setY(int y) { this.y = y; }

	public boolean isDead() { return dead; }

	public void hit() { dead = true; }

	public Rectangle getBounds() {
		return new Rectangle(x, y, 20, 20);
	}

	public void act(int direction) {
		this.x += direction;

		if(firing) {
			long elapsed = (System.nanoTime() - firingTimer) / 1000000;
			if(elapsed > firingDelay) {
				
				firingTimer = System.nanoTime();
				GamePanel.bombs.add(new EnemyBomb(270, x, y));
			}
		}
	}
}