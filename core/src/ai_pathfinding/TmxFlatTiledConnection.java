package ai_pathfinding;

import com.badlogic.gdx.ai.pfa.DefaultConnection;

import nodeNGraph.TmxFlatTiledGraph;
import nodeNGraph.TmxFlatTiledNode;

public class TmxFlatTiledConnection extends DefaultConnection<TmxFlatTiledNode> {

    static final float NON_DIAGONAL_COST = (float)Math.sqrt(2);
    TmxFlatTiledGraph tiledMapGraph;

    public TmxFlatTiledConnection(TmxFlatTiledGraph tiledMapGraph, TmxFlatTiledNode fromNode, TmxFlatTiledNode toNode) {
        super(fromNode, toNode);
        this.tiledMapGraph = tiledMapGraph;
    }

    @Override
    public float getCost() {
        if (tiledMapGraph.diagonal) return 1;
        return getToNode().x != tiledMapGraph.startNode.x && getToNode().y != tiledMapGraph.startNode.y ? NON_DIAGONAL_COST : 1;
    }
}