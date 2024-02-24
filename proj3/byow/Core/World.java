package byow.Core;


import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;
import java.util.HashMap;

public class World implements Serializable {
    public TETile[][] world;
    public Position avatarPosition;
    public World(TETile[][] tiles, Position avatar) {
        this.world = tiles;
        this.avatarPosition = avatar;
    }
}
