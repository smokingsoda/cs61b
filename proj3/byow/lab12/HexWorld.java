package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 60;
    private static final int HEIGHT = 50;
    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    private static class Position {
        public int x;
        public int y;

        public Position (int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Position shiftBy(int dx, int dy) {
            return new Position(this.x + dx, this.y + dy);
        }
    }
    public static TETile randomTile() {
        int tileNum = RANDOM.nextInt(7);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.GRASS;
            case 3: return Tileset.WATER;
            case 4: return Tileset.SAND;
            case 5: return Tileset.LOCKED_DOOR;
            case 6: return Tileset.AVATAR;
            default: return Tileset.MOUNTAIN;
        }
    }

    public static void drawWhole(Position p, int hexagonLength, int worldLength, TETile[][] world) {
        // Upper
        for (int i = 1; i <= worldLength; i++) {
            if (i == worldLength) {
                drawHexagonRow(p, hexagonLength, i, world);
                p = p.shiftBy(2 * hexagonLength - 1, -hexagonLength);
                drawHexagonRow(p, hexagonLength, i - 1, world);
                p = p.shiftBy(-(2 * hexagonLength - 1), -hexagonLength);
            } else {
                drawHexagonRow(p, hexagonLength, i, world);
                p = p.shiftBy(-(2 * hexagonLength - 1), -hexagonLength);
            }
        }
        // Middle
        if (worldLength > 2) {
            for (int i = 1; i <= worldLength - 2; i++) {
                if (i == worldLength - 2) {
                    drawHexagonRow(p, hexagonLength, worldLength, world);
                    p = p.shiftBy(2 * hexagonLength - 1, -hexagonLength);
                }
                else {
                    drawHexagonRow(p, hexagonLength, worldLength, world);
                    p = p.shiftBy(2 * hexagonLength - 1, -hexagonLength);
                    drawHexagonRow(p, hexagonLength, worldLength - 1, world);
                    p = p.shiftBy(-(2 * hexagonLength - 1), -hexagonLength);
                }
            }
        }

        // Lower
        for (int i = worldLength; i >= 1; i--) {
            if (i == worldLength) {
                drawHexagonRow(p, hexagonLength, i - 1, world);
                p = p.shiftBy(-(2 * hexagonLength - 1), -hexagonLength);
                drawHexagonRow(p, hexagonLength, i, world);
                p = p.shiftBy(2 * hexagonLength - 1, -hexagonLength);
            } else {
                drawHexagonRow(p, hexagonLength, i, world);
                p = p.shiftBy(2 * hexagonLength - 1, -hexagonLength);
            }
        }
    }

    public static void drawHexagonRow(Position p, int hexagonLength, int hexagonNum, TETile[][] world) {
        for (int i = 0; i < hexagonNum; i++) {
            addHexagon(p, hexagonLength, world, randomTile());
            p = p.shiftBy(4 * hexagonLength - 2, 0);
        }
    }
    public static void addHexagon(Position p, int length, TETile[][] world, TETile tile) {
        addHexagonHelper(p, length - 1, length, world, tile);
    }

    public static void addHexagonHelper(Position p, int blank, int length, TETile[][] world, TETile tile) {
        if (blank < 0) {
            return;
        }
        else {
            drawRow(p, blank, length, world, tile);
            addHexagonHelper(p.shiftBy(0, -1), blank - 1, length + 2, world, tile);
            drawRow(p.shiftBy(0, -(2 * blank + 1)), blank, length, world, tile);
        }
    }
    public static void drawRow(Position p, int blank, int length, TETile[][] world, TETile tile) {
        for (int i = 0; i < length; i++) {
            world[p.x + blank + i][p.y] = tile;
        }
    }
    public static void drawWorld(TETile[][] world) {
        // initialize tiles

        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        Position p = new Position(30, 49);
        drawWhole(p, 2, 3, world);
    }
    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        drawWorld(world);
        // draws the world to the screen
        ter.renderFrame(world);
    }


}
