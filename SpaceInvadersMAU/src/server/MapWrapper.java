package server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;

public class MapWrapper implements Serializable{
	private  TreeMap<PlayerScore, Integer> playerScoreMap = new TreeMap<PlayerScore, Integer>();
	
	public void put(PlayerScore score) {
		playerScoreMap.put(score, score);
	}
	
	public PlayerScore[] getScoreList() {
		PlayerScore[] temp = new PlayerScore[playerScoreMap.size()];
		for (int i = 0; i < playerScoreMap.size(); i++) {
			temp[i] = playerScoreMap.values().to
		}
		return temp;
	}
	public int size() {
		return playerScoreMap.size();
	}
}
