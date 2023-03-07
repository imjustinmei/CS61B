package PlusWorld;
import org.junit.Test;
import static org.junit.Assert.*;

import byowTools.TileEngine.TERenderer;
import byowTools.TileEngine.TETile;
import byowTools.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of plus shaped regions.
 */
public class PlusWorld {
    private static final int WIDTH = 19;
    private static final int HEIGHT = 19;

    private static final long SEED = 2873123;

    private static final Random RANDOM = new Random(SEED);

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] tiles = new TETile[WIDTH][HEIGHT];
        addPlus(tiles, 5, 2, 2);
        ter.renderFrame(tiles);
    }

    public static void addPlus(TETile[][] tiles, int s, int startX, int startY) {
        fillNothing(tiles);
        for (int i = 0; i < 3; i++) {
            addSquare(tiles, s, startX + s * i, startY + s);
        }
        for (int i = 0; i < 2; i++) {
            addSquare(tiles, s, startX + s, startY + i * s * 2);
        }
    }

    private static void fillNothing(TETile[][] tiles) {
        for (int x = 0; x < tiles.length; x += 1) {
            for (int y = 0; y < tiles[0].length; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    private static void addSquare(TETile[][] tiles, int s, int x, int y) {
        for (int i = y; i < y + s; i++) {
            for (int j = x; j < x + s; j++) {
                tiles[j][i] = Tileset.FLOWER;
            }
        }
    }
}
