package states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;

import Handlers.GameStateManager;

public class Startup extends GameState {
	
	private Texture background;
	int x, y;
	private float timer = 0;

	public Startup(GameStateManager gsm){
		super(gsm);

	}

	@Override
	public void handleInput() {

	}

	@Override
	public void update(float dt) {
		timer += dt;
	}

	@Override
	public void render() {
		
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		
		sb.begin();
		
		//sb.draw(background, 0, 0);

		if (timer > 0.5f) {
			sb.draw(background, x, y);
		} 
		
		if (timer > 3.5f) {
			gsm.setState(new Play(gsm));
		}
		
		sb.end();
		
	}

	@Override
	public void dispose() {

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

}
