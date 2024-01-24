package deque;

import edu.princeton.cs.algs4.In;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Comparator;
import java.util.Iterator;

public class MaxArrayDequeCTest {
    public class IntegerComparator implements Comparator<Integer> {

        public int compare(Integer a, Integer b) {
            return a - b;
        }
    }

    public class FakeIntegerComparator implements Comparator<Integer> {

        public int compare(Integer a, Integer b) {
            return b - a;
        }
    }

    public class StringAlphabetComparator implements Comparator<String> {

        public int compare(String a, String b) {
            return a.compareTo(b);
        }
    }

    @Test
    public void Test1() {
        IntegerComparator c = new IntegerComparator();
        MaxArrayDeque<Integer> b = new MaxArrayDeque<>(c);
        b.addFirst(4);
        b.addFirst(77);
        b.addFirst(795);
        b.addFirst(32109321);
        b.addFirst(99999);
        assertEquals(32109321, (int) b.max(c));
    }

    @Test
    public void Test2() {
        StringAlphabetComparator c = new StringAlphabetComparator();
        MaxArrayDeque<String> b = new MaxArrayDeque<>(c);
        b.addFirst("aaaa");
        b.addFirst("ppbbb");
        b.addFirst("dsade");
        b.addFirst("ewqeow");
        b.addFirst("zdsa");
        assertEquals("zdsa", (String) b.max(c));
    }

    @Test
    public void test3() {
        LinkedListDeque<Integer> a = new LinkedListDeque<>();
        int N = 10000;
        for (int i = 0; i < N; i++) {
            a.addLast(i);
        }
        int actual = 0;
        for (int i : a) {
            assertEquals(actual, i);
            actual += 1;
        }
    }

    @Test
    public void test4() {
        ArrayDeque<Integer> a = new ArrayDeque<>();
        int N = 10000;
        for (int i = 0; i < N; i++) {
            a.addLast(i);
        }
        int actual = 0;
        for (int i : a) {
            assertEquals(actual, i);
            actual += 1;
        }
    }

    @Test
    public void test5() {
        FakeIntegerComparator c1 = new FakeIntegerComparator();
        MaxArrayDeque<Integer> b1 = new MaxArrayDeque<>(c1);
        b1.addFirst(4);
        b1.addFirst(77);
        b1.addFirst(795);
        b1.addFirst(32109321);
        b1.addFirst(99999);
        IntegerComparator c2 = new IntegerComparator();
        MaxArrayDeque<Integer> b2 = new MaxArrayDeque<>(c2);
        b2.addFirst(4);
        b2.addFirst(77);
        b2.addFirst(795);
        b2.addFirst(32109321);
        b2.addFirst(99999);
        assertFalse(b2.equals(b1));
    }

    @Test
    public void test6() {
        FakeIntegerComparator c1 = new FakeIntegerComparator();
        MaxArrayDeque<Integer> b1 = new MaxArrayDeque<>(c1);
        b1.addFirst(4);
        b1.addFirst(77);
        b1.addFirst(791);
        b1.addFirst(32109321);
        b1.addFirst(99999);
        FakeIntegerComparator c2 = new FakeIntegerComparator();
        MaxArrayDeque<Integer> b2 = new MaxArrayDeque<>(c2);
        b2.addFirst(4);
        b2.addFirst(77);
        b2.addFirst(791);
        b2.addFirst(32109321);
        b2.addLast(99999);
        assertFalse(b2.equals(b1));
    }
}
