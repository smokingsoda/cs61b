package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove(){
        AListNoResizing<Integer> right = new AListNoResizing<>();
        BuggyAList<Integer> wrong = new BuggyAList<>();
        right.addLast(4); right.addLast(5); right.addLast(6);
        wrong.addLast(4); wrong.addLast(5); wrong.addLast(6);

        assertEquals(right.size(), wrong.size());
        assertEquals(right.removeLast(), wrong.removeLast());
        assertEquals(right.removeLast(), wrong.removeLast());
        assertEquals(right.removeLast(), wrong.removeLast());
    }
    @Test
    public void randomizedTest(){
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> P = new BuggyAList<>();
        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                P.addLast(randVal);
                //System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int sizeL = L.size();
                int sizeP = P.size();
                assertEquals(sizeL, sizeP);
                //System.out.println("size: " + sizeL);
            } else if (operationNumber == 2 && L.size() > 0) {
                int lastL = L.getLast();
                int lastP = P.getLast();
                assertEquals(lastL, lastP);
                //System.out.println("get the last: " + lastL);
            } else if (operationNumber == 3 && L.size() > 0) {
                int lastL = L.removeLast();
                int lastP = P.removeLast();
                assertEquals(lastL, lastP);
                //System.out.println("remove the last: " + lastL);
            }
            else{
                int size = L.size();
                //System.out.println("size: " + size);
            }
        }
    }
}
