package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

/**
 * Klass som fungerar som ett intermission mellan varje ny nivå som börjar.
 * 
 * @author hannesgranberg
 *
 */

public class IntermissionState extends GameState {

	private GameStateManager gsm;

	private Font font_BIG;
	private Font font_SMALL;

	private Color backgroundColor;
	private Color fontGreen;
	private Color fontBlue;

	private int level = 1;
	private int difficulty = 20;

	private int textLength;

	private int backgroundAlpha = 255;
	private int fontAlpha = 255;

	private int pause = 255;

	private String[] text = { "Level " + level, "Difficulty: " + difficulty };

	public IntermissionState(GameStateManager gsm) {
		this.gsm = gsm;

		init();

	}

	public void init() {

		try {

			// initializes the font
			font_BIG = Font.createFont(Font.TRUETYPE_FONT, new File("SpaceInvadersMAU/resources/fonts/ARCADE_I.TTF"))
					.deriveFont(25f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(
					Font.createFont(Font.TRUETYPE_FONT, new File("SpaceInvadersMAU/resources/fonts/ARCADE_I.TTF")));

			// initializes the font
			font_SMALL = Font.createFont(Font.TRUETYPE_FONT, new File("SpaceInvadersMAU/resources/fonts/ARCADE_I.TTF"))
					.deriveFont(18f);
			ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(
					Font.createFont(Font.TRUETYPE_FONT, new File("SpaceInvadersMAU/resources/fonts/ARCADE_I.TTF")));

		} catch (IOException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		}

	}

	public void update() {
		// TODO Auto-generated method stub

	}

	public void draw(Graphics2D g) {

		g.setFont(font_BIG);

		g.setColor(new Color(0, 0, 0, backgroundAlpha));
		g.fillRect(0, 0, WIDTH, HEIGHT);

		for (int i = 0; i < text.length; i++) {

			if (i == 0) {
				g.setColor(new Color(0, 255, 0, fontAlpha));
			} else {
				g.setFont(font_SMALL);
				g.setColor(new Color(0, 0, 255, fontAlpha));
			}

			textLength = (int) g.getFontMetrics().getStringBounds(text[i], g).getWidth();
			g.drawString(text[i], (WIDTH - textLength) / 2, (700 / 2) - 30 + 60 * i);

		}

		lowerValue();

	}

	private void lowerValue() {
		pause -= 2;
		if (pause < 0) {
			fontAlpha -= 3;
			if (fontAlpha < 0) {
				changeToPlaying();
			}
		}
	}

	private void changeToPlaying() {
		gsm.setState(GameStateManager.PLAYINGSTATE);
	}

	public void keyPressed(int k) {
		// TODO Auto-generated method stub

	}

	public void keyReleased(int k) {
		// TODO Auto-generated method stub

	}

}
