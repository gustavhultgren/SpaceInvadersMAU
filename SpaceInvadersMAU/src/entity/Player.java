package entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import entity.Missile;
import main.GamePanel;
import states.PlayingState;

/**
 * This class represents a player.
 * A player can move left and right. A player can launch missiles to 
 * shoot down enemies. A player can loose lives and gain score.
 * @author Gustav Hultgren
 */
public class Player extends Entity {

	//FIELDS

	private double dx;

	private boolean left;
	private boolean right;

	private boolean firing;
	private long firingTimer;
	private long firingDelay;

	private Color playerColor;

	private int score;
	private int lives;

	/**
	 * Creates a player object.
	 * Sets the players speed to 3, firing delay to 700 milliseconds,
	 * color to white, score to 0 and lives to 3.
	 * @param x - where the player is created on the x-axis.
	 * @param y - where the player is created on the y-axis.
	 */
	public Player(int x, int y, int r, double speed) {
		super(x, y, r, speed);

		dx = 0;

		firing = false;
		firingTimer = System.nanoTime();
		firingDelay = 700;

		playerColor = Color.WHITE;

		score = 0;
		lives = 1;

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

	//This method is used to handle enemy bombs - player collision.
	public Rectangle getBounds() {
		return new Rectangle(x - r, y - r, 2 * r, 2 * r);
	}

	/**
	 * This method is called in class GamePanel. It makes the player move and 
	 * adding missiles to the list every time the player fires.
	 */
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

				PlayingState.missiles.add(new Missile(270, x, y, 3, 7));
			}
		}
	}

	//This method is called in the class GamePanel. It draws the player.
	public void draw(Graphics2D g) {
		g.setColor(playerColor);
		g.fillRect(x - r, y - r, 2 * r, 2 * r);

		g.setStroke(new BasicStroke(3));
		g.setColor(playerColor.darker());
		g.drawRect(x - r, y - r, 2 * r, 2 * r);
		g.setStroke(new BasicStroke(1));
	}

}
