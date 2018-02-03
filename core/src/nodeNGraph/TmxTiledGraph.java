package nodeNGraph;

import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.maps.tiled.TiledMap;

public interface TmxTiledGraph<N extends TmxTiledNode<N>> extends IndexedGraph<N> {

    public void init(TiledMap tiledMap);
    public N getNode(int x, int y);
    public N getNode(int index);
}
