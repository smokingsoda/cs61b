package gitlet;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

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
        return content;
    }

    public String getPath() {
        return path;
    }

    public String contentToSHA1() {
        return sha1(content);
    }

    public String getContentAsString() {
        return new String(getContent(), StandardCharsets.UTF_8);

    }
}