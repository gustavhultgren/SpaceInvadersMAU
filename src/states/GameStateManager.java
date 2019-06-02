package states;

import entity.Player;
import main.GamePanel;

/**
 * This class represents a manager which handles which state the user is in.
 * The GameStateManager updates and draw the current active state in GamePanel.
 * @author Gustav Georgsson, Gustav Hultgren, Tom Eriksson
 *
 */
public class GameStateManager {

	private GameState[] gameStates;
	public static int CURRENTSTATE;
	private int difficulty = 1000;
	protected static int WIDTH = 700;
	protected static int HEIGHT = 700;
	protected static int PLAYER_INIT_X = WIDTH / 2 - 20;
	protected static int PLAYER_INIT_Y = 652;

	// States
	public static final int NUMGAMESTATES = 8;

	public static final int MENUSTATE = 0;
	public static final int PLAYINGSTATE = 1;
	public static final int GAMEOVERSTATE = 2;
	public static final int LEADERBOARDSTATE = 3;
	public static final int HELPSTATE = 4;
	public static final int CHARACTERSELECTIONSTATE = 5;
	public static final int BOSSTATE = 6;
	public static final int INTERMISSIONSTATE = 7;
	
	private GamePanel gp;
	public static Player player = new Player(PLAYER_INIT_X, PLAYER_INIT_Y, 18, 3);

	public GameStateManager(GamePanel gp) {
		this.gp = gp;
		gameStates = new GameState[NUMGAMESTATES];
		CURRENTSTATE = MENUSTATE;
		loadState(CURRENTSTATE);

	}

	public void setHigherDifficulty() {
		difficulty *= 0.7;
	}

	public int getDifficulty() {
		return difficulty;
	}

	private void loadState(int state) {
		if (state == MENUSTATE)
			gameStates[state] = new MenuState(this);

		if (state == PLAYINGSTATE)
			gameStates[state] = new PlayingState(this);

		if (state == GAMEOVERSTATE)
			gameStates[state] = new GameOverState(this);

		if (state == LEADERBOARDSTATE)
			gameStates[state] = new LeaderBoardState(this);

		if (state == HELPSTATE)
			gameStates[state] = new HelpState(this);

		if (state == CHARACTERSELECTIONSTATE)
			gameStates[state] = new CharacterSelectionState(this);
		
		if (state == BOSSTATE)
			gameStates[state] = new BossState(this);
		
	
		if (state == INTERMISSIONSTATE)
				gameStates[state] =new IntermissionState (this);
		
	}

	private void unloadState(int state) {
		gameStates[state] = null;
	}

	public void setState(int state) {
		unloadState(CURRENTSTATE);
		CURRENTSTATE = state;
		loadState(CURRENTSTATE);
	}

	public void update() {
		try {
			gameStates[CURRENTSTATE].update();
		} catch (Exception e) {
		}
	}

	public void draw(java.awt.Graphics2D g) {
		try {
			gameStates[CURRENTSTATE].draw(g);
		} catch (Exception e) {
		}
	}

	public void keyPressed(int k) {
		gameStates[CURRENTSTATE].keyPressed(k);
	}

	public void keyReleased(int k) {
		gameStates[CURRENTSTATE].keyReleased(k);
	}
	
	public void setRunning(boolean b) {
		gp.setRunning(b);
	}
	
	public Player getPlayer() {
		return player;
	}

}