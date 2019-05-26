package states;

public class GameStateManager {

	private GameState[] gameStates;
	private int currentState;
	private int difficulty = 1000;

	// States
	public static final int NUMGAMESTATES = 6;

	public static final int MENUSTATE = 0;
	public static final int PLAYINGSTATE = 1;
	public static final int GAMEOVERSTATE = 2;
	public static final int LEADERBOARDSTATE = 3;
	public static final int HELPSTATE = 4;
	public static final int CHARACTERSELECTIONSTATE = 5;

	public GameStateManager() {
		gameStates = new GameState[NUMGAMESTATES];
		currentState = MENUSTATE;
		loadState(currentState);

	}

	public void setHigherDifficulty() {
		difficulty -= 200;
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
	}

	private void unloadState(int state) {
		gameStates[state] = null;
	}

	public void setState(int state) {
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
	}

	public void update() {
		try {
			gameStates[currentState].update();
		} catch (Exception e) {
		}
	}

	public void draw(java.awt.Graphics2D g) {
		try {
			gameStates[currentState].draw(g);
		} catch (Exception e) {
		}
	}

	public void keyPressed(int k) {
		gameStates[currentState].keyPressed(k);
	}

	public void keyReleased(int k) {
		gameStates[currentState].keyReleased(k);
	}

}