package hashmap;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class test {

    @Test
    public void test1() {
        HashMap expectMap = new HashMap<>();
        MyHashMap actualMap = new MyHashMap<>();
        int N = 10000; int P = 100;
        int randomInt;
        for (int i = 0; i < N; i++) {
            randomInt = StdRandom.uniform(-P, P);
            expectMap.put(randomInt, i);
            actualMap.put(randomInt, i);
        }
        Set expectSet = expectMap.keySet();
        Set actualSet = actualMap.keySet();
        Iterator expectIter = expectSet.iterator();
        Iterator actualIter = actualSet.iterator();
        assertTrue(expectSet.containsAll(actualSet));
        assertTrue(actualSet.containsAll(expectSet));
        for (int i = 0; i < actualMap.size(); i++) {
            assertEquals(expectIter.hasNext(), actualIter.hasNext());
            System.out.println("[expected: " + expectIter.next() + ", actual: " + actualIter.next() + "]");
            assertEquals(expectIter.hasNext(), actualIter.hasNext());
        };
        assertFalse(actualIter.hasNext());
        assertFalse(expectIter.hasNext());
    }
}
