package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.Set;
import java.util.TreeMap;

import static gitlet.Utils.join;

public class Stage implements Serializable {
    private TreeMap<String, String> stageTree; //key: absolute path(Lower Case), value: content(as SHA1)

    public Stage() {
        stageTree = new TreeMap<String, String>();
    }

    public boolean containsFile(String path) {
        return stageTree.containsKey(path.toLowerCase());
    }

    public boolean isEmpty() {
        return stageTree.size() == 0;
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

    public Set stageTreeKeySet() {
        return stageTree.keySet();
    }

    public void clearStageTree() {
        stageTree.clear();
    }

    public String getBlobContentSHA1(String path) {
        if (containsFile(path)) {
            String blobName = getFileSHA1(path);
            File blobFile = join(MainHelper.blobs, blobName);
            Blob targetBlob = (Blob) MainHelper.loadObject(blobFile, Blob.class);
            return targetBlob.contentToSHA1();
        } else {
            return null;
        }
    }

}
