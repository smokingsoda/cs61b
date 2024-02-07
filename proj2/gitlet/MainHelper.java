package gitlet;

import com.google.gson.JsonSerializationContext;
import org.w3c.dom.events.EventException;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
            gitletFolder.mkdir();
            commits.mkdir();
            blobs.mkdir();
            stagingArea.mkdir();
            HashMap<String, String> stage = new HashMap<>();
            save(stagingArea, stage);
            Commit currentCommit = new Commit();
            save(commits, currentCommit);
            updateHEAD(currentCommit);
        } else {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
        }
        System.exit(0);
    }

    public static void add(String fileName) {
        File addingFile = join(CWD, fileName);
        if (addingFile.exists()) {
            byte[] addingFileContent = loadByte(fileName, CWD);
            String addingFileSHA1 = byteToSHA1(addingFileContent);

        }
    }

    public static byte[] loadByte(String fileName, File folder) {
        File loadFile = join(folder, fileName);
        if (loadFile.exists()) {
            return readContents(loadFile);
        }
        else {
            System.out.printf("No file found: %d\n", fileName);
            System.exit(0);
            return null;
        }
    }
    public static Object loadObject(String fileName, File folder, Class className) {
        File loadFile = join(folder, fileName);
        if (loadFile.exists()) {
            return readObject(loadFile, className);
        }
        else {
            System.out.printf("No file found: %d\n", fileName);
            System.exit(0);
            return null;
        }
    }

    public static void save(File folder, Serializable obj) {
        if (folder.exists()) {
            String fileName = objToSHA1(obj);
            File saveFile = join(folder, fileName);
            if (!saveFile.exists()) {
                writeObject(saveFile, obj);
            } else {
                System.out.printf("File exists: %d\n", fileName);
                System.exit(0);
            }
        }
        else {
            System.out.printf("This folder does NOT exist");
            System.exit(0);
        }
    }

    public static String objToSHA1(Serializable obj) {
        byte[] objData = serialize(obj);
        return sha1(objData);
    }

    public static String byteToSHA1(byte[] obj) {
        return sha1(obj);
    }

    public static String dateToString(Date date) {
        String pattern = "MM/dd/yyyy HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    public static void updateHEAD(Commit commit) {
        String commitFileName = objToSHA1(commit);
        File HEAD = join(gitletFolder, "HEAD");
        writeContents(HEAD, commitFileName);
    }

    public static byte[] getHEAD() {
        File HEAD = join(gitletFolder, "HEAD");
        return loadByte("HEAD", gitletFolder);
    }
}
