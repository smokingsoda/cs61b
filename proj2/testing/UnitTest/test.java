package testing.UnitTest;

import gitlet.Commit;
import gitlet.MainHelper;
import gitlet.Stage;
import org.junit.Test;

import java.io.File;
import java.util.Date;

import static testing.UnitTest.Utils.join;


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

    @Test
    public void test2() {

    }
}
