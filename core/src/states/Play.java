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
import main.Level;
import main.Player;
import nodeNGraph.TmxFlatTiledGraph;
import nodeNGraph.TmxFlatTiledNode;
import scenes.Hud;

public class Play extends GameState {

	boolean debug = true;

	Player player;
	Level level;
	Hud hud;
	Box2DDebugRenderer debugRenderer;

	// AI
	private TmxFlatTiledGraph tiledMapGraph;
	int lastScreenX; // last point x, pixels
	int lastScreenY; // last point y, pixels
	int lastEndTileX;
	int lastEndTileY;
	int startTileX; // starting point x, units
	int startTileY; // starting point y, units

	TmxTiledSmoothableGraphPath<TmxFlatTiledNode> path;
	TmxTiledManhattanDistance<TmxFlatTiledNode> heuristic;
	IndexedAStarPathFinder<TmxFlatTiledNode> pathFinder;
	PathSmoother<TmxFlatTiledNode, Vector2> pathSmoother;

	public Play(GameStateManager gsm) {
		super(gsm);

		level = new Level(cam);
		level.init(shape);
		level.create(debug);
		player = new Player(level.getWorld(), debug);
		player.initLevel(level);
		player.create(sb, shape, cam);
		debugRenderer = new Box2DDebugRenderer();
		hud = new Hud(sb);
		cam.update();

		// AI
		tiledMapGraph = new TmxFlatTiledGraph();
		tiledMapGraph.init(level.getMap());

		path = new TmxTiledSmoothableGraphPath<TmxFlatTiledNode>();
		heuristic = new TmxTiledManhattanDistance<TmxFlatTiledNode>();
		pathFinder = new IndexedAStarPathFinder<TmxFlatTiledNode>(tiledMapGraph, true);
		pathSmoother = new PathSmoother<TmxFlatTiledNode, Vector2>(
				new TmxTiledRaycastCollisionDetector<>(tiledMapGraph));
	}

	public void update(float dt) {

		level.update();
		player.update(dt);
		startTileX = (int) player.get_position_tile().x;
		startTileY = (int) player.get_position_tile().y;

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
		
		if(player.get_control().get_target_update()) {
			updatePath(true);
		}
		
		System.out.println("lastScreenX: " + lastScreenX + " y: " + lastScreenY);
		System.out.println("lastEndTileX: " + lastEndTileX + " y: " + lastEndTileY);
		System.out.println("startTileX: " + startTileX + " y: " + startTileY);
		System.out.println();
	}

	public void render() {
		Gdx.gl.glClearColor(1 / 255f, 75 / 255f, 62 / 255f, 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

		cam.update();
		level.render(cam);
		
		// AI
		shape.setProjectionMatrix(cam.combined);
		shape.begin(ShapeType.Filled);
		shape.setColor(Color.GREEN);
		int nodeCount = path.getCount();
		for (int i = 0; i < nodeCount; i++) {
			TmxFlatTiledNode node = path.nodes.get(i);
			shape.rect(node.x * Level.tile_size, node.y * Level.tile_size, Level.tile_size,Level.tile_size);
		}
		shape.end();
		
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
	
	private void updatePath(boolean forceUpdate) {
        int tileX = (int)(player.get_control().get_target_tile().x);
        int tileY = (int)(player.get_control().get_target_tile().y);
        if (forceUpdate || tileX != lastEndTileX || tileY != lastEndTileY) {
            TmxFlatTiledNode startNode = tiledMapGraph.getNode(startTileX, startTileY);
            TmxFlatTiledNode endNode = tiledMapGraph.getNode(tileX, tileY);

            if (startNode != null && endNode != null) {
                if (forceUpdate || endNode.type == TmxFlatTiledNode.TILE_FLOOR) {
                    if (endNode.type == TmxFlatTiledNode.TILE_FLOOR) {
                        lastEndTileX = tileX;
                        lastEndTileY = tileY;
                    } else {
                        endNode = tiledMapGraph.getNode(lastEndTileX, lastEndTileY);
                    }
                    path.clear();
                    tiledMapGraph.startNode = startNode;
                    long startTime = nanoTime();
                    pathFinder.searchNodePath(startNode, endNode, heuristic, path);
                    if (pathFinder.metrics != null) {
                        float elapsed = (TimeUtils.nanoTime() - startTime) / 1000000f;
                        System.out.println("----------------- Indexed A* Path Finder Metrics -----------------");
                        System.out.println("Visited nodes................... = " + pathFinder.metrics.visitedNodes);
                        System.out.println("Open list additions............. = " + pathFinder.metrics.openListAdditions);
                        System.out.println("Open list peak.................. = " + pathFinder.metrics.openListPeak);
                        System.out.println("Path finding elapsed time (ms).. = " + elapsed);
                    }
                }
            }
        }
    }

    private long nanoTime() { return pathFinder.metrics == null ? 0 : TimeUtils.nanoTime(); }

    /*
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        tmpUnprojection = new Vector3();
        viewport.unproject(tmpUnprojection.set(screenX, screenY, 0));
        int intX = (int)tmpUnprojection.x;
        int intY = (int)tmpUnprojection.y;

        //PRINT TILES
        if ((tiledMapGraph.getNodeType(intX, intY) != null)) {
            System.out.println(tiledMapGraph.getNodeType(intX, intY) + "(" + intX + "; " + intY + ")");
        } else {
            System.out.println("It's null" + "(" + intX + "; " + intY + ")");
        }
        return false;
    }
    */

    /*
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        viewport.unproject(tmpUnprojection.set(screenX, screenY, 0));
        int tileX = (int)(tmpUnprojection.x);
        int tileY = (int)(tmpUnprojection.y);
        TmxFlatTiledNode startNode = tiledMapGraph.getNode(tileX, tileY);

        if (startNode != null) {
            if (startNode.type == TmxFlatTiledNode.TILE_FLOOR) {
                startTileX = tileX;
                startTileY = tileY;
                updatePath(true);
            }
        }
        return true;
    }
    */

    /*
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        lastScreenX = screenX;
        lastScreenY = screenY;
        updatePath(false);
        return true;
    }
    */
}
