package deque;

public class DLLink <Item>{
    public Item content;
    public DLLink<Item> previous;
    public DLLink<Item> next;

    public DLLink(DLLink<Item> pre, DLLink<Item> nxt, Item cont){
        this.content = cont;
        this.previous = pre;
        this.next = nxt;
    }
}
