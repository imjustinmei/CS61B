package hashmap;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private int size;
    private double loadFactor;
    private Collection<Node>[] buckets;

    public MyHashMap() {
        buckets = createTable(16);
        loadFactor = 0.75;
    }

    public MyHashMap(int initialSize) {
        buckets = createTable(initialSize);
        loadFactor = 0.75;
    }

    public MyHashMap(int initialSize, double maxLoad) {
        buckets = createTable(initialSize);
        loadFactor = maxLoad;
    }

    private int toHash(K key) {
        return Math.floorMod(key.hashCode(), buckets.length);
    }

    public void clear() {
        size = 0;
        buckets = createTable(buckets.length);
    }

    public boolean containsKey(K key) {
        return find(key) != null;
    }

    public V get(K key) {
        if (containsKey(key)) return find(key).value;
        return null;
    }

    private Node find(K key) {
        for (Node node : buckets[toHash(key)]) {
            if (node.key.equals(key)) return node;
        }
        return null;
    }

    public int size() {
        return size;
    }

    public void put(K key, V value) {
        Node n = find(key);
        if (n != null) {
            if (n.value != value) n.value = value;
            return;
        }
        size++;
        if ((double) size / buckets.length > loadFactor) {
            Collection<Node>[] temp = buckets;
            buckets = createTable(buckets.length * 2);
            for (Collection<Node> bucket : temp) {
                for (Node node : bucket) {
                    buckets[toHash(node.key)].add(node);
                }
            }
        }
        buckets[toHash(key)].add(new Node(key, value));
    }

    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] table = new Collection[tableSize];
        for (int i = 0; i < tableSize; i++) {
            table[i] = createBucket();
        }
        return table;
    }

    public Iterator<K> iterator() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public Set<K> keySet() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public V remove(K key) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    public V remove(K key, V value) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}
