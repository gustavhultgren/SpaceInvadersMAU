package server;

import java.io.Serializable;
import java.util.LinkedList;

public class LeaderboardUpdateResponse implements Serializable{
	private PlayerScore[] scoreList = new PlayerScore[100];
	
	public LeaderboardUpdateResponse(LinkedList<PlayerScore> list) {
		for (int i = 0; i < 100; i++) {
			scoreList[i] = list.get(i);
		}
	}
	
	public PlayerScore[] getScoreList(){
		return scoreList;
	}
	
}
