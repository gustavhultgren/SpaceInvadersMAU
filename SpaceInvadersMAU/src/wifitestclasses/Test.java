package wifitestclasses;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

import server.PlayerScore;

public class Test {

	String getSSID(String os)  {
		String ssid = "";
		if (os.substring(0, 3).equals("mac")) {
			String command = "/System/Library/PrivateFrameworks/Apple80211.framework/Versions/Current/Resources/airport -I";
			try {
				Process proc;

				proc = Runtime.getRuntime().exec(command);
				BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				String line = "";
				int i = 0;
				while ((line = reader.readLine()) != null) {
						if (i == 12) {
							ssid = line.substring(line.indexOf(':') +1);
							return ssid;
						}else {
							i++;
						}
				}
				proc.waitFor();

			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Read the output
		}
		return ssid;

	}
	
	String getOS() {
		String s = System.getProperty("os.name").toLowerCase();
		return s;
	}
	
	private synchronized void writeListToFile(String filename, LinkedList<PlayerScore> list) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("serverFiles/" + filename));) {
			oos.writeObject(list);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Test t = new Test();
//		String os = t.getOS();
//		System.out.println(t.getSSID(os));
		
		LinkedList<PlayerScore> psMau = new LinkedList<PlayerScore>();
		LinkedList<PlayerScore> ps = new LinkedList<PlayerScore>();

		for (int i = 0; i < 100; i++) {
			psMau.add(new PlayerScore("aaa", 1, true));
			ps.add(new PlayerScore("bbb", 1, false));
		}
		t.writeListToFile("savedScores.dat", ps);
		t.writeListToFile("savedScoresMau.dat", psMau);
	}
}
