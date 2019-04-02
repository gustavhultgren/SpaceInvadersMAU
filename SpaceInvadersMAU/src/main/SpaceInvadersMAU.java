package main;

import java.awt.EventQueue;

import javax.swing.JFrame;
/**
 * 
 * @author Tom
 *
 */
public class SpaceInvadersMAU extends JFrame {
	
	public static void main(String[] args) {
		JFrame window = new JFrame("SpaceInvaders MAU");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setContentPane(new GamePanel());
		window.pack();
		window.setLocationRelativeTo(null);
//		window.setResizable(false);
		window.setVisible(true);
	}

}