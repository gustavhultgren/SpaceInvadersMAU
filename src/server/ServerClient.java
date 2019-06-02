package server;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;

import javax.swing.JFrame;

/**
 * Server that takes requests from games and sends back a LeaderBoard response object containing the lists.
 * 
 * @author Tom Eriksson
 *
 */
public class ServerClient {
	private Connection connection = new Connection();
	private ServerSocket serverSocket;
	private RunOnThreadN pool;
	private LinkedList<PlayerScore> list;
	private LinkedList<PlayerScore> mauList;
	private JFrame frame;

	//Initiates variables and reads saved lists from files.
	public ServerClient(int port, int nbrOfThreads) throws IOException {
		pool = new RunOnThreadN(nbrOfThreads);
		list = readScoreFromFile("savedScores.dat");
		mauList = readScoreFromFile("savedScoresMau.dat");
		serverSocket = new ServerSocket(port);
		pool.start();
		connection.start();
	}

	// Starts UI and adds a listener for the closing of the window. If the window is closing then the current lists
	// gets writen to the files. 
	public void start() {
		frame = new JFrame();
		frame.setBounds(0, 0, 400, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setTitle("Server");
		frame.setVisible(true);
		frame.setResizable(false); // Prevent user from change size
		frame.setLocationRelativeTo(null); // Start middle screen
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				writeListToFile("savedScores.dat", list);
				writeListToFile("savedScoresMau.dat", mauList);
				e.getWindow().dispose();
			}
		});
	}

	// Writes the list to file.
	private synchronized void writeListToFile(String filename, LinkedList<PlayerScore> list) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("serverFiles/" + filename));) {
			oos.writeObject(list);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//Reads list from file.
	private synchronized LinkedList<PlayerScore> readScoreFromFile(String filename) {
		LinkedList<PlayerScore> list = null;
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("serverFiles/" + filename));) {

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

	private synchronized LinkedList<PlayerScore> getList() {
		return list;
	}

	private synchronized LinkedList<PlayerScore> getMauList() {
		return mauList;
	}

	// Adds a PlayerScore object to the correct list and sorts the list using the playerScore comparable.
	private synchronized void addAndSort(PlayerScore p) {

		if (p.isMAUScore()) {
			mauList.add(p);
			Collections.sort(mauList);
		} else {
			list.add(p);
			Collections.sort(list);
		}
	}

	// Adds a connected user to the pool to handle. 
	private class Connection extends Thread {
		public void run() {
			System.out.println("Server running, port: " + serverSocket.getLocalPort());
			while (true) {
				try {
					Socket socket = serverSocket.accept();
					pool.execute(new ClientHandler(socket));
				} catch (IOException e) {
					System.err.println(e);
				}
			}
		}
	}

	// Runnable that reads for a playerScore object, and sends a response back. 
	// If name is empty, then send only the lists.
	private class ClientHandler implements Runnable {
		private Socket socket;

		public ClientHandler(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			System.out.println("Klient uppkopplad, servas av " + Thread.currentThread());
			try (ObjectOutputStream dos = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream dis = new ObjectInputStream(socket.getInputStream())) {

				try {
					Object o = dis.readObject();
					if (o instanceof PlayerScore) {
						PlayerScore ps = (PlayerScore) o;
						if (ps.getName().equals("")) {
							LeaderboardUpdateResponse response = new LeaderboardUpdateResponse(getList(), getMauList());
							dos.writeObject(response);
							dos.flush();
						} else {
							addAndSort(ps);
							LeaderboardUpdateResponse response = new LeaderboardUpdateResponse(getList(), getMauList());
							dos.writeObject(response);
							dos.flush();
						}
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
			}
			try {
				socket.close();
			} catch (Exception e) {
			}
			System.out.println("Klient nerkopplad, " + Thread.currentThread() + " återvänder till buffert");
		}
	}

	public static void main(String[] args) {
		try {
			new ServerClient(3500, 10).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
