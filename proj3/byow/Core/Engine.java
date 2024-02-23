package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    private static final Random RANDOM = new Random(38);
    private static final int MAX_HALLWAY_LENGTH = 10;

    public Engine() {
        ter.initialize(WIDTH, HEIGHT);
    }

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
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                finalWorldFrame[x][y] = Tileset.NOTHING;
            }
        }
        Position p = new Position(20, 20);
        int num1 = RANDOM.nextInt(10);
        int num2 = RANDOM.nextInt(10);
        int HallwayNum = num1 * num2;
        LinkedList<Hallway> lH = new LinkedList<>();
        LinkedList<Corner> lC = new LinkedList<>();
        for (int i = 0; i < HallwayNum; i++) {
            Hallway h = randomHallway(p, finalWorldFrame);
            lH.add(h);
            p = h.endPosition;
        }
        for (int i = 0; i < HallwayNum - 1; i++) {
            Hallway pH = lH.get(i);
            Hallway nH = lH.get(i + 1);
            Corner c = new Corner(pH, nH, pH.endPosition);
            lC.add(c);
        }
        for (Hallway h : lH) {
            h.drawHallway(finalWorldFrame);
            ter.renderFrame(finalWorldFrame);
        }
        for (Corner c : lC) {
            c.drawCorner(finalWorldFrame);
            ter.renderFrame(finalWorldFrame);
        }
        ter.renderFrame(finalWorldFrame);
        return finalWorldFrame;
    }

    public Hallway randomHallway(Position p, TETile[][] world) {
        int length = RANDOM.nextInt(MAX_HALLWAY_LENGTH) + 5;
        Direction d = randomDirection();
        return new Hallway(length, p, d, world);
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
