package Handlers;

import java.util.Stack;

import com.mygdx.game.Game;

import states.GameState;
import states.Play;
import states.Menu;
import states.Startup;

public class GameStateManager {

	private Game game;

	private GameState gameStates;

	public static final int STARTUP = 18694987;
	public static final int PLAY = 1657;
	public static final int MENU = 48647;

	public Game game() {
		return game;
	}

	public GameStateManager(Game game) {
		this.game = game;
	}

	public void update(float dt) {
		gameStates.update(dt);
	}

	public void render() {
		gameStates.render();
	}

	public GameState getState() {
		return gameStates;
	}

	public void setState(GameState state) {
		gameStates = state;
	}

	public void pause() {
		gameStates.pause();
	}

	public void resume() {
		gameStates.resume();
	}

}
