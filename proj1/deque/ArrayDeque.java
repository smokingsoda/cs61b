package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {

    private T[] Items;
    private int size;
    private int nextFirst;
    private int nextLast;
    private int frontLength;
    private int backLength;

    public ArrayDeque() {
        this.Items = (T []) new Object [8];
        this.size = 0;
        this.frontLength = 0;
        this.backLength = 0;
        this.nextFirst = 3;
        this.nextLast = 4;

    }

    public int size() {
        return this.size;
    }

    private int capacity() {
        return this.Items.length;
    }


    public void addFirst(T item) {
        if (this.nextFirst == -1) {
            this.resize(this.capacity() * 2);
        }
        this.Items[this.nextFirst] = item;
        frontLength += 1;
        this.nextFirst -= 1;
        this.size += 1;
    }


    public void addLast(T item) {
        if (this.nextLast == this.capacity()) {
            this.resize(this.capacity() * 2);
        }
        this.Items[this.nextLast] = item;
        this.nextLast += 1;
        backLength += 1;
        this.size += 1;
    }


    public void printDeque() {
        if (isEmpty()) {
            System.out.println();
        }
        else {
            for (int index = 0; index < this.size(); index++) {
                System.out.print(this.get(index) + " ");
            }
        }
        System.out.println();
    }


    public T removeFirst() {
        if (isEmpty()) {
            return null;
        } else {
            if (this.size() < this.capacity() / 4 && size() > 8) {
                this.shrink(this.capacity() / 2);
            }
                T returnItem = this.Items[nextFirst + 1];
                this.Items[nextFirst + 1] = null;
                this.nextFirst += 1;
                this.size -= 1;
                if (frontLength == 0) {
                    backLength -= 1;
                }
                else {
                    frontLength -= 1;
                }
                return returnItem;
        }
    }

    public T removeLast() {
        if (isEmpty()) {
            return null;
        } else {
            if (this.size() < this.capacity() / 4 && this.size() > 8) {
                this.shrink(this.capacity() / 2);
            }
            T returnItem = this.Items[nextLast - 1];
            this.Items[nextLast - 1] = null;
            nextLast -= 1;
            this.size -= 1;
            if (backLength == 0) {
                frontLength -= 1;
            }
            else {
                backLength -= 1;
            }
            return returnItem;
        }
    }



    private void shrink(int newsize) {
        T[] returnItem = (T[]) new Object[newsize];
        System.arraycopy(this.Items, nextFirst + 1, returnItem, newsize / 2 - frontLength, size());
        this.nextFirst = newsize / 2 - frontLength - 1;
        this.nextLast = newsize / 2 + backLength;
        this.Items = returnItem;
    }


    public T get(int index) {
        if(index < 0|| index >= this.size()) {
            return null;
        }
        else if (index < frontLength) {
            return this.Items[nextFirst + 1 + index];
        }
        else {
            return this.Items[frontLength + nextFirst + 1 + index - frontLength];
        }
    }


    private void resize(int newsize) {
        T[] returnItems = (T[]) new Object[newsize];
        System.arraycopy(this.Items, nextFirst + 1, returnItems, newsize / 2 - frontLength, size());
        this.nextFirst = newsize / 2 - frontLength - 1;
        this.nextLast = newsize / 2 + backLength;
        this.Items = returnItems;
    }


    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof Deque) {
            Deque other = (Deque) o;
            if (other.size() != this.size()) {
                return false;
            }
            for (int i = 0; i < this.size; i++) {
                if (!this.get(i).equals((other.get(i)))) {
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
