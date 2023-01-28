package gh2;

import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;
import deque.ArrayDeque;

public class GuitarHero {
    private static String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";

    public static void main(String[] args) {
        ArrayDeque<GuitarString> keys = new ArrayDeque<>();
        for (int i = 0; i < keyboard.length(); i ++) {
            keys.addLast(new GuitarString(440.0 * Math.pow(2, (i - 24) / 12.0)));
        }

        while (true) {
            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int index = keyboard.indexOf(key);
                if (index > -1 && index < 37) keys.get(index).pluck();
                else continue;
            }

            /* compute the superposition of samples */
            double sample = 0.0;
            for (int i = 0; i<keys.size(); i++) sample += keys.get(i).sample();

            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            for (int i = 0; i<keys.size(); i++) keys.get(i).tic();
        }
    }
}
