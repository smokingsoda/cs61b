package deque;

public class DLLink <T>{
    public T content;
    public DLLink<T> previous;
    public DLLink<T> next;

    public DLLink(DLLink<T> pre, DLLink<T> nxt, T cont){
        this.content = cont;
        this.previous = pre;
        this.next = nxt;
    }
}
