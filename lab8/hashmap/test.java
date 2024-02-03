package hashmap;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class test {

    //@Test
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

    @Test
    public void test2() {
        HashMap expectMap = new HashMap<>();
        MyHashMap actualMap = new MyHashMap<>();
        ArrayList a = new ArrayList<>();
        int N = 10000; int P = 100000;
        int operation;
        for (int i = 0; i < N; i++) {
            operation = StdRandom.uniform(0,5);
            if (operation == 0) {
                int randomInt = StdRandom.uniform(-P, P);
                expectMap.put(randomInt, i);
                actualMap.put(randomInt, i);
                a.add(randomInt);
            } else if (operation == 1) {
                assertEquals(expectMap.size(), actualMap.size());
                assertTrue(expectMap.keySet().containsAll(actualMap.keySet()));
            } else if (operation == 2 && a.size() > 0) {
                int randomIndex = StdRandom.uniform(0, a.size());
                int randomNumber = (int) a.get(randomIndex);
                assertEquals(expectMap.containsKey(randomNumber), actualMap.containsKey(randomNumber));
                assertEquals(expectMap.get(randomNumber), actualMap.get(randomNumber));
            } else if (operation == 3 && a.size() > 0) {
                int randomIndex = StdRandom.uniform(0, a.size());
                a.remove(randomIndex);
                assertEquals(expectMap.remove(randomIndex), actualMap.remove(randomIndex));
                assertTrue(expectMap.keySet().containsAll(actualMap.keySet()));
            } else if (operation == 4 && a.size() > 0) {
                int randomIndex = StdRandom.uniform(0, a.size());
                a.remove(randomIndex);
                Object value = expectMap.remove(randomIndex);
                assertEquals(value, actualMap.remove(randomIndex, value));
                assertTrue(expectMap.keySet().containsAll(actualMap.keySet()));
            }
        }
    }
}
