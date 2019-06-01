package states;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import audio.AudioPlayer;
import entity.Player;
import main.GamePanel;
import server.PlayerScore;
import tileMap.MenuBackground;

public class LeaderBoardState extends GameState {

	private int currentChoiceOfTable = 0;
	private int currentChoiceInTable = 0;
	private Thread readingThread;

	private String header = "HIGHSCORE TOP 100";
	private String[] subHeader = { "RANK", "NAME", "SCORE" };
	private String[] options = { "MAU", "WORLD WIDE" };
	private Font headerFont;
	private PlayerScore[] scoreList;
	private PlayerScore[] scoreListMau;

	private int yViewCord = 0;
	private MenuBackground bg;

	public LeaderBoardState(GameStateManager gsm) {
		this.gsm = gsm;
		// Initializing variables.
		init();
	}

	public synchronized void getScore() {
		client.requestList();
		scoreList = client.getScoreList();
		scoreListMau = client.getScoreListMau();
	}

	@Override
	public void init() {
		getScore();
		try {
			bg = new MenuBackground("/images/background.png", 1);
			bg.setVector(-0.4, 0);
			headerFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/ARCADE_I.TTF")).deriveFont(40f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/ARCADE_I.TTF")));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		}

		soundFX.put("click", new AudioPlayer("/music/sfx_click.mp3"));
		soundFX.put("enter", new AudioPlayer("/music/sfx_enter.mp3"));
	}

	double vol;

	@Override
	public void update() {
		bg.update();
	}

	private void select() {
		if (currentChoiceOfTable == 0) {
			gsm.setState(GameStateManager.PLAYINGSTATE);
		}
		if (currentChoiceOfTable == 1) {
			// help
		}
		if (currentChoiceOfTable == 2) {
			System.exit(0);
		}
	}

	private boolean isLastSelectionInFrame(int currentChoiceInTable, int yViewCord) {
		if ((-yViewCord + 220 + currentChoiceInTable * 40 - 20) >= -yViewCord + HEIGHT) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isFirstSelectionInFrame(int currentChoiceInTable, int yViewCord) {
		if ((currentChoiceInTable * 40) <= -yViewCord - 20) {
			return true;
		} else {
			return false;
		}
	}

	private void drawBackground(Graphics2D g, int y) {

		bg.draw(g);
		g.setFont(headerFont);
		g.setColor(Color.GRAY.darker());
		g.setStroke(new BasicStroke(2));
		g.drawLine(10, y + 75, WIDTH - 10, y + 75);
		g.setStroke(new BasicStroke(1));

		g.setColor(Color.RED);
		g.drawString(header, 20, y + 50);
		g.setFont(font);

		g.setColor(Color.WHITE);

		for (int i = 0; i < options.length; i++) {

			int length = (int) g.getFontMetrics().getStringBounds(options[i], g).getWidth();

			if (i == currentChoiceOfTable) {
				g.setColor(Color.YELLOW);
				g.setStroke(new BasicStroke(2));
				g.drawLine(120 + (i * 250), 120 + yViewCord, 120 + (i * 250) + length, 120 + yViewCord);
			} else {
				g.setColor(Color.WHITE);
			}
			g.drawString(options[i], 120 + i * 250, y + 115);

		}

		g.setColor(Color.WHITE);

		for (int i = 0; i < subHeader.length; i++) {
			g.drawString(subHeader[i], 40 + i * 240, y + 170);
		}

		g.setColor(Color.GRAY.darker());
		g.setStroke(new BasicStroke(2));
		g.drawLine(10, y + 130, WIDTH - 10, y + 130);
		g.setStroke(new BasicStroke(1));
		g.setColor(Color.WHITE);
	}

	@Override
	public void draw(Graphics2D g) {

		drawBackground(g, yViewCord);

		if (currentChoiceOfTable == 0) {
			for (int i = 0; i < scoreListMau.length; i++) {

				for (int j = 0; j < 3; j++) {

					if (i == currentChoiceInTable) {
						g.setColor(Color.YELLOW);

					} else {
						g.setColor(Color.WHITE);
					}
					if (j == 0) {
						g.drawString(i + 1 + "th", 40 + j * 240, yViewCord + 220 + i * 40);
					} else if (j == 1) {
						g.drawString(scoreListMau[i].getName(), 40 + j * 240, yViewCord + 220 + i * 40);
					} else {
						g.drawString(scoreListMau[i].getScore() + "", 40 + j * 240, yViewCord + 220 + i * 40);
					}
				}
			}
		} else {
			for (int i = 0; i < scoreList.length; i++) {

				for (int j = 0; j < 3; j++) {

					if (i == currentChoiceInTable) {
						g.setColor(Color.YELLOW);
					} else {
						g.setColor(Color.WHITE);
					}
					if (j == 0) {
						g.drawString(i + 1 + "th", 40 + j * 240, yViewCord + 220 + i * 40);
					} else if (j == 1) {
						g.drawString(scoreList[i].getName(), 40 + j * 240, yViewCord + 220 + i * 40);
					} else {
						g.drawString(scoreList[i].getScore() + "", 40 + j * 240, yViewCord + 220 + i * 40);
					}
				}
			}
		}

	}

	@Override
	public void keyPressed(int k) {
		if (k == KeyEvent.VK_ENTER) {
			select();
		}
		if (k == KeyEvent.VK_LEFT) {
			currentChoiceOfTable--;
			if (currentChoiceOfTable == -1) {
				currentChoiceOfTable = options.length - 1;
			}
			soundFX.get("click").play();
		}
		if (k == KeyEvent.VK_RIGHT) {
			currentChoiceOfTable++;
			if (currentChoiceOfTable == options.length) {
				currentChoiceOfTable = 0;
			}
			soundFX.get("click").play();
		}
		if (k == KeyEvent.VK_UP) {
			currentChoiceInTable--;
			if (currentChoiceInTable == -1) {
				currentChoiceInTable = 0;
			}
			if (isFirstSelectionInFrame(currentChoiceInTable, yViewCord)) {
				yViewCord += 40;
			}
			soundFX.get("click").play();
		}
		if (k == KeyEvent.VK_DOWN) {
			currentChoiceInTable++;
			if (currentChoiceInTable == scoreList.length) {
				currentChoiceInTable = scoreList.length - 1;
			}
			if (isLastSelectionInFrame(currentChoiceInTable, yViewCord)
					&& !(currentChoiceInTable == scoreList.length - 1)) {
				yViewCord -= 40;
			}
			soundFX.get("click").play();
		}
		if (k == KeyEvent.VK_ESCAPE) {
			gsm.setState(GameStateManager.MENUSTATE);
		}

		if (k == KeyEvent.VK_MINUS) {
			if (VOLUME <= 0.0) {
				VOLUME = 0;

			} else {
				VOLUME = VOLUME - 0.25;
			}
			GamePanel.setVolume(VOLUME);
			System.out.println("Volym niv�: " + VOLUME);
		}

		if (k == KeyEvent.VK_PLUS) {

			if (VOLUME >= 1.0) {
				VOLUME = 1;
			} else {
				VOLUME = VOLUME + 0.25;
			}
			GamePanel.setVolume(VOLUME);
			System.out.println("Volym niv�: " + VOLUME);
		}

		if (k == KeyEvent.VK_M) {
			if (VOLUME != 0) {
				VOLUME = 0;
			} else {
				VOLUME = 1;
			}
			GamePanel.setVolume(VOLUME);
			System.out.println("Volym niv�: " + VOLUME);
		}
	}

	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub

	}

}
