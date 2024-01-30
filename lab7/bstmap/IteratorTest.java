package bstmap;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Iterator;

public class IteratorTest {
    @Test
    public void Test1() {
        BSTMap actualMap = new BSTMap<Character, Integer>();
        HashMap expectMap = new HashMap<Character, Integer>();
        int N = 4; char C = 'a';
        for (int i = 0; i < N; i++) {
            int randomKey = StdRandom.uniform(1, 10);
            actualMap.put(C + randomKey, randomKey);
            expectMap.put(C + randomKey, randomKey);
        }
        Iterator actual = actualMap.iterator();
        Iterator expect = expectMap.keySet().iterator();
        for (int i = 0; i < N; i++) {
            assertEquals(expect.next(), actual.next());
        }

    }
}
