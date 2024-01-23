package randomizedtest;

import deque.ArrayDeque;
import deque.LinkedListDeque;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;
public class TestBug {
    @Test
    public void randomizedTest(){
        ArrayDeque<Integer> ad = new ArrayDeque<Integer>();
        LinkedListDeque<Integer> ld = new LinkedListDeque<>();
        int N = 1000000;
        for (int i = 0; i < N; i++) {
            int operationrNumber = StdRandom.uniform(0, 5);
            if (operationrNumber == 0) {
                int randVal = StdRandom.uniform(0, 100);
                ad.addFirst(randVal);
                ld.addFirst(randVal);
            } else if (operationrNumber == 1) {
                int randVal = StdRandom.uniform(0, 100);
                ad.addLast(randVal);
                ld.addLast(randVal);
            } else if (operationrNumber == 2 && !ad.isEmpty()) {
                assertEquals(ad.removeFirst(), ld.removeFirst());
            } else if (operationrNumber == 3 && !ad.isEmpty()) {
                assertEquals(ad.removeLast(), ld.removeLast());
            } else if (operationrNumber == 4) {
                assertEquals(ad.size(), ld.size());
            }
        }
    }
}
