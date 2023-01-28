package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    public class Node {
        public T item;
        public Node next;
        public Node prev;
        public Node(T i, Node n, Node p) {
            item = i;
            next = n;
            prev = p;
        }
    }

    private class LinkedListIterator implements Iterator<T> {
        private int wizPos;

        public LinkedListIterator() {
            wizPos = 0;
        }

        public boolean hasNext() {
            return wizPos < size;
        }

        public T next() {
             return get(wizPos++);
        }
    }

    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }

    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel.prev = sentinel;
        size = 0;
    }

    private Node sentinel;
    private int size;

    @Override
    public void addFirst(T item) {
        sentinel.next = new Node(item, sentinel.next, sentinel.prev);
        if (size > 0) sentinel.next.next.prev = sentinel.next;
        else sentinel.prev = sentinel.next;  
        size++;
    }

    @Override
    public void addLast(T item) {
        sentinel.prev = new Node(item, sentinel.next, sentinel.prev);
        if (size > 0) sentinel.prev.prev.next = sentinel.prev;
        else sentinel.next = sentinel.prev;
        size++;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        int i = 0;
        Node n = sentinel;
        while (i++ < size) {
            n = n.next;
            System.out.print(n.item + " ");
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (size == 0) return null;
        T removed = sentinel.next.item;
        sentinel.next = sentinel.next.next;
        sentinel.next.prev = sentinel;
        size--;
        return removed;
    }

    @Override
    public T removeLast() {
        if (size == 0) return null;
        T removed = sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size--;
        return removed;
    }

    @Override
    public T get(int index) {
        if (index >= size) return null;
        Node current = sentinel.next;
        int i;
        i = 0;
        while (i++ < index) {
            current = current.next;
        }
        return current.item;
    }

    public T getRecursive(int index) {
        if (index < 0) return null;

        return getRecursive(index, sentinel.next, 0);
    }

    private T getRecursive(int index, Node n, int cur) {
        if (n == null) return null;
        if (cur == index) return n.item; 
        return getRecursive(index, n.next, cur + 1);
    }

    public boolean equals(Object o) {
        if (!(o instanceof Deque)) return false;
        Deque oDeque = (Deque) o;
        if (size != oDeque.size()) return false;
        
        int i = 0;
        while (i++ < size) {
            if (!get(i).equals(oDeque.get(i))) return false;
        }
        return true;
    }
}