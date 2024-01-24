package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private int size;
    private DLLink<T> first;
    private DLLink<T> last;
    private DLLink FRONT_SENTINEL;
    private DLLink LAST_SENTINEL;

    public LinkedListDeque() {
        this.FRONT_SENTINEL = new DLLink<Integer>(null, null, 89);
        this.LAST_SENTINEL = new DLLink<Integer>(null, null, 64);
        this.FRONT_SENTINEL.next = this.LAST_SENTINEL;
        this.LAST_SENTINEL.previous = this.FRONT_SENTINEL;
        this.first = FRONT_SENTINEL;
        this.last = LAST_SENTINEL;

        this.size = 0;
    }

    /*public boolean isEmpty(){
        return (this.first == this.FRONT_SENTINEL && this.last == this.LAST_SENTINEL && this.size() == 0);
    }*/
    
    public void addFirst(T T) {
        DLLink<T> add = new DLLink<T>(null, null, T); //add is the added DLList
        add.previous = this.FRONT_SENTINEL;//add.previous must be the sentinel
        this.FRONT_SENTINEL.next = add;//sentinel.next must be the add
        if (isEmpty()) {
            add.next = this.LAST_SENTINEL;
            this.LAST_SENTINEL.previous = add;
            this.last = add;
        } else {
            DLLink<T> f = this.first;
            add.next = f;
            f.previous = add;
        }
        this.first = add;
        this.size += 1;
    }
    
    public void addLast(T T) {
        //DLLink<T> l = this.last;
        DLLink<T> add = new DLLink<T>(null, null, T);
        add.next = this.LAST_SENTINEL;
        this.LAST_SENTINEL.previous = add;
        if (isEmpty()) {
            add.previous = this.FRONT_SENTINEL;
            this.FRONT_SENTINEL.next = add;
            this.first = add;
        } else {
            DLLink<T> l = this.last;
            add.previous = l;
            l.next = add;
        }
        this.last = add;
        this.size += 1;
    }

    
    public int size() {
        return this.size;
    }

    
    public void printDeque() {
        DLLink p = this.first;
        for (int i = 0; i < this.size(); i++) {
            System.out.print(p.content + " ");
        }
        System.out.println();
    }

    
    public T removeFirst() {
        if (this.isEmpty()) {
            return null;
        }
        DLLink<T> f = this.first;
        DLLink<T> second = f.next;
        if (second == this.LAST_SENTINEL) {
            this.first = this.FRONT_SENTINEL;
            this.last = this.LAST_SENTINEL;
        } else {
            this.first = second;
            second.previous = this.FRONT_SENTINEL;
        }
        f.previous = null;
        f.next = null;
        this.size -= 1;
        return f.content;
    }

    
    public T removeLast() {
        if (this.isEmpty()) {
            return null;
        }
        DLLink<T> l = this.last;
        DLLink<T> second = l.previous;
        if (second == this.FRONT_SENTINEL) {
            this.first = this.FRONT_SENTINEL;
            this.last = this.LAST_SENTINEL;
        } else {
            this.last = second;
            second.next = this.LAST_SENTINEL;
        }
        l.previous = null;
        l.next = null;
        this.size -= 1;
        return l.content;
    }

    
    public T get(int index) {
        if (index >= this.size()) {
            return null;
        }
        T get_result = null;
        DLLink<T> pointer = this.first;
        for (int i = 0; i <= index; i++) {
            get_result = pointer.content;
            pointer = pointer.next;
        }
        return get_result;
    }

    public T getRecursive(int index) {
        if (index >= size()) {
            return null;
        } else {
            return getHelpRecursive(index, this.first);
        }
    }

    public T getHelpRecursive(int index, DLLink<T> dl) {
        if (index == 0) {
            return dl.content;
        } else {
            return getHelpRecursive(index - 1, dl.next);
        }
    }

    
    public boolean euqals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof LinkedListDeque) {
            LinkedListDeque other = (LinkedListDeque) o;
            if (other.size() != this.size()) {
                return false;
            }
            for (int i = 0; i < this.size; i++) {
                if (!(this.get(i).equals(other.get(i)))){
                    return false;
                };
            }
            return true;
        }
        return false;
    }

    
    public Iterator iterator(){
        return new LinkedListDequeIterator();
    }

    private class LinkedListDequeIterator<T> implements Iterator{

        private int position;
        public LinkedListDequeIterator(){
            this.position = 0;
        }

        
        public boolean hasNext(){
            return this.position < size();
        }

        
        public T next() {
            T returnT = (T)get(position);
            position += 1;
            return returnT;
        }
    }

}


