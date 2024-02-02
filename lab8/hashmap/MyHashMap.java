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

    private int tableSize = initialSize;

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
        return new Collection[initialSize];
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

    @Override
    public void clear() {
        buckets = createTable(initialSize);
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        int index = bucketIndex(key.hashCode());
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
    public int size() {
        return size;
    }

    @Override
    public V get(K key) {
        int index = bucketIndex(key.hashCode());
        Collection targetMap = buckets[index];
        if (targetMap == null) {
            return null;
        }
        Iterator newIterator = targetMap.iterator();
        Node element;
        while (newIterator.hasNext()) {
            element = (Node)newIterator.next();
            if (key.equals(element.key)) {
                return element.value;
            }
        }
        return null;
    }

    @Override
    public void put(K key, V value) {
        int index = bucketIndex(key.hashCode());
        Collection targetMap = buckets[index];
        if (targetMap == null) {
            buckets[index] = createBucket();
            Node newNode = createNode(key, value);
            buckets[index].add(newNode);
            size += 1;
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
        size += 1;
    }

    private int bucketIndex(int HashCode) {
        return Math.floorMod(HashCode, tableSize);
    }

    @Override
    public V remove(K key) {
        return null;
    }

    @Override
    public V remove(K key, V value) {
        return null;
    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return null;
    }
}
