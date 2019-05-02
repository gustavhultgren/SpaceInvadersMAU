package entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * This class represents a Power Up.
 * A Power Up can be dropped by a dead enemy. If the player catches
 * the power up, then it gives the player specific benefits in the game 
 * such as more score or extra lives.
 * @author Gustav Hultgren
 */
public class PowerUp extends Entity {
	
	private int type;
	
	private Color powerUpColor;

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
			powerUpColor = Color.RED;
		}
		else if(type == 2) {
			powerUpColor = Color.GREEN;
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
		g.setColor(powerUpColor);
		g.fillRect((int)(x - r), (int)(y - r), 2 * r, 2 * r);
		
		g.setStroke(new BasicStroke(3));
		g.setColor(powerUpColor.darker());
		g.drawRect((int)(x - r), (int)(y - r), 2 * r, 2 * r);
		g.setStroke(new BasicStroke(1));
	}
	
}
