package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import audio.AudioPlayer;
import main.GamePanel;
import tileMap.MenuBackground;

/**
 * 
 * @author Gustav Hultgren, Tom Eriksson, Gustav Georgsson, Hannes Granberg.
 */

public class MenuState extends GameState {

	protected int currentChoice = 0;
	protected int textLength;
	private String[] options = { "Play", "Help", "Leaderboards", "Quit" };

	// arrow locations
	private int leftArrowX = 0;
	private int leftArrowY = 0;
	private int rightArrowX = 0;
	private int rightArrowY = 0;

	// arrow images
	private BufferedImage leftArrow;
	private BufferedImage rightArrow;

	// Background of the menu
	protected MenuBackground bg;
	protected BufferedImage image;

	public MenuState(GameStateManager gsm) {
		this.gsm = gsm;

		// Initializing variables.
		init();
	}

	@Override
	public void init() {

		try {

			font = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/ARCADE_I.TTF")).deriveFont(Font.PLAIN,
					25);

			// initializes the location of the arrows
			leftArrowX = 250;
			leftArrowY = 263;

			rightArrowX = 410;
			rightArrowY = 263;

			// initializes the font
			font = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/ARCADE_I.TTF")).deriveFont(25f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/ARCADE_I.TTF")));

			bg = new MenuBackground("/images/bg.png", 1);
			bg.setVector(-0.4, 0);
			// initializes both the arrow images
			leftArrow = ImageIO.read(new File("resources/images/LeftArrow.png"));
			rightArrow = ImageIO.read(new File("resources/images/RightArrow.png"));

			image = ImageIO.read(new File("resources/images/headline.png"));

		} catch (IOException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			
		}

		soundFX.put("click", new AudioPlayer("/music/sfx_click.mp3"));
		soundFX.put("enter", new AudioPlayer("/music/sfx_enter.mp3"));
	}

	@Override
	public void update() {
		bg.update();

	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(image, null, 100, 100);
		bg.draw(g);

		// draw menu options
		g.setFont(font);

		for (int i = 0; i < options.length; i++) {
			if (i == currentChoice) {
				g.setColor(Color.WHITE);
			} else {
				g.setColor(Color.GREEN);
			}
			textLength = (int) g.getFontMetrics().getStringBounds(options[i], g).getWidth();
			g.drawString(options[i], (700 - textLength) / 2, (700 / 2) - 60 + 60 * i);

		}

		// draws the arrows at the location and sends the g <Graphics2D> object

		drawLeftArrow(g);
		drawRightArrow(g);

	}

	/**
	 * Method that draws the right arrow
	 */

	public void drawRightArrow(Graphics2D g) {
		g.drawImage(rightArrow, rightArrowX, rightArrowY, null);

	}

	/**
	 * Sets the location of the right arrow
	 * 
	 * @param x the X-location
	 * @param y the Y-location
	 */

	public void setRightArrowLocation(int x, int y) {

		rightArrowX = x;
		rightArrowY = y;
	}

	/**
	 * Method that draws the left arrow
	 */

	public void drawLeftArrow(Graphics2D g) {

		g.drawImage(leftArrow, leftArrowX, leftArrowY, null);

	}

	/**
	 * Sets the location of the left arrow (in pixels)
	 * 
	 * @param x the X-location
	 * @param y the Y-location
	 */

	public void setLeftArrowLocation(int x, int y) {

		leftArrowX = x;
		leftArrowY = y;

	}

	/**
	 * Returns the String-width (roughly)
	 * 
	 * @param operation - is the currentChoice
	 * @return Returns the width
	 */

	public int getStringWidth(int currentChoice) {

		switch (currentChoice) {
		case 0:
			return 100;
		case 1:
			return 100;
		case 2:
			return 190;
		case 3:
			return 100;

		}

		return 0;

	}

	/**
	 * Method that is called when you press [Enter] on your keyboard. The method
	 * sets the new state, or exits, depending on what the currentChoice is.
	 */

	private void select() {
		if (currentChoice == 0) {
			gsm.setState(GameStateManager.CHARACTERSELECTIONSTATE);
		}
		if (currentChoice == 1) {
			gsm.setState(GameStateManager.HELPSTATE);
		}
		if (currentChoice == 2) {
			gsm.setState(GameStateManager.LEADERBOARDSTATE);
		}
		if (currentChoice == 3) {
			System.exit(0);
		}
	}

	public void keyPressed(int k) {

		/**
		 * If you press [Enter]
		 */

		if (k == KeyEvent.VK_ENTER) {
			select();
//			soundFX.get("enter").play();
		}

		/**
		 * If you press [Up]
		 */

		if (k == KeyEvent.VK_UP) {
			currentChoice--;
			if (currentChoice == -1) {
				currentChoice = options.length - 1;

				// moves the arrows to the bottom, in coherence with the <currentChoice>
				setLeftArrowLocation((WIDTH / 2) - getStringWidth(currentChoice), 445);
				setRightArrowLocation((WIDTH / 2) + getStringWidth(currentChoice) - 40, 445);

			} else {

				// moves the arrow upwards, in coherence with the <currentChoice>
				setLeftArrowLocation((WIDTH / 2) - getStringWidth(currentChoice), leftArrowY - 60);
				setRightArrowLocation((WIDTH / 2) + getStringWidth(currentChoice) - 40, rightArrowY - 60);
			}
			soundFX.get("click").play();
		}

		/**
		 * If you press [Down]
		 */

		if (k == KeyEvent.VK_DOWN) {
			currentChoice++;
			if (currentChoice == options.length) {
				currentChoice = 0;

				// moves the arrows to the top (the original values), in coherence with the
				// <currentChoice>
				setLeftArrowLocation(250, 263);
				setRightArrowLocation(420, 263);

			} else {

				// moves the arrow downwards, in coherence with the <currentChoice>
				setLeftArrowLocation((WIDTH / 2) - getStringWidth(currentChoice), leftArrowY + 60);
				setRightArrowLocation((WIDTH / 2) + getStringWidth(currentChoice) - 40, rightArrowY + 60);
			}
			soundFX.get("click").play();
		}

		if (k == KeyEvent.VK_MINUS) {
			if (VOLUME <= 0.0) {
				VOLUME = 0;

			} else {
				VOLUME = VOLUME - 0.25;
			}
			GamePanel.setVolume(VOLUME);
			System.out.println("Volym: " + VOLUME);
		}

		if (k == KeyEvent.VK_PLUS) {

			if (VOLUME >= 1.0) {
				VOLUME = 1;
			} else {
				VOLUME = VOLUME + 0.25;
			}
			GamePanel.setVolume(VOLUME);
			System.out.println("Volym: " + VOLUME);
		}

		if (k == KeyEvent.VK_M) {
			if (VOLUME != 0) {
				VOLUME = 0;
			} else {
				VOLUME = 1;
			}
			GamePanel.setVolume(VOLUME);
			System.out.println("Volym: " + VOLUME);
		}
	}

	@Override
	public void keyReleased(int k) {

	}

}