package states;

import java.awt.Color;
import java.awt.Graphics2D;

import com.sun.glass.events.KeyEvent;


public class GameOverState extends GameState {
	
	public GameOverState(GameStateManager gsm) {
		this.gsm = gsm;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 700, 700);
		g.setColor(Color.WHITE);
		String gameOver = "GAME OVER";
		int length = (int) g.getFontMetrics().getStringBounds(gameOver, g).getWidth();
		g.drawString(gameOver, (700 - length) / 2, (700 / 2) - 150);
		
		String finalScore = "FINAL SCORE: ";
		length = (int) g.getFontMetrics().getStringBounds(finalScore, g).getWidth();
		g.drawString(finalScore, (700 - length) / 2, (700 / 2));
		g.setColor(Color.GREEN);
		g.drawString("" + player.getScore(), 490, (700 / 2));
		
		String exitMessage = "PRESS ESC TO GO BACK TO MENU";
		length = (int) g.getFontMetrics().getStringBounds(exitMessage, g).getWidth();
		g.drawString(exitMessage, (700 - length) / 2, 600);
	}

	@Override
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_ESCAPE) {
			gsm.setState(0);
			soundFX.get("enter").play();
		}
		
	}

	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub
		
	}

}
