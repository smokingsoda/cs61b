package gitlet;

import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;

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
        if (!validateGitlet()) {
            gitletFolder.mkdir();
            commits.mkdir();
            blobs.mkdir();
            TreeMap<String, String> stagingArea = new TreeMap<>();
            saveAsName(stagingArea, gitletFolder, "stagingArea");
            Commit currentCommit = new Commit();
            saveAsSHA1(currentCommit, commits);
            updateHEAD(currentCommit);
        } else {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
        }
        System.exit(0);
    }

    public static void add(String fileName) {
        if (! validateGitlet()) {
            System.out.println("Not a valid gitlet repository");
            System.exit(0);
        }
        File addingFile = join(CWD, fileName);
        TreeMap stagingArea = getStagingArea();
        if (addingFile.exists()) {
            byte[] addingFileContent = loadByte(addingFile);
            Blob newBlob = new Blob(addingFileContent);
            String newBlobSHA1 = objToSHA1(newBlob);
            String HEAD = getHEAD();
            Commit currentCommit = getCommit(HEAD);
            if (!currentCommit.containsBlob(newBlobSHA1)) {
                //String oldBlobSHA1 = (String) stagingArea.get(fileName);
                //deleteBlob(oldBlobSHA1);// Delete old and invalid blob
                stagingArea.put(fileName, newBlobSHA1); //Add to stagingArea or update
                //saveAsName(newBlob, blobs, newBlobSHA1);//Save blob in blobs
                saveAsName(stagingArea, gitletFolder, "stagingArea");//Save stagingArea
            } else {
                stagingArea.remove(fileName);
            }
        } else {
            System.out.println("File does not exist.");
            System.exit(0);
        }
    }

    public static boolean validateGitlet() {
        return gitletFolder.exists();
    }
    public static boolean deleteBlob(String bolbSHA1) {
        if (bolbSHA1 != null) {
            File blob = join(blobs, bolbSHA1);
            return blob.delete();
        } else {
            return false;
        }

    }

    public static byte[] loadByte(File file) {
        return readContents(file);
    }
    public static String loadString(File file) {
        return readContentsAsString(file);
    }
    public static Object loadObject(File file, Class className) {
        return readObject(file, className);
    }

    public static void saveAsName(Serializable obj, File folder, String name) {
        if (folder.exists()) {
            String fileName = name;
            File saveFile = join(folder, fileName);
            writeObject(saveFile, obj);
        }
        else {
            System.out.printf("From Function: saveAsName:\n" +
                    "This folder does NOT exist");
            System.exit(0);
        }
    }
    public static void saveAsSHA1(Serializable obj, File folder) {
        String fileName = objToSHA1(obj);
        saveAsName(obj, folder, fileName);
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

    public static String getHEAD() {
        File HEAD = join(gitletFolder, "HEAD");
        return loadString(HEAD);
    }

    public static Commit getCommit(String commitSHA1) {
        File commit = join(commits, commitSHA1);
        if (commit.exists()) {
            return (Commit) loadObject(commit, Commit.class);
        } else {
            System.out.printf("No such Commit found: %d\n", commitSHA1);
            System.exit(0);
            return null;
        }
    }

    public static TreeMap getStagingArea() {
        return (TreeMap) loadObject(stagingArea, TreeMap.class);
    }
}
