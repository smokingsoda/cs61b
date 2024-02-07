package gitlet;

//import org.junit.Test;

import static gitlet.Utils.join;
//import static org.junit.Assert.*;


import java.io.File;

public class test {
    File CWD = new File(System.getProperty("user.dir"));

    //@Test
    public void test1() {
        File hello1 = new File("/Users/smokingsoda/desktop/cs61b/proj2/Hello.rtf");
        File hello2 = join(CWD, "Hello.rtf");
        System.out.println(hello1.getAbsoluteFile());
        System.out.println(hello2.getAbsoluteFile());
        //assertTrue(hello1.exists());
    }

    //@Test
    public void test2() {

    }
}
