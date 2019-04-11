package gameSate;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Bättre och förkortad version av MainPanel
 * 
 * @author hannesgranberg
 *
 */

public class MenuState extends JPanel implements MouseListener, KeyListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;

	// Dimensions

	private static final int WIDTH = 700;
	private static final int HEIGHT = 700;

	// Text på skärmen

	private JLabel title = new JLabel("Space Invaders MAU");
	private JLabel[] mainMenuLabels = new JLabel[4];

	// Olika fonter, olika storlek

	private Font HUD_FONT;
	private Font HUD_FONT_BIG;
	private Font HUD_FONT_SMALL;

	// bakgrund, en gif

	private URL url;
	private Icon gif;
	private JLabel background;

	// vad som ska markeras (flummigt)

	private int operation = -1;

	// bakgrundsbild

	private BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
	private Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0),
			"blank cursor");

	// muspekaren fungerar

	private boolean cursorWorking = true;

	public MenuState() throws MalformedURLException {

		mainMenuLabels[0] = new JLabel("New Game");
		mainMenuLabels[1] = new JLabel("Leaderboard");
		mainMenuLabels[2] = new JLabel("Exit");
		mainMenuLabels[3] = new JLabel("Help");

		// instansierar och lägger till bakgrunden som en gif

		url = new URL("file:///Users\\gusta\\Desktop\\Javaprgm skola/shipsshooting.gif");
		gif = new ImageIcon(url);
		background = new JLabel(gif);

		// Fönster

		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setLayout(null);
		initFonts();
		setBackground(Color.BLACK);

		/*
		 * Lägger till huvudtiteln
		 */

		add(title);
		title.setFont(HUD_FONT_BIG);
		title.setBounds(85, 20, 700, 200);
		title.setForeground(Color.GREEN);

		/**
		 * Lägger till new game-knappen
		 */

		add(mainMenuLabels[0]);
		mainMenuLabels[0].setFont(HUD_FONT);
		mainMenuLabels[0].setBounds(250, 300, 250, 40);
		mainMenuLabels[0].setVisible(true);
		mainMenuLabels[0].setForeground(Color.WHITE);
		mainMenuLabels[0].setFocusable(true);

		/*
		 * Lägger till leaderboard-knappen
		 */

		add(mainMenuLabels[1]);
		mainMenuLabels[1].setFont(HUD_FONT);
		mainMenuLabels[1].setBounds(210, 400, 300, 40);
		mainMenuLabels[1].setForeground(Color.WHITE);
		mainMenuLabels[1].setFocusable(true);

		/*
		 * Lägger till exit-knappen
		 */

		add(mainMenuLabels[2]);
		mainMenuLabels[2].setFont(HUD_FONT);
		mainMenuLabels[2].setBounds(290, 500, 110, 40);
		mainMenuLabels[2].setForeground(Color.WHITE);
		mainMenuLabels[2].setFocusable(true);

		/*
		 * Lägger till help-knappen
		 */

		add(mainMenuLabels[3]);
		mainMenuLabels[3].setFont(HUD_FONT_SMALL);
		mainMenuLabels[3].setBounds(600, 650, 100, 50);
		mainMenuLabels[3].setForeground(Color.WHITE);
		mainMenuLabels[3].setFocusable(true);

		/*
		 * Lägger till en bakgrund som en gif
		 */

		add(background);
		background.setVisible(true);
		background.setBounds(0, 0, 700, 700);

		addListeners();
		setSelected(operation);

	}

	/**
	 * Lägger till lyssnare
	 */

	public void addListeners() {

		for (int i = 0; i < mainMenuLabels.length; i++) {
			mainMenuLabels[i].addMouseListener(this);
			mainMenuLabels[i].addKeyListener(this);
			mainMenuLabels[i].addMouseMotionListener(this);

		}
		addKeyListener(this);
		addMouseMotionListener(this);

	}

	/**
	 * Initiatear varje font
	 */

	public void initFonts() {
		try {
			HUD_FONT = Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/ARCADE_I.TTF")).deriveFont(27f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/ARCADE_I.TTF")));

			HUD_FONT_BIG = Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/ARCADE_I.TTF")).deriveFont(30f);
			GraphicsEnvironment ge2 = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/ARCADE_I.TTF")));

			HUD_FONT_SMALL = Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/ARCADE_I.TTF")).deriveFont(22f);
			GraphicsEnvironment ge3 = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/ARCADE_I.TTF")));

		} catch (IOException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metod som lyssnar ifall man klickar på musen
	 * 
	 * @param e ett MouseEvent objekt
	 */

	public void mouseClicked(MouseEvent e) {

		if (cursorWorking == true) {

			if (e.getComponent() == mainMenuLabels[0]) {
				mainMenuLabels[1].requestFocusInWindow();
				System.out.println("new game");

			}
			if (e.getComponent() == mainMenuLabels[1]) {
				mainMenuLabels[2].requestFocusInWindow();
				System.out.println("leaderboard");

			}
			if (e.getComponent() == mainMenuLabels[2]) {
				mainMenuLabels[2].requestFocusInWindow();

				System.exit(0);

			}
			if (e.getComponent() == mainMenuLabels[3]) {
				mainMenuLabels[2].requestFocusInWindow();
				System.out.println("help");

			}
		}

	}

	/**
	 * Metod som lyssnar ifall man klickar på en mus
	 * 
	 * @param e MouseEvent objekt
	 */

	public void mousePressed(MouseEvent e) {

	}

	/**
	 * Metod som lyssnar ifall man släpper ett musklick
	 * 
	 * @param e MouseEvent objekt
	 */

	public void mouseReleased(MouseEvent e) {

	}

	/**
	 * Metod som lyssnar ifall musen rör sig in i ett objekt
	 * 
	 * @param e MouseEvent objekt
	 */

	public void mouseEntered(MouseEvent e) {

		Component event = e.getComponent();
		if (cursorWorking == true) {

			for (int i = 0; i < mainMenuLabels.length; i++) {
				if (event == mainMenuLabels[i]) {
					setSelected(i);
				}
			}

		}
	}

	/**
	 * Metod som lyssnar ifall musen lämnar ett objekt
	 * 
	 * @param e ett MouseEvent objekt
	 */

	public void mouseExited(MouseEvent e) {

		Component comp = e.getComponent();

		if (cursorWorking == true) {

			for (int i = 0; i < mainMenuLabels.length; i++) {
				if (comp == mainMenuLabels[i]) {
					setOperation(i);
					mainMenuLabels[i].setForeground(Color.WHITE);

				}

			}

		}

	}

	/**
	 * Metod som lyssnar ifall man skriver in något med tangentbordet
	 * 
	 * @param e
	 */

	public void keyTyped(KeyEvent e) {

	}

	/**
	 * Metod som körs ifall man klickar på en tangent på tangentbordet
	 */

	public void keyPressed(KeyEvent e) {

		int key = e.getKeyCode();

		// Om man tittar på piltangenten - Ner

		if (key == KeyEvent.VK_DOWN) {

			setCursor(blankCursor);
			cursorWorking = false;

			operation++;

			if (operation > 3) {
				operation = 0;
			}
			setSelected(operation);

		}

		// Om man klickar på piltangenten - Upp

		if (key == KeyEvent.VK_UP) {

			setCursor(blankCursor); // döljer muspekaren och gör så att man inte kan använda den
			cursorWorking = false;

			operation--;

			if (operation < 0) {
				operation = 3;
			}

			setSelected(operation);

		}

		// Om man klickar på Space-tangenten

		if (key == KeyEvent.VK_SPACE) {

			if (getOperation() == 0) {
				System.out.println("New Game");
			}

			if (getOperation() == 1) {
				System.out.println("Leaderboard");
			}

			if (getOperation() == 2) {
				System.exit(0);
			}

			if (getOperation() == 3) {
				System.out.println("Help");
			}

		}

		// Om man klickarp på Enter-tangenten

		if (key == KeyEvent.VK_ENTER) {

			setCursor(blankCursor);
			cursorWorking = false;

		}

		// Om man klickar på Shift-tangenten

		if (key == KeyEvent.VK_SHIFT) {
			setCursor(Cursor.getDefaultCursor());
			cursorWorking = true;
		}

	}

	/**
	 * Metod som lyssnar ifall
	 * 
	 * @param e ett KeyEvent objekt
	 */

	public void keyReleased(KeyEvent e) {

	}

	/**
	 * Hämtar den nuvarande operationen som motsvarar markeringen i menyn
	 * 
	 * @return en integer som fungerar som "state"
	 */

	public int getOperation() {
		return operation;
	}

	/**
	 * Bestämmer operationen
	 * 
	 * @param operation en integer
	 */

	public void setOperation(int operation) {
		this.operation = operation;
	}

	/**
	 * Markerar det man har valt
	 * 
	 * @param operation
	 */

	public void setSelected(int operation) {

		for (int i = 0; i < mainMenuLabels.length; i++) {

			if (operation == i) {
				mainMenuLabels[i].setForeground(Color.GREEN);

			} else {
				mainMenuLabels[i].setForeground(Color.WHITE);

			}
		}

	}

	/**
	 * Metod som lyssnar ifall man drar musen samtidigt som man håller ner en knapp
	 * 
	 * @param e MouseEvent objekt
	 */

	public void mouseDragged(MouseEvent e) {

	}

	/**
	 * Metod som lyssnar ifall musen har flyttats
	 * 
	 * @param e MouseEvent objkt
	 */
	public void mouseMoved(MouseEvent e) {

		// Sätter muspekaren tillbaka till default-muspekaren

		setCursor(Cursor.getDefaultCursor());
		cursorWorking = true;

	}

}
