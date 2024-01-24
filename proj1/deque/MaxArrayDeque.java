package deque;

import java.util.Comparator;

public class MaxArrayDeque<Item> extends ArrayDeque<Item> {
    private Comparator<Item> c;
    public MaxArrayDeque(){
        super();
    }

    public MaxArrayDeque(Comparator<Item> c){
        super();
        this.c = c;
    }

    public Item max(Comparator c){
        if (isEmpty()) {
            return null;
        }
        Item returnItem = this.get(0);
        Item next;
        for (int i = 1; i < this.size(); i++) {
            next = this.get(i);
            if(c.compare(returnItem, next) <= 0){
                returnItem = next;
            }
        }
        return returnItem;
    }
}
