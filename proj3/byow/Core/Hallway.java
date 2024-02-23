package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Hallway implements WorldObj {
    private int length;
    private int width;
    public Position startPosition;
    public Direction direction;
    public Position endPosition;

    public Hallway(int length, int width, Position position, Direction direction, TETile[][] world) {
        this.length = length;
        this.width = width;
        this.startPosition = position;
        this.direction = direction;
        this.endPosition = getEndPosition(world);
    }

    public void drawHallway(TETile[][] world) {
        drawFloor(world);
    }

    private Position getEndPosition(TETile[][] world) {
        int returnX = startPosition.x;
        int returnY = startPosition.y;
        returnX = returnX + direction.lateral * length;
        returnY = returnY + direction.vertical * length;
        if (isCollision(returnX, 0, world)) {
            if (returnX < 0) {
                returnX = 0;
            } else {
                returnX = world.length - 1;
            }
        }
        if (isCollision(0, returnY, world)) {
            if (returnY < 0) {
                returnY = 0;
            } else {
                returnY = world[0].length - 1;
            }
        }
        return new Position(returnX, returnY);
    }
    private void drawRow(int deviationX, int deviationY, TETile[][] world, TETile tile) {
        for (int i = 0; i < length; i++) {
            int newX = startPosition.x + i * direction.lateral + deviationX;
            int newY = startPosition.y + i * direction.vertical + deviationY;
            if (!isCollision(newX, newY, world)) {
                world[newX][newY] = tile;
            }
            else {
                break;
            }
        }
    }
    private void drawFloor(TETile[][] world) {
        for (int i = 0; i < width; i++) {
            drawRow(direction.vertical * i, direction.lateral * i, world, Tileset.FLOOR);
        }
    }

    @Override
     public boolean isCollision(int x, int y, TETile[][] world) {
        return x > world.length - 1 || y > world[0].length - 1
               || x < 0 || y < 0;
    }
}
