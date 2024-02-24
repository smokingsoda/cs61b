package byow.Core;

import byow.TileEngine.TETile;

import java.io.Serializable;

public class World implements Serializable {
    public TETile[][] world;
    public World(TETile[][] teTiles) {
        this.world = teTiles;
    }

}
