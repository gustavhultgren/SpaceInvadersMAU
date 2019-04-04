package server;

import java.util.ArrayList;

public class LeaderboardUpdateResponse {
	private ArrayList<PlayerScore> scoreList;
	
	public LeaderboardUpdateResponse(ArrayList scoreList) {
		this.scoreList = scoreList;
	}
	
	public ArrayList<PlayerScore> getScore(){
		return scoreList;
	}
}
