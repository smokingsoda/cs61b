package byow.Core;

import java.io.Serializable;

public class Position implements Serializable, Comparable<Position> {
    public int x;
    public int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Position) {
            return ((Position) object).x == this.x && ((Position) object).y == this.y;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (53 + x) * 53 + y;
    }

    @Override
    public int compareTo(Position other) {
        return this.x + this.y - other.x - other.y;
    }
}
