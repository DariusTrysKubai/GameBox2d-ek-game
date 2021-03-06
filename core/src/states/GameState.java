package states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Game;

import GameData.GameData;
import Handlers.GameStateManager;

public abstract class GameState {
	
	protected GameStateManager gsm;
	protected Game game;
	
	protected SpriteBatch sb;
	protected OrthographicCamera cam;
	protected OrthographicCamera hudcam;
	protected Viewport viewport;
	protected ShapeRenderer shape;
	protected GameData data;

	protected GameState(GameStateManager gsm){
		this.gsm = gsm;
		game = gsm.game();
		sb = game.GetSpriteBatch();
		cam = game.GetCamera();
		hudcam = game.GetHudCamera();
		viewport = game.GetViewport();
		shape = game.GetShape();
		data = game.getGameData();
	}
	
	public abstract void handleInput();
	public abstract void update(float dt);
	public abstract void render();
	public abstract void pause();
	public abstract void resume();
	public abstract void dispose();

}
