package states;

public class GameStateManager {
	
	private GameState[] gameStates;
	private int currentState;
	private int difficulty = 1000;
	
	public static final int NUMGAMESTATES = 5;
	
	public static final int MENUSTATE = 0;
	public static final int PLAYINGSTATE = 1;
	public static final int GAMEOVERSTATE = 2;
	public static final int LEADERBOARDSTATE = 3;
	public static final int BOSS1STATE = 4;
	
	public GameStateManager() {
		
		gameStates = new GameState[NUMGAMESTATES];
		
		currentState = MENUSTATE;
		loadState(currentState);
		
	}
	
	public void setHigherDifficulty() {
		difficulty-=200;
	}
	
	public int getDifficulty() {
		return difficulty;
	}
	
	private void loadState(int state) {
		if(state == MENUSTATE)
			gameStates[state] = new MenuState(this);
		if(state == PLAYINGSTATE)
			gameStates[state] = new PlayingState(this);
		if(state == BOSS1STATE)
			gameStates[state] = new Boss1State(this);
		if(state == GAMEOVERSTATE)
			gameStates[state] = new GameOverState(this);
		if(state == LEADERBOARDSTATE)
			gameStates[state] = new LeaderBoardState(this);
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
		} catch(Exception e) {}
	}
	
	public void draw(java.awt.Graphics2D g) {
		try {
			gameStates[currentState].draw(g);
		} catch(Exception e) {}
	}
	
	public void keyPressed(int k) {
		gameStates[currentState].keyPressed(k);
	}
	
	public void keyReleased(int k) {
		gameStates[currentState].keyReleased(k);
	}
	
}