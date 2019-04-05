package server;

import java.io.Serializable;

public class PlayerScore implements Serializable, Comparable<PlayerScore>{

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

	public int compareTo(PlayerScore other) {
		if(score > other.getScore()) {
			return 1;
		}else if(score < other.getScore()){
			return -1;
		}else {
			return 0;
		}
	}

}
