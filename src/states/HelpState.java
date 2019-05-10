package states;

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

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import javafx.scene.layout.Background;
import tileMap.MenuBackground;

public class HelpState extends GameState {

	private GameStateManager gsm;
	private MenuBackground menuBg;
	private Font font;
	private BufferedImage image;

	public HelpState(GameStateManager gsm) {
		this.gsm = gsm;

		init(); // calls the init-function

	}

	/**
	 * Function that initializes certain variables such as the menubackground and
	 * the font
	 */

	public void init() {

		try {

			//initializes the font
			font = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/ARCADE_I.TTF"))
					.deriveFont(15f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(
					Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/ARCADE_I.TTF")));

		} catch (IOException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		}

		
		try {
			//initializes the "gubbe" image
			image = ImageIO.read(new File("resources/images/gubbe2.jpg"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update() {
//		menuBg.update();

	}

	public void draw(Graphics2D g) {	
		// Draws the first box with text
		g.setFont(font);

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 700, 700); // draws a black background (will be changed to a gif soon)

		// Draws the green rectangle around the text as well as setting the stroke to 3 (thickness of line)
		g.setStroke(new BasicStroke(3));
		g.setColor(Color.GREEN);
		g.drawRect(80, 50, 540, 250);

		// Sets the color to white and draws the String's inside the green rectangle
		g.setColor(Color.WHITE);
		String aboutText[] = { "This game is the same as the", "original Space Invaders with a",
				"touch of Malmo University.The ", "goal of this game is to make ", "you feel some nostalgia as well",
				"as being able to defeat your", "teachers." };

		// X and Y variables for the location of the text
		int aboutTextX = 100;
		int aboutTextY = 90;

		// Draws the text inside the rectangle
		for (int i = 0; i < aboutText.length; i++) {

			g.drawString(aboutText[i], aboutTextX, aboutTextY);
			aboutTextY += 30; // moves the next line of text 30 pixels down
		}

		// Draws the second rectangle below the first one
		g.setColor(Color.GREEN);
		g.drawRect(80, 350, 540, 250);

		// sets the color to white
		g.setColor(Color.WHITE);
		String aboutText2[] = { "* Use the arrow keys to move back", "and forth.", "* Press [Spacebar] to shoot.",
				"* If you lose all lives you lose", "* Gather points by shooting", "enemies" };

		aboutTextY = 390;
		for (int i = 0; i < aboutText2.length; i++) {
			g.drawString(aboutText2[i], aboutTextX, aboutTextY);
			aboutTextY += 30;
		}

		// sets the color to red and puts the escape text at the bottom of the window
		g.setColor(Color.RED);
		String howToLeave = "> Press [Escape] to go back < ";
		g.drawString(howToLeave, 130, 650);

//		// adds gubbe.jpg in the window
		g.drawImage(image, 600, 610, null);

	}

	public void keyPressed(int k) {

		if (k == KeyEvent.VK_ESCAPE) {
			gsm.setState(GameStateManager.MENUSTATE); 

		}

	}

	public void keyReleased(int k) {

	}

}
