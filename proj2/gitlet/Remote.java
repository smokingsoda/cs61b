package gitlet;

import java.io.Serializable;
import java.util.TreeMap;

public class Remote implements Serializable {

    private TreeMap<String, String> remoteMap;

    public Remote() {
        remoteMap = new TreeMap<>();
    }

    public boolean containsRemoteRepo(String repoName) {
        return remoteMap.containsKey(repoName);
    }

    public void putRemoteRepo(String repoName, String repoAbsolutePath) {
        remoteMap.put(repoName, repoAbsolutePath);
    }

    public void removeRemoteRepo(String repoName) {
        remoteMap.remove(repoName);
    }

    public String getRemoteRepoPath(String repoName) {
        return remoteMap.get(repoName);

    }}
