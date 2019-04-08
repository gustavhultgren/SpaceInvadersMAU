package server;

import java.io.Serializable;
import java.util.ArrayList;

public class LeaderboardUpdateResponse implements Serializable{
	private ArrayList<PlayerScore> scoreList;
	
	public LeaderboardUpdateResponse(ArrayList<PlayerScore> scoreList) {
		this.scoreList = scoreList;
	}
	
	public ArrayList<PlayerScore> getScore(){
		return scoreList;
	}
	
	public String toString() {
		String m = "";
		for (PlayerScore elem : scoreList) {
			m += (elem.getScore() + " " + elem.getName() + "\n");
		}
		return m;
	}
	
}
