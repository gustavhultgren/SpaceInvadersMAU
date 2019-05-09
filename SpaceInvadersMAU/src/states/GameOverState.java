package states;

import java.awt.Color;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import server.PlayerScore;
import tileMap.MenuBackground;



public class GameOverState extends GameState {

	private String[] options = { "YES", "NO" };

	private int currentChoice = 0;
	private int frameCounter = 0;
	private String playerName = "";
	private MenuBackground bg;

	public GameOverState(GameStateManager gsm) {
		try {
			bg = new MenuBackground("/images/background.png", 1);
			bg.setVector(-0.4, 0);

		} catch (Exception e) {
			e.printStackTrace();
		}
		this.gsm = gsm;
	}

	@Override
	public void init() {
		
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		bg.update();
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(Graphics2D g) {
		bg.draw(g);
//		g.setColor(Color.BLACK);
//		g.fillRect(0, 0, 700, 700);
		g.setColor(Color.WHITE);
		
		printNameToShort(g);
		String gameOver = "GAME OVER";
		
		int length;
		try {
			Font tempFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/ARCADE_I.TTF"))
					.deriveFont(Font.PLAIN, 60);
			g.setFont(tempFont);
			g.setColor(Color.RED);
			length = (int) g.getFontMetrics().getStringBounds(gameOver, g).getWidth();
			g.drawString(gameOver, (700 - length) / 2, (700 / 2) - 150);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}

		g.setFont(font);
		g.setColor(Color.WHITE);
		String finalScore = "FINAL SCORE: ";
		length = (int) g.getFontMetrics().getStringBounds(finalScore, g).getWidth();
		g.drawString(finalScore, (700 - length) / 2, (700 / 2) - 50);

		Font tempFont;
		try {
			tempFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/ARCADE_I.TTF")).deriveFont(Font.PLAIN,
					50);
			g.setFont(tempFont);
			g.setColor(Color.RED);
			String name = "Name: ";
			length = (int) g.getFontMetrics().getStringBounds(name, g).getWidth();
			g.drawString(name, (700 - length) / 2 - 100, (700 / 2) + 50);
			g.setColor(Color.WHITE);

			g.drawString(playerName, (700) / 2 - 15, (700 / 2) + 50);
		} catch (FontFormatException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		if (frameCounter < 30) {
			g.setColor(Color.WHITE);
			g.fillRect(330 + playerName.length() * 50, 395, 50, 6);
		} else if (frameCounter >= 30 && frameCounter < 60) {
		} else {
			frameCounter = 0;
		}
		frameCounter++;

		try {
			g.setColor(Color.GREEN);

			tempFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/ARCADE_I.TTF")).deriveFont(Font.PLAIN,35);
			g.setFont(tempFont);
			String submit = "SUBMIT SCORE";
			length = (int) g.getFontMetrics().getStringBounds(submit, g).getWidth();

			g.drawString(submit, (700 - length) / 2, (700 / 2) + 150);
			g.drawString("" + player.getScore(), 490, (700 / 2) - 50);

		} catch (FontFormatException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		for (int i = 0; i < options.length; i++) {
			if (i == currentChoice) {
				g.setColor(Color.YELLOW);
			} else {
				g.setColor(Color.WHITE);
			}
			try {
				tempFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/ARCADE_I.TTF"))
						.deriveFont(Font.PLAIN, 35);
				g.setFont(tempFont);
				length = (int) g.getFontMetrics().getStringBounds(options[i], g).getWidth();
				g.drawString(options[i], 200 + 200 * i, (700 / 2) + 250);
			} catch (FontFormatException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void printNameToShort(Graphics2D g) {
		if (frameCounter < 30 && playerName.length() < 3) {
			g.setColor(Color.WHITE);
			String nameTooShort = "NAME TOO SHORT";
			int length = (int) g.getFontMetrics().getStringBounds(nameTooShort, g).getWidth();
			g.drawString(nameTooShort, (700 - length) / 2, 75);
		}
	}

	private void select() {
		if (currentChoice == 0) {
			if (playerName.length() < 3) {
			} else {
				String os = client.getOS();
				String ssid = client.getSSID(os);
				if (ssid.equals("eduroam")) {
					client.requestList(new PlayerScore(playerName, player.getScore(), true));
					gsm.setState(GameStateManager.LEADERBOARDSTATE);
				}else {
					client.requestList(new PlayerScore(playerName, player.getScore(), false));
					gsm.setState(GameStateManager.LEADERBOARDSTATE);
				}
			}
		}
//		
//		int length = (int) g.getFontMetrics().getStringBounds(gameOver, g).getWidth();
//		g.drawString(gameOver, (700 - length) / 2, (700 / 2) - 150);
//		
//		String finalScore = "FINAL SCORE: ";
//		length = (int) g.getFontMetrics().getStringBounds(finalScore, g).getWidth();
//		g.drawString(finalScore, (700 - length) / 2, (700 / 2));
//		g.setColor(Color.GREEN);
//		g.drawString("" + player.getScore(), 490, (700 / 2));
		
//		String exitMessage = "PRESS ESC TO GO BACK TO MENU";
//		length = (int) g.getFontMetrics().getStringBounds(exitMessage, g).getWidth();
//		g.drawString(exitMessage, (700 - length) / 2, 600);
	}

	@Override
	public void keyPressed(int k) {
		if (k == KeyEvent.VK_ENTER) {
			select();

		} else if (k == KeyEvent.VK_LEFT) {
			currentChoice--;
			if (currentChoice == -1) {
				currentChoice = 0;
			}
		} else if (k == KeyEvent.VK_RIGHT) {
			currentChoice++;
			if (currentChoice == options.length) {
				currentChoice = 1;
			}
		} else if (k >= KeyEvent.VK_0 && k <= KeyEvent.VK_Z && playerName.length() <= 5) {
			if (k == KeyEvent.VK_A)
				playerName += "A";
			if (k == KeyEvent.VK_B)
				playerName += "B";
			if (k == KeyEvent.VK_C)
				playerName += "C";
			if (k == KeyEvent.VK_D)
				playerName += "D";
			if (k == KeyEvent.VK_E)
				playerName += "E";
			if (k == KeyEvent.VK_F)
				playerName += "F";
			if (k == KeyEvent.VK_G)
				playerName += "G";
			if (k == KeyEvent.VK_H)
				playerName += "H";
			if (k == KeyEvent.VK_I)
				playerName += "I";
			if (k == KeyEvent.VK_J)
				playerName += "J";
			if (k == KeyEvent.VK_K)
				playerName += "K";
			if (k == KeyEvent.VK_L)
				playerName += "L";
			if (k == KeyEvent.VK_M)
				playerName += "M";
			if (k == KeyEvent.VK_N)
				playerName += "N";
			if (k == KeyEvent.VK_O)
				playerName += "O";
			if (k == KeyEvent.VK_P)
				playerName += "P";
			if (k == KeyEvent.VK_Q)
				playerName += "Q";
			if (k == KeyEvent.VK_R)
				playerName += "R";
			if (k == KeyEvent.VK_S)
				playerName += "S";
			if (k == KeyEvent.VK_T)
				playerName += "T";
			if (k == KeyEvent.VK_U)
				playerName += "U";
			if (k == KeyEvent.VK_V)
				playerName += "V";
			if (k == KeyEvent.VK_W)
				playerName += "W";
			if (k == KeyEvent.VK_X)
				playerName += "X";
			if (k == KeyEvent.VK_Y)
				playerName += "Y";
			if (k == KeyEvent.VK_Z)
				playerName += "Z";
			if (k == KeyEvent.VK_1)
				playerName += "1";
			if (k == KeyEvent.VK_2)
				playerName += "2";
			if (k == KeyEvent.VK_3)
				playerName += "3";
			if (k == KeyEvent.VK_4)
				playerName += "4";
			if (k == KeyEvent.VK_5)
				playerName += "5";
			if (k == KeyEvent.VK_6)
				playerName += "6";
			if (k == KeyEvent.VK_7)
				playerName += "7";
			if (k == KeyEvent.VK_8)
				playerName += "8";
			if (k == KeyEvent.VK_9)
				playerName += "9";
			if (k == KeyEvent.VK_0)
				playerName += "0";
		} else if (k == KeyEvent.VK_BACK_SPACE) {
			if (playerName.length() <= 1) {
				playerName = "";
			} else {
				playerName = playerName.substring(0, playerName.length() - 1);
			}
		}
		if(k == KeyEvent.VK_ESCAPE) {
			gsm.setState(0);
			soundFX.get("enter").play();
		}
	}

	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub

	}

}
