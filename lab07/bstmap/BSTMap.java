package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable, V> implements Map61B<K, V> {
    private int size;
    private BSTNode root;

    private class BSTNode {
        private K key;
        private V value;
        private BSTNode left;
        private BSTNode right;

        public BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public V get(K key) {
        return get(key, root);
    }

    private V get(K key, BSTNode cur) {
        if (cur == null) return null;
        int compare = key.compareTo(cur.key);

        if (compare == 0) return cur.value;
        else if (compare > 0) return get(key, cur.right);
        else return get(key, cur.left);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        if (size == 0) {
            root = new BSTNode(key, value);
            size = 1;
        }
        else put(key, value, root);
    }

    private void put(K key, V value, BSTNode cur) {
        int compare = key.compareTo(cur.key);
        if (compare == 0) cur.value = value;
        else if (compare > 0) {
            if (cur.right == null) {
                cur.right = new BSTNode(key, value);
                size++;
            }
            else put(key, value, cur.right);
        }
        else {
            if (cur.left == null) {
                cur.left = new BSTNode(key, value);
                size++;
            }
            else put(key, value, cur.left);
        }
    }

    public void printInOrder() {
        if (size > 0) print(root);
    }

    private void print(BSTNode node) {
        if (node.left != null) print(node.left);
        System.out.print(node.value + " ");
        if (node.right != null) print(node.right);
    }

    @Override
    public Set keySet() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator iterator() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }
}