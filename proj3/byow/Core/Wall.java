package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Wall implements WorldObj{
    public Wall() {
    }
    public void drawWall(TETile[][] world) {
        for (int x = 0; x < world.length; x++) {
            for (int y = 0; y < world[0].length; y++) {
                drawUnit(x, y, world);
            }
        }
    }
    public void drawUnit(int x, int y, TETile[][] world) {
        if (world[x][y].equals(Tileset.FLOOR)) {
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    if (i == 0 && j == 0) {
                        continue;
                    }
                    if (!isCollision(x + i, y + j, world) && world[x + i][y + j].equals(Tileset.NOTHING)) {
                        world[x + i][y + j] = Tileset.WALL;
                    }
                }
            }
        }

    }

    @Override
    public boolean isCollision(int x, int y, TETile[][] world) {
        return x > world.length - 1 || y > world[0].length - 1
                || x < 0 || y < 0;
    }
}
