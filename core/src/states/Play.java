package states;

import java.awt.geom.RectangularShape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import Handlers.GameStateManager;
import main.Level;
import main.Player;
import scenes.Hud;

public class Play extends GameState {

	boolean debug = false;

	Player player;
	Level level;
	Hud hud;
	Box2DDebugRenderer debugRenderer;

	public Play(GameStateManager gsm) {
		super(gsm);

		level = new Level(cam);
		level.init();
		level.create();

		player = new Player(level.getWorld());
		player.init(level);
		player.create();

		debugRenderer = new Box2DDebugRenderer();

		hud = new Hud(sb);
		
		cam.update();
	}

	public void update(float dt) {

		level.update();
		player.update();

		hud.update(dt);

		// camera control
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			cam.position.y += 5;
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			cam.position.y -= 5;
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			cam.position.x += 5;
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			cam.position.x -= 5;
		}
		
		cam.position.x = player.getBody().getPosition().x;
		cam.position.y = player.getBody().getPosition().y;

	}

	public void render() {
		Gdx.gl.glClearColor(1 / 255f, 75 / 255f, 62 / 255f, 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

		cam.update();
		
		level.getWorld().step(1 / 60f, 6, 2);
		level.render(cam);

		player.render();

		debugRenderer.render(level.getWorld(), cam.combined);
		hud.stage.draw();

		System.out.println("Hello");
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
