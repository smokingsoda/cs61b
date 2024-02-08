package gitlet;

import java.io.File;

import java.io.Serializable;

import static gitlet.Utils.sha1;

public class Blob implements Serializable {
    private String path;
    private byte[] content;

    //private String ref;

    public Blob(String path, byte[] content) {
        this.path = path.toLowerCase();
        this.content = content;
    }

    public byte[] getContent() {
        return this.content;
    }

    public String getPath() {
        return this.path;
    }

    public String contentToSHA1() {
        return sha1(content);
    }

}
