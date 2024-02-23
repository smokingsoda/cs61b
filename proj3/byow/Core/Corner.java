package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Corner implements WorldObj{
    public Hallway prevHallway;
    public Hallway nextHallway;
    public Position position;

    public Corner(Hallway p, Hallway n, Position position) {
        this.prevHallway = p;
        this.nextHallway = n;
        this.position = position;
    }

    public void drawCorner(TETile[][] world) {
        if (!isStright()) {
            drawWall(world);
            drawFloor(world);
        }
    }

    public void drawFloor(TETile[][] world) {
        if (!isCollision(position.x - prevHallway.direction.lateral, position.y - prevHallway.direction.vertical, world)) {
            world[position.x - prevHallway.direction.lateral][position.y - prevHallway.direction.vertical] = Tileset.FLOOR;
        }
        if (!isCollision(position.x + nextHallway.direction.lateral, position.y + nextHallway.direction.vertical, world)) {
            world[position.x + nextHallway.direction.lateral][position.y + nextHallway.direction.vertical] = Tileset.FLOOR;
        }
    }
    public void drawWall(TETile[][] world) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == 0 && j == 0) {
                    continue;
                } else {
                    int newX = position.x + i;
                    int newY = position.y + j;
                    if (!isCollision(newX, newY, world)) {
                        world[position.x + i][position.y + j] = Tileset.WALL;
                    }
                }
            }
        }
    }
    public boolean isStright() {
        Direction pD = prevHallway.direction;
        Direction nD = nextHallway.direction;
        return pD.lateral * nD.vertical + pD.vertical * nD.lateral == 0;
    }

    @Override
    public boolean isCollision(int x, int y, TETile[][] world) {
        return x > world.length - 1 || y > world[0].length - 1
                || x < 0 || y < 0;
    }
}
