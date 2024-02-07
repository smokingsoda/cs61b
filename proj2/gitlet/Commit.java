package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date; // TODO: You'll likely use this in this class

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

    private String[] content;

    /* TODO: fill in the rest of this class. */
    public Commit(String message, Commit parent, Date timeStamp, Object[] content) {
        this.message = message;
        if (parent == null) {
            this.parent = null;
        } else {
            this.parent = sha1(parent);
        }
        String pattern = "MM/dd/yyyy HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        this.timeStamp = df.format(timeStamp);
        int length = content.length;
        this.content = new String[length];
        for (int i = 0; i < length; i++) {
            this.content[i] = sha1(content[i]);
        }
    }

    public void storeCommit(File CommitFolder) {
        String selfSHA1 = sha1(this);
        File commitFile = join(CommitFolder, selfSHA1);
        writeObject(commitFile, this);
    }
}
