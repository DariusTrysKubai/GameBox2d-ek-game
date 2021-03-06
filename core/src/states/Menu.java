package states;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Game;

import Handlers.GameStateManager;

public class Menu extends GameState {

	Stage stage;
	Table table;
	Viewport viewport;
	TextButton button_play;
	TextButton button_exit;
	GameStateManager gsm;

	public Menu(final GameStateManager gsm) {
		super(gsm);

		this.gsm = gsm;
		Skin skin = new Skin(Gdx.files.internal("skins2/flat-earth-ui.json"));
		viewport = new FitViewport(Game.V_WIDTH, Game.V_HEIGHT, hudcam);
		stage = new Stage(viewport, sb);
		table = new Table(skin);

		button_play = new TextButton("PLAY", skin);
		button_play.setSize(400, 40);
		button_play.addCaptureListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				gsm.setState(new Play(gsm));
			}
		});

		button_exit = new TextButton("EXIT", skin);
		button_exit.setSize(400, 40);

		table.add(button_play);
		table.row().pad(10);
		table.add(button_exit);

		table.setPosition((Game.V_WIDTH / 2) - (table.getWidth() / 2), (Game.V_HEIGHT / 2) - (table.getHeight() / 2));
		
		Gdx.input.setInputProcessor(stage);
		stage.addActor(table);
	}

	@Override
	public void handleInput() {

	}

	@Override
	public void update(float dt) {
		handleInput();
		stage.act();
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		stage.draw();
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
