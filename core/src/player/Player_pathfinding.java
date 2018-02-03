package player;

import com.badlogic.gdx.ai.pfa.PathSmoother;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import ai_pathfinding.TmxTiledManhattanDistance;
import ai_pathfinding.TmxTiledRaycastCollisionDetector;
import ai_pathfinding.TmxTiledSmoothableGraphPath;
import main.Level;
import nodeNGraph.TmxFlatTiledGraph;
import nodeNGraph.TmxFlatTiledNode;

public class Player_pathfinding {

	boolean debug = true;

	Player player;
	Level level;
	Player_control control;
	ShapeRenderer shape;

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

	public Player_pathfinding(Player player, Player_control control, Level level) {
		this.player = player;
		this.control = control;
		this.level = level;
	}

	public void create() {
		// AI
		tiledMapGraph = new TmxFlatTiledGraph();
		tiledMapGraph.init(level.getMap());

		path = new TmxTiledSmoothableGraphPath<TmxFlatTiledNode>();
		heuristic = new TmxTiledManhattanDistance<TmxFlatTiledNode>();
		pathFinder = new IndexedAStarPathFinder<TmxFlatTiledNode>(tiledMapGraph, true);
		pathSmoother = new PathSmoother<TmxFlatTiledNode, Vector2>(
				new TmxTiledRaycastCollisionDetector<>(tiledMapGraph));
	}

	public void init(ShapeRenderer shape) {
		this.shape = shape;
	}

	public void update(boolean is_target_updated) {

		if (is_target_updated) {
			startTileX = (int) player.get_position_tile().x;
			startTileY = (int) player.get_position_tile().y;
			updatePath(true);
		}

	}

	public void render(OrthographicCamera cam) {
		// AI
		shape.setProjectionMatrix(cam.combined);
		shape.begin(ShapeType.Filled);
		shape.setColor(Color.GREEN);
		int nodeCount = path.getCount();
		for (int i = 0; i < nodeCount; i++) {
			TmxFlatTiledNode node = path.nodes.get(i);
			shape.rect(node.x * Level.tile_size, node.y * Level.tile_size, Level.tile_size, Level.tile_size);
		}
		shape.end();
	}

	private void updatePath(boolean forceUpdate) {
		int tileX = (int) (player.get_control().get_target_tile().x);
		int tileY = (int) (player.get_control().get_target_tile().y);
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
						
						//.out.println("----------------- Indexed A* Path Finder Metrics -----------------");
						//System.out.println("Visited nodes................... = " + pathFinder.metrics.visitedNodes);
						//System.out.println("Open list additions............. = " + pathFinder.metrics.openListAdditions);
						//System.out.println("Open list peak.................. = " + pathFinder.metrics.openListPeak);
						//System.out.println("Path finding elapsed time (ms).. = " + elapsed);
						
					}
				}
			}
		}
	}

	private long nanoTime() {
		return pathFinder.metrics == null ? 0 : TimeUtils.nanoTime();
	}
	
	public TmxTiledSmoothableGraphPath<TmxFlatTiledNode> getPath() {
		return path;
	}

}
