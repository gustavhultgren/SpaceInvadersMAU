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
import java.util.HashMap;

import javax.swing.ImageIcon;
import tileMap.*;
import javafx.scene.layout.Background;
import audio.AudioPlayer;

public class MenuState extends GameState {

	private int currentChoice = 0;
	private int textLength;
	private String[] options = { "Play", "Help", "Leaderboards", "Quit" };
	
	private AudioPlayer bgMusic;
	private HashMap<String, AudioPlayer> soundFX;
	
	private MenuBackground bg;
	
	public MenuState(GameStateManager gsm) {
		this.gsm = gsm;
		
		bgMusic = new AudioPlayer("/music/si.mp3");
		bgMusic.play();
		
		soundFX = new HashMap<String, AudioPlayer>();
		soundFX.put("click", new AudioPlayer("/music/sfx_click.mp3"));
		soundFX.put("enter", new AudioPlayer("/music/sfx_enter.mp3"));
		
		// Initializing variables.
		init();
	}

	@Override
	public void init() {
	
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/ARCADE_I.TTF")).deriveFont(25f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/ARCADE_I.TTF")));
			bg = new MenuBackground("/images/BackgroundTest.png", 2.0);
			bg.setVector(-0.2, 0);
			
			
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
			gsm.setState(GameStateManager.PLAYINGSTATE);
		}
		if (currentChoice == 1) {
			// help
		}
		if (currentChoice == 2) {
			gsm.setState(GameStateManager.LEADERBOARDSTATE);
		}
		if (currentChoice == 3) {
			System.exit(0);
		}
	}

	public void keyPressed(int k) {
		if (k == KeyEvent.VK_ENTER) {
			select();
			soundFX.get("enter").play();
		}
		if (k == KeyEvent.VK_UP) {
			currentChoice--;
			if (currentChoice == -1) {
				currentChoice = options.length - 1;
			}
			soundFX.get("click").play();
		}
		if (k == KeyEvent.VK_DOWN) {
			currentChoice++;
			if (currentChoice == options.length) {
				currentChoice = 0;
			}
			soundFX.get("click").play();
		}
	}

	@Override
	public void keyReleased(int k) {

	}

}