package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import tileMap.MenuBackground;
/**
 * Klass som fungerar som ett intermission mellan varje ny nivå som börjar.
 * 
 * @author hannesgranberg
 *
 */

public class IntermissionState extends GameState {

	private GameStateManager gsm;

	
	private int difficulty = 20;

	private int textLength;

	private int fontAlpha = 255;

	private int pause = 255;
	private MenuBackground bg;
	
	public IntermissionState(GameStateManager gsm) {
		this.gsm = gsm;

		init();

	}

	public void init() {

		try {
			// initializes the font
			font = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/ARCADE_I.TTF")).deriveFont(30f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/ARCADE_I.TTF")));

			smallFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/ARCADE_I.TTF")).deriveFont(17f);
			GraphicsEnvironment ge2 = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge2.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/ARCADE_I.TTF")));

			bg = new MenuBackground("/images/playingBG.png", 1);
			bg.setVector(-0.4, 0);
			STATECOUNTER++;
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		}

	}

	public void update() {
		bg.update();
		
		// TODO Auto-generated method stub

	}

	public void draw(Graphics2D g) {
		String stringCounter = String.valueOf(STATECOUNTER);
		String[] text = { "Level " + stringCounter, "Prepare yourself"};

		bg.draw(g);
		g.setFont(font );


		for (int i = 0; i < text.length; i++) {

			if (i == 0) {
				g.setColor(new Color(0, 255, 0, fontAlpha));
			} else {
				g.setFont(smallFont);
				g.setColor(new Color(255, 0, 0, fontAlpha));
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