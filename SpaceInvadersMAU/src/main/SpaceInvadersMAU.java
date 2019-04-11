package main;

import java.awt.EventQueue;
import java.net.MalformedURLException;

import javax.swing.JFrame;

import gameSate.MenuState;
/**
 * 
 * @author Tom
 *
 */
public class SpaceInvadersMAU extends JFrame {

	public static void main(String[] args) throws MalformedURLException {
		JFrame window = new JFrame("SpaceInvaders MAU");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setContentPane(new MenuState());
		window.pack();
		window.setLocationRelativeTo(null);
//		window.setResizable(false);
		window.setVisible(true);
	}

}