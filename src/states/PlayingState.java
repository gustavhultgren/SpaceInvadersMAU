package states;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import javax.imageio.ImageIO;

import audio.AudioPlayer;
import entity.Boss;
import entity.Enemy;
import entity.EnemyBomb;
import entity.Missile;
import entity.Player;
import entity.PowerUp;
import entity.PowerUpText;
import main.GamePanel;
import tileMap.MenuBackground;

/**
 * 
 * @author Gustav Hultgren, Tom Eriksson
 */
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
	private int frameCounter = 0;
	private boolean bossActive = true;

	// Entity
	public static LinkedList<LinkedList<Enemy>> enemies;
	public static Boss purpleShip;
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
			heartImage = ImageIO.read(new File("resources/images/heart.png"));

		} catch (Exception e) {
			e.printStackTrace();
		}

		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();

		player = new Player(PLAYER_INIT_X, PLAYER_INIT_Y, 18, 3);

		enemies = new LinkedList<LinkedList<Enemy>>();

		/**
		 * Adding enemies to the list and sets each enemies X and Y-value so it looks
		 * good.
		 */
		for (int i = 0; i < 3; i++) {
			LinkedList<Enemy> row;
			enemies.add(row = new LinkedList<Enemy>());
			for (int j = 0; j < 8; j++) {
				Enemy enemy = new Enemy(ENEMY_INIT_X + 60 * j, ENEMY_INIT_Y + 50 * i, 1, 1, gsm.getDifficulty());
				row.add(enemy);
			}
		}
		
		purpleShip = new Boss(ENEMY_INIT_X, ENEMY_INIT_Y , 1, 1, gsm.getDifficulty());
	
		

		missiles = new LinkedList<Missile>();
		bombs = new LinkedList<EnemyBomb>();
		powerUps = new LinkedList<PowerUp>();
		powerUpTexts = new LinkedList<PowerUpText>();

		soundFX.put("click", new AudioPlayer("/music/sfx_click.mp3"));
		soundFX.put("enter", new AudioPlayer("/music/sfx_enter.mp3"));
		soundFX.put("win", new AudioPlayer("/music/sfx_win.mp3"));
		soundFX.put("gameOver", new AudioPlayer("/music/sfx_gameOver.mp3"));
		soundFX.put("enemyHit", new AudioPlayer("/music/sfx_enemyHit.mp3"));

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
		player.update();
		
		purpleShip.update(ENEMY_DIRECTION, false);

		// Updating missiles:
		for (int i = 0; i < missiles.size(); i++) {
			boolean remove = missiles.get(i).update();
			if (remove) {
				missiles.remove(i);
				i--;
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
			Missile m = missiles.get(i);
			if(m.getBounds().intersects(purpleShip.getBounds())) {
				missiles.remove(i);
				purpleShip.killed();
				
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
						 * Type 1 -- +1 life (5%)
						 * Type 2 -- +50 score (10%)
						 */
						double rand = Math.random();
						if(rand < 0.05) {
							powerUps.add(new PowerUp(e.getX(), e.getY(), 13, 3.0, 1)); //Type 1
							System.out.println("Skapad 1");
						} else if(rand < 0.20) {
							powerUps.add(new PowerUp(e.getX(), e.getY(), 13, 3.0, 2)); //Type 2
							System.out.println("Skapad 2");
						}

						player.addScore(100);

						enemies.set(j, temp);
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

		// // Check for dead enemies:
		// for (int i = 0; i < enemies.size(); i++) {
		// for (int j = 0; j < enemies.get(i).size(); j++) {
		// if (!enemies.get(i).get(j).isDead()) {
		// Enemy e = enemies.get(i).get(j);
		//
		// /**
		// * Type 1 -- +1 life
		// * Type 2 -- +50 score
		// * Type 3 -- Slow motion
		// */
		// double rand = Math.random();
		// if(rand < 0.5) {
		// powerUps.add(new PowerUp(e.getX(), e.getY(), 16, 0.7, 1)); //Type 1
		// System.out.println("Skapad");
		// } else if(rand < 0.5) {
		// powerUps.add(new PowerUp(e.getX(), e.getY(), 16, 0.5, 2)); //Type 2
		// System.out.println("Skapad 2");
		// } else if(rand < 0.5) {
		// powerUps.add(new PowerUp(e.getX(), e.getY(), 16, 0.7, 2)); //Type 3
		// System.out.println("Skapad 3");
		// }
		//
		// player.addScore(10);
		//
		// LinkedList<Enemy> temp = enemies.get(i);
		// temp.remove(j);
		// enemies.set(i, temp);
		// j--;
		// }
		// }
		// }

		// Check for PowerUp - player collision and activating the power up:
		for (int i = 0; i < powerUps.size(); i++) {
			PowerUp powerUp = powerUps.get(i);

			if (powerUp.getBounds().intersects(player.getBounds())) {
				int type = powerUp.getType();

				if(type == 1) {
					if(player.getLives() < 4) {
						player.addLife(1);
						powerUpTexts.add(new PowerUpText(player.getX() - 60, player.getY() - 30, 0, 0, 1000, "+1 LIFE"));
					} else {
						powerUpTexts.add(new PowerUpText(player.getX() - 70, player.getY() - 30, 0, 0, 1000, "FULL LIFE"));
					}

				} 
				else if(type == 2) {
					player.addScore(50);
					powerUpTexts.add(new PowerUpText(player.getX() - 78, player.getY() - 30, 0, 0, 1000, "+50 SCORE"));
				}

				powerUps.remove(i);
				i--;
			}
		}
		
			
	
	

		// Check for dead player:
		if (player.isDead()) {
			gsm.setState(GameStateManager.GAMEOVERSTATE);
		}

		if (nbr == 24) {
			int score = player.getScore();
			gsm.setHigherDifficulty();
			gsm.setState(GameStateManager.PLAYINGSTATE);
			player.setScore(score);
		}
	}

	@Override
	public void draw(Graphics2D g) {
		
		bg.draw(g);
		player.draw(g);
		
		g.setColor(Color.GRAY.darker());
		g.setStroke(new BasicStroke(2));
		g.drawLine(10, 75, WIDTH - 10, 75);
		g.setStroke(new BasicStroke(1));
		
		
		if (bossActive) {
			purpleShip.draw(g);
		}
		
		
		for (int i = 0; i < enemies.size(); i++) {
			for (int j = 0; j < enemies.get(i).size();j++) {
				enemies.get(i).get(j).draw(g, frameCounter, i*2);
				
			}
			
		}

		

		for (int i = 0; i < missiles.size(); i++) {
			missiles.get(i).draw(g);
		}

		for (int i = 0; i < powerUps.size(); i++) {
			powerUps.get(i).draw(g);
		}

		for (int i = 0; i < powerUpTexts.size(); i++) {
			powerUpTexts.get(i).draw(g);
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
			g.drawImage(heartImage, 545 + (40 * i), 25, 32, 32, null);
		}

		if (paused) {
			drawMenu(g);
		}
		
		 if(frameCounter < 60)
			 frameCounter++;
		 else {
			 frameCounter = 0;
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
		
		if (key == KeyEvent.VK_MINUS) {
			if (VOLUME <= 0.0) {
					VOLUME = 0;

				} else {
					VOLUME = VOLUME - 0.25;
				}
			GamePanel.setVolume(VOLUME);
			System.out.println("Volym nivå: " + VOLUME);
			}

			if (key == KeyEvent.VK_PLUS) {
				
				if (VOLUME >= 1.0) {
					VOLUME = 1;
				} else {
					VOLUME = VOLUME + 0.25;
				}
				GamePanel.setVolume(VOLUME);
				System.out.println("Volym nivå: " + VOLUME);
			}

			if (key == KeyEvent.VK_M) {
				if (VOLUME != 0) {
					VOLUME = 0;
				} else {
					VOLUME = 1;
				}
				GamePanel.setVolume(VOLUME);
				System.out.println("Volym nivå: " + VOLUME);
			}
		
		
		

		if (key == KeyEvent.VK_LEFT)
			player.setLeft(true);
		if (key == KeyEvent.VK_RIGHT)
			player.setRight(true);
		if (key == KeyEvent.VK_SPACE)
			player.setFiring(true);
		if (key == KeyEvent.VK_ESCAPE) {
			pause();
			soundFX.get("click").play();
		}
		if (key == KeyEvent.VK_E && paused) {
			soundFX.get("enter").play();
			gsm.setState(GameStateManager.MENUSTATE);
		}

}

	public void keyReleased(int key) {
	
			
			
		if (key == KeyEvent.VK_LEFT)
			player.setLeft(false);
		if (key == KeyEvent.VK_RIGHT)
			player.setRight(false);
		if (key == KeyEvent.VK_SPACE)
			player.setFiring(false);
	}
	

	
	
	}

