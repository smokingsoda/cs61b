package byow.Core;

import byow.TileEngine.TETile;

public interface WorldObj {
    boolean isCollision(int x, int y, TETile[][] world);
}
