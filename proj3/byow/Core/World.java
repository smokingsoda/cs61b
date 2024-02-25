package byow.Core;


import byow.TileEngine.TETile;

import java.io.Serializable;
import java.util.HashSet;

public class World implements Serializable {
    public TETile[][] world;
    public Position avatarPosition;
    public Position entityPosition;
    public HashSet<Position> floorSet;
    public HashSet<Position> wallSet;
    public World(TETile[][] tiles, Position avatar, Position entity) {
        this.world = tiles;
        this.avatarPosition = avatar;
        this.entityPosition = entity;
        this.floorSet = new HashSet<>();
        this.wallSet = new HashSet<>();
    }
}
