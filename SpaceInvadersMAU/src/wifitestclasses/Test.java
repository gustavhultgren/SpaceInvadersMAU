package wifitestclasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Test {

		    public static void main(String[] args) throws IOException, InterruptedException {
		    	
		    	// För att kolla vilket os
		    	String s = System.getProperty("os.name").toLowerCase();
		    	System.out.println(s);
		    	
		    	// För att hitta info om wifi <mac>

		        String command = "/System/Library/PrivateFrameworks/Apple80211.framework/Versions/Current/Resources/airport -I";

		        Process proc = Runtime.getRuntime().exec(command);

		        // Read the output

		        BufferedReader reader =  
		              new BufferedReader(new InputStreamReader(proc.getInputStream()));

		        String line = "";
		        while((line = reader.readLine()) != null) {
		            System.out.print(line + "\n");
		        }

		        proc.waitFor();   

		    }
}
