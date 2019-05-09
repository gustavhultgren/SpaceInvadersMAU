package states;

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.HashMap;

import audio.AudioPlayer;
import entity.Player;
import gameClient.Client;

/**
 * GameState represents a state in the system. It contains some common
 * variables and methods that is used in different states.
 * @author Gustav Hultgren & Tom Eriksson
 */
public abstract class GameState {
	
	//Dimensions.
	protected static int WIDTH = 700;
	protected static int HEIGHT = 700;
	protected static int PLAYER_INIT_X = WIDTH / 2 - 20;
	protected static int PLAYER_INIT_Y = 610;
	protected static int PLAYER_WIDTH = 30;
	protected static int PLAYER_HEIGHT = 30;
	protected static int ENEMY_INIT_X = 100;
	protected static int ENEMY_INIT_Y = 80;
	protected static int ENEMY_DIRECTION = -1;
	protected static int GROUND = 650;
	protected static Font font;
	protected static Client client = new Client("127.0.0.1", 3500);

	protected static HashMap<String, AudioPlayer> soundFX = new HashMap<String, AudioPlayer>();
	
	protected GameStateManager gsm;
	protected static Player player;

	public abstract void init();
	public abstract void update();
	public abstract void draw(Graphics2D g);
	public abstract void keyPressed(int k);
	public abstract void keyReleased(int k);
	
}