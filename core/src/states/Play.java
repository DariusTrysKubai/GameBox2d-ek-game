package states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

import GameData.GameData;
import Handlers.GameStateManager;
import InputHandlers.MyInputMUX;
import items.ItemManager;
import main.Level;
import player.Player;
import scenes.Hud;

public class Play extends GameState {

	boolean debug = false;

	GameData data;
	Player player;
	Level level;
	Hud hud;
	ItemManager itemmanager;
	MyInputMUX input;
	Box2DDebugRenderer debugRenderer;

	public Play(GameStateManager gsm) {
		super(gsm);
		level = new Level(cam);
		level.init(shape);
		level.create(debug);
		player = new Player(level.getWorld(), debug);
		player.create(sb, shape, cam);
		player.initLevel(level);
		player.initControl(viewport);
		debugRenderer = new Box2DDebugRenderer();
		hud = new Hud(sb, hudcam);
		itemmanager = new ItemManager();
		itemmanager.init(sb, cam, hudcam, shape, player);
		itemmanager.create();
		itemmanager.initLevel(level);
		cam.update();
		hud.init_player(player);
		hud.init_itemManager(itemmanager);
		player.initHud(hud);
		level.load_items(itemmanager);
		
		// Input
		input = new MyInputMUX();
		input.setHudProcessor(hud.getStage());
		input.setGestureProcessor(player.get_control().getGestureProcessor());
		input.setGameProcessor(player.get_control().getInputProcessor());
		input.create();
	}

	public void update(float dt) {

		level.update();
		player.update(dt);
		itemmanager.update();
		hud.update(dt);

		// Set the camera position to the player middle
		cam.position.x = player.getBody().getPosition().x;
		cam.position.y = player.getBody().getPosition().y;
	}

	public void render() {
		Gdx.gl.glClearColor(1 / 255f, 75 / 255f, 62 / 255f, 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

		cam.update();
		level.render(cam);
		itemmanager.render();
		player.render(cam);
		if (debug) {
			debugRenderer.render(level.getWorld(), cam.combined);
		}

		hud.getStage().draw();

		// important!
		level.getWorld().step(1 / 60f, 6, 2);
	}

	public void dispose() {
		player.dispose();
		level.dispose();
		shape.dispose();
	}

	@Override
	public void handleInput() {
		// TODO Auto-generated method stub

	}

}
