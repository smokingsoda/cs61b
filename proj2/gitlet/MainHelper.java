package gitlet;

import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
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
    static final File HEAD = join(gitletFolder, "HEAD");

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
            String addingFilePath = addingFile.getAbsolutePath();
            Blob addBlob = new Blob(addingFilePath, loadByte(addingFile));
            //1.Create new blob
            String addBlobName = addBlob.contentToSHA1();
            //2. Convert the blob's content into SHA1
            String currentCommitBlob = currentCommit.getBlob(addingFilePath);
            if (currentCommitBlob == null || !(currentCommitBlob.equals(addBlobName))) {
                File addBlobFile = join(stagingAreaBlobs, addBlobName);
                //3. Create a file in addStage folder
                saveFile(addBlob, addBlobFile);
                //4. Store this blob in the certain file
                addStageArea.putFile(addingFile.getAbsolutePath(), addBlobName);
                //5. Store the addingFile's A path as key, the File content(SHA1) as value in case of looking up this file;
            }
            else {
                addStageArea.removeFile(addingFilePath);
            }
            saveFile(addStageArea, addStageFile);
            //6. Save the addStageArea
        } else {
            System.out.println("File does not exist.");
            System.exit(0);
        }
    }

    public static void commit(String message, Date timeStamp) {
        if (! validateGitlet()) {
            System.out.println("Not a valid gitlet repository");
            System.exit(0);
        }
        Commit parentCommit = getHEAD();//1.Get the current Commit
        Commit childCommit = parentCommit.createChildCommit(message, timeStamp);//2.Create a brand-new Commit(everything is different)
        Stage addStage = (Stage) loadObject(addStageFile, Stage.class);
        Stage removeStage = (Stage) loadObject(removeStageFile, Stage.class);
        if (addStage.isEmpty() && removeStage.isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        } else {
            addToCommit(addStage, childCommit);//3.According to the stageArea, Modify the childCommit, and move the blobs to blobs
            removeFromCommit(removeStage, childCommit);
            saveAsSHA1(childCommit, commits);//4.Save childCommit
            updateHEAD(childCommit); // Upadate Head
            addStage.clearStageTree();//5.Clear stage
            removeStage.clearStageTree();
            saveFile(addStage, addStageFile);//6.Save stage
            saveFile(removeStage, removeStageFile);
            clearStagingAreaBlobs();
        }
    }

    public static void rm(String fileName) {
        if (!validateGitlet()) {
            System.out.println("Not a valid gitlet repository");
            System.exit(0);
        }
        File removingFile = join(CWD, fileName);
        Commit currentCommit = getHEAD();
        Stage addStageArea = (Stage) loadObject(addStageFile, Stage.class);
        Stage removeStageArea = (Stage) loadObject(removeStageFile, Stage.class);
        String removingFilePath = removingFile.getAbsolutePath();
        Boolean addStageAreaContains = addStageArea.containsFile(removingFilePath);
        Boolean currentCommitContains = currentCommit.containsBlob(removingFilePath);
        if (addStageAreaContains || currentCommitContains) {
            if (addStageAreaContains) {
                addStageArea.removeFile(removingFilePath);
            }
            if (currentCommitContains) {
                removeStageArea.putFile(removingFilePath, "dummy SHA1");
                restrictedDelete(removingFilePath);
            }
            saveFile(addStageArea, addStageFile);
            saveFile(removeStageArea, removeStageFile);
        } else {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
    }

    /**
     * ===== Object Persistence Functions =====
     */
    public static Serializable loadObject(File file, Class className) {
        return readObject(file, className);
    }
    public static void saveFile(Serializable obj, File file) {
        writeObject(file, obj);
    }


    /**
     * ===== Convert Object into SHA1 =====
     */
    public static String objToSHA1(Serializable obj) {
        byte[] objData = serialize(obj);
        return sha1(objData);
    }


    /**
     * ===== Alternative Load & Save Functions =====
     */
    public static byte[] loadByte(File file) {
        return readContents(file);
    }
    public static String loadString(File file) {
        return readContentsAsString(file);
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
    //TODO: Maybe I want to remove this method?
    public static void saveAsSHA1(Serializable obj, File folder) {
        String fileName = objToSHA1(obj);
        saveAsName(obj, folder, fileName);
    }


    /**
     * ===== Commit Related Functions =====
     */
    public static String dateToString(Date date) {
        String pattern = "MM/dd/yyyy HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }
    public static void addToCommit(Stage addStage, Commit currentCommit) {
        Set<String> addStageKeySet = addStage.stageTreeKeySet();
        for(String path : addStageKeySet) {
            String blobName = addStage.getFileSHA1(path);
            File blob = join(stagingAreaBlobs, blobName);
            blob.renameTo(join(blobs, blobName));
            currentCommit.putBlob(path, blobName);
        }
    }
    public static void removeFromCommit(Stage removeStage, Commit currentCommit) {
        Set<String> removeStageKeySet = removeStage.stageTreeKeySet();
        for(String path : removeStageKeySet) {
            currentCommit.removeBlob(path);
        }
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

    /**
     * ===== HEAD Related Functions =====
     */
    public static void updateHEAD(Commit commit) {
        String commitFileName = objToSHA1(commit);
        writeContents(HEAD, commitFileName);
    }

    public static Commit getHEAD() {
        String targetCommitSHA1 = loadString(HEAD);
        File targetCommitFile = join(commits, targetCommitSHA1);
        return (Commit) loadObject(targetCommitFile, Commit.class);
    }

    public static void clearStagingAreaBlobs() {
        File[] files = stagingAreaBlobs.listFiles();
        //System.out.println(files[0].getAbsolutePath());
        for (File f : files) {
            f.delete();
        }
    }
    /**
     * ===== Main Class Related Function =====
     */
    public static boolean validateGitlet() {
        return gitletFolder.exists();
    }
}
