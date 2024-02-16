package gitlet;



import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.io.File;
import java.util.*;

import static gitlet.Utils.join;
import static gitlet.Utils.writeContents;


/** Represents a gitlet commit object.
 *
 *  does at a high level.
 *
 *  @author Junru Wang
 */
public class Commit implements Serializable {
    /**
     *
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    protected String message;
    /** The parent of current commit, representing by SHA-1 code. */
    protected String parent;
    /** Time data*/
    protected Date timeStamp;
    protected TreeMap<String, String> content;
    protected static final SimpleDateFormat sdf
            = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy Z", Locale.US);

    public Commit(String message, Commit parent, Date timeStamp, TreeMap<String, String> content) {
        this.message = message;
        this.parent = MainHelper.objToSHA1(parent);
        this.timeStamp = timeStamp;
        this.content = content;
    }

    public Commit() {
        message = "initial commit";
        parent = null;
        timeStamp = new Date(0);
        content = new TreeMap<>();
    }

    public TreeMap getContent() {
        return content;
    }

    public boolean containsBlob(String name) {
        return content.containsKey(name);
    }

    public void putBlob(String name, String blob) {
        content.put(name, blob);
    }

    public String getBlob(String name) {
        return content.get(name);
    }

    public String removeBlob(String name) {
        return content.remove(name);
    }

    public Commit createChildCommit(String message, Date timeStamp) {
        TreeMap<String, String> childContent = new TreeMap<>();
        Set<String> parentContentSet = content.keySet();
        for (String name : parentContentSet) {
            childContent.put(name, content.get(name));
        } //Copy the content
        return new Commit(message, this, timeStamp, childContent);
    }

    public String getParent() {
        return parent;
    }

    public void retrieveFile(String fileName) {
        String retrieveBlobName = getBlob(fileName);
        if (retrieveBlobName == null) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
            return;
        }
        File retrieveBlobFile = join(MainHelper.blobs, retrieveBlobName);
        Blob retrieveBlob = (Blob) MainHelper.loadObject(retrieveBlobFile, Blob.class);
        File retrieveFile = join(MainHelper.CWD, fileName);
        byte[] retrieveFileContent = retrieveBlob.getContent();
        writeContents(retrieveFile, retrieveFileContent);
    }

    public Set<String> contentKeySet() {
        return content.keySet();
    }

    @Override
    public String toString() {
        Commit.sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String returnString = "Date: " + Commit.sdf.format(timeStamp);
        returnString = returnString + "\n" + message;
        return returnString;
    }

    public String getBlobContentSHA1(String fileName) {
        if (containsBlob(fileName)) {
            String blobName = getBlob(fileName);
            File blobFile = join(MainHelper.blobs, blobName);
            Blob targetBlob = (Blob) MainHelper.loadObject(blobFile, Blob.class);
            return targetBlob.contentToSHA1();
        } else {
            return null;
        }
    }

    public String getMessage() {
        return message;
    }

    public mergedCommit createMergedChildCommit(Commit otherCommit, Date timeStamp, String firstBranchName, String secondBranchName) {
        TreeMap<String, String> childContent = new TreeMap<>();
        Set<String> parentContentSet = content.keySet();
        for(String name : parentContentSet) {
            childContent.put(name, content.get(name));
        }//Copy the content
        String message = String.format("Merged %s into %s.", firstBranchName, secondBranchName);
        return new mergedCommit(message, this, otherCommit, timeStamp, childContent);
    }
}
