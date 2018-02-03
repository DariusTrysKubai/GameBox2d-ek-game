package nodeNGraph;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.utils.Array;

public abstract class TmxTiledNode<N extends TmxTiledNode<N>> {

    public static final int TILE_FLOOR = 0;
    public static final int TILE_WALL = 1;

    public final int x;
    public final int y;
    public final int type;

    protected Array<Connection<N>> connections;

    public TmxTiledNode(int x, int y, int type, Array<Connection<N>> connections) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.connections = connections;
    }

    public abstract int getIndex();
    public Array<Connection<N>> getConnections() { return this.connections; }
}
