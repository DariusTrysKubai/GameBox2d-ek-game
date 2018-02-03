package ai_pathfinding;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

public class TmxMapToGraph {

	static final int TILE_FLOOR = 0;
	static final int TILE_WALL = 1;
	

	private TmxMapToGraph() {
	}

	public static int[][] tilesToNodes(int mapWidthInTiles, int mapHeightInTiles, TiledMap tiledMap) {
		int[][] map = new int[mapWidthInTiles][mapHeightInTiles];
		TiledMapTileLayer layer;
		layer = (TiledMapTileLayer) tiledMap.getLayers().get(5);
		for (int x = 0; x < layer.getWidth(); x++) {
			for (int y = 0; y < layer.getHeight(); y++) {
				Cell cell = layer.getCell(x, y);
				if (cell != null) {
					map[x][y] = TILE_WALL;
				} else {
					map[x][y] = TILE_FLOOR;
				}
			}
		}
		return map;
	}
}