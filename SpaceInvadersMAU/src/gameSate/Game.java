package gameSate;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import entity.Enemy;
import entity.EnemyBomb;
import entity.Missile;
import entity.Player;

/**
 * This class represents a GamePanel. 
 * The GamePanel is responsible to update, render and draw game object.
 * The plan is to handle all game logic in one separate class.
 * @author Gustav Hultgren
 */
public class Game extends JPanel implements Runnable, KeyListener {

	private static final long serialVersionUID = 8281174180155475994L;

	// FIELDS
	public static int WIDTH = 700;
	public static int HEIGHT = 700;

	public static int PLAYER_INIT_X = WIDTH / 2 - 20;
	public static int PLAYER_INIT_Y = 610;
	public static int PLAYER_WIDTH = 30;
	public static int PLAYER_HEIGHT = 30;

	public static int ENEMY_INIT_X = 100;
	public static int ENEMY_INIT_Y = 80;
	private int direction = -1;

	private static int GROUND = 650;

	private Thread thread;
	private boolean running;

	private BufferedImage image;
	private Graphics2D g;
	private Font HUD_FONT;
	private Random rand = new Random();

	private int FPS = 60;
	private double averageFPS;

	// Entity
	public static Player player;
	public static LinkedList<LinkedList<Enemy>> enemies;
	public static ArrayList<Missile> missiles;
	public static LinkedList<EnemyBomb> bombs; // ToDo: Ta bort denna listan och f�r varje enemy l�ngst ut: rita bomb.

	// Images
	private BufferedImage heartImage;
	private BufferedImage dimg;

	/**
	 * Creates a GamePanel-object. 
	 */
	public Game() {
		super();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));

		initFonts();
		initImages();

		setFocusable(true);
		requestFocus();
	}

	// FUNCTIONS

	public void addNotify() {
		super.addNotify();
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
		addKeyListener(this);
	}

	public void run() {
		running = true;

		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();

		player = new Player(PLAYER_INIT_X, PLAYER_INIT_Y);

		enemies = new LinkedList<LinkedList<Enemy>>();
		//Adding enemies to the list and sets each enemies X and Y-value so it looks good.
		for (int i = 0; i < 4; i++) {
			LinkedList<Enemy> row;
			enemies.add(row = new LinkedList<Enemy>());
			for (int j = 0; j < 8; j++) {
				Enemy enemy = new Enemy(ENEMY_INIT_X + 40 * j, ENEMY_INIT_Y + 40 * i);
				row.add(enemy);
			}
		}

		missiles = new ArrayList<Missile>();
		bombs = new LinkedList<EnemyBomb>();

		long startTime;
		long URDTimeMillis;
		long waitTime;
		long totalTime = 0;

		int frameCount = 0;
		int maxFrameCount = 30;

		long targetTime = 1000 / FPS;

		/**
		 * This is the game loop. It updates, render and draws all the game objects.
		 */
		while (running) {

			startTime = System.nanoTime();

			gameUpdate();
			gameRender();
			gameDraw();

			URDTimeMillis = (System.nanoTime() - startTime) / 1000000;

			waitTime = targetTime - URDTimeMillis;

			try {
				if (waitTime < 0) {
					waitTime = 2;
				}
				Thread.sleep(waitTime);
			} catch (Exception e) {
				e.printStackTrace();
			}

			totalTime += System.nanoTime() - startTime;
			frameCount++;
			if (frameCount == maxFrameCount) {
				averageFPS = 1000.0 / (((double) totalTime / frameCount) / 1000000);
				frameCount = 0;
				totalTime = 0;
			}
		}

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.WHITE);
		g.setFont(HUD_FONT);
		String gameOver = "GAME OVER";
		int length = (int) g.getFontMetrics().getStringBounds(gameOver, g).getWidth();
		g.drawString(gameOver, (WIDTH - length) / 2, (HEIGHT / 2) - 150);
		String finalScore = "FINAL SCORE: ";
		length = (int) g.getFontMetrics().getStringBounds(finalScore, g).getWidth();
		g.drawString(finalScore, (WIDTH - length) / 2, (HEIGHT / 2));
		g.setColor(Color.GREEN);
		g.drawString("" + player.getScore(), 490, (HEIGHT / 2));
		gameDraw();
	}

	private void initImages() {
		try {
			heartImage = ImageIO.read(new File("src/resources/heart.png"));
			resize(heartImage, 32, 32);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private BufferedImage resize(BufferedImage img, int width, int height) {
		Image tmp = heartImage.getScaledInstance(height, width, Image.SCALE_SMOOTH);
		dimg = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();
		return dimg;
	}

	/**
	 * This method allows the usage of custom fonts.
	 */
	private void initFonts() {
		try {
			HUD_FONT = Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/ARCADE_I.TTF")).deriveFont(25f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/ARCADE_I.TTF")));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method is updating all game objects. Basically creates animation.
	 */
	private void gameUpdate() {

		// Updating player:
		player.update();

		// Updating missiles:
		for (int i = 0; i < missiles.size(); i++) {
			boolean remove = missiles.get(i).update();
			if (remove) {
				missiles.remove(i);
				i--;
			}
		}

		// Updating enemy bombs:
		for (int i = 0; i < bombs.size(); i++) {
			boolean remove = bombs.get(i).update();
			if (remove) {
				bombs.remove(i);
				i--;
			}
		}

		/**
		 * This section is written by Tom Eriksson and Gustav Hultgren.
		 * This specific section ends where: "-------..." is.
		 */
		int biggestRow = 0;
		for (LinkedList<Enemy> list : enemies) {
			if (list.size() > biggestRow) {
				biggestRow = list.size();
			}
		}

		int selectedColumn = rand.nextInt(biggestRow);


		boolean isShooter = false;

		for (int i = 0; i < enemies.size(); i++) {
			for (int j = 0; j < enemies.get(i).size(); j++) {

				int counter = 0;
				for (int h = 0; h < enemies.size(); h++) {
					for(int g = 0; g < enemies.get(h).size(); g++) {
						if (h > counter && g == selectedColumn) {
							counter = h;
						}
					}
				}

				if(j == selectedColumn && i == counter) {
					isShooter = true;
					enemies.get(i).get(j).act(direction, isShooter);
					isShooter = false;
				}else {
					enemies.get(i).get(j).act(direction, isShooter);
				}

				int x = enemies.get(i).get(j).getX();

				if (x >= WIDTH - 23 && direction != -1) {
					direction = -1;

					for (int h = 0; h < enemies.size(); h++) {
						Iterator<Enemy> i1 = enemies.get(h).iterator();
						while (i1.hasNext()) {
							Enemy e = (Enemy) i1.next();
							e.setY(e.getY() + 15);
						}
					}

				}
				if (x <= 3 && direction != 1) {
					direction = 1;

					for (int h = 0; h < enemies.size(); h++) {
						Iterator<Enemy> i2 = enemies.get(h).iterator();
						while (i2.hasNext()) {
							Enemy e = (Enemy) i2.next();
							e.setY(e.getY() + 15);
						}
					}

				}
			}
		}
		//----------------------------------------------------

		// Check for player missile - enemy collision:
		for (int i = 0; i < missiles.size(); i++) {
			Missile b = missiles.get(i);
			for (int j = 0; j < enemies.size(); j++) {
				for (int h = 0; h < enemies.get(j).size(); h++) {
					Enemy e = enemies.get(j).get(h);
					if (b.getBounds().intersects(e.getBounds())) {
						missiles.remove(i);
						LinkedList<Enemy> temp = enemies.get(j);
						temp.remove(h);
						enemies.set(j, temp);
						player.addScore(10);
					}
				}
			}
		}

		// Check for enemy bombs - player collision:
		for (int i = 0; i < bombs.size(); i++) {
			EnemyBomb eb = bombs.get(i);
			if (eb.getBounds().intersects(player.getBounds())) {
				bombs.remove(i);
				player.loseLife();
			}
		}

		// Check for dead enemies:
		for (int i = 0; i < enemies.size(); i++) {
			for (int j = 0; j < enemies.get(i).size(); j++) {
				if (enemies.get(i).get(j).isDead()) {
					LinkedList<Enemy> temp = enemies.get(i);
					temp.remove(j);
					enemies.set(i, temp);
					j--;
				}
			}
		}

		// Check for dead enemies:
		if (player.isDead()) {
			running = false;
		}

	}

	/**
	 * This method is rendering all game objects. Basically it draws the player,
	 * enemy/enemies, enemy bombs etc. 
	 */
	private void gameRender() {
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		g.setColor(Color.GRAY.darker());
		g.setStroke(new BasicStroke(2));
		g.drawLine(10, 75, WIDTH - 10, 75);
		g.setStroke(new BasicStroke(1));

		g.setColor(Color.GREEN);
		g.setStroke(new BasicStroke(3));
		g.drawLine(10, GROUND, WIDTH - 10, GROUND);
		g.setStroke(new BasicStroke(1));

		player.draw(g);

		drawEnemies(g);

		for (int i = 0; i < missiles.size(); i++) {
			missiles.get(i).draw(g);
		}

		for (int i = 0; i < bombs.size(); i++) {
			bombs.get(i).draw(g);
		}

		// Draw Score:
		g.setColor(Color.WHITE);
		g.setFont(HUD_FONT);
		g.drawString("SCORE:", Game.WIDTH / 8, 50);
		g.setColor(Color.GREEN);
		g.drawString("" + player.getScore(), 230, 50);

		// Draw Lives:
		g.setColor(Color.WHITE);
		g.drawString("LIVES:", 400, 50);
		g.setColor(Color.GREEN);
		for (int i = 0; i < player.getLives(); i++) {
			g.drawImage(dimg, 545 + (40 * i), 25, null);
		}

	}

	private void gameDraw() {
		Graphics g2 = this.getGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();

	}

	public void drawEnemies(Graphics2D g) {
		for (LinkedList<Enemy> list : enemies) {
			for (Enemy elem : list) {
				g.setColor(Color.GRAY);
				g.fillRect(elem.getX(), elem.getY(), 20, 20);
			}
		}
	}

	/**
	 * The keyPressed and keyReleased-method is responsible 
	 * to handle key events to make the player move and fire missiles.
	 */
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent key) {
		int keyCode = key.getKeyCode();
		if (keyCode == KeyEvent.VK_LEFT)
			player.setLeft(true);
		if (keyCode == KeyEvent.VK_RIGHT)
			player.setRight(true);
		if (keyCode == KeyEvent.VK_Z)
			player.setFiring(true);
	}

	@Override
	public void keyReleased(KeyEvent key) {
		int keyCode = key.getKeyCode();
		if (keyCode == KeyEvent.VK_LEFT)
			player.setLeft(false);
		if (keyCode == KeyEvent.VK_RIGHT)
			player.setRight(false);
		if (keyCode == KeyEvent.VK_Z)
			player.setFiring(false);
	}

}
