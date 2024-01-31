package bstmap;

import edu.princeton.cs.algs4.BST;
import org.junit.Test;

import java.sql.Array;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V>, Iterable<K> {
    private Node root;
    private int size;

    public BSTMap() {
        root = null;
        size = 0;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    private Node put(Node parent, Node child, K key, V value) {
        if (child == null) {
            child = new Node(key, value, null, null, parent);
            size += 1;
        } else {
            int cmp = child.key.compareTo(key);
            if (cmp > 0) {
                child.left = put(child, child.left, key, value);
            } else if (cmp < 0) {
                child.right = put(child, child.right, key, value);
            } else {
                child.value = value;
            }
        }
        return child;
    }

    @Override
    public void put(K key, V value) {
        root = put(null, root, key, value);
    }

    private boolean containsKey(Node node, K key) {
        if (node == null) {
            return false;
        } else {
            int cmp = node.key.compareTo(key);
            if (cmp > 0) {
                return containsKey(node.left, key);
            } else if (cmp < 0) {
                return containsKey(node.right, key);
            } else {
                return true;
            }
        }
    }
    @Override
    public boolean containsKey(K key) {
        return containsKey(root, key);
    }

    private V get(Node node, K key) {
        if (node == null) {
            return null;
        } else {
            int cmp = node.key.compareTo(key);
            if (cmp > 0) {
                return get(node.left, key);
            } else if (cmp < 0) {
                return get(node.right, key);
            } else {
                return node.value;
            }
        }
    }
    @Override
    public V get(K key) {
        return get(root, key);
    }

    public int size() {
        return size;
    }


    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException("Unsupport");
    }


    private V remove(Node node, K key) {
        if (node == null) {
            return null;
        } else {
            int cmp = node.key.compareTo(key);
            {
                if (cmp > 0) {
                    return remove(node.left, key);
                } else if (cmp < 0) {
                    return remove(node.right, key);
                } else {
                    size -= 1;
                    return removeThisNode(node);
                }
            }
        }
    }

    private V removeThisNode(Node node) {
        V returnValue = node.value;
        int children = node.children();
        if (children == 0) {
            remove0Children(node);
        } else if (children == 1) {
            remove1Children(node);
        } else {
            remove3Children(node);
        }
        return returnValue;
    }
    private void remove0Children(Node node) {
        Node parentNode = node.parent;
        if (parentNode == null) {
            root = null;
            return;
        }
        int cmp = parentNode.whichSide(node);
        if (cmp < 0) {
            parentNode.left = null;
        }
        else if (cmp > 0) {
            parentNode.right = null;
        } else {
            throw new UnsupportedOperationException("Wrong!");
        }
    }

    private void remove1Children(Node node) {
        Node parentNode = node.parent;
        Node childOfNode = node.getTheOnlyChild();
        if (parentNode == null) {
            root = childOfNode;
            root.parent = null;
            return;
        }
        int cmp = parentNode.whichSide(node);
        if (cmp < 0) {
            parentNode.left = childOfNode;
            childOfNode.parent = parentNode;
        } else if (cmp > 0) {
            parentNode.right = childOfNode;
            childOfNode.parent = parentNode;
        } else {
            throw new UnsupportedOperationException("Trying to remove a child which doesn't belong to its parent!");
        }
    }

    private void remove3Children(Node node) {
        Node successor = getSuccessor(node);
        exchangeNode(node, successor);
        removeThisNode(successor);
    }

    private Node getSuccessor(Node node) {
        if (node.right == null) {
            throw new UnsupportedOperationException("Trying to get a wrong successor!");
        } else {
            return goDeepLeft(node.right);
        }
    }

    private Node goDeepLeft(Node node) {
        if (node.left == null) {
            return node;
        } else {
            return goDeepLeft(node.left);
        }
    }
    private void exchangeNode(Node O1, Node O2) {
        K midKey = O1.key;
        V midValue = O1.value;
        O1.key = O2.key;
        O1.value = O2.value;
        O2.key = midKey;
        O2.value = midValue;
    }
    @Override
    public V remove(K key) {
        return remove(root, key);
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
        public Node left, right, parent;

        public Node() {
            key = null;
            value = null;
            left = null;
            right = null;
            parent = null;
        }

        public Node(K k, V v,Node l, Node r, Node p) {
            key = k;
            value = v;
            left = l;
            right = r;
            parent = p;
        }

        public int children() {
            int count = 0;
            if (left != null) {
                count += 1;
            } if (right != null) {
                count += 1;
            }
            return count;
        }

        //Return 0 if is not children; return negative number if is left; return positive number if is right.
        public int whichSide(Node node) {
            int cmp = 0;
            if (left != null && left.equals(node)) {
                cmp -= 1;
            } if (right != null && right.equals(node)) {
                cmp += 1;
            }
            return cmp;
        }

        public Node getTheOnlyChild() {
            if (children() != 1){
                throw new UnsupportedOperationException("Try to get the not only child");
            } else {
                if (left != null) {
                    return left;
                } else {
                    return right;
                }
            }
        }

    }
}