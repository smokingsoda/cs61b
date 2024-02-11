package gitlet;

// TODO: any imports you need here

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.io.File;
import java.util.*;

import static gitlet.Utils.join;
import static gitlet.Utils.writeContents;


/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author Junru Wang
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    /** The parent of current commit, representing by SHA-1 code. */
    private String parent;
    /** Time data*/
    private Date timeStamp;
    private TreeMap<String, String> content;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy Z", Locale.US);

    /* TODO: fill in the rest of this class. */
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

    public boolean containsBlob(String path) {
        return content.containsKey(path.toLowerCase());
    }

    public void putBlob(String path, String blob) {
        content.put(path.toLowerCase(), blob);
    }

    public String getBlob(String path) {
        return content.get(path.toLowerCase());
    }

    public String removeBlob(String path) {
        return content.remove(path.toLowerCase());
    }

    public Commit createChildCommit(String message, Date timeStamp) {
        TreeMap<String, String> childContent = new TreeMap<>();
        Set<String> parentContentSet = content.keySet();
        for(String path : parentContentSet) {
            childContent.put(path, content.get(path));
        }//Copy the content
        return new Commit(message, this, timeStamp, childContent);
    }

    public String getParent() {
        return parent;
    }

    public void retrieveFile(String path) {
        String retrieveBlobName = getBlob(path.toLowerCase());
        if (retrieveBlobName == null) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
            return;
        }
        File retrieveBlobFile = join(MainHelper.blobs, retrieveBlobName);
        Blob retrieveBlob = (Blob) MainHelper.loadObject(retrieveBlobFile, Blob.class);
        File retrieveFile = new File(path);
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

    public String getBlobContentSHA1(String path) {
        if (containsBlob(path)) {
            String blobName = getBlob(path);
            File blobFile = join(MainHelper.blobs, blobName);
            Blob targetBlob = (Blob) MainHelper.loadObject(blobFile, Blob.class);
            return targetBlob.contentToSHA1();
        } else {
            return null;
        }
    }
}
