package ai_pathfinding;

import com.badlogic.gdx.ai.pfa.Heuristic;

import nodeNGraph.TmxTiledNode;

public class TmxTiledManhattanDistance<N extends TmxTiledNode<N>> implements Heuristic<N> {

    public TmxTiledManhattanDistance() {}

    @Override
    public float estimate(N node, N endNode) {
        return Math.abs(endNode.x - node.x) + Math.abs(endNode.y - node.y);
    }
}
