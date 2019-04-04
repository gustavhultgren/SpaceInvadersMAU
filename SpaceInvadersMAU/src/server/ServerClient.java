package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerClient {
	private Connection connection = new Connection();
	private ServerSocket serverSocket;
	private RunOnThreadN pool;

	public ServerClient(int port, int nbrOfThreads) throws IOException {
		pool = new RunOnThreadN(nbrOfThreads);
		serverSocket = new ServerSocket(port);
		pool.start();
		connection.start();
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
				
				
				ticket = dis.readUTF();
				response = getResponse(ticket);
				dos.writeUTF(response);
				dos.flush();
			} catch(IOException e) {}
			try {
				socket.close();
			} catch(Exception e) {}
			System.out.println("Klient nerkopplad, " + Thread.currentThread() + " återvänder till buffert");
		}
	}

	public static void main(String[] args) throws IOException {
		new LotteryServerF(3465,50);
	}
}
