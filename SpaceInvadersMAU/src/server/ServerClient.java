package server;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerClient extends Thread{
	private int port;
	
	private ServerSocket serverSocket;
	
	public ServerClient(int port) {
		this.port = port;
		
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
		}
		start();
	}
	
	public void run() {
		
	}
	
	private class Worker {
		
	}
}
