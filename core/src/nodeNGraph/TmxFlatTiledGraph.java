package nodeNGraph;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;

import ai_pathfinding.TmxFlatTiledConnection;
import ai_pathfinding.TmxMapToGraph;
import main.Level;

public class TmxFlatTiledGraph implements TmxTiledGraph<TmxFlatTiledNode> {

	public static final int graphWidth = Level.tile_size * 100;
	public static final int graphHeight = Level.tile_size * 100;

	protected Array<TmxFlatTiledNode> nodes;

	public boolean diagonal;
	public TmxFlatTiledNode startNode;

	public TmxFlatTiledGraph() {
		this.nodes = new Array<TmxFlatTiledNode>(graphWidth * graphHeight);
		this.diagonal = true;
		this.startNode = null;
	}

	@Override
	public void init(TiledMap tiledMap) {
		int map[][] = TmxMapToGraph.tilesToNodes(graphWidth, graphHeight, tiledMap);
		for (int x = 0; x < graphWidth; x++) {
			for (int y = 0; y < graphHeight; y++) {
				nodes.add(new TmxFlatTiledNode(x, y, map[x][y], 4));
			}
		}
		for (int x = 0; x < graphWidth; x++) {
			int idx = x * graphHeight;
			for (int y = 0; y < graphHeight; y++) {
				TmxFlatTiledNode n = nodes.get(idx + y);
				if (x > 0)
					addConnection(n, -1, 0);
				if (y > 0)
					addConnection(n, 0, -1);
				if (x < graphWidth - 1)
					addConnection(n, 1, 0);
				if (y < graphHeight - 1)
					addConnection(n, 0, 1);
			}
		}
	}

	@Override
	public TmxFlatTiledNode getNode(int x, int y) {
		if (((x >= 0) && (x < graphWidth)) && ((y >= 0) && (y < graphHeight))) {
			return nodes.get(x * graphHeight + y);
		} else {
			return null;
		}
	}

	@Override
	public TmxFlatTiledNode getNode(int index) {
		return nodes.get(index);
	}

	@Override
	public int getIndex(TmxFlatTiledNode node) {
		return node.getIndex();
	}

	@Override
	public int getNodeCount() {
		return nodes.size;
	}

	@Override
	public Array<Connection<TmxFlatTiledNode>> getConnections(TmxFlatTiledNode fromNode) {
		return fromNode.getConnections();
	}

	public String getNodeType(int x, int y) {
		if (((x >= 0) && (x < graphWidth)) && ((y >= 0) && (y < graphHeight))) {
			if (nodes.get(x * graphHeight + y).type == 0) {
				return "TILE_FLOOR";
			} else if (nodes.get(x * graphHeight + y).type == 1) {
				return "TILE_WALL";
			} else {
				return "Unknown node type";
			}
		} else {
			return null;
		}
	}

	public void toggleDiagonal() {
		diagonal = !diagonal;
	}

	private void addConnection(TmxFlatTiledNode n, int xOffset, int yOffset) {
		TmxFlatTiledNode target = getNode(n.x + xOffset, n.y + yOffset);
		if (target.type == TmxFlatTiledNode.TILE_FLOOR) {
			n.getConnections().add(new TmxFlatTiledConnection(this, n, target));
		}
	}
}
