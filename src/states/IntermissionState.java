package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import main.GamePanel;
import tileMap.MenuBackground;

/**
 * Class works as an information state between 
 * each state. Tells the player the level 
 * they're on and other information. 
 * 
 * @author Hannes Granberg & Gustav Georgsson
 *
 */

public class IntermissionState extends GameState {

	private GameStateManager gsm;

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

	}

	public void draw(Graphics2D g) {
		bg.draw(g);
		g.setFont(font);
		String stringCounter = String.valueOf(STATECOUNTER);



		String[] text = { "Level " + stringCounter,"Arrow Keys to move, space to shoot", "Use X and S for Power-ups" };
		String[] text2 = { "Level " + stringCounter,"Prepare yourself" };
		String[] text3 = { "Boss Level", "Prepare yourself" };

		if (STATECOUNTER == 1) {

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


		if (STATECOUNTER == 2 ||STATECOUNTER == 5) {



			for (int i = 0; i < text3.length; i++) {

				if (i == 0) {
					g.setColor(new Color(0, 255, 0, fontAlpha));
				} else {
					g.setFont(smallFont);
					g.setColor(new Color(255, 0, 0, fontAlpha));
				}

				textLength = (int) g.getFontMetrics().getStringBounds(text3[i], g).getWidth();
				g.drawString(text3[i], (WIDTH - textLength) / 2, (700 / 2) - 30 + 60 * i);

			}

			lowerValue();

		} else  if (STATECOUNTER > 1){

			for (int i = 0; i < text2.length; i++) {

				if (i == 0) {
					g.setColor(new Color(0, 255, 0, fontAlpha));
				} else {
					g.setFont(smallFont);
					g.setColor(new Color(255, 0, 0, fontAlpha));
				}

				textLength = (int) g.getFontMetrics().getStringBounds(text2[i], g).getWidth();
				g.drawString(text2[i], (WIDTH - textLength) / 2, (700 / 2) - 30 + 60 * i);

			}
			lowerValue();
		}
	}



	private void lowerValue() {

		pause -= 2;
		if (pause < 0) {
			fontAlpha -= 3;
			if (fontAlpha < 0 && pause < 0) {
				if (STATECOUNTER == 2 || STATECOUNTER == 5)
					gsm.setState(6);
				else {
					gsm.setState(1);
				}

			}
		}
	}

	public void keyPressed(int k) {


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
		// TODO Auto-generated method stub

	}

}