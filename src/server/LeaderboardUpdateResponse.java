package server;

import java.io.Serializable;
import java.util.LinkedList;

public class LeaderboardUpdateResponse implements Serializable {
	private PlayerScore[] scoreList = new PlayerScore[100];
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
