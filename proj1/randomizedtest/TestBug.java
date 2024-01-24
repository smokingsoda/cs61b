package randomizedtest;

import deque.ArrayDeque;
import deque.LinkedListDeque;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;
public class TestBug {
    @Test
    public void randomizedTest() {
        ArrayDeque<Integer> ad = new ArrayDeque<Integer>();
        LinkedListDeque<Integer> ld = new LinkedListDeque<>();
        int N = 10000000;
        for (int i = 0; i < N; i++) {
            int operationrNumber = StdRandom.uniform(0, 6);
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
            } else if (operationrNumber == 5 && ld.size() >= 1) {
                int randomIndex = StdRandom.uniform(0, ld.size());
                int adGet = ad.get(randomIndex);
                int ldGet = ld.get(randomIndex);
                assertEquals(adGet, ldGet);
            }
        }
    }

    @Test
    public void addFirstTest() {
        ArrayDeque<Integer> ad = new ArrayDeque<Integer>();
        LinkedListDeque<Integer> ld = new LinkedListDeque<>();
        int N = 10;
        for (int i = 0; i < N; i++) {
            int randVal = StdRandom.uniform(0, 100);
            ad.addFirst(randVal);
            ld.addFirst(randVal);
        }
        for (int i = 0; i < N; i++) {
            assertEquals(ld.removeFirst(), ad.removeFirst());
        }
    }


    @Test
    public void addLastTest() {
        ArrayDeque<Integer> ad = new ArrayDeque<Integer>();
        LinkedListDeque<Integer> ld = new LinkedListDeque<>();
        int N = 10000;
        for (int i = 0; i < N; i++) {
            int randVal = StdRandom.uniform(0, 100);
            ad.addLast(randVal);
            ld.addLast(randVal);
        }
        for (int i = 0; i < N; i++) {
            assertEquals(ld.removeLast(), ad.removeLast());
        }
    }

}
