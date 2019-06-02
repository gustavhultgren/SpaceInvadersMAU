package states;

/**
 *  class that handles when player enters
 *  the boss level. Class is similar to 
 *  playingState. But with key differences
 *  to the firing, drawing of enemy.
 *  
 *  @author Joakim Thiman, Gustav Georgsson, Tom Eriksson & Gustav Hultgren
 * 
 */

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
import java.util.LinkedList;
import javax.imageio.ImageIO;

import audio.AudioPlayer;
import entity.BossRolf;
import entity.EnemyBomb;
import entity.Missile;
import entity.Player;
import entity.PowerUp;
import main.GamePanel;
import tileMap.MenuBackground;

public class BossState extends PlayingState {

	private boolean paused;
	private int textLength;

	private int nbr = 0;
	private Graphics2D g;
	private String score;
	private String instruction = "GAME PAUSED";
	private String gameOver = "Press ESC to resume";
	private MenuBackground bg;
	private int frameCounter = 0;
	// Entities
	public static BossRolf rolfBoss;
	public static LinkedList<EnemyBomb> bombs;
	public static LinkedList<Missile> missiles;

	private BufferedImage image;
	private BufferedImage heartImage;

	public BossState(GameStateManager gsm) {
		super(gsm);
		init();
	}

	@Override
	public void init() {

		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();

		rolfBoss = new BossRolf(ENEMY_INIT_X, ENEMY_INIT_Y, 1, 1, gsm.getDifficulty());

		missiles = new LinkedList<Missile>();
		bombs = new LinkedList<EnemyBomb>();
		// powerUps = new LinkedList<PowerUp>();
		// powerUpTexts = new LinkedList<PowerUpText>();

		try {
			heartImage = ImageIO.read(new File("resources/images/Heart.png"));
			bg = new MenuBackground("/images/playingBG.png", 1);
			bg.setVector(-0.4, 0);
			font = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/ARCADE_I.TTF")).deriveFont(25f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/ARCADE_I.TTF")));

		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

		// Updating player.
		GameStateManager.player.update();

		// Updating Boss
		rolfBoss.update(ENEMY_DIRECTION, true, GameStateManager.player);

		int x = rolfBoss.getX();

		// Makes Boss move within bounds.
		if (x >= WIDTH - 190 && ENEMY_DIRECTION != -1) {
			ENEMY_DIRECTION = -1;
		}

		if (x <= 10 && ENEMY_DIRECTION != 1) {
			ENEMY_DIRECTION = 1;
		}

		// Updating missiles.
		for (int i = 0; i < missiles.size(); i++) {
			boolean remove = missiles.get(i).update();
			if (remove) {
				missiles.remove(i);
				i--;

			}
		}
		// Updating enemy bombs.
		for (int i = 0; i < bombs.size(); i++) {
			boolean remove = bombs.get(i).update();
			if (remove) {
				bombs.remove(i);
				i--;
			}
		}

		// Checking for dead player.
		if (GameStateManager.player.isDead()) {
			gsm.setState(2);
		}

		// If player dies GameState changes to GameOverState.
		if (nbr == 1) {
			gsm.setHigherDifficulty();
			gsm.setState(2);
		}

		// Checks for player missiles - BossRolf collision.
		for (int i = 0; i < missiles.size(); i++) {
			Missile m = missiles.get(i);
			if (m.getBounds().intersects(rolfBoss.getBounds())) {
				missiles.remove(i);
				rolfBoss.hit();
				// rolfBoss.isDead();
			}
		}
		if (rolfBoss.getLives() < 1) {
			GameStateManager.player.addScore(1000);
			rolfBoss.isDead();
			gsm.setState(7);

		}

		// Check for enemy bombs - player collision.
		for (int i = 0; i < bombs.size(); i++) {
			EnemyBomb eb = bombs.get(i);
			if (eb.getBounds().intersects(GameStateManager.player.getBounds())) {
				bombs.remove(i);
				GameStateManager.player.loseLife();
			}
		}
	}

	@Override
	public void draw(Graphics2D g) {
		bg.draw(g);

		GameStateManager.player.draw(g);

		rolfBoss.draw(g);

		// Draw Score
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

		// Draw BossRolf Health bar:
		g.drawRect(rolfBoss.getX() + 30, rolfBoss.getY() - 10, 150, 15);
		for (int i = 0; i < rolfBoss.getLives(); i++) {
			if (rolfBoss.getLives() >= 20) {
				g.fillRect(rolfBoss.getX() + 32 + (5 * i), rolfBoss.getY() - 8, 2, 10);
			} else if (rolfBoss.getLives() < 20 && rolfBoss.getLives() >= 15) {
				g.setColor(Color.YELLOW);
				g.fillRect(rolfBoss.getX() + 32 + (5 * i), rolfBoss.getY() - 8, 2, 10);
			} else if (rolfBoss.getLives() <= 14 & rolfBoss.getLives() >= 10) {
				g.setColor(Color.YELLOW.darker());
				g.fillRect(rolfBoss.getX() + 32 + (5 * i), rolfBoss.getY() - 8, 2, 10);
			} else if (rolfBoss.getLives() < 10 && rolfBoss.getLives() >= 5) {
				g.setColor(Color.RED);
				g.fillRect(rolfBoss.getX() + 32 + (5 * i), rolfBoss.getY() - 8, 2, 10);
			} else if (rolfBoss.getLives() < 5) {
				g.setColor(Color.RED.darker());
				g.fillRect(rolfBoss.getX() + 32 + (5 * i), rolfBoss.getY() - 8, 2, 10);
			}
		}

		for (int i = 0; i < missiles.size(); i++) {
			missiles.get(i).draw(g);
		}

		for (int i = 0; i < bombs.size(); i++) {
			bombs.get(i).draw(g);
		}

		if (paused) {
			drawPauseMenu(g);
		}

		if (frameCounter < 60)
			frameCounter++;
		else {
			frameCounter = 0;
		}

		for (int i = 0; i < Player.savedPowerUps.size(); i++) {
			Player.savedPowerUps.get(i).draw(g, 20 + i * 55, 660, 50, 26);
		}
	}

	public void drawMenu(Graphics2D g) {
		score = GameStateManager.player.getScore() + "";
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

	/**
	 * The keyPressed and keyReleased-method is responsible to handle key events to
	 * make the player move and fire missiles.
	 * 
	 * Key pressed also adjust the volume.
	 */
	@Override
	public void keyPressed(int key) {

		if (key == KeyEvent.VK_MINUS) {
			if (VOLUME <= 0.0) {
				VOLUME = 0;

			} else {
				VOLUME = VOLUME - 0.25;
			}
			GamePanel.setVolume(VOLUME);
			System.out.println("Volym: " + VOLUME);
		}

		if (key == KeyEvent.VK_PLUS) {

			if (VOLUME >= 1.0) {
				VOLUME = 1;
			} else {
				VOLUME = VOLUME + 0.25;
			}
			GamePanel.setVolume(VOLUME);
			System.out.println("Volym: " + VOLUME);
		}

		if (key == KeyEvent.VK_M) {
			if (VOLUME != 0) {
				VOLUME = 0;
			} else {
				VOLUME = 1;
			}
			GamePanel.setVolume(VOLUME);
			System.out.println("Volym: " + VOLUME);
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

	@Override
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

	public void pause() {
		if (!paused) {
			paused = true;
		} else {
			paused = false;
		}
	}
}