package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JPanel;

import entity.Player;
import states.GameStateManager;

/**
 * This class represents a GamePanel. 
 * The GamePanel is responsible to update, render and draw game object.
 * The plan is to handle all game logic in one separate class.
 * @author Gustav Hultgren
 */
public class GamePanel extends JPanel implements Runnable, KeyListener {

	private static final long serialVersionUID = 8281174180155475994L;

	// Dimensions.s
	public static int WIDTH = 700;
	public static int HEIGHT = 700;

	// Game thread.
	private Thread thread;
	private boolean running;
	private int FPS = 60;
	private long targetTime = 1000 / FPS;

	// Image.
	private BufferedImage image;
	private Graphics2D g;
	private Font HUD_FONT;
	private Random rand = new Random();

	private Player player;

	// Game state manager.
	private GameStateManager gsm;

	/**
	 * Creates a GamePanel-object. 
	 */
	public GamePanel() {
		super();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));

		setFocusable(true);
		requestFocus();
	}

	// FUNCTIONS

	public void addNotify() {
		super.addNotify();
		if (thread == null) {
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}

	public void init() {
		image = new BufferedImage(
				WIDTH, HEIGHT,
				BufferedImage.TYPE_INT_RGB
				);
		g = (Graphics2D) image.getGraphics();
		running = true;
		gsm = new GameStateManager();
	}

	public void run() {

		init();

		long start;
		long elapsed;
		long wait;

		// game loop
		while(running) {

			start = System.nanoTime();

			update();
			draw();
			drawToScreen();

			elapsed = System.nanoTime() - start;

			wait = targetTime - elapsed / 1000000;
			if(wait < 0) wait = 5;

			try {
				Thread.sleep(wait);
			}
			catch(Exception e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * This method is updating all game objects. Basically creates animation.
	 */
	private void update() {
		gsm.update();
	}

	/**
	 * This method is rendering all game objects. Basically it draws the player,
	 * enemy/enemies, enemy bombs etc. 
	 */
	private void draw() {
		gsm.draw(g);
	}

	private void drawToScreen() {
		Graphics g2 = this.getGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
	}

	public void setRunning(boolean b) {
		running = b;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		gsm.keyPressed(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		gsm.keyReleased(e.getKeyCode());
	}
}
