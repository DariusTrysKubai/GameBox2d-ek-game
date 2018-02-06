package player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class Player_input {

	InputListener listen;

	public Player_input() {

	}

	public void create() {

		listen = new InputListener() {
			// many different types of inputs to override
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("hey yo");
				return super.touchDown(event, x, y, pointer, button);
			}
		};
	}

	public InputListener getListener() {
		return listen;
	}
}
