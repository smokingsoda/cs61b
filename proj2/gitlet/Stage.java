package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.Set;
import java.util.TreeMap;

import static gitlet.Utils.join;

public class Stage implements Serializable {
    private TreeMap<String, String> stageTree; //key: absolute path(Lower Case), value: content(as SHA1)

    public Stage() {
        stageTree = new TreeMap<>();
    }

    public boolean containsFile(String name) {
        return stageTree.containsKey(name);
    }

    public boolean isEmpty() {
        return stageTree.size() == 0;
    }

    public String getFileSHA1(String name) {
        return stageTree.get(name);
    }

    public void putFile(String path, String blobSHA1) {
        stageTree.put(path, blobSHA1);
    }

    public String removeFile(String name) {
        return stageTree.remove(name);
    }

    public Set stageTreeKeySet() {
        return stageTree.keySet();
    }

    public void clearStageTree() {
        stageTree.clear();
    }

    public String getBlobContentSHA1(String name) {
        if (containsFile(name)) {
            String blobName = getFileSHA1(name);
            File blobFile = join(MainHelper.stagingAreaBlobs, blobName);
            Blob targetBlob = (Blob) MainHelper.loadObject(blobFile, Blob.class);
            return targetBlob.contentToSHA1();
        } else {
            return null;
        }
    }

}
