package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    public ArrayDeque() {
        array = (T[]) new Object[8];
        size = 0;
        nextFirst = 3;
        nextLast = 4;
    }

    private class ArrayDequeIterator implements Iterator<T> {
        private int wizPos;

        public ArrayDequeIterator() {
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
        return new ArrayDequeIterator();
    }

    private int size;
    private int nextFirst;
    private int nextLast;
    private T[] array;

    private void expand() {
        T[] temp = (T[]) new Object[array.length * 2];
        System.arraycopy(array, nextLast, temp, 0, array.length - nextLast);
        System.arraycopy(array, 0, temp, array.length - nextLast, nextLast);
        array = temp;
        nextFirst = array.length - 1;
        nextLast = array.length / 2;
    }

    private void shrink() {
        if (isEmpty() || (double) size / array.length > 0.25) return;
        T[] temp = (T[]) new Object[size];
        if (nextLast < nextFirst) {
            System.arraycopy(array, nextFirst + 1, temp, 0, array.length - nextFirst - 1);
            System.arraycopy(array, 0, temp, array.length - nextFirst - 1, nextLast);
        } else System.arraycopy(array, nextFirst + 1, temp, 0, size);
        array = temp;
        nextFirst = array.length - 1;
        nextLast = 0;
    }

    @Override
    public void addFirst(T item) {
        if (size++ == array.length) expand();
        array[nextFirst--] = item;
        if (nextFirst == -1) nextFirst = array.length - 1;
    }

    @Override
    public void addLast(T item) {
        if (size++ == array.length) expand();
        array[nextLast++] = item;
        if (nextLast == array.length) nextLast = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        for (int i = 0; i < size; i++) System.out.print(get(i) + " ");
        System.lineSeparator();
    }

    @Override
    public T removeFirst() {
        if (size == 0) return null;
        size--;
        if (nextFirst == array.length - 1) nextFirst = -1;
        T removed = array[++nextFirst];
        shrink();
        return removed;
    }

    @Override
    public T removeLast() {
        if (size == 0) return null;
        size--;
        if (nextLast == 0) nextLast = array.length;
        T removed = array[--nextLast];
        shrink();
        return removed;
    }

    @Override
    public T get(int index) {
        if (index >= size) return null;
        int offset = nextFirst + index + 1;
        if (offset >= array.length) return array[offset - array.length];
        return array[offset];
    }

     public boolean equals(Object o) {
         if (!(o instanceof Deque)) return false;
         Deque oDeque = (Deque) o;
         if (size != oDeque.size()) return false;

         for (int i = 0; i < size; i++) {
             if (!(oDeque.get(i).equals(get(i)))) return false;
         }
         return true;
     }
}