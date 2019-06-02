package states;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import javax.imageio.ImageIO;

import audio.AudioPlayer;
import entity.Enemy;
import entity.EnemyBomb;
import entity.Missile;
import entity.Player;
import entity.PowerUp;
import entity.PowerUpText;
import entity.PurpleShip;
import main.GamePanel;
import tileMap.MenuBackground;

/**
 * This class represents the state where the actual game is running.
 * It is in this class that most game logic take place.
 * @author Gustav Hultgren, Tom Eriksson, Gustav Georgsson
 */
public class PlayingState extends GameState {

	private BufferedImage image;
	private Random rand = new Random();
	private int nbr = 0;
	private int nbrLevels = 0;
	private boolean paused;
	private Graphics2D g;

	private String score;
	private String instruction = "Press ESC to resume";
	private String gameOver = "GAME PAUSED";
	private int textLength;
	private int frameCounter = 0;
	private boolean bossActive = true;

	// Entity
	public static LinkedList<LinkedList<Enemy>> enemies;
	public static PurpleShip purpleShip;
	private LinkedList<LinkedList<Enemy>> newEnemies;
	public static LinkedList<Missile> missiles;
	public static LinkedList<EnemyBomb> bombs;
	public static LinkedList<PowerUp> powerUps;
	
	public static LinkedList<PowerUpText> powerUpTexts;

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
			bg = new MenuBackground("/images/playingBG.png", 1);
			bg.setVector(-0.4, 0);

			font = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/ARCADE_I.TTF")).deriveFont(25f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/ARCADE_I.TTF")));

			heartImage = ImageIO.read(new File("resources/images/heart.png"));

		} catch (Exception e) {
			e.printStackTrace();
		}

		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();

		enemies = new LinkedList<LinkedList<Enemy>>();

		/**
		 * Adding enemies to the list and sets each enemies X and Y-value so it looks
		 * good.
		 */
		enemies = new LinkedList<LinkedList<Enemy>>();
		for (int i = 0; i < 2; i++) {
			LinkedList<Enemy> row;
			enemies.add(row = new LinkedList<Enemy>());
			for (int j = 0; j < 3; j++) {
				Enemy enemy = new Enemy(ENEMY_INIT_X + 60 * j, ENEMY_INIT_Y + 50 * i, 1, 1, gsm.getDifficulty());
				row.add(enemy);
			}
		}

		purpleShip = new PurpleShip(ENEMY_INIT_X, ENEMY_INIT_Y, 1, 1, gsm.getDifficulty());

		newEnemies = repopulateEnemies();

		missiles = new LinkedList<Missile>();
		bombs = new LinkedList<EnemyBomb>();
		powerUps = new LinkedList<PowerUp>();
		
		powerUpTexts = new LinkedList<PowerUpText>();

		soundFX.put("click", new AudioPlayer("/music/sfx_click.mp3"));
		soundFX.put("enter", new AudioPlayer("/music/sfx_enter.mp3"));
		soundFX.put("win", new AudioPlayer("/music/sfx_win.mp3"));
		soundFX.put("gameOver", new AudioPlayer("/music/sfx_gameOver.mp3"));
		soundFX.put("enemyHit", new AudioPlayer("/music/sfx_enemyHit.mp3"));
		soundFX.put("collectPU", new AudioPlayer("/music/sfx_collectPU.mp3"));

	}

	@Override
	public void update() {

		while (paused) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		bg.update();

		// Updating player:
		GameStateManager.player.update();

		purpleShip.update(ENEMY_DIRECTION, false);

		// Updating missiles:
		for (int i = 0; i < missiles.size(); i++) {
			boolean remove = missiles.get(i).update();
			if (remove) {
				missiles.remove(i);
				i--;
				System.out.println("Funkar 1");
			}
		}

		// Updating PowerUps:
		for (int i = 0; i < powerUps.size(); i++) {
			powerUps.get(i).update();
			if (powerUps.get(i).getY() > HEIGHT) {
				powerUps.remove(i);
			}
		}

		// Updating PowerUpTexts:
		for (int i = 0; i < powerUpTexts.size(); i++) {
			boolean remove = powerUpTexts.get(i).update();
			if (remove) {
				powerUpTexts.remove(i);
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
					enemies.get(i).get(j).update(ENEMY_DIRECTION, isShooter);
					isShooter = false;
				} else {
					enemies.get(i).get(j).update(ENEMY_DIRECTION, isShooter);
				}

				int x = enemies.get(i).get(j).getX();

				if (x >= WIDTH - 50 && ENEMY_DIRECTION != -1) {
					ENEMY_DIRECTION = -1;

					for (int h = 0; h < enemies.size(); h++) {
						Iterator<Enemy> i1 = enemies.get(h).iterator();
						while (i1.hasNext()) {
							Enemy e = (Enemy) i1.next();
							e.setY(e.getY() + 30);
						}
					}

				}
				if (x <= 3 && ENEMY_DIRECTION != 1) {
					ENEMY_DIRECTION = 1;

					for (int h = 0; h < enemies.size(); h++) {
						Iterator<Enemy> i2 = enemies.get(h).iterator();

						while (i2.hasNext()) {

							Enemy e = (Enemy) i2.next();
							e.setY(e.getY() + 30);
						}
					}
				}
			}
		}

		// ----------------------------------------------------
		// checks if enemies touch player

		// Check for player missile - enemy collision:

		for (int i = 0; i < missiles.size(); i++) {
			Missile m = missiles.get(i);
			if (m.getBounds().intersects(purpleShip.getBounds())) {
				missiles.remove(i);
				purpleShip.killed();

				powerUps.add(new PowerUp(purpleShip.getX(), purpleShip.getY(), 25, 3.0, PowerUp.RAYGUN)); // Type 3

			}

			for (int j = 0; j < enemies.size(); j++) {
				for (int h = 0; h < enemies.get(j).size(); h++) {
					Enemy e = enemies.get(j).get(h);
					if (m.getBounds().intersects(e.getBounds())) {
						e.killed();
						missiles.remove(i);
						LinkedList<Enemy> temp = enemies.get(j);
						temp.remove(h);

						/**
						 * Type 1 -- +1 life (5%) Type 2 -- +50 score (10%) Type 3 -- Type 4 --
						 */

						double rand = Math.random();
						if (rand < 0.03) {
							powerUps.add(new PowerUp(e.getX(), e.getY(), 20, 3.0, PowerUp.HEART)); // Type 1
						} else if (rand <= 0.07) {
							powerUps.add(new PowerUp(e.getX(), e.getY(), 20, 3.0, PowerUp.SHIELD)); // Type 4
						} else if (rand < 0.15) {
							powerUps.add(new PowerUp(e.getX(), e.getY(), 20, 3.0, PowerUp.SCORE)); // Type 2
						}

						GameStateManager.player.addScore(100);

						enemies.set(j, temp);
						nbr++;
					}
				}
			}

			/////////////////////////////

			if (nbrLevels == 1) {
				gsm.setState(GameStateManager.BOSSTATE);

				nbrLevels = 0;
				////////////////////////////////////
			}
		}

		for (int i = 0; i < enemies.size(); i++) {
			for (int j = 0; j < enemies.get(i).size(); j++) {
				Enemy e = enemies.get(i).get(j);
				if (e.getY() >= 620) {
					gsm.setState(2);
				}
			}

		}

		// Check for enemy bombs - player collision:
		for (int i = 0; i < bombs.size(); i++) {
			EnemyBomb eb = bombs.get(i);
			if (eb.getBounds().intersects(GameStateManager.player.getBounds())) {
				bombs.remove(i);
				if (GameStateManager.player.getShieldStatus() == false) {
					GameStateManager.player.loseLife();
					if (GameStateManager.player.getLives() <= 0) {
						GameStateManager.player.isDead();
					}
				}
			}
		}

		// Check for PowerUp - player collision and activating the power up:
		for (int i = 0; i < powerUps.size(); i++) {
			PowerUp powerUp = powerUps.get(i);

			if (powerUp.getBounds().intersects(GameStateManager.player.getBounds())) {
				soundFX.get("collectPU").play();

				int type = powerUp.getType();

				if (type == PowerUp.HEART) {
					if (GameStateManager.player.getLives() < 4) {
						GameStateManager.player.addLife(1);
						powerUpTexts
								.add(new PowerUpText(GameStateManager.player.getX() - 60, GameStateManager.player.getY() - 30, 0, 0, 1000, "+1 LIFE"));
					} else {
						powerUpTexts
								.add(new PowerUpText(GameStateManager.player.getX() - 70, GameStateManager.player.getY() - 30, 0, 0, 1000, "FULL LIFE"));
					}
				} else if (type == PowerUp.SCORE) {
					GameStateManager.player.addScore(50);
					powerUpTexts.add(new PowerUpText(GameStateManager.player.getX() - 78, GameStateManager.player.getY() - 30, 0, 0, 1000, "+50 SCORE"));
				}

				if (Player.savedPowerUps.size() < 6) {

					if (type == PowerUp.RAYGUN) {
						Player.savedPowerUps.add(powerUp);
						powerUpTexts.add(
								new PowerUpText(GameStateManager.player.getX() - 70, GameStateManager.player.getY() - 30, 0, 0, 1000, "RAY GUN ACUIRED"));
					} else if (type == PowerUp.SHIELD) {
						Player.savedPowerUps.add(powerUp);
						powerUpTexts.add(
								new PowerUpText(GameStateManager.player.getX() - 70, GameStateManager.player.getY() - 30, 0, 0, 1000, "SHIELD AQUIRED"));
					}

				} else {
					powerUpTexts
							.add(new PowerUpText(GameStateManager.player.getX() - 70, GameStateManager.player.getY() - 30, 0, 0, 1000, "FULL POWERUP"));
				}

				powerUps.remove(i);
				i--;
			}
		}

		// Check for dead player:
		if (GameStateManager.player.isDead()) {
			gsm.setState(GameStateManager.GAMEOVERSTATE);

		}

		if (nbr == 6) {
			gsm.setState(GameStateManager.INTERMISSIONSTATE);
			gsm.setRunning(false);
			enemies = newEnemies;
			newEnemies = repopulateEnemies();
			gsm.setRunning(true);
			nbr = 0;
			nbrLevels++;
		}
	}

	@Override
	public void draw(Graphics2D g) {

		bg.draw(g);

		GameStateManager.player.draw(g);

		g.setColor(Color.GRAY.darker());
		g.setStroke(new BasicStroke(2));
		g.drawLine(10, 75, WIDTH - 10, 75);
		g.setStroke(new BasicStroke(1));

		if (bossActive) {
			purpleShip.draw(g);
		}

		for (int i = 0; i < enemies.size(); i++) {
			for (int j = 0; j < enemies.get(i).size(); j++) {
				enemies.get(i).get(j).draw(g, frameCounter, i * 2);
			}
		}

		for (int i = 0; i < missiles.size(); i++) {
			missiles.get(i).draw(g);
		}

		for (int i = 0; i < powerUps.size(); i++) {
			powerUps.get(i).draw(g);
		}

		for (int i = 0; i < Player.savedPowerUps.size(); i++) {
			Player.savedPowerUps.get(i).draw(g, 20 + i * 55, 660, 50, 26);
		}

		for (int i = 0; i < powerUpTexts.size(); i++) {
			powerUpTexts.get(i).draw(g);
		}

		for (int i = 0; i < bombs.size(); i++) {
			bombs.get(i).draw(g);
		}

		// Draw Score:

		g.setColor(Color.GRAY.darker());
		g.setStroke(new BasicStroke(2));
		g.drawLine(10, 75, WIDTH - 10, 75);
		g.setStroke(new BasicStroke(1));

		g.setColor(Color.WHITE);
		g.setFont(font);
		g.drawString("SCORE:", GamePanel.WIDTH / 8, 50);
		g.setColor(Color.GREEN);
		g.drawString("" + GameStateManager.player.getScore(), 230, 50);

		// Draw Lives:
		g.setColor(Color.WHITE);
		g.drawString("LIVES:", 400, 50);

		g.setColor(Color.GREEN);

		for (int i = 0; i < GameStateManager.player.getLives(); i++) {
			g.drawImage(heartImage, 545 + (40 * i), 25, 32, 32, null);
		}

		if (paused) {
			drawPauseMenu(g);
		}

		if (frameCounter < 60)
			frameCounter++;
		else {
			frameCounter = 0;
		}
	}

	public void drawPauseMenu(Graphics2D g) {
		score = GameStateManager.player.getScore() + "";
		g.setColor(new Color(0, 0, 0, 100));
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
		
	
		if (key == KeyEvent.VK_MINUS) {
			if (VOLUME <= 0.0) {
				VOLUME = 0;

			} else {
				VOLUME = VOLUME - 0.25;
			}
			GamePanel.setVolume(VOLUME);
			System.out.println("Volym niv�: " + VOLUME);
		}

		if (key == KeyEvent.VK_PLUS) {

			if (VOLUME >= 1.0) {
				VOLUME = 1;
			} else {
				VOLUME = VOLUME + 0.25;
			}
			GamePanel.setVolume(VOLUME);
			System.out.println("Volym niv�: " + VOLUME);
		}

		if (key == KeyEvent.VK_M) {
			if (VOLUME != 0) {
				VOLUME = 0;
			} else {
				VOLUME = 1;
			}
			GamePanel.setVolume(VOLUME);
			System.out.println("Volym niv�: " + VOLUME);
		}
	
		if (key == KeyEvent.VK_LEFT)
			GameStateManager.player.setLeft(true);
		if (key == KeyEvent.VK_RIGHT)
			GameStateManager.player.setRight(true);
		if (key == KeyEvent.VK_SPACE)
			GameStateManager.player.setFiring(true);
		if (key == KeyEvent.VK_ESCAPE) {
			pause();
			soundFX.get("click").play();
		}
		// To activate PowerUp Ray gun:
		if (key == KeyEvent.VK_X) {
			for (int i = 0; i < Player.savedPowerUps.size(); i++) {

				if (Player.savedPowerUps.get(i).getType() == PowerUp.RAYGUN) {

					GameStateManager.player.activateRaygun(Player.savedPowerUps, i);
					break;
				}
			}
		}
		// To activate PowerUp Shield:
		if (key == KeyEvent.VK_S) {

			for (int i = 0; i < Player.savedPowerUps.size(); i++) {

				if (Player.savedPowerUps.get(i).getType() == PowerUp.SHIELD) {

					GameStateManager.player.activateShield(Player.savedPowerUps, i);
					break;
				}
			}
		}

		if (key == KeyEvent.VK_E && paused) {
			soundFX.get("enter").play();
			gsm.setState(GameStateManager.MENUSTATE);

		}
	}

	private LinkedList<LinkedList<Enemy>> repopulateEnemies() {
		gsm.setHigherDifficulty();
		LinkedList<LinkedList<Enemy>> temp = new LinkedList<LinkedList<Enemy>>();
		for (int i = 0; i < 3; i++) {
			LinkedList<Enemy> row;
			temp.add(row = new LinkedList<Enemy>());
			for (int j = 0; j < 8; j++) {
				Enemy enemy = new Enemy(ENEMY_INIT_X + 60 * j, ENEMY_INIT_Y + 50 * i, 1, 1, gsm.getDifficulty());
				row.add(enemy);
			}
		}
		return temp;
	}

	public void keyReleased(int key) {
		
		if (key == KeyEvent.VK_LEFT)
			GameStateManager.player.setLeft(false);
		if (key == KeyEvent.VK_RIGHT)
			GameStateManager.player.setRight(false);
		if (key == KeyEvent.VK_SPACE)
			GameStateManager.player.setFiring(false);
		if (key == KeyEvent.VK_X) {
		
		}
		}

	}

