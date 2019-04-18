package server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class LeaderboardUpdateResponse implements Serializable{
	private PlayerScore[] scoreList;
	
	public LeaderboardUpdateResponse(PlayerScore[] scoreList) {
		this.scoreList = scoreList;
	}
	
	public PlayerScore[] getScoreList(){
		return scoreList;
	}
	
}
