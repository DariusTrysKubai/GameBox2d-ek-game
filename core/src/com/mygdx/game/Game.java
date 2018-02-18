package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import GameData.GameData;
import Handlers.GameStateManager;
import states.Menu;

public class Game extends ApplicationAdapter {

	public static final String TITLE = "Grow your haer";
	public static final float SCALE = 0.5f;
	public static final int V_WIDTH = (int) (1920 * SCALE);
	public static final int V_HEIGHT = (int) (1080 * SCALE);
	public static final int ORIGINAL_WIDTH = 5;
	public static final int ORIGINAL_HEIGHT = 288;
	public static final float PPM = 100;

	public static final float STEP = 1 / 60f;
	private float accum;

	private BitmapFont font;

	boolean debug = true;

	private SpriteBatch sb;
	private OrthographicCamera cam;
	private OrthographicCamera hudcam;
	private Viewport viewport;
	private ShapeRenderer shape;
	private GameData data;
	GameStateManager gsm;

	public SpriteBatch GetSpriteBatch() {
		return sb;
	}

	public OrthographicCamera GetCamera() {
		return cam;
	}

	public OrthographicCamera GetHudCamera() {
		return hudcam;
	}

	public Viewport GetViewport() {
		return viewport;
	}

	public ShapeRenderer GetShape() {
		return shape;
	}
	
	public GameData getGameData() {
		return data;
	}

	@Override
	public void create() {

		data = new GameData();
		data.load();

		shape = new ShapeRenderer();
		sb = new SpriteBatch();
		cam = new OrthographicCamera(V_WIDTH, V_HEIGHT);
		// cam.position.set(cam.viewportWidth/2,cam.viewportHeight/2,0);
		cam.translate(V_WIDTH / 2, V_HEIGHT / 2);
		cam.update();
		hudcam = new OrthographicCamera();
		hudcam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		viewport = new FitViewport(1280 / 2, 720 / 2, cam);
		viewport.apply();
		sb.setProjectionMatrix(cam.combined);
		shape.setProjectionMatrix(cam.combined);
		font = new BitmapFont();
		gsm = new GameStateManager(this);
		gsm.setState(new Menu(gsm));
	}

	@Override
	public void render() {
		// System.out.println(Gdx.graphics.getFramesPerSecond());

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		cam.zoom = 1f;

		accum += Gdx.graphics.getDeltaTime();
		while (accum >= STEP) {
			accum -= STEP;
			gsm.update(STEP);
		}

		gsm.render();
	}

	@Override
	public void dispose() {
	}

	public void resize(int w, int h) {
		viewport.update(w, h);
	}

	@Override
	public void pause() {
		Gdx.app.log(this.getClass().getName(), "pause");
		gsm.pause();
		data.save();
	}

	@Override
	public void resume() {
		Gdx.app.log(this.getClass().getName(), "resume");
		gsm.resume();
	}

}
