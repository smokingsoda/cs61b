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
    static final File addStageFile = join(stagingArea, "addStage");
    static final File removeStageFile = join(stagingArea, "removeStage");
    static final File stagingAreaBlobs = join(stagingArea, "stagingAreaBlobs");
    static final File commits = join(gitletFolder, "commits");
    static final File blobs = join(gitletFolder, "blobs");

    /**
     * .gitlet
     *      -commits
     *      -blobs
     *      -stagingArea
     *          -addStage(obj)
     *          -removeStage(obj)
     *          -stagingAreaBlobs
     *      -HEAD
     */

    public static void init() {
        if (!validateGitlet()) {
            gitletFolder.mkdir();
            commits.mkdir();
            blobs.mkdir();
            stagingArea.mkdir();
            stagingAreaBlobs.mkdir();
            Stage addStage = new Stage();
            Stage removeStage = new Stage();
            Commit currentCommit = new Commit();
            saveAsSHA1(currentCommit, commits);
            saveFile(addStage, addStageFile);
            saveFile(removeStage, removeStageFile);
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
        if (addingFile.exists()) {
            Commit currentCommit = getHEAD();
            Stage addStageArea = (Stage) loadObject(addStageFile, Stage.class);
            if (!currentCommit.containsBlob(addingFile)) {
                Blob addBlob = new Blob(addingFile.getAbsolutePath(), loadByte(addingFile));
                //1.Create new blob
                String addBlobName = addBlob.contentToSHA1();
                //2. Convert the blob's content into SHA1
                File addBlobFile = join(stagingAreaBlobs, addBlob.contentToSHA1());
                //3. Create a file in addStage folder
                saveFile(addBlob, addBlobFile);
                //4. Store this blob in the certain file
                addStageArea.putFile(addingFile.getAbsolutePath(), addBlobName);
                //5. Store the addingFile's A path as key, the File content(SHA1) as value in case of looking up this file;
                saveFile(addStageArea, addStageFile);
                //6. Save the addStageArea
            }
            else {
                addStageArea.removeFile(addingFile.getAbsolutePath());
            }
        } else {
            System.out.println("File does not exist.");
            System.exit(0);
        }
    }

    public static void saveFile(Serializable obj, File file) {
        writeObject(file, obj);
    }

    public static boolean validateGitlet() {
        return gitletFolder.exists();
    }

    public static byte[] loadByte(File file) {
        return readContents(file);
    }
    public static String loadString(File file) {
        return readContentsAsString(file);
    }
    public static Serializable loadObject(File file, Class className) {
        return readObject(file, className);
    }

    //TODO: Maybe I want to remove this method?
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
    public static String fileToSHA1(File file) {
        if (file.exists()) {
            Serializable obj = loadObject(file, Serializable.class);
            return objToSHA1(obj);
        } else {
            System.out.println("file To SHA1");
            System.exit(0);
            return null;
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

    public static Commit getHEAD() {
        File HEAD = join(gitletFolder, "HEAD");
        String targetCommitSHA1 =  loadString(HEAD);
        File targetCommitFile = join(commits, targetCommitSHA1);
        return (Commit) loadObject(targetCommitFile, Commit.class);
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

}
