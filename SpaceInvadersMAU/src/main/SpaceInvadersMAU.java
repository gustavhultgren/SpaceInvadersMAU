package main;

import java.net.MalformedURLException;

import javax.swing.JFrame;

/**
 * This class contains the main thread.
 * 
 * @author Gustav Hultgren
 */
public class SpaceInvadersMAU extends JFrame {

	public static void main(String[] args) throws MalformedURLException {
		JFrame window = new JFrame("SpaceInvaders MAU");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setContentPane(new GamePanel());
		window.pack();
		window.setLocationRelativeTo(null);
		window.setResizable(false);
		window.setVisible(true);
	}

}
