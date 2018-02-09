package InputHandlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class MyInputMUX {

	Stage stagehud;
	PlayerInputProcessor gameinputprocessor;
	PlayerGestureProcessor playergestureprocessor;
	
	InputMultiplexer multiplexer;

	public MyInputMUX() {
		multiplexer = new InputMultiplexer();
	}
	
	public void setHudProcessor(Stage stagehud) {
		this.stagehud = stagehud;
	}
	
	public void setGameProcessor(PlayerInputProcessor gameinputprocessor) {
		this.gameinputprocessor = gameinputprocessor;
	}
	
	public void setGestureProcessor(PlayerGestureProcessor playergestureprocessor) {
		this.playergestureprocessor = playergestureprocessor;
	}


	public void create() {
		multiplexer.addProcessor(stagehud);
		multiplexer.addProcessor(new GestureDetector(playergestureprocessor));
		multiplexer.addProcessor(gameinputprocessor);
		Gdx.input.setInputProcessor(multiplexer);
	}

	public void update() {

	}

}
