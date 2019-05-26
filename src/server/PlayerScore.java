package server;

import java.io.Serializable;

public class PlayerScore implements Serializable, Comparable<PlayerScore> {

	private String playerName;
	private int score;
	private boolean isMAUScore;

	public PlayerScore(String playerName, int score, boolean isMAUScore) {
		this.playerName = playerName;
		this.score = score;
		this.isMAUScore = isMAUScore;
	}

	public String getName() {
		return playerName;
	}

	public int getScore() {
		return score;
	}

	public boolean isMAUScore() {
		return isMAUScore;
	}

	public int compareTo(PlayerScore other) {
		if (score < other.getScore()) {
			return 1;
		} else if (score > other.getScore()) {
			return -1;
		} else {
			if (playerName.compareTo(other.playerName) > 0) {
				return 1;
			} else if (playerName.compareTo(other.playerName) < 0) {
				return -1;
			} else {
				return 0;
			}
		}
	}

}
