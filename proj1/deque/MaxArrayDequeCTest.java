package deque;

import edu.princeton.cs.algs4.In;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Comparator;

public class MaxArrayDequeCTest {
    public class IntegerComparator implements Comparator<Integer>{
        @Override
        public int compare(Integer a, Integer b){
            return a - b;
        }
    }

    public class StringAlphabetComparator implements Comparator<String> {
        @Override
        public int compare(String a, String b) {
            return a.compareTo(b);
        }
    }
        @Test
        public void Test1(){
            IntegerComparator c = new IntegerComparator();
            MaxArrayDeque<Integer> b = new MaxArrayDeque<>(c);
            b.addFirst(4);
            b.addFirst(77);
            b.addFirst(795);
            b.addFirst(32109321);
            b.addFirst(99999);
            assertEquals(32109321, (int)b.max(c));
        }

        @Test
        public void Test2(){
        StringAlphabetComparator c = new StringAlphabetComparator();
        MaxArrayDeque<String> b = new MaxArrayDeque<>();
        b.addFirst("aaaa");
        b.addFirst("ppbbb");
        b.addFirst("dsade");
        b.addFirst("ewqeow");
        b.addFirst("zdsa");
        assertEquals("zdsa", (String)b.max(c));
        }
    }
