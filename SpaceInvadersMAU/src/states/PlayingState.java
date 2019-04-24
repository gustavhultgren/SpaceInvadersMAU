package states;
import tileMap.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import javax.imageio.ImageIO;

import entity.Enemy;
import entity.EnemyBomb;
import entity.Missile;
import entity.Player;
import main.GamePanel;
import tileMap.MenuBackground;

public class PlayingState extends GameState {

	private BufferedImage image;
	private Graphics2D g;
	private Random rand = new Random();
	private int nbr = 0;
	private boolean paused;
	private String score;
	private String instruction = "Press ESC to resume";
	private String gameOver = "GAME PAUSED";
	private int textLength;
	
	// Entity
	public static LinkedList<LinkedList<Enemy>> enemies;
	public static ArrayList<Missile> missiles;
	public static LinkedList<EnemyBomb> bombs;
	// ToDo: Ta bort denna listan och f�r varje enemy l�ngst ut: rita bomb.

	// Images
	private BufferedImage heartImage;
	private MenuBackground bg;
	
	
	public PlayingState(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}

	@Override
	public void init() {
		
		
		try {
			bg = new MenuBackground("/images/retrospaces.png", 1.0);
			bg.setVector(-0.5, 0);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();

		player = new Player(PLAYER_INIT_X, PLAYER_INIT_Y);

		enemies = new LinkedList<LinkedList<Enemy>>();
		// Adding enemies to the list and sets each enemies X and Y-value so it looks
		// good.
		for (int i = 0; i < 2; i++) {
			LinkedList<Enemy> row;
			enemies.add(row = new LinkedList<Enemy>());
			for (int j = 0; j < 4; j++) {
				Enemy enemy = new Enemy(ENEMY_INIT_X + 40 * j, ENEMY_INIT_Y + 40 * i, gsm.getDifficulty());
				row.add(enemy);
			}
		}

		missiles = new ArrayList<Missile>();
		bombs = new LinkedList<EnemyBomb>();

		try {
			heartImage = ImageIO.read(new File("resources/images/heart.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void update() {
		bg.update();
		
		while (paused) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
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
		 * This section is written by Tom Eriksson and Gustav Hultgren. This specific
		 * section ends where: "-------..." is.
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
					for (int g = 0; g < enemies.get(h).size(); g++) {
						if (h > counter && g == selectedColumn) {
							counter = h;
						}
					}
				}

				if (j == selectedColumn && i == counter) {
					isShooter = true;
					enemies.get(i).get(j).act(ENEMY_DIRECTION, isShooter);
					isShooter = false;
				} else {
					enemies.get(i).get(j).act(ENEMY_DIRECTION, isShooter);
				}

				int x = enemies.get(i).get(j).getX();

				if (x >= WIDTH - 23 && ENEMY_DIRECTION != -1) {
					ENEMY_DIRECTION = -1;

					for (int h = 0; h < enemies.size(); h++) {
						Iterator<Enemy> i1 = enemies.get(h).iterator();
						while (i1.hasNext()) {
							Enemy e = (Enemy) i1.next();
							e.setY(e.getY() + 15);
						}
					}

				}
				if (x <= 3 && ENEMY_DIRECTION != 1) {
					ENEMY_DIRECTION = 1;

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
		// ----------------------------------------------------

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
						nbr++;
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

		// Check for dead player:
		if (player.isDead()) {
			gsm.setState(1);
		}

		if (nbr == 8) {
			gsm.setHigherDifficulty();
			gsm.setState(1);

		}
	}

	@Override
	public void draw(Graphics2D g) {
		bg.draw(g);
//		g.setColor(Color.black);
//		g.fillRect(0, 0, WIDTH, HEIGHT);

		g.setColor(Color.GRAY.darker());
		g.setStroke(new BasicStroke(2));
		g.drawLine(10, 75, WIDTH - 10, 75);
		g.setStroke(new BasicStroke(1));

		g.setColor(Color.GREEN);
		g.setStroke(new BasicStroke(3));
		g.drawLine(10, GROUND, WIDTH - 10, GROUND);
		g.setStroke(new BasicStroke(1));

		player.draw(g);

		for (LinkedList<Enemy> list : enemies) {
			for (Enemy elem : list) {
				g.setColor(Color.GRAY);
				g.fillRect(elem.getX(), elem.getY(), 20, 20);
			}
		}

		for (int i = 0; i < missiles.size(); i++) {
			missiles.get(i).draw(g);
		}

		for (int i = 0; i < bombs.size(); i++) {
			bombs.get(i).draw(g);
		}

		// Draw Score:
		g.setColor(Color.WHITE);
		g.setFont(font);
		g.drawString("SCORE:", GamePanel.WIDTH / 8, 50);
		g.setColor(Color.GREEN);
		g.drawString("" + player.getScore(), 230, 50);

		// Draw Lives:
		g.setColor(Color.WHITE);
		g.drawString("LIVES:", 400, 50);
		g.setColor(Color.GREEN);
		for (int i = 0; i < player.getLives(); i++) {
			g.drawImage(heartImage, 545 + (40 * i), 25, null);
		}

		if (paused) {
			drawMenu(g);
		}
	}
	
	public void drawMenu(Graphics2D g) {
		score = player.getScore() + "";
		g.setColor(new Color(0, 0, 0, 70));
		g.fillRect(0, 0, 700, 700);
		g.setColor(Color.WHITE);
		textLength = (int) g.getFontMetrics().getStringBounds(gameOver, g).getWidth();
		g.drawString(gameOver, (700 - textLength) / 2, (700 / 2) - 150);
		String finalScore = "CURRENT SCORE: ";
		textLength = (int) g.getFontMetrics().getStringBounds(finalScore, g).getWidth();
		g.drawString(finalScore, (700 - textLength) / 2, (700 / 2));
		g.setColor(Color.GREEN);
		g.drawString(score, 510, (700 / 2));
		textLength = (int) g.getFontMetrics().getStringBounds(instruction, g).getWidth();
		g.drawString(instruction, (700 - textLength) / 2, (900 / 2));
	}

	public void pause() {
		if (!paused) {
			paused = true;

		} else {
			paused = false;
		}
	}

	/**
	 * The keyPressed and keyReleased-method is responsible to handle key events to
	 * make the player move and fire missiles.
	 */
	public void keyPressed(int key) {
		if (key == KeyEvent.VK_LEFT)
			player.setLeft(true);
		if (key == KeyEvent.VK_RIGHT)
			player.setRight(true);
		if (key == KeyEvent.VK_Z)
			player.setFiring(true);
		if (key == KeyEvent.VK_ESCAPE)
			pause();
		if (key == KeyEvent.VK_E && paused)
			gsm.setState(GameStateManager.MENUSTATE);
	}

	public void keyReleased(int key) {
		if (key == KeyEvent.VK_LEFT)
			player.setLeft(false);
		if (key == KeyEvent.VK_RIGHT)
			player.setRight(false);
		if (key == KeyEvent.VK_Z)
			player.setFiring(false);
	}

}