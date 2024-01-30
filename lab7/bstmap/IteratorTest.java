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
        HashMap expectMap = new HashMap<String, Integer>();
        ArrayList<String> a = new ArrayList<>();
        int N = 100000;
        for (int i = 0; i < N; i++) {
            int randomOperation = StdRandom.uniform(0, 100);
            if (randomOperation < 30) {
                String C = "hello";
                int randomKey = StdRandom.uniform(1, 100);
                actualMap.put(C + randomKey, randomKey);
                expectMap.put(C + randomKey, randomKey);
                a.add(C + randomKey);
            } else if (randomOperation >= 40) {
                assertEquals(expectMap.size(), actualMap.size());
            } else if (randomOperation < 90 && a.size() > 0) {
                int randomIndex = StdRandom.uniform(0, a.size());
                String randomString = a.remove(randomIndex);
                assertEquals(expectMap.remove(randomString), actualMap.remove(randomString));
            } else if (randomOperation >= 95 && a.size() > 0) {
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
        int N = 1000000;
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
            }
        }
    }
    @Test
    public void Test3() {
        BSTMap actualMap = new BSTMap<String, Integer>();
        int N = 10;
        Stopwatch sw1 = new Stopwatch();
        for (int i = 0; i < N; i++) {
            String C = "hello";
            int randomKey = StdRandom.uniform(1, 10000);
            actualMap.put(C + randomKey, randomKey);
        }
        double timeInSecond1 = sw1.elapsedTime();
        System.out.println("N = " + N + ", Time = " + timeInSecond1);
        actualMap = new BSTMap<String, Integer>();
        N = 100;
        Stopwatch sw2 = new Stopwatch();
        for (int i = 0; i < N; i++) {
            String C = "hello";
            int randomKey = StdRandom.uniform(1, 10000);
            actualMap.put(C + randomKey, randomKey);
        }
        double timeInSecond2 = sw1.elapsedTime();
        System.out.println("N = " + N + ", Time = " + timeInSecond2);
        N = 500;
        actualMap = new BSTMap<String, Integer>();
        Stopwatch sw3 = new Stopwatch();
        for (int i = 0; i < N; i++) {
            String C = "hello";
            int randomKey = StdRandom.uniform(1, 10000);
            actualMap.put(C + randomKey, randomKey);
        }
        double timeInSecond3 = sw1.elapsedTime();
        System.out.println("N = " + N + ", Time = " + timeInSecond3);
        N = 1000;
        actualMap = new BSTMap<String, Integer>();
        Stopwatch sw4 = new Stopwatch();
        for (int i = 0; i < N; i++) {
            String C = "hello";
            int randomKey = StdRandom.uniform(1, 10000);
            actualMap.put(C + randomKey, randomKey);
        }
        double timeInSecond4 = sw1.elapsedTime();
        System.out.println("N = " + N + ", Time = " + timeInSecond4);
        N = 5000;
        actualMap = new BSTMap<String, Integer>();
        Stopwatch sw5 = new Stopwatch();
        for (int i = 0; i < N; i++) {
            String C = "hello";
            int randomKey = StdRandom.uniform(1, 10000);
            actualMap.put(C + randomKey, randomKey);
        }
        double timeInSecond5 = sw1.elapsedTime();
        System.out.println("N = " + N + ", Time = " + timeInSecond5);
        N = 10000;
        actualMap = new BSTMap<String, Integer>();
        Stopwatch sw6 = new Stopwatch();
        for (int i = 0; i < N; i++) {
            String C = "hello";
            int randomKey = StdRandom.uniform(1, 10000);
            actualMap.put(C + randomKey, randomKey);
        }
        double timeInSecond6 = sw1.elapsedTime();
        System.out.println("N = " + N + ", Time = " + timeInSecond6);
        N = 50000;
        actualMap = new BSTMap<String, Integer>();
        Stopwatch sw7 = new Stopwatch();
        for (int i = 0; i < N; i++) {
            String C = "hello";
            int randomKey = StdRandom.uniform(1, 10000);
            actualMap.put(C + randomKey, randomKey);
        }
        double timeInSecond7 = sw1.elapsedTime();
        System.out.println("N = " + N + ", Time = " + timeInSecond7);
    }

}
