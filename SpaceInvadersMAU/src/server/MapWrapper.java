package server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

public class MapWrapper implements Serializable{
	private  TreeMap<PlayerScore, PlayerScore> playerScoreMap = new TreeMap<PlayerScore, PlayerScore>();
	
	public void put(PlayerScore score) {
		playerScoreMap.put(score, score);
	}
	
	public PlayerScore[] getScoreList() {
		return (PlayerScore[])playerScoreMap.values().toArray();
	}
}
