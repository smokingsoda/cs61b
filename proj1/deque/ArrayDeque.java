package deque;

public class ArrayDeque<Item> {
    private Item[] items;
    private int size;

    public ArrayDeque(){
        this.items =(Item[]) new Object[8];
        this.size = 0;
    }

    public int size(){
        return this.size;
    }
    public boolean isEmpty(){
        return(this.size() == 0);
    }

    public int capacity(){
        return this.items.length;
    }

    public void addFirst(Item item){
        if(this.size() + 1> this.capacity()){
            this.resize(this.size() * 2);
        }
        Item[] mid = (Item[]) new Object[this.capacity()];
        mid[0] = item;
        System.arraycopy(this.items, 0, mid, 1, this.size());
        this.items = mid;
        this.size += 1;
    }

    public void addLast(Item item){
        if(this.size() + 1 > this.capacity()){
            this.resize(this.size() * 2);
        }
        this.items[this.size()] = item;
        this.size += 1;
    }

    public void printDeque(){
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

    public Item removeFirst(){
        if(isEmpty()){
            return null;
        }
        else {
            if (this.size() <= this.capacity() / 4) {
                this.resize(this.capacity() / 4);
            }
            Item[] mid = (Item[]) new Object[this.capacity()];
            System.arraycopy(this.items, 1, mid, 0, this.size() - 1);
            Item f = this.items[0];
            this.items  = mid;
            this.size -= 1;
            return f;
        }
    }

    public Item removeLast(){
        if (isEmpty()){
            return null;
        }
        else{
            if (this.size() <= this.capacity() / 4) {
                this.resize(this.capacity() / 4);
            }

            Item l = this.items[this.size() - 1];
            this.items[this.size() - 1] = null;
            this.size -= 1;
            return l;
        }
    }

    public Item get(int index){
        if(index >= this.size()){
            return null;
        }
        else {
            return this.items[index];
        }
    }

    public void resize(int newsize){
        Item[] mid = (Item[]) new Object[newsize];
        System.arraycopy(this.items, 0, mid,0, this.size());
        this.items = mid;
    }
}
