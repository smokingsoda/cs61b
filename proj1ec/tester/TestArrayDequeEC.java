package tester;
import static org.junit.Assert.*;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;
public class TestArrayDequeEC {
    @Test
    public void randomizedTest(){
        StudentArrayDeque<Integer> student = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> teacher = new ArrayDequeSolution<>();
        int N = 50000;
        String[] errorMessage = new String[3];
        for (int i = 0; i < N; i++) {
            int op = StdRandom.uniform(0,5);
            if (op == 0) {
                int value = StdRandom.uniform(0, 100);
                student.addFirst(value);
                teacher.addFirst(value);
                errorMessageOperator(errorMessage, "addFirst" + "(" + value + ")");
            } else if (op == 1) {
                int value = StdRandom.uniform(0, 100);
                student.addLast(value);
                teacher.addLast(value);
                errorMessageOperator(errorMessage, "addLast" + "(" + value + ")");
            } else if (op == 2 && !teacher.isEmpty()) {
                errorMessageOperator(errorMessage, "removeFirst" + "()");
                assertEquals("\n" + errorMessage[0] + "\n" + errorMessage[1] + "\n" + errorMessage[2],teacher.removeFirst(), student.removeFirst());
            } else if (op == 3 && !teacher.isEmpty()) {
                errorMessageOperator(errorMessage, "removeLast" + "()");
                assertEquals("\n" + errorMessage[0] + "\n" + errorMessage[1] + "\n" + errorMessage[2], teacher.removeLast(), student.removeLast());
            } else if (op == 4) {
                errorMessageOperator(errorMessage, "size" + "()");
                assertEquals("\n" + errorMessage[0] + "\n" + errorMessage[1] + "\n" + errorMessage[2], teacher.size(), student.size());
            }
        }
    }
    public void errorMessageOperator(String[] errorMessage, String Operation){
        assert errorMessage.length == 3;
        for (int i = 0; i < errorMessage.length - 1 ; i++) {
            errorMessage[i] = errorMessage[i + 1];
        }
        errorMessage[errorMessage.length - 1] = Operation;
    }
}
