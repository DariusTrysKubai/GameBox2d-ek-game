package nodeNGraph;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.utils.Array;

public class TmxFlatTiledNode extends TmxTiledNode<TmxFlatTiledNode> {

    public TmxFlatTiledNode(int x, int y, int type, int connectionCapacity) {
        super(x, y, type, new Array<Connection<TmxFlatTiledNode>>(connectionCapacity));
    }

    @Override
    public int getIndex() {
        return x * TmxFlatTiledGraph.graphHeight + y;
    }
}
