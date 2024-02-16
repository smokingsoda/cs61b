package gitlet;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

import static gitlet.Utils.sha1;

public class Blob implements Serializable {
    private String name;
    private byte[] content;

    //private String ref;

    public Blob(String name, byte[] content) {
        this.name = name.toLowerCase();
        this.content = content;
    }

    public byte[] getContent() {
        return content;
    }

    public String getName() {
        return name;
    }

    public String contentToSHA1() {
        return sha1(content);
    }

    public String getContentAsString() {
        return new String(getContent(), StandardCharsets.UTF_8);

    }
}
