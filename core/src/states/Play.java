package states;

import java.awt.geom.RectangularShape;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ai.pfa.PathSmoother;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
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
import com.badlogic.gdx.utils.TimeUtils;

import Handlers.GameStateManager;
import ai_pathfinding.TmxTiledManhattanDistance;
import ai_pathfinding.TmxTiledRaycastCollisionDetector;
import ai_pathfinding.TmxTiledSmoothableGraphPath;
import items.ItemManager;
import main.Level;
import nodeNGraph.TmxFlatTiledGraph;
import nodeNGraph.TmxFlatTiledNode;
import player.Player;
import scenes.Hud;

public class Play extends GameState {

	boolean debug = false;

	Player player;
	Level level;
	Hud hud;
	ItemManager itemmanager;
	Box2DDebugRenderer debugRenderer;

	public Play(GameStateManager gsm) {
		super(gsm);

		level = new Level(cam);
		level.init(shape);
		level.create(debug);
		player = new Player(level.getWorld(), debug);
		player.create(sb, shape, cam);
		player.initLevel(level);
		debugRenderer = new Box2DDebugRenderer();
		hud = new Hud(sb, hudcam);
		itemmanager = new ItemManager();
		itemmanager.init(sb, cam, hudcam, shape, player);
		itemmanager.create();
		cam.update();
		
		hud.init_player(player);
		level.load_items(itemmanager);
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
		
		hud.stage.draw();

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
