package bstmap;

import edu.princeton.cs.algs4.BST;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable, V> implements Map61B<K, V> {
    private int size;
    private BSTNode root;

    private class BSTNode<K extends Comparable, V> {
        public V nodeValue;
        public K nodeKey;
        public BSTNode left;
        public BSTNode right;
        private BSTNode nodeParent;
        public int children;


        public BSTNode(K key, V value, BSTNode parent) {
            nodeKey = key;
            nodeValue = value;
            children = 0;
            nodeParent = parent;
        }

        public boolean isLeaf() {
            return (children == 0);
        }

        public boolean isLeftChild() {
            if (nodeParent.left == null) {
                return false;
            }
            return nodeParent.left.equals(this);
        }

        public boolean isRightChild() {
            if (nodeParent.right == null) {
                return false;
            }
            return nodeParent.right.equals(this);
        }

        @Override
        public String toString() {
            return "[key: " + nodeKey + ", value: " + nodeValue + "]";
        }

        private boolean toLeft(K key) {
            return nodeKey.compareTo(key) > 0;
        }

        private boolean toRight(K key) {
            return nodeKey.compareTo(key) < 0;
        }

        public boolean containsKey(K key) {
            if (nodeKey.equals(key)) {
                return true;
            } else if (toLeft(key)) {
                if (left == null) {
                    return false;
                } else {
                    return left.containsKey(key);
                }
            } else if (toRight(key)) {
                if (right == null) {
                    return false;
                } else {
                    return right.containsKey(key);
                }
            }
            return false;
        }

        //Put the node that will belong to its parent.
        //Compare its value to its parent's to decide where to put it.
        public void put(K key, V value) {
            if (toLeft(key)) {
                if (left == null) {
                    left = new BSTNode(key, value, this);
                    children += 1;
                } else {
                    left.put(key, value);
                }
            } else if (toRight(key)) {
                if (right == null) {
                    right = new BSTNode(key, value, this);
                    children += 1;
                } else {
                    right.put(key, value);
                }
            } else if (nodeKey.compareTo(key) == 0) {
                nodeValue = value;
            }
        }

        public BSTNode get(K key) {
            BSTNode targetNode = null;
            if (nodeKey.equals(key)) {
                targetNode = this;
            } else if (toLeft(key)) {
                if (left == null) {
                    targetNode = null;
                } else {
                    targetNode = left.get(key);
                }
            } else if (toRight(key)) {
                if (right == null) {
                    targetNode = null;
                } else {
                    targetNode = right.get(key);
                }
            }
            return targetNode;
        }

        private BSTNode goDeepLeft() {
            if (left == null) {
                return this;
            } else {
                return left.goDeepLeft();
            }
        }

        public BSTNode getSuccessor() {
            if (right == null) {
                return null;
            } else {
                return right.goDeepLeft();
            }
        }

    }

        public BSTMap() {
            size = 0;
            root = null;
        }

        @Override
        public void clear() {
            root = null;
            size = 0;
        }

        @Override
        public boolean containsKey(K key) {
            if (size() == 0) {
                return false;
            }
            return root.containsKey(key);
        }

        @Override
        public V get(K key) {
            if (size() == 0) {
                return null;
            }
            BSTNode returnNode = root.get(key);
            if (returnNode == null) {
                return null;
            } else {
                return (V)returnNode.nodeValue;
            }
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public void put(K key, V value) {
            if (root == null) {
                root = new BSTNode<K, V>(key, value, null);
            } else {
                root.put(key, value);
            }
            size += 1;
        }

        @Override
        public Set<K> keySet() {
            throw new UnsupportedOperationException("This operation is not supported.");
        }

        @Override
        public V remove(K key) {
            if (root == null) {
                return null;
            }
            else {
                BSTNode targetNode = root.get(key);
                BSTNode returnNode = targetNode;
                if (targetNode == null) {
                    return null;
                }
                else if (targetNode.children == 0) {
                    if (targetNode.equals(root)) {
                        root = null;
                    } else {
                        remove0(targetNode);
                    }
                } else if (targetNode.children == 1) {
                    if (targetNode.equals(root)) {
                        if (targetNode.left != null) {
                            this.root = targetNode.left;
                            targetNode.left.nodeParent = null;
                        } else if (targetNode.right != null) {
                            this.root = targetNode.right;
                            targetNode.right.nodeParent = null;
                        }
                        cleanNode(targetNode);
                    } else {
                        remove1(targetNode);
                    }
                } else if (targetNode.children == 2) {
                    BSTNode Successor = targetNode.getSuccessor();
                    exchangeNode(Successor, targetNode);
                    if (Successor.children == 0) {
                        remove0(Successor);
                    } else if (Successor.children == 1) {
                        remove1(Successor);
                    }
                }
                size -= 1;
                return (V) returnNode.nodeValue;
            }
        }

        private void remove0(BSTNode targetNode) {
            BSTNode parentNode = targetNode.nodeParent;
            if (targetNode.isLeftChild()) {
                parentNode.left = null;
            } else if (targetNode.isRightChild()) {
                parentNode.right = null;
            }
            parentNode.children -= 1;
            cleanNode(targetNode);
        }

        private void remove1(BSTNode targetNode) {
            BSTNode parentNode = targetNode.nodeParent;
            BSTNode childNode;
            if (targetNode.left == null) {
                childNode = targetNode.right;
            } else {
                childNode = targetNode.left;
            }
            if (targetNode.isLeftChild()) {
                parentNode.left = childNode;
            } else if (targetNode.isRightChild()) {
                parentNode.right = childNode;
            }
            parentNode.children -= 1;
            cleanNode(targetNode);
        }


        private void cleanNode(BSTNode targetNode) {
        targetNode = null;
        }



    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException("This operation is not supported.");
    }

    public void printInOrder() {
        throw new UnsupportedOperationException("Not Support yet");
    }

    private void exchangeNode(BSTNode node1, BSTNode node2) {
        Object midKey = node1.nodeKey;
        Object midValue = node1.nodeValue;
        node1.nodeKey = node2.nodeKey;
        node1.nodeValue = node2.nodeValue;
        node2.nodeKey = (K) midKey;
        node2.nodeValue = (V) midValue;
    }
    @Override
    public Iterator<K> iterator() {
        return new BSTMapIterator<K>();
    }

    private class BSTMapIterator<K> implements Iterator<K> {
        @Override
        public boolean hasNext() {
            return false;
        }

        public K next() {
            return null;
        }
    }
}
