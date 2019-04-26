package serverTestClasses;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import server.LeaderboardUpdateResponse;
import server.PlayerScore;

public class Client extends Thread{

	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private JFrame frame;
	private JPanel panel;
	private String ip;
	private int port;
	private JTextArea txtArea;
	private JScrollPane scroll;
	private PlayerScore[] scoreList;

	public Client(String ip, int port) {
		this.ip = ip;
		this.port = port;
//		init();
		connect();
//		start();
	}

	public void run() {
		try {

			Object o = ois.readObject();
			if (o instanceof LeaderboardUpdateResponse) {
//				txtArea.append("Leaderboard received" + "\n");
				LeaderboardUpdateResponse l = (LeaderboardUpdateResponse) o;
				scoreList = l.getScoreList();
				System.out.println(scoreList[1].getName());
//				for (PlayerScore elem : list) {
//					txtArea.append(elem.getName() + " " + elem.getScore() + "\n");
//				}
			}
//			send();


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
		

//		panel.add(scroll, BorderLayout.CENTER);

	}

	public void connect() {
		try {
			socket = new Socket(ip, port);
			oos = new ObjectOutputStream((socket.getOutputStream()));
			ois = new ObjectInputStream((socket.getInputStream()));
//			txtArea.append("Client connected" + "\n");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(PlayerScore ps) {
		try {
			oos.writeObject(ps);
//			txtArea.append("playerScore sent" + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public PlayerScore[] getScoreList(){
		return scoreList;
	}

	public static void main(String[] args) {
		new Client("127.0.0.1", 3500);
	}

}
