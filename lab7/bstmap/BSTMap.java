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
        right = new BSTMap<K, V>();
        branches = new BSTMap[]{left, right};
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

    private BSTMap[] search(BSTMap Parent, K key) {
        BSTMap[] ChildAndParent = new BSTMap[]{null, null};
        if (root == null) {
            ChildAndParent[0] = this;
            ChildAndParent[1] = null;
        } else {
            int cmp = root.key.compareTo(key);
            if (cmp > 0) {
                ChildAndParent[0] = left;
                ChildAndParent[1] = this;
            } else if (cmp < 0) {
                ChildAndParent[0] = left;
                ChildAndParent[1] = this;
            } else {
                ChildAndParent[0] = this;
                ChildAndParent[1] = Parent;
            }
        }
        return ChildAndParent;
    }

    private void setMap(BSTMap targetMap, K key, V value) {
        targetMap.root = new Node(key, value);
        targetMap.left = new BSTMap();
        targetMap.right = new BSTMap();
        targetMap.branches = new BSTMap[]{targetMap.left, targetMap.right};
        targetMap.size += 1;
    }

    private void setMap(BSTMap targetMap, K key, V value, BSTMap left, BSTMap right) {
        targetMap.root = new Node(key, value);
        targetMap.left = left;
        targetMap.right = right;
        targetMap.branches = new BSTMap[]{targetMap.left, targetMap.right};
        targetMap.size = 1;
    }

    @Override
    public void put(K key, V value) {
        BSTMap targetMap[] = search(this, key);
        if (targetMap[0].root == null) {
            setMap(targetMap[0], key, value);
        } else if (targetMap[0].root.key.equals(key)) {
            targetMap[0].root.value = value;
        } else {
            targetMap[0].put(key, value);
        }
        sizeCal();
    }

    private void sizeCal() {
        if (root == null) {
            size = 0;
            return;
        } else {
            size = 1 + left.size() + right.size();
        }
    }

    @Override
    public boolean containsKey(K key) {
        BSTMap targetMap[] = search(this, key);
        if (targetMap[0].root == null) {
            return false;
        } else if (targetMap[0].root.key.equals(key)) {
            return true;
        } else {
            return targetMap[0].containsKey(key);
        }
    }

    @Override
    public V get(K key) {
        BSTMap targetMap[] = search(this, key);
        if (targetMap[0].root == null) {
            return null;
        } else if (targetMap[0].root.key.equals(key)) {
            return (V) targetMap[0].root.value;
        } else {
            return (V) targetMap[0].get(key);
        }
    }

    public int size() {
        return size;
    }

    private int validBranches() {
        int count = -1;
        if (root == null) {
            return count;
        } else {
            count += 1;
        }
        if (left.root != null) {
            count += 1;
        }
        if (right.root != null) {
            count += 1;
        }
        return count;
    }

    private boolean isLeaf() {
        return validBranches() == 0;
    }

    private boolean isValid() {
        return root != null;
    }

    private boolean hasLeftLeaf() {
        if (isValid() && left.root != null) {
            return true;
        }
        return false;
    }

    private boolean hasRightLeaf() {
        if (isValid() && right.root != null) {
            return true;
        }
        return false;
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException("Unsupport");
    }

    private boolean isLeft(BSTMap Child) {
        return left.equals(Child);
    }

    private boolean isRight(BSTMap Child) {
        return right.equals(Child);
    }

    private BSTMap getSuccessor() {
        if (!right.left.isValid()) {
            return right;
        }
        else return right.goDeepLeft();
    }

    private BSTMap goDeepLeft() {
        if (!left.isValid()) {
            return this;
        } else {
            return left.goDeepLeft();
        }
    }

    private void exchangeNode(BSTMap otherMap) {
        K midKey = (K) otherMap.root.key;
        V midValue = (V) otherMap.root.value;
        otherMap.root.key = root.key;
        otherMap.root.value = root.value;
        root.key = midKey;
        root.value = midValue;
    }

    private void resize() {
        if (!isValid()) {
            size = 0;
        } else {
            left.resize();
            right.resize();
            size = 1 + left.size() + right.size();
        }
    }

    private void removeHelper(BSTMap parent, BSTMap child, K key) {
        if (!hasLeftLeaf() && !hasRightLeaf()) {
            child.clear();
        } else if (!hasLeftLeaf() && hasRightLeaf()) {
            if (parent == null) {
                setMap(child, (K)child.right.root.key, (V)child.right.root.value, child.right.left, child.right.right);
            }
            else if (parent.isLeft(child)) {
                parent.left = child.right;
            } else {
                parent.right = child.right;
            }
        } else if (hasLeftLeaf() && !hasRightLeaf()) {
            if (parent == null) {
                setMap(child, (K)child.left.root.key, (V)child.left.root.value, child.left.left, child.left.right);
                child.size -= 1;
            }
            else if (parent.isLeft(child)) {
                parent.left = child.left;
            } else {
                parent.right = child.left;
            }
        } else {
            BSTMap successor = child.getSuccessor();
            BSTMap successorParent = child.search(child, key)[1];
            child.exchangeNode(successor);
            successor.removeHelper(successorParent, successor, key);
        }
    }
    @Override
    public V remove(K key) {
        BSTMap[] targetMap = search(null, key);
        if (targetMap[0].root == null) {
            return null;
        }
        else if (targetMap[0].root.key.equals(key)) {
            V returnValue = (V) targetMap[0].root.value;
            removeHelper(targetMap[1], targetMap[0], key);
            sizeCal();
            return returnValue;
        }
        else {
            V returnValue = (V) targetMap[0].remove(key);
            sizeCal();
            return returnValue;
        }
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