package deque;

import java.util.Iterator;

public interface Deque<T>{
    void addFirst(T T);

    void addLast(T T);

    default public boolean isEmpty() {
        return (this.size() == 0);
    }

    int size();

    void printDeque();

    T removeFirst();

    T removeLast();

    T get(int index);

    boolean euqals(Object o);
}