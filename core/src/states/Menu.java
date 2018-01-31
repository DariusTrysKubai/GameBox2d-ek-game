package states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.math.Vector3;

import Handlers.GameStateManager;

public class Menu extends GameState {
	

	

	public Menu(GameStateManager gsm) {
		super(gsm);

	}

	@Override
	public void handleInput() {

		int tempX = Gdx.input.getX();
		int tempY = Gdx.input.getY();
		Vector3 mouseVector = hudcam.unproject(new Vector3(tempX, tempY, 0));

	}

	@Override
	public void update(float dt) {
		handleInput();

	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

	}

	@Override
	public void dispose() {

		
	}

}
