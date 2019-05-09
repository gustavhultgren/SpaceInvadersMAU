package gameClient;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import server.LeaderboardUpdateResponse;
import server.PlayerScore;

public class Client extends Thread {

	private JFrame frame;
	private JPanel panel;
	private String ip;
	private int port;
	private JTextArea txtArea;
	private JScrollPane scroll;
	private PlayerScore[] scoreList;
	private PlayerScore[] scoreListMau;

	public Client(String ip, int port) {
		this.ip = ip;
		this.port = port;
		start();
	}

	public synchronized void requestList() {
		try (Socket socket = new Socket(ip, port);
				ObjectOutputStream oos = new ObjectOutputStream((socket.getOutputStream()));
				ObjectInputStream ois = new ObjectInputStream((socket.getInputStream()));) {

			oos.writeObject(new PlayerScore("", 0, false));

			Object o = ois.readObject();
			if (o instanceof LeaderboardUpdateResponse) {
				LeaderboardUpdateResponse l = (LeaderboardUpdateResponse) o;
				scoreList = l.getScoreList();
				scoreListMau = l.getScoreListMAU();
			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}
	public synchronized void requestList(PlayerScore ps) {
		try (Socket socket = new Socket(ip, port);
				ObjectOutputStream oos = new ObjectOutputStream((socket.getOutputStream()));
				ObjectInputStream ois = new ObjectInputStream((socket.getInputStream()));) {

			oos.writeObject(ps);

			Object o = ois.readObject();
			if (o instanceof LeaderboardUpdateResponse) {
				LeaderboardUpdateResponse l = (LeaderboardUpdateResponse) o;
				scoreList = l.getScoreList();
				scoreListMau = l.getScoreListMAU();
			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}
	

	public void init() {
		txtArea = new JTextArea();
		panel = new JPanel(new BorderLayout());
		frame = new JFrame();
		scroll = new JScrollPane(txtArea);
		frame.add(scroll);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		frame.setBounds(0, 0, 400, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Client");
		frame.setVisible(true);
		frame.setResizable(false); // Prevent user from change size
		frame.setLocationRelativeTo(null); // Start middle screen
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				e.getWindow().dispose();
			}
		});

	}
	public synchronized PlayerScore[] getScoreList() {
		return scoreList;
	}
	
	public synchronized PlayerScore[] getScoreListMau() {
		return scoreListMau;
	}
	
	public String getSSID(String os)  {
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
							ssid = line.substring(line.indexOf(':') +2).toLowerCase();
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
	
	public String getOS() {
		String s = System.getProperty("os.name").toLowerCase();
		return s;
	}
}
