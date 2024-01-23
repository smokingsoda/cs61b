package tester;
import static org.junit.Assert.*;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;
public class TestArrayDequeEC {
    @Test
    public void randomizedTest() {
        StudentArrayDeque<Integer> student = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> teacher = new ArrayDequeSolution<>();
        int N = 50000;
        String errorMessage = "";
        for (int i = 0; i < N; i++) {
            int op = StdRandom.uniform(0, 5);
            if (op == 0) {
                int value = StdRandom.uniform(0, 100);
                student.addFirst(value);
                teacher.addFirst(value);
                errorMessage = errorMessage + "\naddFirst" + "(" + value + ")";
            } else if (op == 1) {
                int value = StdRandom.uniform(0, 100);
                student.addLast(value);
                teacher.addLast(value);
                errorMessage = errorMessage + "\naddLast" + "(" + value + ")";
            } else if (op == 2 && !teacher.isEmpty()) {
                errorMessage = errorMessage + "\nremoveFirst" + "(" + ")";
                assertEquals(errorMessage, teacher.removeFirst(), student.removeFirst());
            } else if (op == 3 && !teacher.isEmpty()) {
                errorMessage = errorMessage + "\nremoveLast" + "(" + ")";
                assertEquals(errorMessage, teacher.removeLast(), student.removeLast());
            } else if (op == 4) {
                errorMessage = errorMessage + "\nsize" + "(" + ")";
                assertEquals(errorMessage, teacher.size(), student.size());
            }
        }
    }
}
