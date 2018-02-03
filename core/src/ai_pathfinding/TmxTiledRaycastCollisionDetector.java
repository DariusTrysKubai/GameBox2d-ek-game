package ai_pathfinding;

import com.badlogic.gdx.ai.utils.Collision;
import com.badlogic.gdx.ai.utils.Ray;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
import com.badlogic.gdx.math.Vector2;

import nodeNGraph.TmxTiledGraph;
import nodeNGraph.TmxTiledNode;

public class TmxTiledRaycastCollisionDetector<N extends TmxTiledNode<N>> implements RaycastCollisionDetector<Vector2> {

    TmxTiledGraph<N> tiledMapGraph;

    public TmxTiledRaycastCollisionDetector(TmxTiledGraph<N> tiledMapGraph) {
        this.tiledMapGraph = tiledMapGraph;
    }

    @Override
    public boolean collides(Ray<Vector2> ray) {
        int x0 = (int)ray.start.x;
        int y0 = (int)ray.start.y;
        int x1 = (int)ray.end.x;
        int y1 = (int)ray.end.y;
        int tmp;

        boolean steep = Math.abs(y1 - y0) > Math.abs(x1 - x0);
        if (steep) {
            //swap x0 and y0
            tmp = x0;
            x0 = y0;
            y0 = tmp;
            //swap x1 and y1
            tmp = x1;
            x1 = y1;
            y1 = tmp;
        }
        if (x0 > x1) {
            //swap x0 and x1
            tmp = x0;
            x0 = x1;
            x1 = tmp;
            //swap y0 and y1
            tmp = y0;
            y0 = y1;
            y1 = tmp;
        }

        int deltax = x1 - x0;
        int deltay = Math.abs(y1 - y0);
        int error = 0;
        int y = y0;
        int ystep = (y0 < y1 ? 1 : -1);
        for (int x = x0; x <= x1; x++) {
            N tile = steep ? tiledMapGraph.getNode(y, x) : tiledMapGraph.getNode(x, y);
            if (tile.type != TmxTiledNode.TILE_FLOOR) return true;
            error += deltay;
            if (error + error >= deltax) {
                y += ystep;
                error -= deltax;
            }
        }
        return false;
    }

    @Override
    public boolean findCollision(Collision<Vector2> outputCollision, Ray<Vector2> inputRay) {
        throw new UnsupportedOperationException();
    }
}
