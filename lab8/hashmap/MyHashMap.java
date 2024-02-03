package hashmap;

import org.w3c.dom.Node;

import java.util.*;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }
    private double loadFactor = 0.75;

    private int initialSize = 16;

    private int size;

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        buckets = createTable(initialSize);
        size = 0;
    }

    public MyHashMap(int initialSize) {
        this.initialSize = initialSize;
        buckets = createTable(this.initialSize);
        size = 0;
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        this.initialSize = initialSize;
        this.loadFactor = maxLoad;
        buckets = createTable(this.initialSize);
        size = 0;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<Node>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return new Collection[tableSize];
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

    @Override
    public void clear() {
        buckets = createTable(initialSize);
        size = 0;
    }

    private boolean containsKey(K key, Collection[] buckets) {
        int index = bucketIndex(key.hashCode(), buckets.length);
        Collection targetMap = buckets[index];
        if (targetMap == null) {
            return false;
        }
        Iterator newIterator = targetMap.iterator();
        Node element;
        while (newIterator.hasNext()) {
            element = (Node)newIterator.next();
            if (key.equals(element.key)) {
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean containsKey(K key) {
        return containsKey(key, this.buckets);
    }

    @Override
    public int size() {
        return size;
    }

    private V get(K key, Collection[] buckets) {
        Node targetNode = getNode(key, buckets);
        if (targetNode == null) {
            return null;
        } else {
            return targetNode.value;
        }
    }
    private Node getNode(K key, Collection[] buckets) {
        int index = bucketIndex(key.hashCode(), buckets.length);
        Collection targetMap = buckets[index];
        if (targetMap == null) {
            return null;
        }
        Iterator newIterator = targetMap.iterator();
        Node element;
        while (newIterator.hasNext()) {
            element = (Node)newIterator.next();
            if (key.equals(element.key)) {
                return element;
            }
        }
        return null;
    }
    @Override
    public V get(K key) {
        return get(key, this.buckets);
    }

    private void resize(Collection[] buckets) {
        Collection[] newBuckets = createTable(buckets.length * 2);
        MyHashMapIterator nodeIterator = new MyHashMapIterator();
        while (nodeIterator.hasNext()) {
            Node putNode = nodeIterator.nextNode();
            putNewBucket(putNode.key, putNode.value, newBuckets);
        }
        this.buckets = newBuckets;
    }

    private void putNewBucket(K key, V value, Collection[] buckets) {
        int index = bucketIndex(key.hashCode(), buckets.length);
        Collection targetMap = buckets[index];
        if (targetMap == null) {
            buckets[index] = createBucket();
            Node newNode = createNode(key, value);
            buckets[index].add(newNode);
            return;
        }
        Iterator newIterator = targetMap.iterator();
        Node element;
        while(newIterator.hasNext()) {
            element = (Node) newIterator.next();
            if (key.equals(element.key)) {
                element.value = value;
                return;
            }
        }
        Node newNode = createNode(key, value);
        targetMap.add(newNode);
    }

    @Override
    public void put(K key, V value) {
        put(key, value, this.buckets);
    }

    private void put(K key, V value, Collection[] buckets) {
        int index = bucketIndex(key.hashCode(), buckets.length);
        Collection targetMap = buckets[index];
        if (targetMap == null) {
            buckets[index] = createBucket();
            Node newNode = createNode(key, value);
            buckets[index].add(newNode);
        } else {
            Iterator newIterator = targetMap.iterator();
            Node element;
            while (newIterator.hasNext()) {
                element = (Node) newIterator.next();
                if (key.equals(element.key)) {
                    element.value = value;
                    return;
                }
            }
            Node newNode = createNode(key, value);
            targetMap.add(newNode);
        }
        size += 1;
        if ((size) / buckets.length >= loadFactor) {
            resize(this.buckets);
        }
    }

    private int bucketIndex(int HashCode, int tableSize) {
        return Math.floorMod(HashCode, tableSize);
    }

    private V remove(K key, Collection[] buckets) {
        int removeIndex = bucketIndex(key.hashCode(), buckets.length);
        Collection targetBucket = buckets[removeIndex];
        Node returnNode = null;
        if (containsKey(key)) {
            returnNode = getNode(key, buckets);
            if (targetBucket.remove(returnNode)) {
                size -= 1;
                return returnNode.value;
            }
        }
        return null;
    }
    @Override
    public V remove(K key) {
        return remove(key, buckets);
    }

    @Override
    public V remove(K key, V value) {
        return null;
    }

    @Override
    public Set<K> keySet() {
        Iterator MyHashMapIterator = iterator();
        HashSet returnSet = new HashSet<>();
        for (K key : this) {
            returnSet.add(key);
        }
        return returnSet;
    }

    @Override
    public Iterator<K> iterator() {
        return new MyHashMapIterator();
    }

    private class MyHashMapIterator implements Iterator {

        private Collection nowBucket;

        private Iterator nowIterator;

        private int index;

        private int count;

        public MyHashMapIterator() {
            index = 0;
            count = 0;
            nowBucket = getBucket();
            nowIterator = getIterator(nowBucket);
        }

        private Iterator getIterator(Collection nowBucket) {
            if (nowBucket == null) {
                return null;
            } else {
                return nowBucket.iterator();
            }
        }

        private Collection getBucket() {
            while(index < buckets.length) {
                if (buckets[index] != null) {
                    Collection returnBucket = buckets[index];
                    index += 1;
                    return returnBucket;
                } else {
                    index += 1;
                }
            }
            return null;
        }
        @Override
        public boolean hasNext() {
            return count < size();
        }

        @Override
        public K next() {
            Node returnNode = nextNode();
            return returnNode.key;
        }

        public Node nextNode() {
            if (nowIterator.hasNext() == false) {
                nowBucket = getBucket();
                nowIterator = getIterator(nowBucket);
            }
            Node returnNode = (Node) nowIterator.next();
            count += 1;
            return returnNode;
        }
    }
}
