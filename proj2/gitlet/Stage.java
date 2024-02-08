package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.Set;
import java.util.TreeMap;

public class Stage implements Serializable {
    private TreeMap<String, String> stageTree; //key: absolute path(Lower Case), value: content(as SHA1)

    public Stage() {
        stageTree = new TreeMap<String, String>();
    }

    public boolean containsFile(String path) {
        return stageTree.containsKey(path.toLowerCase());
    }

    public String getFileSHA1(String path) {
        return stageTree.get(path.toLowerCase());
    }

    public void putFile(String path, String blobSHA1) {
        stageTree.put(path.toLowerCase(), blobSHA1);
    }

    public String removeFile(String path) {
        return stageTree.remove(path.toLowerCase());
    }
    public void printAllKey() {
        Set S = stageTree.keySet();
        System.out.println(S);
    }

}
