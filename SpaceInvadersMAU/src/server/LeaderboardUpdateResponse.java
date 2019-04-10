package server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class LeaderboardUpdateResponse implements Serializable{
	private ArrayList<PlayerScore> scoreList;
	
	public LeaderboardUpdateResponse(ArrayList<PlayerScore> scoreList) {
		this.scoreList = scoreList;
	}
	
	public ArrayList<PlayerScore> getScore(){
		return scoreList;
	}
	
	public String toString() {
		ArrayList<PlayerScore> tempList = scoreList;
		Collections.reverse(tempList);
		
		String m = "";
		for (PlayerScore elem : tempList) {
			m += (elem.getScore() + " " + elem.getName() + "\n");
		}
		return m;
	}
	
}
