package server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

public class ServerTest {
	
	private MapWrapper playerScoreMap = new MapWrapper();

	
	public void writeScoreToFile(PlayerScore score) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("serverFiles/filtest.dat"));) {
			playerScoreMap.put(score);
			oos.writeObject(playerScoreMap);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public ArrayList<PlayerScore> readScoreFromFile() {
		ArrayList<PlayerScore> tempList = new ArrayList();
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("serverFiles/filtest.dat"));) {
			playerScoreMap = (MapWrapper) ois.readObject();
			
			tempList = playerScoreMap.getScoreList();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return tempList;
	}

	public static void main(String[] args) {
		ServerTest test = new ServerTest();
		List<PlayerScore> readList = test.readScoreFromFile();

//		Random rand = new Random();
//		int number;
//		for (int i = 0; i < 3; i++) {
//			number = rand.nextInt(100);
//			PlayerScore ps = new PlayerScore("b", 1000);
//			test.writeScoreToFile(ps);
//		}
		
		for (PlayerScore elem : readList) {
			System.out.println(elem.getScore());
		}

	}
}
