package gitlet;

import java.util.Date;
import java.util.TimeZone;
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

    @Override
    public String toString() {
        Commit.sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String returnString = "Merge" + parent.substring(0, 6) + secondParent.substring(0, 6) + "\n";
        returnString = returnString + "Date: " + Commit.sdf.format(timeStamp);
        returnString = returnString + "\n" + message;
        return returnString;
    }
}
