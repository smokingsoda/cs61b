package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> c;
    public MaxArrayDeque(){
        super();
    }

    public MaxArrayDeque(Comparator<T> c){
        super();
        this.c = c;
    }

    public T max(Comparator c){
        if (isEmpty()) {
            return null;
        }
        T returnT = this.get(0);
        T next;
        for (int i = 1; i < this.size(); i++) {
            next = this.get(i);
            if(c.compare(returnT, next) <= 0){
                returnT = next;
            }
        }
        return returnT;
    }
}
