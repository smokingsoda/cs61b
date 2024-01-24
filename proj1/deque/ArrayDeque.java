package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] items;
    private int size;

    public ArrayDeque() {
        this.items =(T[]) new Object[8];
        this.size = 0;
    }


    public int size() {
        return this.size;
    }
    /*public boolean isEmpty(){
        return(this.size() == 0);
    }*/

    private int capacity(){
        return this.items.length;
    }


    public void addFirst(T item) {
        if (this.size() + 1> this.capacity()) {
            this.resize(this.size() * 2);
        }
        T[] mid = (T []) new Object[this.capacity()];
        mid[0] = item;
        System.arraycopy(this.items, 0, mid, 1, this.size());
        this.items = mid;
        this.size += 1;
    }


    public void addLast(T item) {
        if (this.size() + 1 > this.capacity()) {
            this.resize(this.size() * 2);
        }
        this.items[this.size()] = item;
        this.size += 1;
    }


    public void printDeque() {
        if (isEmpty()) {
            System.out.println();
        }
        else {
            for (int index = 0; index < this.size(); index++) {
                System.out.print(this.items[index] + " ");
            }
        }
        System.out.println();
    }


    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        else {
            if (this.size() <= this.capacity() / 4) {
                this.resize(this.capacity() / 4);
            }
            T[] mid = (T[]) new Object[this.capacity()];
            System.arraycopy(this.items, 1, mid, 0, this.size() - 1);
            T f = this.items[0];
            this.items  = mid;
            this.size -= 1;
            return f;
        }
    }


    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        else {
            if (this.size() <= this.capacity() / 4) {
                this.resize(this.capacity() / 4);
            }

            T l = this.items[this.size() - 1];
            this.items[this.size() - 1] = null;
            this.size -= 1;
            return l;
        }
    }


    public T get(int index) {
        if(index >= this.size()) {
            return null;
        }
        else {
            return this.items[index];
        }
    }


    private void resize(int newsize) {
        T[] mid = (T[]) new Object[newsize];
        System.arraycopy(this.items, 0, mid,0, this.size());
        this.items = mid;
    }


    public boolean euqals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ArrayDeque) {
            ArrayDeque other = (ArrayDeque) o;
            if (other.size() != this.size()) {
                return false;
            }
            for (int i = 0; i < this.size; i++) {
                if (!(this.get(i).equals(other.get(i)))) {
                    return false;
                };
            }
            return true;
        }
        return false;
    }


    public Iterator<T> iterator() {
        return new ArrayDequeIterator<T>();
    }

    private class ArrayDequeIterator<T> implements Iterator {
        private int poistion;
        public ArrayDequeIterator() {
            this.poistion = 0;
        }


        public boolean hasNext() {
            return this.poistion < size();
        }


        public T next() {
            T returnItem = (T)get(poistion);
            poistion += 1;
            return returnItem;
        }
    }
}
