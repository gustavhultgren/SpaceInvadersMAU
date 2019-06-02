package server;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Object sent from server to client with sorted lists of PlayerScore elements.
 * @author Tom Eriksson
 *
 */
public class LeaderboardUpdateResponse implements Serializable {
	// List of scores not recorded on school wifi.
	private PlayerScore[] scoreList = new PlayerScore[100];
	
	// List of scores recorded on school wifi.
	private PlayerScore[] scoreListMAU = new PlayerScore[100];

	public LeaderboardUpdateResponse(LinkedList<PlayerScore> list, LinkedList<PlayerScore> mauList) {
		for (int i = 0; i < 100; i++) {
			scoreList[i] = list.get(i);
			scoreListMAU[i] = mauList.get(i);
		}
	}

	public PlayerScore[] getScoreList() {
		return scoreList;
	}

	public PlayerScore[] getScoreListMAU() {
		return scoreListMAU;
	}

}
