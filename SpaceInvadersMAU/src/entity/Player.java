package entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import entity.Missile;
import main.GamePanel;

public class Player {

	//FIELDS
	private int x;
	private int y;
	private int r;

	private int dx;
	private int speed;

	private boolean left;
	private boolean right;

	private boolean firing;
	private long firingTimer;
	private long firingDelay;

	private Color playerColor;
	
	private int score;
	private int lives;

	public Player(int x, int y) {
		this.x = x;
		this.y = y;
		r = 15;

		dx = 0;
		speed = 3;

		firing = false;
		firingTimer = System.nanoTime();
		firingDelay = 700;

		playerColor = Color.WHITE;
		
		score = 0;
		lives = 3;

	}

	public int getX() { return x; }
	public int getY() { return y; }
	public int getR() { return r; }

	public void setLeft(boolean b) { 
		left = b; 
	}
	public void setRight(boolean b) { 
		right = b; 
	}
	public void setFiring(boolean b) {
		firing = b; 
	}
	
	public int getScore() {
		return score;
	}
	
	public void addScore(int increment) {
		score += increment;
	}
	
	public int getLives() {
		return lives;
	}
	
	public void loseLife() {
		lives--;
	}
	
	public boolean isDead() {
		return lives <= 0;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(x - r, y - r, 2 * r, 2 * r);
		
	}

	public void update() {
		if(left) 
			dx = -speed;
		if(right) 
			dx = speed;

		x += dx;

		if(x < r) 
			x = r;
		if(y < r) 
			y = r;

		if(x > GamePanel.WIDTH - r) 
			x = GamePanel.WIDTH - r;

		dx = 0;

		if(firing) {
			long elapsed = (System.nanoTime() - firingTimer) / 1000000;
			if(elapsed > firingDelay) {
				firingTimer = System.nanoTime();
				GamePanel.missiles.add(new Missile(270, x, y));
			}
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(playerColor);
		g.fillRect(x - r, y - r, 2 * r, 2 * r);

		g.setStroke(new BasicStroke(3));
		g.setColor(playerColor.darker());
		g.drawRect(x - r, y - r, 2 * r, 2 * r);
		g.setStroke(new BasicStroke(1));
	}

}
