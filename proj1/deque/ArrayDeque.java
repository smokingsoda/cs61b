package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T>{
    private T[] Ts;
    private int size;

    public ArrayDeque(){
        this.Ts =(T[]) new Object[8];
        this.size = 0;
    }

    public int size(){
        return this.size;
    }
    /*public boolean isEmpty(){
        return(this.size() == 0);
    }*/

    public int capacity(){
        return this.Ts.length;
    }

    public void addFirst(T T){
        if(this.size() + 1> this.capacity()){
            this.resize(this.size() * 2);
        }
        T[] mid = (T[]) new Object[this.capacity()];
        mid[0] = T;
        System.arraycopy(this.Ts, 0, mid, 1, this.size());
        this.Ts = mid;
        this.size += 1;
    }

    public void addLast(T T){
        if(this.size() + 1 > this.capacity()){
            this.resize(this.size() * 2);
        }
        this.Ts[this.size()] = T;
        this.size += 1;
    }

    public void printDeque(){
        if (isEmpty()) {
            System.out.println();
        }
        else {
            for (int index = 0; index < this.size(); index++) {
                System.out.print(this.Ts[index] + " ");
            }
        }
        System.out.println();
    }

    public T removeFirst(){
        if(isEmpty()){
            return null;
        }
        else {
            if (this.size() <= this.capacity() / 4) {
                this.resize(this.capacity() / 4);
            }
            T[] mid = (T[]) new Object[this.capacity()];
            System.arraycopy(this.Ts, 1, mid, 0, this.size() - 1);
            T f = this.Ts[0];
            this.Ts  = mid;
            this.size -= 1;
            return f;
        }
    }

    public T removeLast(){
        if (isEmpty()){
            return null;
        }
        else{
            if (this.size() <= this.capacity() / 4) {
                this.resize(this.capacity() / 4);
            }

            T l = this.Ts[this.size() - 1];
            this.Ts[this.size() - 1] = null;
            this.size -= 1;
            return l;
        }
    }

    public T get(int index){
        if(index >= this.size()){
            return null;
        }
        else {
            return this.Ts[index];
        }
    }

    public void resize(int newsize){
        T[] mid = (T[]) new Object[newsize];
        System.arraycopy(this.Ts, 0, mid,0, this.size());
        this.Ts = mid;
    }

    public Iterator<T> iterator(){
        return new ArrayDequeIterator<T>();
    }

    private class ArrayDequeIterator<T> implements Iterator{
        private int poistion;
        public ArrayDequeIterator(){
            this.poistion = 0;
        }

        @Override
        public boolean hasNext() {
            return this.poistion < size();
        }

        @Override
        public T next() {
            T returnT = (T) get(poistion);
            poistion += 1;
            return returnT;
        }
    }
}
