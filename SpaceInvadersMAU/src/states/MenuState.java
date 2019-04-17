package states;

import java.awt.Color;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;

import javafx.scene.layout.Background;
import tileMap.MenuBackground;


public class MenuState extends GameState {

	private int currentChoice = 0;
	private int textLength;
	private String[] options = { "Play", "Help", "Leaderboards", "Quit" };
	
	private MenuBackground bg;
	
	public MenuState(GameStateManager gsm) {
		this.gsm = gsm;
		// Initializing variables.
		init();
	}

	@Override
	public void init() {

	
		try {
			
			bg = new MenuBackground("/images/menuBG.gif", 1);
			font = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/ARCADE_I.TTF")).deriveFont(25f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/ARCADE_I.TTF")));
		
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
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
		
		
		g.setFont(font);
		//g.setColor(Color.BLACK);
//		g.fillRect(0, 0, WIDTH, HEIGHT);

		for (int i = 0; i < options.length; i++) {
			if (i == currentChoice) {
				g.setColor(Color.WHITE);
			} else {
				g.setColor(Color.GREEN);
			}
			textLength = (int) g.getFontMetrics().getStringBounds(options[i], g).getWidth();
			g.drawString(options[i], (700 - textLength) / 2, (700 / 2) - 60 + 60 * i);
			
		}
		
	}

	private void select() {
		if (currentChoice == 0) {
			gsm.setState(GameStateManager.LEVEL1STATE);
		}
		if (currentChoice == 1) {
			// help
		}
		if (currentChoice == 2) {
			System.exit(0);
		}
	}

	public void keyPressed(int k) {
		if (k == KeyEvent.VK_ENTER) {
			select();
		}
		if (k == KeyEvent.VK_UP) {
			currentChoice--;
			if (currentChoice == -1) {
				currentChoice = options.length - 1;
			}
		}
	}

	@Override
	public void keyReleased(int k) {

	}

}