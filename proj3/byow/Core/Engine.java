package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.LinkedList;
import java.util.Random;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    private Random RANDOM;
    private static final int MAX_HALLWAY_LENGTH = 35;
    private static final int MAX_HALLWAY_NUM = 60;
    private static final int MAX_HALLWAY_WIDTH = 1;


    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        ter.initialize(WIDTH, HEIGHT);
        input = input.substring(1, input.length() - 1);
        this.RANDOM = new Random(Integer.parseInt(input));
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                finalWorldFrame[x][y] = Tileset.NOTHING;
            }
        }
        Position p = new Position(RANDOM.nextInt(WIDTH), RANDOM.nextInt(HEIGHT));
        int HallwayNum = RANDOM.nextInt(MAX_HALLWAY_NUM) + 1;
        LinkedList<Hallway> lH = new LinkedList<>();
        for (int i = 0; i < HallwayNum; i++) {
            Hallway h = randomHallway(p, finalWorldFrame);
            lH.add(h);
            p = h.endPosition;
        }
        for (Hallway h : lH) {
            h.drawHallway(finalWorldFrame);
        }
        Wall wall = new Wall();
        wall.drawWall(finalWorldFrame);
        ter.renderFrame(finalWorldFrame);
        return finalWorldFrame;
    }
    public void drawWall(int x, int y, TETile[][] world) {
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
    public boolean isCollision(int x, int y, TETile[][] world) {
        return x > world.length - 1 || y > world[0].length - 1
                || x < 0 || y < 0;
    }

    public Hallway randomHallway(Position p, TETile[][] world) {
        int length = RANDOM.nextInt(MAX_HALLWAY_LENGTH) + 1;
        int width = RANDOM.nextInt(MAX_HALLWAY_WIDTH) + 1;
        Direction d = randomDirection();
        return new Hallway(length, width, p, d, world);
    }

    public Direction randomDirection() {
        int randomInt = RANDOM.nextInt(4);
        switch (randomInt) {
            case 0: return new Direction(0, 1);
            case 1: return new Direction(1, 0);
            case 2: return new Direction(0, -1);
            case 3: return new Direction(-1, 0);
            default: throw new RuntimeException("Wrong");


        }
    }
}
