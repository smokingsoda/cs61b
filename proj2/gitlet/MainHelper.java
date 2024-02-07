package gitlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.HashMap;

import static gitlet.Utils.*;

public class MainHelper {
    static final File CWD = new File(System.getProperty("user.dir"));
    static final File gitletFolder = join(CWD, ".gitlet");
    static final File stagingArea = join(gitletFolder, "stagingArea");
    static final File commits = join(gitletFolder, "commits");
    static final File blobs = join(gitletFolder, "blobs");
    /**
     * Creates a new Gitlet version-control system in the
     * current directory. This system will automatically
     * start with one commit: a commit that contains no files
     * and has the commit message initial commit (just like that,
     * with no punctuation). It will have a single branch: master,
     * which initially points to this initial commit, and master
     * will be the current branch. The timestamp for this initial
     * commit will be 00:00:00 UTC, Thursday, 1 January 1970 in
     * whatever format you choose for dates (this is called “The
     * (Unix) Epoch”, represented internally by the time 0.)
     * Since the initial commit in all repositories created
     * by Gitlet will have exactly the same content, it follows
     * that all repositories will automatically share this commit
     * (they will all have the same UID) and all commits in all
     * repositories will trace back to it.
     */

    public static void init() {
        if (!gitletFolder.exists()) {
            Date initialDate = new Date(0);
            gitletFolder.mkdir();
            commits.mkdir();
            blobs.mkdir();
            HashMap<String, String> stage = new HashMap<>();
            writeObject(stagingArea, stage);
            commit("initial commit", null, initialDate, null, commits);
        }
        System.exit(0);
    }

    public static void commit(String message, Commit parent, Date timeStamp, Object[] content, File commitFolder) {
        Commit currentCommit = new Commit(message, parent, timeStamp, content);
        currentCommit.storeCommit(commitFolder);
    }

    public static void add(String fileName) {
        File currentFile = join(CWD, fileName);
        if (!currentFile.exists()) {
            System.out.println("File does not exist.");
            System.exit(0);
        } else {
            HashMap stage = readObject(stagingArea, HashMap.class);
            if (stage.containsKey(fileName)) {
                String blobName = (String) stage.get(fileName);
                File blobFile = join(blobs, blobName);
                if (!blobFile.exists()) {
                    byte[] content = new byte[0];
                    try {
                        content = Files.readAllBytes(currentFile.toPath());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Blob newBlob = new Blob(content);
                    String newBlobSHA1 = sha1(newBlob);
                    File newBlobFile = join(blobs, newBlobSHA1);
                    writeObject(newBlobFile, newBlob);
                }
            }
        }
    }
}
