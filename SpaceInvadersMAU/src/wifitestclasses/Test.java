package wifitestclasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

	public static void main(String[] args) {
		Test t = new Test();
		String os = t.getOS();
		System.out.println(t.getSSID(os));
	}
}
