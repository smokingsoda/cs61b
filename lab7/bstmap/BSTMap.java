package bstmap;

import edu.princeton.cs.algs4.BST;
import org.junit.Test;

import java.sql.Array;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V>, Iterable<K> {
    private Node root;
    private BSTMap left;
    private BSTMap right;
    private BSTMap[] branches;
    private int size;

    public BSTMap() {
        root = null;
        left = null;
        right = null;
        branches = null;
        size = 0;
    }

    private BSTMap(K key, V value) {
        root = new Node(key, value);
        left = new BSTMap<K, V>();
        right = new BSTMap<K,V>();
        branches = new BSTMap[] {left, right};
        size = 1;
    }
    @Override
    public void clear() {
        root = null;
        left = null;
        right = null;
        branches = null;
        size = 0;
    }

    private BSTMap search(K key) {
        if (root == null) {
            return this;
        } else {
            int cmp = root.key.compareTo(key);
            if (cmp > 0) {
                return left;
            } else if (cmp < 0) {
                return right;
            } else {
                return this;
            }
        }
    }
    private void setMap(BSTMap targetMap, K key, V value) {
        targetMap.root = new Node(key, value);
        targetMap.left = new BSTMap();
        targetMap.right = new BSTMap();
        targetMap.branches = new BSTMap[]{left, right};
        targetMap.size += 1;
    }

    @Override
    public void put(K key, V value) {
        BSTMap targetMap = search(key);
        if (targetMap.root == null) {
            setMap(targetMap, key, value);
        } else if (targetMap.root.key.equals(key)) {
            targetMap.root.value = value;
        } else {
            targetMap.put(key, value);
        }
        sizeCal();
    }

    private void sizeCal() {
        if (root == null) {
            return;
        } else {
            size = 1 + left.size() + right.size();
        }
    }

    @Override
    public boolean containsKey(K key) {
        BSTMap targetMap = search(key);
        if (targetMap.root == null) {
            return false;
        } else if (targetMap.root.key.equals(key)) {
            return true;
        } else {
            return targetMap.containsKey(key);
        }
    }

    @Override
    public V get(K key) {
        BSTMap targetMap = search(key);
        if (targetMap.root == null) {
            return null;
        } else if (targetMap.root.key.equals(key)) {
            return (V) targetMap.root.value;
        } else {
            return (V) targetMap.get(key);
        }
    }

    public int size() {
        return size;
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException("Unsupport");
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException("Unsupport");
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException("Unsupport");
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException("Unsupport");
    }


    public void printInOrder() {
        throw new UnsupportedOperationException("Unsupport");
    }


    private class Node {
        public K key;
        public V value;

        public Node(K k, V v) {
            key = k;
            value = v;
        }
    }
}