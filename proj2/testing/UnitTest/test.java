package testing.UnitTest;

import gitlet.Commit;
import gitlet.Main;
import gitlet.MainHelper;
import gitlet.Stage;
import org.junit.Test;


import java.io.File;
import java.util.Date;

import static testing.UnitTest.Utils.join;
import static testing.UnitTest.Utils.sha1;



public class test {
    public File CWD = new File("/Users/smokingsoda/desktop/cs61b/proj2/");
    public File gitletFolder = join(CWD, ".gitlet");
    public File stagingArea = join(gitletFolder, "stagingArea");
    public File addStageFile = join(stagingArea, "addStage");
    public File removeStageFile = join(stagingArea, "removeStage");
    public File stagingAreaBlobs = join(stagingArea, "stagingAreaBlobs");
    public File commits = join(gitletFolder, "commits");
    public File blobs = join(gitletFolder, "blobs");
    public File HEAD = join(gitletFolder, "HEAD");
    //@Test
    public void test1() {
        File currentCommit = join(commits, "3f6a63aac3f87708cc9947d47ab3282dd6460d78");
        Commit commit = (Commit) MainHelper.loadObject(currentCommit, Commit.class);
    }

    //@Test
    public void test2() {
        Commit parentCommit = (Commit)MainHelper.loadObject(join(commits, "502e84d2e0a6b8aace8ba9dc1ff1f93458a9eb76"), Commit.class);
        Commit childCommit = (Commit)MainHelper.loadObject(join(commits, "6d6b7dade3f79a8d2bbe3e2cde917329de2fb964"), Commit.class);
    }

    @Test
    public void test3() {
        Stage addStage = (Stage) MainHelper.loadObject(addStageFile, Stage.class);
        Commit currentCommit = MainHelper.getHEADCommit();
        //File Hello = join(CWD, "Hello.txt");
        //byte[] HelloByte = MainHelper.loadByte(Hello);
        //String SHA1 = sha1(HelloByte);
    }

    //@Test
    public void test4() {
        String[] args1 = {"add", "Hello.txt"};
        String[] args2 = {"rm", "Hello.txt"};
        Main.main(args1);
        Main.main(args2);
    }

    //@Test
    public void test5() {
        String[] args1 = {"add", "f.txt"};
        Main.main(args1);
        String[] args2 = {"add", "g.txt"};
        Main.main(args2);
    }

}
