package byow.Core;

import java.io.Serializable;

public class Pair implements Serializable, Comparable<Pair> {
    public Position key;
    public int value;

    public Pair(Position key, int value) {
        this.key = key;
        this.value = value;
    }
    @Override
    public int compareTo(Pair other) {
        return this.value - other.value;

    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Pair) {
            return this.key.equals(((Pair) other).key);
        }
        return false;
    }
}
