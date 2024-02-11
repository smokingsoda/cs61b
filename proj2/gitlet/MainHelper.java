package gitlet;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

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
    static final File branches = join(gitletFolder, "branches");
    static final File masterBranch = join(branches, "master");

    /**
     * .gitlet
     *      -commits
     *      -blobs
     *      -stagingArea
     *          -addStage(obj)
     *          -removeStage(obj)
     *          -stagingAreaBlobs
     *      -HEAD
     *      -branches
     *          -master
     *          -other branches
     */

    public static void init() {
        if (!validateGitlet()) {
            gitletFolder.mkdir();
            commits.mkdir();
            blobs.mkdir();
            stagingArea.mkdir();
            stagingAreaBlobs.mkdir();
            branches.mkdir();
            Stage addStage = new Stage();
            Stage removeStage = new Stage();
            Commit currentCommit = new Commit();
            saveAsSHA1(currentCommit, commits);
            saveFile(addStage, addStageFile);
            saveFile(removeStage, removeStageFile);
            updateBranch(currentCommit, "master");
            updateHEAD("master");
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
            Commit currentCommit = getHEADCommit();
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
        Commit parentCommit = getHEADCommit();//1.Get the current Commit
        String currentBranch = getHEADBranch();
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
            updateBranch(childCommit, currentBranch);
            updateHEAD(currentBranch); // Upadate Head
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
        Commit currentCommit = getHEADCommit();
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

    public static void log() {
        if (!validateGitlet()) {
            System.out.println("Not a valid gitlet repository");
            System.exit(0);
        }
        String currentBranch = getHEADBranch();
        String currentCommitName = getBranchCommitName(currentBranch);
        recursiveLog(currentCommitName);
    }

    public static void recursiveLog(String childCommitName) {
        File childCommitFile = join(commits, childCommitName);
        Commit childCommit = (Commit) loadObject(childCommitFile, Commit.class);
        String parentCommitName = childCommit.getParent();
        System.out.println("===");
        System.out.println("commit " + childCommitName);
        System.out.println(childCommit);
        System.out.println();
        if (parentCommitName == null) {
            return;
        } else {
            recursiveLog(parentCommitName);
        }
    }

    public static void status() {
        if (!validateGitlet()) {
            System.out.println("Not a valid gitlet repository");
            System.exit(0);
        }
        TreeMap<String, String> modificationFileMap = new TreeMap<>();
        TreeSet<String> untrackedFileSet = new TreeSet<>();
        Commit currentCommit = getHEADCommit();
        Stage addStage = (Stage) loadObject(addStageFile, Stage.class);
        Stage removeStage = (Stage) loadObject(removeStageFile, Stage.class);
        Set<String> addStageFileSet = addStage.stageTreeKeySet();
        Set<String> removeStageFileSet = removeStage.stageTreeKeySet();
        Set<String> stagedFilesSet = new TreeSet();
        Set<String> removedFilesSet = new TreeSet<>();
        TreeSet<String> branchesSet = new TreeSet<>();
        TreeSet<String> allFile = new TreeSet<>();
        /**
         * === Branch Part ===
         */
        System.out.println("=== Branches ===");
        File[] branchesFile = branches.listFiles();
        String currentBranch = getHEADBranch();
        for(File f : branchesFile) {
            String fName = f.getName();
            branchesSet.add(fName);
        }
        for(String fName : branchesSet) {
            if (fName.equals(currentBranch)) {
                fName = "*" + fName;
            }
            System.out.println(fName);
        }
        System.out.println();
        /**
         * === Working Directory File Part ===
         */
        //CWD files
        File[] CWDFile = CWD.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.exists() && pathname.isFile();
            }
        });
        for (File f : CWDFile) {
            allFile.add(f.getAbsolutePath().toLowerCase());
        }

        //addStage files
        for (String fAbsolutePath : addStageFileSet) {
            allFile.add(fAbsolutePath.toLowerCase());
        }
        //removeStage files
        for (String fAbsolutePath : removeStageFileSet) {
            allFile.add(fAbsolutePath.toLowerCase());
        }

        for (String fAbsolutePath : allFile) {
            File f = new File(fAbsolutePath);
            String fSHA1 = loadFileToSHA1(f);
            String fName = f.getName();
            if (f.exists()
                    && currentCommit.containsBlob(fAbsolutePath)
                        && !fSHA1.equals(currentCommit.getBlob(fAbsolutePath))
                            && ! addStage.containsFile(fAbsolutePath)) {
                //Tracked in the current commit, changed in the working directory, but not staged;
                modificationFileMap.put(fName, "modified");
                continue;
            } else if (f.exists()
                        && addStage.containsFile(fAbsolutePath)
                            && !fSHA1.equals(addStage.getFileSHA1(fAbsolutePath))) {
                //Staged for addition, but with different contents than in the working directory;
                modificationFileMap.put(fName, "modified");
                continue;
            } else if (addStage.containsFile(fAbsolutePath) && !f.exists()) {
                //Staged for addition, but deleted in the working directory;
                modificationFileMap.put(fName, "deleted");
                continue;
            } else if (!removeStage.containsFile(fAbsolutePath)
                    && currentCommit.containsBlob(fAbsolutePath) && ! f.exists()) {
                //Not staged for removal, but tracked in the current commit and deleted from the working directory.
                modificationFileMap.put(fName, "deleted");
            } else if (addStage.containsFile(fAbsolutePath)) {
                stagedFilesSet.add(fName);
            } else if (removeStage.containsFile(fAbsolutePath)) {
                removedFilesSet.add(fName);
            } else if (!addStage.containsFile(fAbsolutePath) && ! currentCommit.containsBlob(fAbsolutePath)) {
                untrackedFileSet.add(fName);
            }
        }

        /**
         * === Stage File Part ===
         */
        System.out.println("=== Staged Files ===");
        for (String fName : stagedFilesSet) {
            System.out.println(fName);
        }
        System.out.println();
        /**
         * === Remove File Part ===
         */
        System.out.println("=== Removed Files ===");
        for (String fName : removedFilesSet) {
            System.out.println(fName);
        }
        System.out.println();
        /**
         * === Modification File Part ===
         */
        System.out.println("=== Modifications Not Staged For Commit ===");
        Set<String> modificationFileSet = modificationFileMap.keySet();
        for (String fName : modificationFileSet) {
            System.out.println(fName + " (" + modificationFileMap.get(fName) + ")");
        }
        System.out.println();
        /**
         * === Untracked File Part ===
         */
        System.out.println("=== Untracked Files ===");
        for (String fName : untrackedFileSet) {
            System.out.println(fName);
        }
        System.out.println();
    }

    public static void branch(String branchName) {
        if (!validateGitlet()) {
            System.out.println("Not a valid gitlet repository");
            System.exit(0);
        }
        File[] branchesFile = branches.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile();
            }
        });// Determine whether it is a file
        for(File b : branchesFile) {
            if (b.getName().equals(branchName)) {
                System.out.println("A branch with that name already exists.");
                System.exit(0);
                return;
            }
        }
        Commit currentCommit = getHEADCommit();
        updateBranch(currentCommit, branchName);
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
    public static String loadFileToSHA1(File file) {
        if (file.exists()) {
            return sha1(loadByte(file));
        }
        else {
            return null;
        }
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

    /**
     * ===== HEAD Related Functions =====
     */
    public static void updateHEAD(String branchName) {
        writeContents(HEAD, branchName);
    }

    public static void updateBranch(Commit currentCommit, String branchName) {
        File currentBranch = join(branches, branchName);
        String currentCommitSHA1 = objToSHA1(currentCommit);
        writeContents(currentBranch, currentCommitSHA1);
    }

    public static Commit getHEADCommit() {
        String targetBranch = loadString(HEAD);
        String targetCommitSHA1 = loadString(join(branches, targetBranch));
        File targetCommitFile = join(commits, targetCommitSHA1);
        return (Commit) loadObject(targetCommitFile, Commit.class);
    }

    public static String getHEADBranch() {
        return loadString(HEAD);
    }

    public static String getBranchCommitName(String branchName) {
        File currentBranch = join(branches, branchName);
        return loadString(currentBranch);
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
