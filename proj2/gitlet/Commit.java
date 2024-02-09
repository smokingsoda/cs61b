package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.Set;
import java.util.TreeMap;

import static gitlet.Utils.*;


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
    private String timeStamp;
    private TreeMap<String, String> content;

    /* TODO: fill in the rest of this class. */
    public Commit(String message, Commit parent, Date timeStamp, TreeMap<String, String> content) {
        this.message = message;
        this.parent = MainHelper.objToSHA1(parent);
        this.timeStamp = MainHelper.dateToString(timeStamp);
        this.content = content;
    }

    public Commit() {
        message = "initial commit";
        parent = null;
        timeStamp = MainHelper.dateToString(new Date(0));
        content = new TreeMap<>();
    }

    public TreeMap getContent() {
        return content;
    }

    public boolean containsBlob(String blob) {
        return content.containsKey(blob);
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

}
