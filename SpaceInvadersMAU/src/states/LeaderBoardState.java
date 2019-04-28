package states;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;

import server.LeaderboardUpdateResponse;
import server.PlayerScore;
import serverTestClasses.Client;

public class LeaderBoardState extends GameState {

	private int currentChoiceOfTable = 0;
	private int currentChoiceInTable = 0;
	private Thread readingThread;

	private String header = "HIGHSCORE TOP 100";
	private String[] subHeader = { "RANK", "NAME", "SCORE" };
	private String[] options = { "MAU", "WORLD WIDE" };
	private Font headerFont;
	private PlayerScore[] scoreList;
	
	private int yViewCord = 0;

	public LeaderBoardState(GameStateManager gsm) {
		this.gsm = gsm;
		getScore();
		// Initializing variables.
		init();
	}
	
	public synchronized void getScore() {
		scoreList = client.getScoreList();
	}

//	public PlayerScore[] readPS(String ip, int port) {
//
//		LeaderboardUpdateResponse lbur = null;
//		try (Socket socket = new Socket(ip, port);
//				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
//				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());) {
//			oos.writeObject(new Object());
//			oos.flush();
//			lbur = (LeaderboardUpdateResponse) ois.readObject();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return lbur.getScoreList();
//	}

//	public void readScoreFromFile() {
////		LinkedList<PlayerScore> list = null;
//		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("serverFiles/filtest.dat"));) {
//
//			scoreList = (LinkedList<PlayerScore>) ois.readObject();
//
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
////		return list;
//	}

//	public void writeListToFile() {
//		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("serverFiles/filtest.dat"));) {
//			oos.writeObject(scoreList);
//			oos.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

//	public void add(PlayerScore p) {
//		scoreList.add(p);
//		Collections.sort(scoreList);
//	}
//
//	public void startReadThread() {
//		Runnable r = new Runnable() {
//			public void run() {
//				scoreList = readPS("127.0.0.1", 3500);
//			}
//		};
//		readingThread = new Thread(r);
//		readingThread.start();
//	}

	@Override
	public void init() {

//		startReadThread();
//		
//		for (int i = 0; i < scoreList.length; i++) {
//			scoreList[i] = new PlayerScore("Tom", 10000);
//		}
		try {
			headerFont = Font.createFont(Font.TRUETYPE_FONT, new File("res/fonts/ARCADE_I.TTF")).deriveFont(40f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("res/fonts/ARCADE_I.TTF")));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update() {

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
		g.setFont(headerFont);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		g.setColor(Color.GRAY.darker());
		g.setStroke(new BasicStroke(2));
		g.drawLine(10, y + 75, WIDTH - 10, y + 75);
		g.setStroke(new BasicStroke(1));

		g.setColor(Color.RED);
		g.drawString(header, 20, y + 50);
		g.setFont(font);

		g.setColor(Color.WHITE);

		for (int i = 0; i < options.length; i++) {
			if (i == currentChoiceOfTable) {
				g.setColor(Color.YELLOW);
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
		}
		if (k == KeyEvent.VK_RIGHT) {
			currentChoiceOfTable++;
			if (currentChoiceOfTable == options.length) {
				currentChoiceOfTable = 0;
			}
		}
		if (k == KeyEvent.VK_UP) {
			currentChoiceInTable--;
			if (currentChoiceInTable == -1) {
				currentChoiceInTable = 0;
			}
			if (isFirstSelectionInFrame(currentChoiceInTable, yViewCord)) {
				yViewCord += 40;
			}
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
		}
	}

	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub

	}

}
