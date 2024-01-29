package bstmap;

import bstmap.BSTMap;

public class selfTest {
    BSTMap<Integer, String> myBSTmap = new BSTMap<>();

    public static void main(String[] args) {
        selfTest selfTest = new selfTest();
        selfTest.run();
    }

    public void run() {
        myBSTmap.put(1, "a");
        myBSTmap.put(2, "a");
        myBSTmap.put(3, "a");
        myBSTmap.put(4, "5");
        myBSTmap.printInOrder();
        // 其他处理...
    }
}
