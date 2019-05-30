package states;

import main.GamePanel;

public class GameStateManager {

	private GameState[] gameStates;
	public static int CURRENTSTATE;
	private int difficulty = 1000;

	// States
	public static final int NUMGAMESTATES = 7;

	public static final int MENUSTATE = 0;
	public static final int PLAYINGSTATE = 1;
	public static final int GAMEOVERSTATE = 2;
	public static final int LEADERBOARDSTATE = 3;
	public static final int HELPSTATE = 4;
	public static final int CHARACTERSELECTIONSTATE = 5;
	public static final int BOSSTATE = 6;
	
	private GamePanel gp;

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

}