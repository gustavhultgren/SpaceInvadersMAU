package server;

public class PlayerScore {

	private String playerName;
	private int score;
	
	public PlayerScore(String playerName, int score) {
		this.playerName = playerName;
		this.score = score;
	}
	
	public String getName() {
		return playerName;
	}
	
	public int getScore() {
		return score;
	}
	
}
