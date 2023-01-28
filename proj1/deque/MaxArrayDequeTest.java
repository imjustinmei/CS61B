package deque;

import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.*;

public class MaxArrayDequeTest {

    public class Greatest implements Comparator<Integer> {
        public int compare(Integer a, Integer b) {
            return a - b;
        }
    }
    @Test
    public void largestTest() {

        MaxArrayDeque<Integer> ad1 = new MaxArrayDeque<>(new Greatest());
        for (int i = 0; i < 10; i++) ad1.addLast(i);
        assertEquals(9, (int) ad1.max());
    }
}
