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
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import tileMap.*;
import javafx.scene.layout.Background;
import tileMap.MenuBackground;
import audio.AudioPlayer;

public class MenuState extends GameState {

	private int currentChoice = 0;
	private int textLength;
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
	private AudioPlayer bgMusic;
	private MenuBackground bg;

	public MenuState(GameStateManager gsm) {
		this.gsm = gsm;
		
		bgMusic = new AudioPlayer("/music/si.mp3");
		bgMusic.play();
		
		soundFX.put("click", new AudioPlayer("/music/sfx_click.mp3"));
		soundFX.put("enter", new AudioPlayer("/music/sfx_enter.mp3"));
		
		// Initializing variables.
		init();
	}

	@Override
	public void init() {
    
		// initializes the location of the arrows
		leftArrowX = 250;
		leftArrowY = 263;

		rightArrowX = 410;
		rightArrowY = 263;

		try {

			// initializes the background
			bg = new MenuBackground("SpaceInvadersMAU/resources/images/menuBG.gif", 1);

			// initializes the font
			font = Font.createFont(Font.TRUETYPE_FONT, new File("SpaceInvadersMAU/resources/fonts/ARCADE_I.TTF"))
					.deriveFont(25f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(
					Font.createFont(Font.TRUETYPE_FONT, new File("SpaceInvadersMAU/resources/fonts/ARCADE_I.TTF")));

			bg = new MenuBackground("/images/BackgroundTest.png", 1);
			bg.setVector(-0.4, 0);
      
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
		bg.update();
		
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(Graphics2D g) {
		bg.draw(g);
		
		// draw menu options

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 700, 700);
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
			gsm.setState(GameStateManager.PLAYINGSTATE);
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
			soundFX.get("enter").play();
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
	}

	@Override
	public void keyReleased(int k) {

	}

}