package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        // TODO: YOUR CODE HERE
        SLList<Integer> test = new SLList<Integer>();
        AList<Integer> Ns = new AList<Integer>();
        AList<Double> times = new AList<Double>();
        AList<Integer> opCounts = new AList<Integer>();
        int i = 1; int j = 1000;
        while (i <= 128000){
            test.addLast(1);
            if (Math.floorDiv(i, j) == 1){
                Ns.addLast(i);
                opCounts.addLast(10000);
                int k = 1;
                Stopwatch sw = new Stopwatch();
                while (k <= 10000){
                    test.getLast();
                    k += 1;
                }
                times.addLast(sw.elapsedTime());
                j *= 2;
            }
             i += 1;
        }
        printTimingTable(Ns, times, opCounts);
    }
}
