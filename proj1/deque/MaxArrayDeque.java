package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> c;

    public MaxArrayDeque(Comparator<T> c) {
        super();
        this.c = c;
    }


    /*public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof MaxArrayDeque) {
            MaxArrayDeque other = (MaxArrayDeque) o;
            if (other.c != this.c) {
                return false;
            }
            if (other.max(other.c) != (this.max(this.c))) {
                return false;
            }
            return super.equals(o);
        }
        return false;
    }*/

    public T max(Comparator<T> comparator) {
        if (isEmpty()) {
            return null;
        }
        int maxIndex = 0;
        for (int i = 0; i < this.size(); i++) {
            if (comparator.compare(get(i), get(maxIndex)) >= 0) {
                maxIndex = i;
            }
        }
        return get(maxIndex);
    }

    public T max() {
        return this.max(this.c);
    }
}
