package serverTestClasses;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

import javax.swing.ImageIcon;

import server.LeaderboardUpdateResponse;
import server.PlayerScore;

public class Client extends Thread {

	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	private String ip;
	private int port;

	public Client(String ip, int port) {
		this.ip = ip;
		this.port = port;
		connect();
		start();
		send();
	}

	public void run() {
		try {
			while (true) {
				Object o = ois.readObject();
				if (o instanceof LeaderboardUpdateResponse) {
					System.out.println("Leaderboard received");
					LeaderboardUpdateResponse l = (LeaderboardUpdateResponse)o;
					System.out.println(l.toString());
				}

			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public void connect() {
		try {
			socket = new Socket(ip, port);
			oos = new ObjectOutputStream((socket.getOutputStream()));
			ois = new ObjectInputStream((socket.getInputStream()));
			System.out.println("Client connected");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void send() {
		try {
			oos.writeObject(new PlayerScore("Hannes", 9000));
			System.out.println("Score sent");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Client("127.0.0.1", 3500);
	}

}
