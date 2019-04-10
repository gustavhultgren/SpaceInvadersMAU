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

	private MapWrapper playerScoreMap;
	int number;

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

	public void load() {
		readScoreFromFile();
	}

	public static void main(String[] args) {
		ServerTest test = new ServerTest();

		ArrayList<PlayerScore> list = test.readScoreFromFile();
//		test.writeScoreToFile(new PlayerScore("Tom", 400));

		for (PlayerScore elem : list) {
			System.out.println(elem.getScore());
		}

//		Random rand = new Random();
//		int number;
//		for (int i = 0; i < 3; i++) {
//			number = rand.nextInt(100);
//			PlayerScore ps = new PlayerScore("Karlsson", 500);
//			test.writeScoreToFile(ps);
//		}

	}
}
