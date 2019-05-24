package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.swing.Timer;
import states.Boss1State;

/**
 * 
 * @author Joakim Thiman
 */

public class Boss1 extends Entity{

//	public int health = 5;
	private boolean dead;
	private boolean firing;
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
	public Boss1(int x, int y, int r, double speed, int firingDelay) {
		super(x, y, r, speed);
		

		dead = false;
		firing = true;
		firingTimer = System.nanoTime();
		this.firingDelay = firingDelay;

		try {
			boss1Image = ImageIO.read(getClass().getResourceAsStream("/images/rolfskepp.png"));

		} catch (Exception e) {

		}
		rectangle = new Rectangle(x, y, boss1Image.getWidth()-80, 165);
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

//	public void killed() { 
//		dead = true; 
//		}

	public boolean isDead() {
		return dead;
	}

	//public void hit() { 
	//	health--; 
	//	} 

	// Handles Player-Enemy Collision
	public Rectangle getBounds() {
		return rectangle;
	}

	/**
	 * This method is called in class GamePanel to update the Boss.
	 * 
	 * @param direction - sets the direction of the Boss. + for right and - for left.
	 * @param isShooter
	 */
	public void update(int direction, boolean isShooter) {
		this.x += direction;
		rectangle.setBounds(x, y, boss1Image.getWidth()-80, 165);


		if(firing) {
			long elapsed = (System.nanoTime() - firingTimer) / 1000000;
			if(elapsed > firingDelay) {

				firingTimer = System.nanoTime();
				if (isShooter) {

//					//Adding enemy bombs to list which is then drawn onto the panel.
					Boss1State.bombs.add(new EnemyBomb(275, (int)rectangle.getCenterX()+10, y+150, 3, 6));
					Boss1State.bombs.add(new EnemyBomb(275,(int)rectangle.getCenterX(), y+150, 3, 6));
					Boss1State.bombs.add(new EnemyBomb(275, (int)rectangle.getCenterX() -10, y+150, 3, 6));


				}
			}
		}
	}
	public void draw(Graphics2D g) {
			if(!isDead())
				g.drawImage(boss1Image, null, (int)rectangle.getX()-20, 10);
//			System.out.println("IMG x: " + this.getX() + ", y: " + this.getY());
//			System.out.println("Rect x: " + rectangle.getCenterX() + "  y: " + rectangle.getCenterY());
	}
}

