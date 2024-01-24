package deque;

import java.util.Iterator;

public class LinkedListDeque<Item> implements Deque<Item>, Iterable<Item> {
    private int size;
    private DLLink<Item> first;
    private DLLink<Item> last;
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

    public void addFirst(Item item) {
        DLLink<Item> add = new DLLink<Item>(null, null, item); //add is the added DLList
        add.previous = this.FRONT_SENTINEL;//add.previous must be the sentinel
        this.FRONT_SENTINEL.next = add;//sentinel.next must be the add
        if (isEmpty()) {
            add.next = this.LAST_SENTINEL;
            this.LAST_SENTINEL.previous = add;
            this.last = add;
        } else {
            DLLink<Item> f = this.first;
            add.next = f;
            f.previous = add;
        }
        this.first = add;
        this.size += 1;
    }

    public void addLast(Item item) {
        //DLLink<Item> l = this.last;
        DLLink<Item> add = new DLLink<Item>(null, null, item);
        add.next = this.LAST_SENTINEL;
        this.LAST_SENTINEL.previous = add;
        if (isEmpty()) {
            add.previous = this.FRONT_SENTINEL;
            this.FRONT_SENTINEL.next = add;
            this.first = add;
        } else {
            DLLink<Item> l = this.last;
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

    public Item removeFirst() {
        if (this.isEmpty()) {
            return null;
        }
        DLLink<Item> f = this.first;
        DLLink<Item> second = f.next;
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

    public Item removeLast() {
        if (this.isEmpty()) {
            return null;
        }
        DLLink<Item> l = this.last;
        DLLink<Item> second = l.previous;
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

    public Item get(int index) {
        if (index >= this.size()) {
            return null;
        }
        Item get_result = null;
        DLLink<Item> pointer = this.first;
        for (int i = 0; i <= index; i++) {
            get_result = pointer.content;
            pointer = pointer.next;
        }
        return get_result;
    }

    public Item getRecrusive(int index) {
        if (index >= size()) {
            return null;
        } else {
            return getHelpRecrusive(index, this.first);
        }
    }

    public Item getHelpRecrusive(int index, DLLink<Item> dl) {
        if (index == 0) {
            return dl.content;
        } else {
            return getHelpRecrusive(index - 1, dl.next);
        }
    }

    public Iterator iterator(){
        return new LinkedListDequeIterator();
    }

    private class LinkedListDequeIterator<Item> implements Iterator{

        private int position;
        public LinkedListDequeIterator(){
            this.position = 0;
        }

        public boolean hasNext(){
            return this.position < size();
        }

        @Override
        public Item next() {
            Item returnItem = (Item)get(position);
            position += 1;
            return returnItem;
        }
    }

}


