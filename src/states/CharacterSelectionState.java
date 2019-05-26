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

import tileMap.MenuBackground;

public class CharacterSelectionState extends GameState {

	private int currentChoice;
	private int textLength;

//	private Player player;
	private MenuBackground bg;

	private String title;
	private String char1Name;
	private String char2Name;

	private BufferedImage playerImage;
	private BufferedImage playerImage2;

	public CharacterSelectionState(GameStateManager gsm) {
		this.gsm = gsm;

		init();

	}

	@Override
	public void init() {
		try {
			bg = new MenuBackground("/images/background.png", 1);
			bg.setVector(-0.4, 0);

			font = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/ARCADE_I.TTF")).deriveFont(34f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/ARCADE_I.TTF")));
			
			smallFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/ARCADE_I.TTF")).deriveFont(17f);
			GraphicsEnvironment ge2 = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/ARCADE_I.TTF")));
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			playerImage = ImageIO.read(new File("resources/images/playerImage.png"));
			playerImage2 = ImageIO.read(new File("resources/images/playerImage2.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		currentChoice = 1;
		title = "CHOOSE CHARACTER";
		char1Name = "CHAR 1";
		char2Name = "CHAR 2";
		
	}

	@Override
	public void update() {
		bg.update();
		System.out.println(currentChoice);

	}

	@Override
	public void draw(Graphics2D g) {
		bg.draw(g);

		g.setFont(font);

		textLength = (int) g.getFontMetrics().getStringBounds(title, g).getWidth();

		g.drawString(title, (700 - textLength) / 2, 200);

		g.drawImage(playerImage, 130, 300, 200, 200, null);
		g.drawImage(playerImage2, 370, 300, 200, 200, null);
		
		writeCharacterNames(g);
		drawLines(g);

	}
	
	public void drawLines(Graphics2D g) {
		g.setStroke(new BasicStroke(3));
		
		if(currentChoice == 1) {
			g.drawLine(170, 495, 260, 495);
		} else if (currentChoice == 2) {
			g.drawLine(430, 495, 520, 495);
		}
	}
	
	public void writeCharacterNames(Graphics2D g) {
			g.setFont(smallFont);
			g.drawString(char1Name, 170, 300);
			g.drawString(char2Name, 420, 300);
	}

	private void select() {
		if (currentChoice == 1) {
			player.setPlayerImage(1);

		} else if (currentChoice == 2) {
			player.setPlayerImage(2);

		}

		gsm.setState(1);
	}

	@Override
	public void keyPressed(int k) {
		if (k == KeyEvent.VK_ENTER) {
			select();
			soundFX.get("enter").play();
		}

		if (k == KeyEvent.VK_RIGHT) {
			currentChoice++;

			System.out.println(currentChoice);

			// code that visualizes change

			soundFX.get("click").play();
		}

		if (k == KeyEvent.VK_LEFT) {
			currentChoice--;

			System.out.println(currentChoice);

			// code that visualizes change

			soundFX.get("click").play();
		}

	}

	@Override
	public void keyReleased(int k) {

	}

}
