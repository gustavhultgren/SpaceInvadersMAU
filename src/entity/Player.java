package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import main.GamePanel;
import states.BossState;
import states.GameStateManager;
import states.PlayingState;

/**
 * This class represents a player. A player can move left and right. A player
 * can launch missiles to shoot down enemies. A player can loose lives and gain
 * score.
 * 
 * @author Gustav Hultgren
 */
public class Player extends Entity {

	private double dx;

	private boolean left;
	private boolean right;

	private boolean firing;
	private long firingTimer;
	private long firingDelay;
	private boolean raygunActivated = false;
	private boolean shieldActivated = false;

	private int score;
	private int lives;

	private BufferedImage playerImage;
	private BufferedImage shieldImage;

	/**
	 * Creates a player object. Sets the players speed to 3, firing delay to 700
	 * milliseconds, color to white, score to 0 and lives to 3.
	 * 
	 * @param x - where the player is created on the x-axis.
	 * @param y - where the player is created on the y-axis.
	 */
	public Player(int x, int y, int r, double speed) {
		super(x, y, r, speed);

		dx = 0;

		firing = false;
		firingTimer = System.nanoTime();
		firingDelay = 700;

		score = 0;
		lives = 3;

		try {
			playerImage = ImageIO.read(new File("resources/images/playerImage.png"));
			shieldImage = ImageIO.read(new File("resources/images/playerShield.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void activateShield(LinkedList<PowerUp> list, int element) {
		if (shieldActivated == false) {
			shieldActivated = true;
			Timer timer = new Timer();
			TimerTask task = new TimerTask() {
				public void run() {
					shieldActivated = false;
					list.remove(element);
				}
			};
			timer.schedule(task, 7000);
		}
	}
	
	public void activateRaygun(LinkedList<PowerUp> list, int element) {
		if (raygunActivated == false) {
			raygunActivated = true;
			Timer timer = new Timer();
			TimerTask task = new TimerTask() {
				public void run() {
					raygunActivated = false;
					list.remove(element);
				}
			};
			timer.schedule(task, 3000);
		}
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

	public void setLeft(boolean b) {
		left = b;
	}

	public void setRight(boolean b) {
		right = b;
	}

	public void setFiring(boolean b) {
		firing = b;
	}
	public void shieldActivated(boolean b) {
		shieldActivated = b;
	}
	
	public void raygunActivated(boolean b) {
		shieldActivated = b;
	}

	public boolean getShieldStatus() {
		return shieldActivated;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void addScore(int increment) {
		score += increment;
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public void loseLife() {
		lives--;
	}

	public void addLife(int increment) {
		lives += increment;
	}

	public boolean isDead() {
		return lives <= 0;
	}

	public void setPlayerImage(int choice) {
		if (choice == 1) {
			try {
				playerImage = ImageIO.read(new File("resources/images/playerImage.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else if (choice == 2) {
			try {
				playerImage = ImageIO.read(new File("resources/images/playerImage2.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else if (choice == 3) {
			try {
				playerImage = ImageIO.read(new File("resources/images/player3Image.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (choice == 4) {
			try {
				playerImage = ImageIO.read(new File("resources/images/player4Image.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// This method is used to handle enemy bombs - player collision.
	public Rectangle getBounds() {
		if (shieldActivated) {
			return new Rectangle(x - 64, y - 64, 128, 128);
		}
		return new Rectangle(x - r, y - r, 2 * r, 2 * r);
	}

	/**
	 * This method is called in class GamePanel. It makes the player move and adding
	 * missiles to the list every time the player fires.
	 */
	public void update() {
		if (left)
			dx = -speed;
		if (right)
			dx = speed;

		x += dx;

		if (x < r)
			x = r;
		if (y < r)
			y = r;

		if (x > GamePanel.WIDTH - r)
			x = GamePanel.WIDTH - r;

		dx = 0;

		if (firing) {
			long elapsed = (System.nanoTime() - firingTimer) / 1000000;
			if (elapsed > firingDelay) {
				firingTimer = System.nanoTime();

				if (GameStateManager.CURRENTSTATE == GameStateManager.PLAYINGSTATE) {
					PlayingState.missiles.add(new Missile(270, x, y, 3, 12, false, Color.GREEN));
				} else {
					BossState.missiles.add(new Missile(270, x, y, 3, 12, false, Color.GREEN));

				}
			}
		}

		if (raygunActivated) {
			long elapsed = (System.nanoTime() - firingTimer) / 1000000;
			if (elapsed > firingDelay / 8) {
				firingTimer = System.nanoTime();

				PlayingState.missiles.add(new Missile(270, x, y, 5, 24, true, Color.GREEN));
			}
		}
	}

	// This method is called in the class GamePanel. It draws the player.
	public void draw(Graphics2D g) {
		if (shieldActivated) {
			g.drawImage(shieldImage, null, x - 64, y - 64);
		} else {
			g.drawImage(playerImage, x - 32, y - 32, 80, 80, null);
		}
	}

}
