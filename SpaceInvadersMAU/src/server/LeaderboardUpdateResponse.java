package server;

import java.io.Serializable;
import java.util.ArrayList;

public class LeaderboardUpdateResponse implements Serializable{
	private ArrayList<PlayerScore> scoreList;
	
	public LeaderboardUpdateResponse(ArrayList scoreList) {
		this.scoreList = scoreList;
	}
	
	public ArrayList<PlayerScore> getScore(){
		return scoreList;
	}
}
