package entity;

/**
 * This class is used as a Superclass. It contains some common variables and
 * methods that is used in most subclasses.
 * 
 * @author Gustav Hultgren
 */
public class Entity {

	protected int x;
	protected int y;
	protected int r;
	protected double speed;

	public Entity(int x, int y, int r, double speed) {
		this.x = x;
		this.y = y;
		this.r = r;
		this.speed = speed;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

}