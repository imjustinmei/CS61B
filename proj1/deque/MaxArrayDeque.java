package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> c) {
        super();
        comparator = c;
    }

    public T max() {
        if (isEmpty()) return null;
        T currentMax = get(0);
        for (T check : this) if (comparator.compare(currentMax, check) < 0) currentMax = check;
        return currentMax;
    }

    public T max(Comparator<T> c) {
        if (isEmpty()) return null;
        T currentMax = get(0);
        for (T check : this) if (c.compare(currentMax, check) < 0) currentMax = check;
        return currentMax;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MaxArrayDeque)) return false;
        MaxArrayDeque oDeque = (MaxArrayDeque) o;

        if (size() != oDeque.size() || !(max().equals(oDeque.max()))) return false;
        for (int i = 0; i < size(); i++) {
            if (!(oDeque.get(i).equals(get(i)))) return false;
        }
        return true;
    }
}