package gitlet;

import java.io.Serializable;

public class Blob implements Serializable {
    private byte[] content;
    //private String ref;

    public Blob(byte[] content) {
        this.content = content;
    }

    public byte[] getContent() {
        return this.content;
    }

}
