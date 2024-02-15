package gitlet;

import java.util.Date;
import java.util.TreeMap;

public class mergedCommit extends Commit{
    protected String secondParent;

    public mergedCommit(Commit parent, Commit secondParent, Date timeStamp, TreeMap<String, String> content) {
        this.parent = MainHelper.objToSHA1(parent);
        this.secondParent = MainHelper.objToSHA1(secondParent);
        this.timeStamp = timeStamp;
        this.content = content;
        this.message = String.format("Merged %s into %s", this.parent, this.secondParent);
    }

}
