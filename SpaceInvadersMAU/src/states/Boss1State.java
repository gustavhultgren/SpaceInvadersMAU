package states;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import javax.imageio.ImageIO;
import audio.AudioPlayer;
import entity.Boss;
import entity.Boss1;
import entity.EnemyBomb;
import entity.Missile;
import entity.Player;
import entity.PowerUp;
import entity.PowerUpText;
import main.GamePanel;
import tileMap.MenuBackground;

/**
 * 
 * @author Joakim Thiman
 */

public class Boss1State extends GameState {

	private boolean paused;
	private int textLength;
	private int nbr = 0;
	private Graphics2D g;
	private Random rand = new Random();
	private String score;
	private String instruction = "GAME PAUSED";
	private String gameOver = "Press ESC to resume";
	private MenuBackground bg;

	//Entities
	public static Boss1 rolfBoss;
	public static LinkedList<EnemyBomb> bombs;
	public static ArrayList<Missile> missiles;

	private BufferedImage image;
	private BufferedImage heartImage;

	public Boss1State(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}

	@Override
	public void init() {
		try {
			bg = new MenuBackground("/images/background.png", 1);
			bg.setVector(-0.4, 0);

		} catch (Exception e) {
			e.printStackTrace();
		}

		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();

		player = new Player(PLAYER_INIT_X, PLAYER_INIT_Y, 18, 3);

		rolfBoss = new Boss1(ENEMY_INIT_X, ENEMY_INIT_Y , 1, 1, gsm.getDifficulty());

		missiles = new ArrayList<Missile>();
		bombs = new LinkedList<EnemyBomb>();
		//		powerUps = new LinkedList<PowerUp>();
		//		powerUpTexts = new LinkedList<PowerUpText>();

		try {
			heartImage = ImageIO.read(new File("resources/images/Heart.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		soundFX.put("click", new AudioPlayer("/music/sfx_click.mp3"));
		soundFX.put("enter", new AudioPlayer("/music/sfx_enter.mp3"));
		soundFX.put("win", new AudioPlayer("/music/sfx_win.mp3"));
		soundFX.put("gameOver", new AudioPlayer("/music/sfx_gameOver.mp3"));
		soundFX.put("enemyHit", new AudioPlayer("/music/sfx_enemyHit.mp3"));
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

		// Updating player.
		player.update();

		//Updating Boss
		rolfBoss.update(ENEMY_DIRECTION, true);  

		int x = rolfBoss.getX();

		//Makes Boss move within bounds.
		if (x >= WIDTH - 190 && ENEMY_DIRECTION != -1) {
			ENEMY_DIRECTION = -1;
		}

		if (x <= 10 && ENEMY_DIRECTION != 1) {
			ENEMY_DIRECTION = 1;
		}

		//Updating missiles.
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
		if (player.isDead()) {
			gsm.setState(2);
		}

		// If player dies gamestate changes to gameoverstate.
		if (nbr == 1) {
			gsm.setHigherDifficulty();
			gsm.setState(2);
		}

		//Checks for player missiles - enemy collision.
		for (int i = 0; i < missiles.size(); i++) {
			Missile m = missiles.get(i);
			if(m.getBounds().intersects(rolfBoss.getBounds())) {
				System.out.println("Rolf: You hit me!");
				missiles.remove(i);
				rolfBoss.isDead();
				player.addScore(200);
			}
		}

		//Check for enemy bombs - player collision.
		for (int i = 0; i < bombs.size(); i++) {
			EnemyBomb eb = bombs.get(i);
			if (eb.getBounds().intersects(player.getBounds())) {
				bombs.remove(i);
				player.loseLife();
			}
		}
	}

	@Override
	public void draw(Graphics2D g) {
		//Draws the background.
		bg.draw(g);

		//Draws the player-object.
		player.draw(g);

		//Draws the Boss-object
		rolfBoss.draw(g);

		g.setColor(Color.GRAY.darker());
		g.setStroke(new BasicStroke(2));
		g.drawLine(10, 75, WIDTH - 10, 75);
		g.setStroke(new BasicStroke(1));

		g.setColor(Color.GREEN);
		g.setStroke(new BasicStroke(3));
		g.drawLine(10, GROUND, WIDTH - 10, GROUND);
		g.setStroke(new BasicStroke(1));

		// Draw player score.
		g.setColor(Color.WHITE);
		g.setFont(font);
		g.drawString("SCORE:", GamePanel.WIDTH / 8, 50);
		g.setColor(Color.GREEN);
		g.drawString("" + player.getScore(), 230, 50);

		// Draws player lives.
		g.setColor(Color.WHITE);
		g.drawString("LIVES:", 400, 50);
		g.setColor(Color.GREEN);
		for (int i = 0; i < player.getLives(); i++) {
			g.drawImage(heartImage, 545 + (40 * i), 25, 32, 32, null);
		}

		//Draws pause menu.
		if (paused) {
			drawMenu(g);
		}

		//Draws missile-entity.
		for (int i = 0; i < missiles.size(); i++) {
			missiles.get(i).draw(g);
		}

		//Draws Enemy Bomb-entity.
		for (int i = 0; i < bombs.size(); i++) {
			bombs.get(i).draw(g);
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

	/**
	 * The keyPressed and keyReleased-method is responsible to handle key events to
	 * make the player move and fire missiles.
	 */
	@Override
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

	@Override
	public void keyReleased(int key) {
		if (key == KeyEvent.VK_LEFT)
			player.setLeft(false);
		if (key == KeyEvent.VK_RIGHT)
			player.setRight(false);
		if (key == KeyEvent.VK_Z)
			player.setFiring(false);
	}

	public void pause() {
		if (!paused) {
			paused = true;
		} else {
			paused = false;
		}
	}
}


