package states;

import java.awt.Color;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import javafx.scene.layout.Background;
import tileMap.MenuBackground;

/**
 * 
 * @author hannesgranberg och..?
 *
 */

public class MenuState extends GameState {

	// reference variables
	private int currentChoice = 0;
	private int textLength;
	private int textHeight;
	private int[] pixelLength = new int[4];
	private int stringXValue;
	private int stringYValue;

	// Menu choices
	private String[] options = { "Play", "Help", "Leaderboards", "Quit" };

	// arrow locations
	private int leftArrowX = 0;
	private int leftArrowY = 0;
	private int rightArrowX = 0;
	private int rightArrowY = 0;

	// arrow images
	private BufferedImage leftArrow;
	private BufferedImage rightArrow;
	private static final int arrowPixelWidth = 32; // the width of the arrow image (is known)
	private static final int arrowShifted = 50; // will be used later with the arrows

	// Background of the menu
	private MenuBackground bg;

	public MenuState(GameStateManager gsm) {
		this.gsm = gsm;
		// Initializing variables.
		init();
	}

	@Override
	public void init() {

		// initializes the location of the arrows
		leftArrowX = 252;
		leftArrowY = 263;

		rightArrowX = 420;
		rightArrowY = 263;

		try {

			// initializes the font
			font = Font.createFont(Font.TRUETYPE_FONT, new File("SpaceInvadersMAU/resources/fonts/ARCADE_I.TTF"))
					.deriveFont(25f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(
					Font.createFont(Font.TRUETYPE_FONT, new File("SpaceInvadersMAU/resources/fonts/ARCADE_I.TTF")));

		} catch (IOException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		}

		try {

			// initializes both the arrow images
			leftArrow = ImageIO.read(new File("SpaceInvadersMAU/resources/images/LeftArrow.png"));
			rightArrow = ImageIO.read(new File("SpaceInvadersMAU/resources/images/RightArrow.png"));

		} catch (Exception e) {

		}
	}

	@Override
	public void update() {
//		bg.update();

	}

	@Override

	/**
	 * This is the draw-method that draws the main menu. This method gets updated in
	 * relation to the frames
	 */
	public void draw(Graphics2D g) {

		// draws black background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		// sets the font for the text
		g.setFont(font);

		// draw menu options and changes the color of the text depending on what you
		// have chosen

		for (int i = 0; i < options.length; i++) {
			if (i == currentChoice) {
				g.setColor(Color.WHITE);
			} else {
				g.setColor(Color.GREEN);
			}
			textLength = (int) g.getFontMetrics().getStringBounds(options[i], g).getWidth(); // gets the width of the
																								// string

			textHeight = (int) g.getFontMetrics().getStringBounds(options[i], g).getHeight(); // gets the height of the
																								// string

			stringXValue = (700 - textLength) / 2;
			stringYValue = (700 / 2) - 60 + 60 * i;
			g.drawString(options[i], stringXValue, stringYValue); // draws the string

			pixelLength[i] = textLength; // saves the length of the text written in the menu, will be used for drawing
											// arrows

		}

		// draws the arrows at the location and sends the g <Graphics2D> object

		drawLeftArrow(g);
		drawRightArrow(g);

	}

	/**
	 * Method that draws the right arrow
	 */

	private void drawRightArrow(Graphics2D g) {

		g.drawImage(rightArrow, rightArrowX, rightArrowY, null);

	}

	/**
	 * Sets the location of the right arrow
	 * 
	 * @param x the X-location
	 * @param y the Y-location
	 */

	private void setRightArrowLocation(int x, int y) {

		rightArrowX = x;
		rightArrowY = y;

	}

	/**
	 * Method that draws the left arrow
	 */

	private void drawLeftArrow(Graphics2D g) {

		g.drawImage(leftArrow, leftArrowX, leftArrowY, null);

	}

	/**
	 * Sets the location of the left arrow (in pixels)
	 * 
	 * @param x the X-location
	 * @param y the Y-location
	 */

	private void setLeftArrowLocation(int x, int y) {

		leftArrowX = x;
		leftArrowY = y;

	}

	/**
	 * Returns the String-width of each of the strings in the options-array.
	 * 
	 * @param operation - is the currentChoice
	 * @return Returns the width
	 */

	private int getStringWidth(int currentChoice) {

		switch (currentChoice) {
		case 0:
			return pixelLength[currentChoice];
		case 1:
			return pixelLength[currentChoice];
		case 2:
			return pixelLength[currentChoice];
		case 3:
			return pixelLength[currentChoice];

		}

		return 0;

	}

	/**
	 * Method that is called when you press [Enter] on your keyboard. The method
	 * sets the new state, or exits, depending on what the currentChoice is.
	 */

	private void select() {

		if (currentChoice == 0) {
			gsm.setState(GameStateManager.INTERMISSIONSTATE);
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

	/**
	 * Method that is called if you press a key on the keyboard.
	 */

	public void keyPressed(int k) {

		/**
		 * ~~~~~~~~~~~~~~ QUICK NOTE ~~~~~~~~~~~~~~
		 * 
		 * the X locations that are sent if you press Down or Up are calculated with the
		 * following function:
		 * 
		 * - Firstly, we start of the middle of the window (WIDTH / 2) - Secondly, we
		 * remove or add, depending on which side the arrow is on, the half of the width
		 * of the string, since the middle of the string is in the middle of the window
		 * - Thirdly, we shift the arrows another 50 pixels from the string so there is
		 * some room between the arrows and the text - Finally, if the arrow is on the
		 * right, we remove the Width of the arrow image, otherwise the left-pointing
		 * arrow would be drawn even further to the right, resulting in uneven spacing.
		 */

		/**
		 * If you press [Enter]
		 */

		if (k == KeyEvent.VK_ENTER) {
			select();
		}

		/**
		 * If you press [Up]
		 */

		if (k == KeyEvent.VK_UP) {
			currentChoice--;
			if (currentChoice == -1) {
				currentChoice = options.length - 1;

				// moves the arrows to the bottom, in coherence with the <currentChoice>
				setLeftArrowLocation((WIDTH / 2) - getStringWidth(currentChoice) / 2 - arrowShifted, 445);
				setRightArrowLocation(
						(WIDTH / 2) + (getStringWidth(currentChoice) / 2) + arrowShifted - arrowPixelWidth, 445);

			} else {

				// moves the arrow upwards, in coherence with the <currentChoice>
				setLeftArrowLocation((WIDTH / 2) - getStringWidth(currentChoice) / 2 - arrowShifted, leftArrowY - 60);
				setRightArrowLocation(
						(WIDTH / 2) + (getStringWidth(currentChoice) / 2) + arrowShifted - arrowPixelWidth,
						rightArrowY - 60);
			}
		}

		/**
		 * If you press [Down]
		 */

		if (k == KeyEvent.VK_DOWN) {
			currentChoice++;
			if (currentChoice == options.length) {
				currentChoice = 0;

				// moves the arrows to the top
				setLeftArrowLocation((WIDTH / 2) - getStringWidth(currentChoice) / 2 - arrowShifted, 263);
				setRightArrowLocation(
						(WIDTH / 2) + (getStringWidth(currentChoice) / 2) + arrowShifted - arrowPixelWidth, 263);

			} else {

				// moves the arrow downwards, in coherence with the <currentChoice>
				setLeftArrowLocation((WIDTH / 2) - getStringWidth(currentChoice) / 2 - arrowShifted, leftArrowY + 60);
				setRightArrowLocation(
						(WIDTH / 2) + (getStringWidth(currentChoice) / 2) + arrowShifted - arrowPixelWidth,
						rightArrowY + 60);
			}
		}
	}

	@Override
	public void keyReleased(int k) {

	}

}