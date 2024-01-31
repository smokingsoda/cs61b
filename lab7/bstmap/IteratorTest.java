package bstmap;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class IteratorTest {
    //@Test
    public void Test1() {
        BSTMap actualMap = new BSTMap<String, Integer>();
        TreeMap expectMap = new TreeMap<String, Integer>();
        ArrayList<String> a = new ArrayList<>();
        int P = 1000;
        int N = 10000000;
        for (int i = 0; i < N; i++) {
            int randomOperation = StdRandom.uniform(0, 4);
            if (randomOperation == 0) {
                String C = "hello";
                int randomKey = StdRandom.uniform(0, P);
                actualMap.put(C + randomKey, randomKey);
                expectMap.put(C + randomKey, randomKey);
                a.add(C + randomKey);
            } else if (randomOperation == 1) {
                assertEquals(expectMap.size(), actualMap.size());
                assertEquals(expectMap.toString(), actualMap.toString());
            } else if (randomOperation == 2 && a.size() > 0) {
                int randomIndex = StdRandom.uniform(0, a.size());
                String randomString = a.remove(randomIndex);
                assertEquals(expectMap.remove(randomString), actualMap.remove(randomString));
            } else if (randomOperation == 3 && a.size() > 0) {
                int randomIndex = StdRandom.uniform(0, a.size());
                String randomString = a.get(randomIndex);
                assertEquals(expectMap.containsKey(randomString), actualMap.containsKey(randomString));
            }
        }
    }

    //@Test
    public void Test2() {
        BSTMap actualMap = new BSTMap<String, Integer>();
        TreeMap expectMap = new TreeMap<String, Integer>();
        ArrayList<String> a = new ArrayList<>();
        int N = 1000;
        for (int i = 0; i < N; i++) {
            int randomOperation = StdRandom.uniform(0, 100);
            if (i < N / 2) {
                String C = "hello";
                int randomKey = StdRandom.uniform(1, 10000);
                actualMap.put(C + randomKey, randomKey);
                expectMap.put(C + randomKey, randomKey);
                a.add(C + randomKey);
            } else if (i >= N / 2 && a.size() > 0) {
                int randomIndex = StdRandom.uniform(0, a.size());
                String randomString = a.remove(randomIndex);
                assertEquals(expectMap.containsKey(randomString), actualMap.containsKey(randomString));
                assertEquals(expectMap.remove(randomString), actualMap.remove(randomString));
                assertEquals(expectMap.size(), actualMap.size());
                assertEquals(expectMap.get(randomString), actualMap.get(randomString));
                assertEquals(expectMap.containsKey(randomString), actualMap.containsKey(randomString));
                assertEquals(expectMap.toString(), actualMap.toString());
            }
        }
    }

    //@Test
    public void Test3() {
        BSTMap actualMap = new BSTMap<Integer, Integer>();
        int N = (int) Math.pow(2, 15);
        int P = 32768;
        Stopwatch sw1 = new Stopwatch();
        for (int i = 0; i < N; i++) {
            int C = StdRandom.uniform(1, P);
            actualMap.put(C, C);
        }
        double timeInSecond1 = sw1.elapsedTime();
        System.out.println("N = " + N + ", Time = " + timeInSecond1);
        actualMap = new BSTMap<Integer, Integer>();
        N = (int) Math.pow(2, 16);
        Stopwatch sw2 = new Stopwatch();
        for (int i = 0; i < N; i++) {
            int C = StdRandom.uniform(-P, P);
            actualMap.put(C, C);
        }
        double timeInSecond2 = sw1.elapsedTime();
        System.out.println("N = " + N + ", Time = " + timeInSecond2);
        N = (int) Math.pow(2, 17);
        actualMap = new BSTMap<Integer, Integer>();
        Stopwatch sw3 = new Stopwatch();
        for (int i = 0; i < N; i++) {
            int C = StdRandom.uniform(-P, P);
            actualMap.put(C, C);
        }
        double timeInSecond3 = sw1.elapsedTime();
        System.out.println("N = " + N + ", Time = " + timeInSecond3);
        N = (int) Math.pow(2, 18);
        actualMap = new BSTMap<Integer, Integer>();
        Stopwatch sw4 = new Stopwatch();
        for (int i = 0; i < N; i++) {
            int C = StdRandom.uniform(-P, P);
            actualMap.put(C, C);
        }
        double timeInSecond4 = sw1.elapsedTime();
        System.out.println("N = " + N + ", Time = " + timeInSecond4);
        N = (int) Math.pow(2, 19);
        actualMap = new BSTMap<Integer, Integer>();
        Stopwatch sw5 = new Stopwatch();
        for (int i = 0; i < N; i++) {
            int C = StdRandom.uniform(-P, P);
            actualMap.put(C, C);
        }
        double timeInSecond5 = sw1.elapsedTime();
        System.out.println("N = " + N + ", Time = " + timeInSecond5);
        N = (int) Math.pow(2, 20);
        actualMap = new BSTMap<Integer, Integer>();
        Stopwatch sw6 = new Stopwatch();
        for (int i = 0; i < N; i++) {
            int C = StdRandom.uniform(-P, P);
            actualMap.put(C, C);
        }
        double timeInSecond6 = sw1.elapsedTime();
        System.out.println("N = " + N + ", Time = " + timeInSecond6);
        N = (int) Math.pow(2, 21);
        actualMap = new BSTMap<Integer, Integer>();
        Stopwatch sw7 = new Stopwatch();
        for (int i = 0; i < N; i++) {
            int C = StdRandom.uniform(-P, P);
            actualMap.put(C, C);
        }
        double timeInSecond7 = sw1.elapsedTime();
        System.out.println("N = " + N + ", Time = " + timeInSecond7);
    }

    //@Test
    public void test4() {
        BSTMap actualMap = new BSTMap<Integer, Integer>();
        TreeMap expectMap = new TreeMap<Integer, Integer>();
        ArrayList<Integer> a = new ArrayList<>();
        actualMap.put(4,4);expectMap.put(4,4);
        actualMap.put(2,2);expectMap.put(2,2);
        actualMap.put(6,6);expectMap.put(6,6);
        actualMap.put(1,1);expectMap.put(1,1);
        actualMap.put(3,3);expectMap.put(3,3);
        actualMap.put(5,5);expectMap.put(5,5);
        actualMap.put(7,7);expectMap.put(7,7);
        a.add(4);a.add(2);a.add(5);a.add(1);a.add(3);a.add(6);a.add(7);
        actualMap.printInOrder();
        System.out.println(expectMap.toString());
        for (int i = 0; i < 7; i++) {
            int randomIndex = StdRandom.uniform(0, a.size());
            Integer randomInteger = a.get(randomIndex);
            assertEquals(expectMap.containsKey(randomInteger), actualMap.containsKey(randomInteger));
            assertEquals(expectMap.get(randomInteger), actualMap.get(randomInteger));
            assertEquals(expectMap.size(), actualMap.size());
        }
        assertEquals(expectMap.remove(4), actualMap.remove(4));
        actualMap.printInOrder();
        System.out.println(expectMap.toString());

    }

    @Test
    public void test5() {
        BSTMap actualMap = new BSTMap<Integer, Integer>();
        TreeMap expectMap = new TreeMap<Integer, Integer>();
        ArrayList<Integer> a = new ArrayList<>();
        int N = 10000;
        int P = 10000;
        for (int i = 0; i < N; i++) {
            int randomInt = StdRandom.uniform(-P, P);
            actualMap.put(randomInt,randomInt);
            expectMap.put(randomInt,randomInt);
        }
        Iterator actualIterator = actualMap.iterator();
        Iterator expectIterator = expectMap.keySet().iterator();
        for (int i = 0; i < actualMap.size(); i++) {
            Object p = expectIterator.next(); Object q = actualIterator.next();
            System.out.println(p + ", " + q);
            assertEquals(p, q);
            assertEquals(expectIterator.hasNext(), actualIterator.hasNext());
        }

        Iterator NEWactualIterator = actualMap.iterator();
        Iterator NEWexpectIterator = expectMap.keySet().iterator();
        for (int i = 0; i < actualMap.size(); i++) {
            Object p = NEWexpectIterator.next(); Object q = NEWactualIterator.next();
            System.out.println(p + ", " + q);
            assertEquals(p, q);
            assertEquals(NEWexpectIterator.hasNext(), NEWactualIterator.hasNext());
        }
        assertTrue(expectMap.keySet().containsAll(actualMap.keySet()));
    }

}
