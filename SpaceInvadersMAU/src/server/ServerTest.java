package server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.LinkedList;

public class ServerTest {

	private LinkedList<PlayerScore> list = new LinkedList<PlayerScore>();

	public void writeListToFile() {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("serverFiles/filtest.dat"));) {
			oos.writeObject(this.list);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void add(PlayerScore p) {
		list.add(p);
		Collections.sort(list);
	}

	public void fillList() {
		for (int i = 0; i < 100; i++) {
			list.add(new PlayerScore("Tom", i * 100));
		}
	}

	public LinkedList<PlayerScore> readScoreFromFile() {
		LinkedList<PlayerScore> list = null;
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("serverFiles/filtest.dat"));) {

			list = (LinkedList<PlayerScore>) ois.readObject();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static void main(String[] args) {
		ServerTest test = new ServerTest();

		LinkedList<PlayerScore> list = test.readScoreFromFile();
//		test.add(new PlayerScore("Elin", 1001));
//		test.writeListToFile();

		for (PlayerScore elem : list) {
			System.out.println(elem.getName() + " " + elem.getScore());
		}
	}
}
