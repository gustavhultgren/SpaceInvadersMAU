package server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class ServerClient {
	private Connection connection = new Connection();
	private ServerSocket serverSocket;
	private RunOnThreadN pool;
	
	private MapWrapper playerScoreMap = 

	public ServerClient(int port, int nbrOfThreads) throws IOException {
		pool = new RunOnThreadN(nbrOfThreads);
		serverSocket = new ServerSocket(port);
		pool.start();
		connection.start();
	}

	
	public synchronized void writeScoreToFile(PlayerScore score) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("serverFiles/filtest.dat"));) {
			playerScoreMap.put(score, score);
			oos.writeObject(playerScoreMap);
			oos.flush();
			System.out.println("playerscore added and written " + playerScoreMap.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void readScoreFromFile() {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("serverFiles/filtest.dat"));) {
			playerScoreMap = (TreeMap<PlayerScore, PlayerScore>) ois.readObject();

			System.out.println("Scoremap read " + playerScoreMap.size());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized ArrayList<PlayerScore> playerScoreMapToArray(){
		return new ArrayList<PlayerScore>(playerScoreMap.values());
	}
	
	private class Connection extends Thread {
		public void run() {
			System.out.println("ServerF running, port: " + serverSocket.getLocalPort());
			while(true) {
				try  {
					Socket socket = serverSocket.accept();
					pool.execute(new ClientHandler(socket));
				} catch(IOException e) { 
					System.err.println(e);
				}
			}
		}
	}

	private class ClientHandler implements Runnable {
		private Socket socket;

		public ClientHandler(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			System.out.println("Klient uppkopplad, servas av " + Thread.currentThread());
			try (ObjectOutputStream dos = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream dis = new ObjectInputStream(socket.getInputStream())	) {
				
				
				PlayerScore request;
				try {
					request = (PlayerScore) dis.readObject();
					readScoreFromFile();
					writeScoreToFile(request);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				
				
				LeaderboardUpdateResponse response = new LeaderboardUpdateResponse(playerScoreMapToArray());
				
				// stoppa in request
				// skriv tillbaka till filen
				// Skicka tillbaka en kopia
				
				dos.writeObject(response);
				dos.flush();
			} catch(IOException e) {}
			try {
				socket.close();
			} catch(Exception e) {}
			System.out.println("Klient nerkopplad, " + Thread.currentThread() + " återvänder till buffert");
		}
	}

	public static void main(String[] args) throws IOException {
		new ServerClient(3500,10);
	}
}
