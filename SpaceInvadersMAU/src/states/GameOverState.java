package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;


public class GameOverState extends GameState {
	
	private String[] options = { "YES", "NO"};
	
	private int currentChoice = 0;
	
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
		int length;
		try {
			Font tempFont = Font.createFont(Font.TRUETYPE_FONT, new File("res/fonts/ARCADE_I.TTF")).deriveFont(Font.PLAIN, 60);
			g.setFont(tempFont);
			g.setColor(Color.RED);
			length = (int) g.getFontMetrics().getStringBounds(gameOver, g).getWidth();
			g.drawString(gameOver, (700 - length) / 2, (700 / 2) - 150);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		
		g.setFont(font);
		g.setColor(Color.WHITE);
		String finalScore = "FINAL SCORE: ";
		length = (int) g.getFontMetrics().getStringBounds(finalScore, g).getWidth();
		g.drawString(finalScore, (700 - length) / 2, (700 / 2));

		Font tempFont;
		try {
			g.setColor(Color.GREEN);

			tempFont = Font.createFont(Font.TRUETYPE_FONT, new File("res/fonts/ARCADE_I.TTF")).deriveFont(Font.PLAIN, 35);
			g.setFont(tempFont);
			String submit = "SUBMIT SCORE";
			length = (int) g.getFontMetrics().getStringBounds(submit, g).getWidth();

			g.drawString(submit, (700 - length) / 2, (700 / 2) + 100);
			g.drawString("" + player.getScore(), 490, (700 / 2));

		} catch (FontFormatException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		for (int i = 0; i < options.length; i++) {
			if (i == currentChoice) {
				g.setColor(Color.YELLOW);
			} else {
				g.setColor(Color.WHITE);
			}
			try {
				tempFont = Font.createFont(Font.TRUETYPE_FONT, new File("res/fonts/ARCADE_I.TTF")).deriveFont(Font.PLAIN, 50);
				g.setFont(tempFont);
				length = (int) g.getFontMetrics().getStringBounds(options[i], g).getWidth();
				g.drawString(options[i], 200 + 200 * i, (700 / 2) + 200 );
			} catch (FontFormatException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void select() {
		if (currentChoice == 0) {
			gsm.setState(GameStateManager.MENUSTATE);
		}
		if (currentChoice == 1) {
			gsm.setState(GameStateManager.MENUSTATE);		}
	}

	@Override
	public void keyPressed(int k) {
		if (k == KeyEvent.VK_ENTER) {
			select();
			
		} else if (k == KeyEvent.VK_LEFT) {
			currentChoice--;
			if (currentChoice == -1) {
				currentChoice = 0;
			}
		} else if (k == KeyEvent.VK_RIGHT)	{
			currentChoice++;
			if (currentChoice == options.length) {
				currentChoice = 1;
			}
		}	
	}

	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub
		
	}

}
