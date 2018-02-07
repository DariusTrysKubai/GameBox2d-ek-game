package InputHandlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.mygdx.game.Game;

public class PlayerInputProcessor implements InputProcessor{

	boolean pressed = false;
	
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// stuff goes here
		pressed = true;
		//Gdx.app.log(this.getClass().getName(), "Screen V_WIDTH: " + Game.V_WIDTH + " graphics width " + Gdx.graphics.getWidth());
		//Gdx.app.log(this.getClass().getName(), "touch down x: " + screenX + " y: " + screenY);
		//
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		//
		pressed = false;
		//
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean getPressed() {
		return pressed;
	}

}
