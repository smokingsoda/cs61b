package gitlet;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
     * -commits
     * -six-number shorten id
     * -blobs
     * -stagingArea
     * -addStage(obj)
     * -removeStage(obj)
     * -stagingAreaBlobs
     * -HEAD
     * -branches
     * -master
     * -other branches
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
            saveAsSHA1(currentCommit, commits, 6);
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
        File addingFile = join(CWD, fileName);
        if (addingFile.exists()) {
            Commit currentCommit = getHEADCommit();
            Stage addStageArea = (Stage) loadObject(addStageFile, Stage.class);
            Stage removeStageArea = (Stage) loadObject(removeStageFile, Stage.class);
            String addingFileName = addingFile.getName();
            Blob addBlob = new Blob(addingFileName, loadByte(addingFile));
            //1.Create new blob
            String addBlobName = objToSHA1(addBlob);
            //2. Convert the blob's content into SHA1
            String currentCommitBlob = currentCommit.getBlob(addingFileName);
            if (currentCommitBlob == null || !(currentCommitBlob.equals(addBlobName))) {
                File addBlobFile = join(stagingAreaBlobs, addBlobName);
                //3. Create a file in addStage folder
                saveFile(addBlob, addBlobFile);
                //4. Store this blob in the certain file
                addStageArea.putFile(addingFileName, addBlobName);
                //5. Store the addingFile's A path as key, the File content(SHA1) as value in case of looking up this file;
            } else {
                addStageArea.removeFile(addingFileName);
            }
            removeStageArea.removeFile(addingFileName);
            saveFile(addStageArea, addStageFile);
            saveFile(removeStageArea, removeStageFile);
            //6. Save the addStageArea
        } else {
            System.out.println("File does not exist.");
            System.exit(0);
        }
    }

    public static void commit(String message, Date timeStamp) {
        if (message.equals("")) {
            System.out.println("Please enter a commit message.");
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
            saveAsSHA1(childCommit, commits, 6);//4.Save childCommit
            updateBranch(childCommit, currentBranch);
            updateHEAD(currentBranch); // Update Head
            addStage.clearStageTree();//5.Clear stage
            removeStage.clearStageTree();
            saveFile(addStage, addStageFile);//6.Save stage
            saveFile(removeStage, removeStageFile);
            clearStagingAreaBlobs();
        }
    }

    public static void rm(String fileName) {
        File removingFile = join(CWD, fileName);
        Commit currentCommit = getHEADCommit();
        Stage addStageArea = (Stage) loadObject(addStageFile, Stage.class);
        Stage removeStageArea = (Stage) loadObject(removeStageFile, Stage.class);
        String removingFileName = removingFile.getName();
        Boolean addStageAreaContains = addStageArea.containsFile(removingFileName);
        Boolean currentCommitContains = currentCommit.containsBlob(removingFileName);
        if (addStageAreaContains || currentCommitContains) {
            if (addStageAreaContains) {
                addStageArea.removeFile(removingFileName);
            }
            if (currentCommitContains) {
                removeStageArea.putFile(removingFileName, "dummy SHA1");
                restrictedDelete(removingFileName);
            }
            saveFile(addStageArea, addStageFile);
            saveFile(removeStageArea, removeStageFile);
        } else {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }
    }

    public static void log() {
        String currentBranch = getHEADBranch();
        String currentCommitName = getBranchCommitName(currentBranch);
        recursiveLog(currentCommitName);
    }

    public static void recursiveLog(String childCommitName) {
        Commit childCommit = getCommit(childCommitName);
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
        TreeMap<String, String> modificationFileMap = new TreeMap<>();
        TreeSet<String> untrackedFileSet = new TreeSet<>();
        Commit currentCommit = getHEADCommit();
        Stage addStage = (Stage) loadObject(addStageFile, Stage.class);
        Stage removeStage = (Stage) loadObject(removeStageFile, Stage.class);
        Set<String> addStageFileSet = addStage.stageTreeKeySet();
        Set<String> removeStageFileSet = removeStage.stageTreeKeySet();
        Set<String> stagedFilesSet = new TreeSet();
        Set<String> removedFilesSet = new TreeSet<>();
        Set<String> currentCommitFileSet = currentCommit.contentKeySet();
        TreeSet<String> branchesSet = new TreeSet<>();
        TreeSet<String> allFile = new TreeSet<>();
        /**
         * === Branch Part ===
         */
        System.out.println("=== Branches ===");
        File[] branchesFile = branches.listFiles();
        String currentBranch = getHEADBranch();
        for (File f : branchesFile) {
            String fName = f.getName();
            branchesSet.add(fName);
        }
        for (String fName : branchesSet) {
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
            allFile.add(f.getName());
        }
        //current commit files
        for (String fName : currentCommitFileSet) {
            allFile.add(fName);
        }
        //addStage files
        for (String fName: addStageFileSet) {
            allFile.add(fName);
        }
        //removeStage files
        for (String fName : removeStageFileSet) {
            allFile.add(fName);
        }

        for (String fName : allFile) {
            File f = join(CWD, fName);
            String fSHA1 = loadFileToSHA1(f);
            if (f.exists()
                    && currentCommit.containsBlob(fName)
                    && !fSHA1.equals(currentCommit.getBlobContentSHA1(fName))
                    && !addStage.containsFile(fName)) {
                //Tracked in the current commit, changed in the working directory, but not staged;
                modificationFileMap.put(fName, "modified");
                continue;
            } else if (f.exists()
                    && addStage.containsFile(fName)
                    && !fSHA1.equals(addStage.getBlobContentSHA1(fName))) {
                //Staged for addition, but with different contents than in the working directory;
                modificationFileMap.put(fName, "modified");
                continue;
            } else if (addStage.containsFile(fName) && !f.exists()) {
                //Staged for addition, but deleted in the working directory;
                modificationFileMap.put(fName, "deleted");
                continue;
            } else if (!removeStage.containsFile(fName)
                    && currentCommit.containsBlob(fName)
                    && !f.exists()) {
                //Not staged for removal, but tracked in the current commit and deleted from the working directory.
                modificationFileMap.put(fName, "deleted");
            } else if (addStage.containsFile(fName)) {
                stagedFilesSet.add(fName);
            } else if (removeStage.containsFile(fName)) {
                removedFilesSet.add(fName);
            } else if (!addStage.containsFile(fName) && !currentCommit.containsBlob(fName)) {
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
        File newBranchFile = join(branches, branchName);
        if (newBranchFile.exists()) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        } else {
            Commit currentCommit = getHEADCommit();
            updateBranch(currentCommit, branchName);
        }
    }

    public static void rmBranch(String branchName) {
        File removeBranchFile = join(branches, branchName);
        if (!removeBranchFile.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        } else {
            String headBranch = getHEADBranch();
            if (headBranch.equals(branchName)) {
                System.out.println("Cannot remove the current branch.");
            } else {
                removeBranchFile.delete();
            }
        }
    }

    public static void checkoutFileNameFromCommit(String fileName, Commit commit) {
        //File file = join(CWD, fileName);
        commit.retrieveFile(fileName);
    }

    public static void checkoutFileName(String fileName) {
        Commit currentCommit = getHEADCommit();
        //File file = join(CWD, fileName);
        currentCommit.retrieveFile(fileName);
    }

    public static void checkoutCommitFileName(String commitSHA1, String fileName) {
        Commit targetCommit = getCommit(commitSHA1);
        //File file = join(CWD, fileName);
        targetCommit.retrieveFile(fileName);
    }

    public static void checkoutBranchName(String branchName) {
        File targetBranchFile = join(branches, branchName);
        if (!targetBranchFile.exists()) {
            System.out.println("No such branch exists.");
            System.exit(0);
        } else if (branchName.toLowerCase().equals(getHEADBranch())) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }
        String targetCommitSHA1 = getBranchCommitName(branchName);
        ///
        checkoutCommitSHA1(targetCommitSHA1);///////
        ///
        updateHEAD(branchName);
    }

    public static void checkoutCommitSHA1(String targetCommitSHA1) {
        Commit targetCommit = getCommit(targetCommitSHA1);
        Commit currentCommit = getHEADCommit();
        File[] CWDFiles = CWD.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile();
            }
        });
        Set<String> targetCommitContentSet = targetCommit.contentKeySet();
        for (String fName : targetCommitContentSet) {
            String targetFContent = targetCommit.getBlob(fName);
            File f = join(CWD, fName);
            String currentFContent = loadFileToSHA1(f);
            if (f.exists()
                    && !currentCommit.containsBlob(fName)
                    && !targetFContent.equals(currentFContent)) {
                System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                System.exit(0);
                return;
            }
        }
        for (File f : CWDFiles) {
            String fName = f.getName();
            if (currentCommit.containsBlob(fName)
                    && !targetCommit.containsBlob(fName)) {
                f.delete();
            }
        }
        for (String fName : targetCommitContentSet) {
            targetCommit.retrieveFile(fName);
        }
        Stage addStageArea = (Stage) loadObject(addStageFile, Stage.class);
        Stage removeStageArea = (Stage) loadObject(removeStageFile, Stage.class);
        clearStagingAreaBlobs();
        addStageArea.clearStageTree();
        removeStageArea.clearStageTree();
        saveFile(addStageArea, addStageFile);
        saveFile(removeStageArea, addStageFile);
    }

    public static void reset(String commitSHA1) {
        checkoutCommitSHA1(commitSHA1);
        String currentBranch = getHEADBranch();
        Commit currentCommit = getCommit(commitSHA1);
        updateBranch(currentCommit, currentBranch);
    }

    public static void globalLog() {
        Set<File> commitSet = getCommitSet();
        for (File comFile : commitSet) {
            Commit commit = (Commit) loadObject(comFile, Commit.class);
            System.out.println("===");
            System.out.println("commit " + comFile.getName());
            System.out.println(commit);
            System.out.println();
        }

    }

    public static void find(String keyWords) {
        Set<File> commitSet = getCommitSet();
        Set<String> commitNamePrinted = new HashSet<>();
        for (File comFile : commitSet) {
            Commit commit = (Commit) loadObject(comFile, Commit.class);
            String commitMessage = commit.getMessage();
            if (commitMessage.equals(keyWords)) {
                commitNamePrinted.add(comFile.getName());
            }
        } for (String commitName : commitNamePrinted) {
            System.out.println(commitName);

        }
        if (commitNamePrinted.isEmpty()) {
            System.out.println("Found no commit with that message.");
            System.exit(0);
        }
    }

    public static void merge(String targetBranchName) {
        String currentBranchName = getHEADBranch();
        Stage addStage = (Stage) loadObject(addStageFile, Stage.class);
        Stage removeStage = (Stage) loadObject(removeStageFile, Stage.class);
        File targetBranchFile = join(branches, targetBranchName);
        if (currentBranchName.equals(targetBranchName)) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
            return;
        }
        if (!addStage.isEmpty() || !removeStage.isEmpty()) {
            System.out.println("You have uncommitted changes.");
            System.exit(0);
            return;
        }
        if (!targetBranchFile.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
            return;
        }
        String targetCommitName = getBranchCommitName(targetBranchName);
        String currentCommitName = getBranchCommitName(currentBranchName);
        String latestCommonCommitName = getLatestCommonAncestorName(targetCommitName, currentCommitName);
        Commit targetCommit = getCommit(targetCommitName);
        Commit currentCommit = getCommit(currentCommitName);
        Commit splitCommit = getCommit(latestCommonCommitName);
        if (latestCommonCommitName.equals(targetCommitName)) {
            System.out.println("Given branch is an ancestor of the current branch.");
            System.exit(0);
            return;
        } else if (latestCommonCommitName.equals(currentCommitName)) {
            checkoutCommitSHA1(targetCommitName);
            updateBranch(targetCommit, currentBranchName);
            updateHEAD(currentBranchName);
            System.out.println("Current branch fast-forwarded.");
            System.exit(0);
            //Perhaps I want to move the branch?
            return;
        } else {
            boolean conflictFlag = false;
            Date nowDate = new Date();
            Commit newCommit = currentCommit.createMergedChildCommit(targetCommit, nowDate, targetBranchName, currentBranchName);
            Set<String> allFilesNameSet = new HashSet<>();
            Map<String, String> targetCommitMap = targetCommit.getContent();
            Map<String, String> currentCommitMap = currentCommit.getContent();
            Map<String, String> splitCommitMap = splitCommit.getContent();
            allFilesNameSet = putCommitFilePathInSet(targetCommit, allFilesNameSet);
            allFilesNameSet = putCommitFilePathInSet(splitCommit, allFilesNameSet);
            boolean overwrittenFlag = false;
            for (String fName : allFilesNameSet) {
                String splitCommitFContent = splitCommitMap.get(fName);
                String currentCommitFContent = currentCommitMap.get(fName);
                String targetCommitFContent = targetCommitMap.get(fName);
                boolean hasSpiltCommitF = splitCommitMap.containsKey(fName);
                boolean hasCurrentCommitF = currentCommitMap.containsKey(fName);
                boolean hasTargetCommitF = targetCommitMap.containsKey(fName);
                File f = join(CWD, fName);
                if (f.exists() && !hasCurrentCommitF) {
                    if (hasSpiltCommitF && !splitCommitFContent.equals(currentCommitFContent)) {
                        overwrittenFlag = true;
                    } else if (hasTargetCommitF && !targetCommitFContent.equals(currentCommitFContent)) {
                        overwrittenFlag = true;
                    }
                    if (overwrittenFlag) {
                        System.out.println("There is an untracked file in the way; delete it, or add and commit it first.");
                        System.exit(0);
                        return;
                    }
                }
            }
            allFilesNameSet = putCommitFilePathInSet(currentCommit, allFilesNameSet);
            for (String fName : allFilesNameSet) {
                File f = join(CWD, fName);
                String splitCommitFContent = splitCommitMap.get(fName);
                String currentCommitFContent = currentCommitMap.get(fName);
                String targetCommitFContent = targetCommitMap.get(fName);
                boolean hasSpiltCommitF = splitCommitMap.containsKey(fName);
                boolean hasCurrentCommitF = currentCommitMap.containsKey(fName);
                boolean hasTargetCommitF = targetCommitMap.containsKey(fName);
                if (hasSpiltCommitF
                        && hasCurrentCommitF
                        && hasTargetCommitF) {
                    if (splitCommitFContent.equals(currentCommitFContent)
                            && !splitCommitFContent.equals(targetCommitFContent)) {
                        //modified in Other but not HEAD    -->     other
                        checkoutFileNameFromCommit(fName, targetCommit);
                        add(fName);
                    } else if (splitCommitFContent.equals(targetCommitFContent)
                            && !splitCommitFContent.equals(currentCommitFContent)) {
                        //modified in HEAD but not other    -->     HEAD
                        continue;
                    } else if (!splitCommitFContent.equals(targetCommitFContent)
                            && !splitCommitFContent.equals(currentCommitFContent)) {
                        if (targetCommitFContent.equals(currentCommitFContent)) {
                            //no Conflict
                            continue;
                        } else if (!targetCommitFContent.equals(currentCommitFContent)) {
                            //modified in other and HEAD    -->     Conflict
                            Blob currentBlob = (Blob) loadObject(join(blobs, currentCommitFContent), Blob.class);
                            Blob targetBlob = (Blob) loadObject(join(blobs, targetCommitFContent), Blob.class);
                            putConflictContent(currentBlob, targetBlob, f);
                            conflictFlag = true;
                            add(fName);
                        }
                    }
                } else if (!hasSpiltCommitF
                        && !hasTargetCommitF
                        && hasCurrentCommitF) {
                    //not in split nor other but in HEAD --> HEAD
                    continue;
                } else if (!hasSpiltCommitF
                        && !hasCurrentCommitF
                        && hasTargetCommitF) {
                    //not in split nor HEAD but in other --> other
                    checkoutFileNameFromCommit(fName, targetCommit);
                    add(fName);
                } else if (!hasTargetCommitF
                        && hasSpiltCommitF
                        && hasCurrentCommitF
                        && splitCommitFContent.equals(currentCommitFContent)) {
                    //unmodified in HEAD but not present in other --> REMOVE
                    rm(fName);
                } else if (!hasCurrentCommitF
                        && hasSpiltCommitF
                        && hasTargetCommitF
                        && splitCommitFContent.equals(targetCommitFContent)) {
                    //unmodified in other but not present in HEAD --> REMAIN REMOVED
                    //TODO: Perhaps I want to consider what is "remain removed"
                    //f.delete();
                    continue;
                } else if (!hasSpiltCommitF
                        && hasCurrentCommitF
                        && hasTargetCommitF) {
                    if (currentCommitFContent.equals(targetCommitFContent)) {
                        //same way
                        continue;
                    } else {
                        //conflict
                        Blob currentBlob = (Blob) loadObject(join(blobs, currentCommitFContent), Blob.class);
                        Blob targetBlob = (Blob) loadObject(join(blobs, targetCommitFContent), Blob.class);
                        putConflictContent(currentBlob, targetBlob, f);
                        conflictFlag = true;
                        add(fName);
                    }
                } else if (!hasTargetCommitF
                        && hasSpiltCommitF
                        && hasCurrentCommitF) {
                    if (splitCommitFContent.equals(currentCommitFContent)) {
                        //same way
                        continue;
                    } else {
                        //conflict
                        Blob currentBlob = (Blob) loadObject(join(blobs, currentCommitFContent), Blob.class);
                        putConflictContent(currentBlob, null, f);
                        conflictFlag = true;
                        add(fName);
                    }
                } else if (!hasCurrentCommitF
                        && hasSpiltCommitF
                        && hasTargetCommitF) {
                    if (splitCommitFContent.equals(targetCommitFContent)) {
                        //same way
                        continue;
                    } else {
                        //conflict
                        Blob targetBlob = (Blob) loadObject(join(blobs, targetCommitFContent), Blob.class);
                        putConflictContent(null, targetBlob, f);
                        conflictFlag = true;
                        add(fName);
                    }
                } else if (hasSpiltCommitF
                            && !hasTargetCommitF
                            && !hasCurrentCommitF) {
                    //same way
                    continue;
                } else if (!hasSpiltCommitF && !hasTargetCommitF && !hasCurrentCommitF) {
                    System.out.println("Wrong");
                }
            }
            if (conflictFlag) {
                System.out.println("Encountered a merge conflict.");
            }
            addStage = (Stage) loadObject(addStageFile, Stage.class);
            removeStage = (Stage) loadObject(removeStageFile, Stage.class);
            if (addStage.isEmpty() && removeStage.isEmpty()) {
                System.out.println("No changes added to the commit.");
                System.exit(0);
            } else {
                addToCommit(addStage, newCommit);//3.According to the stageArea, Modify the childCommit, and move the blobs to blobs
                removeFromCommit(removeStage, newCommit);
                saveAsSHA1(newCommit, commits, 6);//4.Save childCommit
                updateBranch(newCommit, currentBranchName);
                updateHEAD(currentBranchName); // Update Head
                addStage.clearStageTree();//5.Clear stage
                removeStage.clearStageTree();
                saveFile(addStage, addStageFile);//6.Save stage
                saveFile(removeStage, removeStageFile);
                clearStagingAreaBlobs();
            }
        }
    }

    public static void putConflictContent(Blob currentBlob, Blob targetBlob, File file) {
        String writeString = "<<<<<<< HEAD\n";
        if (currentBlob != null) {
            String currentContent = currentBlob.getContentAsString();
            writeString = writeString + currentContent;
        }
        writeString = writeString + "=======\n";
        if (targetBlob != null) {
            String targetContent = targetBlob.getContentAsString();
            writeString = writeString + targetContent;
        }
        writeString = writeString + ">>>>>>>\n";
        writeContents(file, writeString);
    }

    public static Set<String> putCommitFilePathInSet(Commit commit, Set set) {
        Set<String> commitContentSet = commit.contentKeySet();
        for(String name : commitContentSet) {
            set.add(name);
        }
        return set;
    }
    public static String getLatestCommonAncestorName(String commitName1, String commitName2) {
        Set<String> Commit1AncestorNameSet = getCommitAncestorNameSet(commitName1);
        Set<String> Commit2AncestorNameSet = getCommitAncestorNameSet(commitName2);
        Set<String> commonAncestorNameSet = new HashSet<>();
        for (String AncestorName : Commit1AncestorNameSet) {
            if (Commit2AncestorNameSet.contains(AncestorName)) {
                commonAncestorNameSet.add(AncestorName);
            }
        }
        Set<String> commonAncestorParentNameSet = new HashSet<>();
        for (String commonAncestorName : commonAncestorNameSet) {
            Commit commonAncestor = getCommit(commonAncestorName);
            String commonAncestorParentName = commonAncestor.getParent();
            if (commonAncestorParentName != null) {
                commonAncestorParentNameSet.add(commonAncestorParentName);
            }
        }
        for (String commonAncestorName : commonAncestorNameSet) {
            if (!commonAncestorParentNameSet.contains(commonAncestorName)) {
                return commonAncestorName;
            }
        }
        return null;
    }
    public static Set<String> getCommitAncestorNameSetRecursive(String childCommitName, Set<String> resultSet) {
        resultSet.add(childCommitName);
        Commit childCommit = getCommit(childCommitName);
        String parentCommitName = childCommit.getParent();
        if (parentCommitName == null) {
            return resultSet;
        } else if (childCommit instanceof mergedCommit) {
            resultSet = getCommitAncestorNameSetRecursive(parentCommitName, resultSet);
            return getCommitAncestorNameSetRecursive(((mergedCommit) childCommit).getSecondParent(), resultSet);
        } else {
            return getCommitAncestorNameSetRecursive(parentCommitName, resultSet);
        }

    }
    //Includes itself
    public static Set<String> getCommitAncestorNameSet(String commitName) {
        Set<String> nameSet = new HashSet<>();
        getCommitAncestorNameSetRecursive(commitName, nameSet);
        return nameSet;
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
    public static void saveAsSHA1(Serializable obj, File folder, int length) {
        String fileName = objToSHA1(obj);
        if (length == 40) {
            saveAsName(obj, folder, fileName);
        } else if (length == 6) {
            File abbreFolder = join(folder, fileName.substring(0,6));
            if (!abbreFolder.exists()) {
                abbreFolder.mkdir();
            }
            saveAsName(obj, abbreFolder, fileName);
        }

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
    public static Set<File> getCommitSet() {
        File[] commitFolders = commits.listFiles();
        Set<File> commitSet = new HashSet<>();
        for (File CF : commitFolders) {
            File[] coms = CF.listFiles();
            for (File com : coms) {
                commitSet.add(com);
            }
        }
        return commitSet;
    }
    public static void removeFromCommit(Stage removeStage, Commit currentCommit) {
        Set<String> removeStageKeySet = removeStage.stageTreeKeySet();
        for(String path : removeStageKeySet) {
            currentCommit.removeBlob(path);
        }
    }
    public static Commit getCommit(String commitSHA1) {
        File commitFile = null;
        if (commitSHA1.length() == 0) {
            System.out.println("Too short");
            System.exit(0);
            return null;
        } else if (commitSHA1.length() <= 6) {
            File[] folders = commits.listFiles();
            for (File folder : folders) {
                if (commitSHA1.equals(folder.getName().substring(0, commitSHA1.length()))) {
                    File[] files = folder.listFiles();
                    if (files.length == 1) {
                        commitFile = files[0];
                        break;
                    } else {
                        System.out.println("Too short 2");
                        System.exit(0);
                        return null;
                    }
                }
            }
        } else if (commitSHA1.length() <= 40) {
            File[] folders = commits.listFiles();
            for (File folder : folders) {
                if (commitSHA1.substring(0,6).equals(folder.getName())) {
                    File[] files = folder.listFiles();
                    if (files.length == 1) {
                        commitFile = files[0];
                        break;
                    } else {
                        System.out.println("Too short 2");
                        System.exit(0);
                        return null;
                    }
                }
            }
        } else {
            System.out.println("too long");
            System.exit(0);
            return null;
        }
        if (commitFile == null) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
            return null;
        }else {
            Commit returnCommit = (Commit) loadObject(commitFile, Commit.class);
            return returnCommit;
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
        File targetCommitFolder = join(commits, targetCommitSHA1.substring(0,6));
        File targetCommitFile = join(targetCommitFolder, targetCommitSHA1);
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
