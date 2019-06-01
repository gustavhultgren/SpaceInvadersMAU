package states;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sun.glass.events.KeyEvent;

import entity.Player;
import main.GamePanel;
import tileMap.MenuBackground;

/**
 * This class represents a state in the game where the user can choose 
 * what kind of character he or she wants to play the game with.
 * @author Gustav Hultgren
 */
public class CharacterSelectionState extends GameState {

	private int currentChoice, textLength, counter;

	private MenuBackground bg;

	private String title, char1Name, char2Name, char3Name, char4Name, message;

	private BufferedImage playerImage, player2Image, player3Image, player4Image;

	public CharacterSelectionState(GameStateManager gsm) {
		this.gsm = gsm;

		init();
	}

	/**
	 * This method initializes all the variables being used in the class.
	 */
	@Override
	public void init() {
		try {
			bg = new MenuBackground("/images/background.png", 1);
			bg.setVector(-0.4, 0);

			font = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/ARCADE_I.TTF")).deriveFont(30f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/ARCADE_I.TTF")));

			smallFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/ARCADE_I.TTF")).deriveFont(17f);
			GraphicsEnvironment ge2 = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge2.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/ARCADE_I.TTF")));

		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			playerImage = ImageIO.read(new File("resources/images/playerImage.png"));
			player2Image = ImageIO.read(new File("resources/images/playerImage2.png"));
			player3Image = ImageIO.read(new File("resources/images/player3Image.png"));
			player4Image = ImageIO.read(new File("resources/images/player4Image.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		currentChoice = 1;
		counter = 0;
		title = "CHOOSE YOUR CHARACTER";
		char1Name = "CHAR 1";
		char2Name = "CHAR 2";
		char3Name = "CHAR 3";
		char4Name = "CHAR 4";
		message = "PRESS ENTER TO START";
	}

	@Override
	public void update() {
		bg.update();
	}

	@Override
	public void draw(Graphics2D g) {
		bg.draw(g);

		g.setFont(font);

		textLength = (int) g.getFontMetrics().getStringBounds(title, g).getWidth();
		g.setColor(Color.YELLOW);
		g.drawString(title, (700 - textLength) / 2, 100);

		g.drawImage(playerImage, 130, 140, 200, 200, null);
		g.drawImage(player2Image, 370, 140, 200, 200, null);
		g.drawImage(player3Image, 130, 380, 180, 180, null);
		g.drawImage(player4Image, 385, 390, 170, 170, null);

		g.setColor(Color.GREEN);
		drawMessage(g);
		writeCharacterNames(g);
		drawLines(g);
	}

	/**
	 * This method draws / writes a message to the user in the bottom of the window.
	 * @param g
	 */
	public void drawMessage(Graphics2D g) {
		g.setFont(font);
		textLength = (int) g.getFontMetrics().getStringBounds(message, g).getWidth();
		g.drawString(message, (700 - textLength) / 2, 650);
	}
	
	/**
	 * This method draws / writes all the character names above the character image.
	 * @param g
	 */
	public void writeCharacterNames(Graphics2D g) {
		g.setFont(smallFont);
		g.drawString(char1Name, 170, 150);
		g.drawString(char2Name, 420, 150);
		g.drawString(char3Name, 170, 380);
		g.drawString(char4Name, 420, 380);
	}

	/**
	 * This method draws the lines which visualizes what character is
	 * being selected.
	 * @param g
	 */
	public void drawLines(Graphics2D g) {
		g.setStroke(new BasicStroke(3));

		if (counter < 15) {
			if (currentChoice == 1) {
				g.drawLine(170, 335, 260, 335);
			} else if (currentChoice == 2) {
				g.drawLine(430, 335, 520, 335);
			} else if (currentChoice == 3) {
				g.drawLine(170, 575, 260, 575);
			} else if (currentChoice == 4) {
				g.drawLine(430, 575, 520, 575);
			}
		} else if (counter >= 30) {
			counter = 0;
		}
		counter++;
	}

	/**
	 * This method handles the selection. The setPlayerImage method sets
	 * the playerImage in class Player.
	 */
	private void select() {
		if (currentChoice == 1) {
			GameStateManager.player.setPlayerImage(1);
		} else if (currentChoice == 2) {
			GameStateManager.player.setPlayerImage(2);
		} else if (currentChoice == 3) {
			GameStateManager.player.setPlayerImage(3);
		} else if (currentChoice == 4) {
			GameStateManager.player.setPlayerImage(4);
		}

		gsm.setState(GameStateManager.INTERMISSIONSTATE);
	}

	/**
	 * This method handles key input.
	 */
	@Override
	
	public void keyPressed(int k) {
		if (k == KeyEvent.VK_ENTER) {
			select();
			soundFX.get("enter").play();
		}

		if (k == KeyEvent.VK_RIGHT) {
			currentChoice++;
			
			if (currentChoice == 5) {
				currentChoice = 1;
			}

			System.out.println(currentChoice);

			soundFX.get("click").play();
		}

		if (k == KeyEvent.VK_LEFT) {
			currentChoice--;

			if (currentChoice == 0) {
				currentChoice = 4;
			}

			System.out.println(currentChoice);

			soundFX.get("click").play();
		}

		if (k == KeyEvent.VK_MINUS) {
			if (VOLUME <= 0.0) {
				VOLUME = 0;

			} else {
				VOLUME = VOLUME - 0.25;
			}
			GamePanel.setVolume(VOLUME);
			System.out.println("Volym niv�: " + VOLUME);
		}

		if (k == KeyEvent.VK_PLUS) {

			if (VOLUME >= 1.0) {
				VOLUME = 1;
			} else {
				VOLUME = VOLUME + 0.25;
			}
			GamePanel.setVolume(VOLUME);
			System.out.println("Volym niv�: " + VOLUME);
		}

		if (k == KeyEvent.VK_M) {
			if (VOLUME != 0) {
				VOLUME = 0;
			} else {
				VOLUME = 1;
			}
			GamePanel.setVolume(VOLUME);
			System.out.println("Volym niv�: " + VOLUME);
		}

	}

	@Override
	public void keyReleased(int k) {

	}

}
