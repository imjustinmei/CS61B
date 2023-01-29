package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove() {
        BuggyAList<Integer> l1 = new BuggyAList<>();
        AListNoResizing<Integer> l2 = new AListNoResizing<>();

        for (int i = 0; i < 3; i++) {
            l1.addLast(i);
            l2.addLast(i);
        }

        for (int i = 0; i < 3; i++) assertEquals(l1.removeLast(), l2.removeLast());
    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> B = new BuggyAList<>();
        int N = 500;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                B.addLast(randVal);
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
            } else if (operationNumber == 2) {
                if (!(L.size() > 0)) continue;
                assertEquals(L.removeLast(), B.removeLast());
            } else {
                if (!(L.size() > 0)) continue;
                assertEquals(L.getLast(), B.getLast());
            }
        }
    }
}
