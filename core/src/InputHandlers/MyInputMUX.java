package InputHandlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class MyInputMUX {

	Stage stagehud;
	PlayerInputProcessor Gameinputprocessor;
	
	InputMultiplexer multiplexer;

	public MyInputMUX() {
		multiplexer = new InputMultiplexer();
	}
	
	public void setHudProcessor(Stage stagehud) {
		this.stagehud = stagehud;
	}
	
	public void setGameProcessor(PlayerInputProcessor Gameinputprocessor) {
		this.Gameinputprocessor = Gameinputprocessor;
	}

	public void create() {
		multiplexer.addProcessor(stagehud);
		multiplexer.addProcessor(Gameinputprocessor);
		Gdx.input.setInputProcessor(multiplexer);
	}

	public void update() {

	}

}
