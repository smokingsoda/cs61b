package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.File;
import java.util.*;

import static byow.Core.Utility.*;


public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 40;
    public static final int HEIGHT = 40;
    private Random RANDOM;
    private static final int MAX_HALLWAY_LENGTH = 35;
    private static final int MAX_HALLWAY_NUM = 60;
    private static final int MAX_HALLWAY_WIDTH = 1;
    private static final Font BIG_FONT = new Font("Monaco", Font.BOLD, 30);
    private static final Font SMALL_FONT = new Font("Monaco", Font.BOLD, 15);
    private inputSource source;
    private static final HashMap<String, Direction> directions = new HashMap<>();
    private boolean isVisual = false;
    public World finalWorldFrame;
    private static final File DIR = new File(System.getProperty("user.dir"));
    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public Engine() {
        directions.put("up", new Direction(0, 1));
        directions.put("down", new Direction(0, -1));
        directions.put("left", new Direction(-1, 0));
        directions.put("right", new Direction(1, 0));
    }
    public void initializeMenu() {
        // Enable
        if (isVisual) {
            StdDraw.enableDoubleBuffering();
            StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
            StdDraw.setXscale(0, WIDTH);
            StdDraw.setYscale(0, HEIGHT);
            StdDraw.clear(Color.BLACK);
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.setFont(BIG_FONT);
            StdDraw.text(WIDTH/2, HEIGHT/2 + 5, "CS 61B: THE GAME");
            StdDraw.setFont(SMALL_FONT);
            StdDraw.text(WIDTH/2, HEIGHT/2, "NEW GAME (N)");
            StdDraw.text(WIDTH/2, HEIGHT/2 - 5, "LOAD GAME (L)");
            StdDraw.text(WIDTH/2, HEIGHT/2 - 10, "QUIT (Q)");
            StdDraw.show();
        }
    }
    public void menu() {
        while (source.hasNext()) {
            char c = source.next();
            if (c == 'n') {
                newGame();
                break;
            } else if (c == 'l') {
                loadGame();
                break;
            } else if (c == 'q') {
                quitGame();
                break;
            }
        }
    }

    public void newGame() {
        extractSeed();
        if (isVisual) {
            TERenderer ter = new TERenderer();
            ter.initialize(WIDTH, HEIGHT);
            StdDraw.clear(Color.BLACK);
        }
        finalWorldFrame = new World(new TETile[WIDTH][HEIGHT], new Position(0, 0), new Position(0, 0));
        initializeWorld();
        playGame();
    }
    public void loadGame() {
        File loadFile= join(DIR, "byow.txt");
        this.finalWorldFrame = readObject(loadFile, World.class);
        playGame();
    }

    public void playGame() {
        HashSet validateOperation = new HashSet<>();
        char[] charsOp = new char[]{'w', 'a', 's', 'd', 'q', ':'};
        for (char op : charsOp) {
            validateOperation.add(op);
        }
        while(source.hasNext()) {
            updateWorld();
            char c = source.next();
            if (validateOperation.contains(c)) {
                if (c == ':' && source.hasNext() && source.next() == 'q') {
                    saveGame();
                    break;
                } else {
                    move(c);

                }
            }

        }
    }
    public void saveGame() {
        File saveFile = join(DIR, "byow.txt");
        writeObject(saveFile, this.finalWorldFrame);
    }
    public void updateWorld() {
        for (Position p : finalWorldFrame.floorSet) {
            finalWorldFrame.world[p.x][p.y] = Tileset.FLOOR;
        }
        for (Position p : finalWorldFrame.wallSet) {
            finalWorldFrame.world[p.x][p.y] = Tileset.WALL;
        }
        entityBFS();
        finalWorldFrame.world[finalWorldFrame.entityPosition.x][finalWorldFrame.entityPosition.y] = Tileset.FLOWER;
        finalWorldFrame.world[finalWorldFrame.avatarPosition.x][finalWorldFrame.avatarPosition.y] = Tileset.AVATAR;
        if (isVisual) {
            ter.renderFrame(finalWorldFrame.world);
        }
        System.out.println(this.finalWorldFrame.world[27][30]);
        System.out.println(this.finalWorldFrame.floorSet.contains(new Position(27,30)));

    }
    public void move(char operation) {
        Direction direction = new Direction(0, 0);
        switch (operation) {
            case 'w':
                direction = directions.get("up");
                break;
            case 'a':
                direction = directions.get("left");
                break;
            case 's':
                direction = directions.get("down");
                break;
            case 'd':
                direction = directions.get("right");
        }
        int newX = finalWorldFrame.avatarPosition.x + direction.lateral;
        int newY = finalWorldFrame.avatarPosition.y + direction.vertical;
        if (canMove(newX, newY)) {
            this.finalWorldFrame.world[finalWorldFrame.avatarPosition.x][finalWorldFrame.avatarPosition.y] = Tileset.FLOOR;
            finalWorldFrame.avatarPosition = new Position(newX, newY);
            System.out.println(finalWorldFrame.avatarPosition + "qqq");
        }
    }
    public void extractSeed() {
        HashSet validSeed = new HashSet();
        for (int i = 0; i < 10; i++) {
            validSeed.add(Integer.toString(i));
        }
        String seed = "";
        while(source.hasNext()) {
            if (isVisual) {
                StdDraw.clear(Color.BLACK);
                StdDraw.text(WIDTH / 2, HEIGHT / 2, "Seed: " + seed);
                StdDraw.show();
            }

            char c = source.next();
            if (c == 's') {
                this.RANDOM = new Random(Long.parseLong(seed));
                break;
            }
            if (validSeed.contains(Character.toString(c))) {
                seed = seed + c;
            }
        }
    }

    public void quitGame() {

    }
    public void interactWithKeyboard() {
        isVisual = true;
        this.source = new keyBoardInput();
        initializeMenu();
        menu();
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
        isVisual = false;
        this.source = new stringInput(input);
        initializeMenu();
        menu();
        return this.finalWorldFrame.world;
    }

    public void initializeWorld() {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                this.finalWorldFrame.world[x][y] = Tileset.NOTHING;
            }
        }
        Position p = new Position(RANDOM.nextInt(WIDTH), RANDOM.nextInt(HEIGHT));
        int HallwayNum = RANDOM.nextInt(MAX_HALLWAY_NUM) + 1;
        LinkedList<Hallway> lH = new LinkedList<>();
        for (int i = 0; i < HallwayNum; i++) {
            Hallway h = randomHallway(p, this.finalWorldFrame.world);
            lH.add(h);
            p = h.endPosition;
        }
        for (Hallway h : lH) {
            h.drawHallway(this.finalWorldFrame.world);
        }
        Wall wall = new Wall();
        wall.drawWall(this.finalWorldFrame.world);
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                if (this.finalWorldFrame.world[x][y].equals(Tileset.FLOOR)) {
                    finalWorldFrame.floorSet.add(new Position(x, y));
                } else if (this.finalWorldFrame.world[x][y].equals(Tileset.WALL)) {
                    finalWorldFrame.wallSet.add(new Position(x, y));
                }
            }
        }
        addAvatar();
        //addGate(finalWorldFrame.wallList);
        addEntity();
        updateWorld();
    }
    public void addAvatar() {
        int random = RANDOM.nextInt(finalWorldFrame.floorSet.size());
        for (Position position : finalWorldFrame.floorSet) {
            if (random == 0) {
                finalWorldFrame.avatarPosition = new Position(position.x, position.y);
                break;
            } else {
                random -= 1;
            }
        }
        //finalWorldFrame.floorSet.add(finalWorldFrame.avatarPosition);
    }
    public void addGate(ArrayList<Position> wallList) {
        Position position = wallList.get(RANDOM.nextInt(wallList.size()));
        this.finalWorldFrame.world[position.x][position.y] = Tileset.LOCKED_DOOR;
    }
    public void addEntity() {
        int random = RANDOM.nextInt(finalWorldFrame.floorSet.size());
        for (Position position : finalWorldFrame.floorSet) {
            if (random == 0) {
                finalWorldFrame.entityPosition = position;
                break;
            } else {
                random -= 1;
            }
        }
        //finalWorldFrame.floorSet.add(finalWorldFrame.entityPosition);
    }
    public void entityBFS() {
//        HashSet<Position> floorSet = new HashSet<>();
//        for (int i = 0; i < finalWorldFrame.world.length; i++) {
//            for (int j = 0; j < finalWorldFrame.world[0].length; j++) {
//                if (finalWorldFrame.world[i][j].equals(Tileset.FLOOR)) {
//                    floorSet.add(new Position(i, j));
//                }
//            }
//        }
//        for (Position p : finalWorldFrame.floorSet) {
//            floorSet.add(p);
//
//        }
        HashMap<Position, Position> edgeMap = new HashMap<>();
        HashMap<Position, Integer> distMap = new HashMap<>();
        PriorityQueue<Pair> fringe = new PriorityQueue<>();
        for (Position fP : finalWorldFrame.floorSet) {
            Pair pair = new Pair(fP, 10000);
            edgeMap.put(fP, null);
            if (fP.equals(finalWorldFrame.entityPosition)) {
                distMap.put(fP, 0);
                pair.value = 0;
                fringe.add(pair);
            } else {
                distMap.put(fP, 10000);
                fringe.add(pair);
            }
        }
        while (fringe.size() != 0) {
            Pair currentPair = fringe.poll();
            Position sourcePosition = currentPair.key;
            int x = sourcePosition.x;
            int y = sourcePosition.y;
            int sourceDistanceToRoot = currentPair.value;
            HashSet<Position> udlf = new HashSet<>();
            udlf.add(new Position(x + 1, y));
            udlf.add(new Position(x - 1, y));
            udlf.add(new Position(x, y - 1));
            udlf.add(new Position(x, y + 1));
            for (Position currentPosition : udlf) {
                if (finalWorldFrame.floorSet.contains(currentPosition)) {
                    int targetDistanceToRoot = distMap.get(currentPosition);
                    if (sourceDistanceToRoot + 1 < targetDistanceToRoot) {
                        Pair newPair = new Pair(currentPosition, sourceDistanceToRoot + 1);
                        fringe.remove(newPair);
                        distMap.put(currentPosition, sourceDistanceToRoot + 1);
                        //Don't forget to plus 1!!! That's crucial!
                        fringe.add(newPair);
                        edgeMap.put(currentPosition, sourcePosition);
                    }
                }
            }
        }
        Position p = finalWorldFrame.avatarPosition;
            while (!p.equals(finalWorldFrame.entityPosition)) {
            Position targetPosition = edgeMap.get(p);
            finalWorldFrame.world[targetPosition.x][targetPosition.y] = Tileset.GRASS;
            p = targetPosition;
        }
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
            case 0: return directions.get("up");
            case 1: return directions.get("right");
            case 2: return directions.get("down");
            case 3: return directions.get("left");
            default: throw new RuntimeException("Wrong");
        }
    }

    public boolean canMove(int x, int y) {
        return x >= 0 && x < this.finalWorldFrame.world.length && y >= 0
                && y < this.finalWorldFrame.world[0].length &&
                (!this.finalWorldFrame.world[x][y].equals(Tileset.WALL));
    }
}
