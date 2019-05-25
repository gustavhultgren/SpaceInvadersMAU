package states;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sun.glass.events.KeyEvent;

import entity.Player;

public class CharacterSelectionState extends MenuState {

	private Player player;
	
	private String title;
	
	private BufferedImage playerImage;
	private BufferedImage playerImage2;

	public CharacterSelectionState(GameStateManager gsm) {
		super(gsm);
		
		init();
		
	}

	@Override
	public void init() {
		try {
			playerImage = ImageIO.read(new File("resources/images/playerImage.png"));
			playerImage2 = ImageIO.read(new File("resources/images/playerImage2.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		title = "CHOOSE CHARACTER";
	}

	@Override
	public void update() {
		bg.update();
		System.out.println(currentChoice);

	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(image, null, 100, 100);
		bg.draw(g);
		
		g.setFont(font);
		
		textLength = (int) g.getFontMetrics().getStringBounds(title, g).getWidth();
		
		g.drawString(title, (700 - textLength) / 2, 200);
		
		g.drawImage(playerImage, 150, 400, 100, 100, null);
		g.drawImage(playerImage2, 350, 400, 100, 100, null);
		
	}

	private void select() throws IOException {
		if (currentChoice == 0) {
			player.setPlayerImage(1);

		} else if (currentChoice == 1) {
			player.setPlayerImage(2);

		}

		gsm.setState(1);
	}

	@Override
	public void keyPressed(int k) {
		if (k == KeyEvent.VK_ENTER) {
			try {
				select();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			soundFX.get("enter").play();
		}

		if (k == KeyEvent.VK_RIGHT) {
			currentChoice++;
			if (currentChoice == -1) {
				currentChoice = 0;
			}

			// code that visualizes change

			soundFX.get("click").play();
		}

		if (k == KeyEvent.VK_LEFT) {
			currentChoice--;
			if (currentChoice == 2) {
				currentChoice = 1;
			}

			//code that visualizes change

			soundFX.get("click").play();
		}

	}

	@Override
	public void keyReleased(int k) {

	}

}
