package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.TreeSet;
import java.util.Comparator;
import edu.princeton.cs.algs4.In;

public class Engine {
    TERenderer ter = new TERenderer();
    public final TETile[][] tiles;
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static final int MINROOMSIZE = 6;
    public static final int MAXROOMSIZE = 11;
    public static final int[] POS = new int[2];

    public Engine() {
        ter.initialize(WIDTH, HEIGHT);
        tiles = new TETile[WIDTH][HEIGHT];
    }

    private void fillNothing(TETile[][] tiles) {
        for (int x = 0; x < tiles.length; x += 1) {
            for (int y = 0; y < tiles[0].length; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    private void addRoom(int x, int y, int w, int h) {
        for (int j = x; j < x + w; j++) {
            for (int i = y; i < y + h; i++) {
                if (tiles[j][i] != Tileset.NOTHING) continue;
                if ((j - x) % (w - 1) == 0 || (i - y) % (h - 1) == 0) tiles[j][i] = Tileset.WALL;
                else tiles[j][i] = Tileset.FLOOR;
            }
        }
    }

    private void addHallway(int sX, int sY, int eX, int eY) {
        int temp = sX;
        int end = eX;
        if (sX > eX) {
            sX = eX;
            eX = temp;
        }
        for (int i = sX; i < eX + 1; i++) {
            for (int j = 0; j < 2; j++) if (tiles[i][sY - 1 + j * 2] == Tileset.NOTHING) tiles[i][sY - 1 + j * 2] = Tileset.WALL;
            tiles[i][sY] = Tileset.FLOOR;
        }
        if (sY > eY) {
            temp = sY;
            sY = eY;
            eY = temp;
        }
        for (int i = sY; i < eY + 1; i++) {
            for (int j = 0; j < 2; j++) if (tiles[end - 1 + j * 2][i] == Tileset.NOTHING) tiles[end - 1 + j * 2][i] = Tileset.WALL;
            tiles[end][i] = Tileset.FLOOR;
        }
    }

    static class distance implements Comparator<Room> {
        public int compare(Room r1, Room r2) {
            int[] vals1 = r1.getValues();
            int[] vals2 = r2.getValues();

            return vals1[1] * vals1[1] + vals1[0] * vals1[0] - vals2[0] * vals2[0] - vals2[1] * vals2[1];
        }
    }

    private int[] worldGen(Random rand) {
        int numRooms = rand.nextInt(10, 15);
        TreeSet<Room> rooms = new TreeSet<>(new distance());
        fillNothing(tiles);

        outer:
        for (int i = 0; i < numRooms;) {
            int x = rand.nextInt(WIDTH);
            int y = rand.nextInt(HEIGHT);
            int width = rand.nextInt(MINROOMSIZE, MAXROOMSIZE);
            int height = rand.nextInt(MINROOMSIZE, MAXROOMSIZE);

            if (x + width >= WIDTH || y + height >= HEIGHT) continue;
            Room newRoom = new Room(x, y, width, height);
            for (Room r : rooms) if (newRoom.intersects(r) || r.intersects(newRoom)) continue outer;

            addRoom(x, y, width, height);
            rooms.add(newRoom);
            i++;
        }

        Room prev = null;
        for (Room r : rooms) {
            if (prev == null) {
                prev = r;
                continue;
            };

            int[] oCenter = prev.getCenter();
            int[] rCenter = r.getCenter();
            addHallway(rCenter[0], rCenter[1], oCenter[0], oCenter[1]);
            prev = r;
        }

        for (Room r : rooms) {
            int[] vals = r.getValues();
            if (tiles[vals[0] + vals[2] / 2][vals[1]] == Tileset.WALL) {
                tiles[vals[0] + vals[2] / 2][vals[1]] = Tileset.LOCKED_DOOR;
                break;
            }
        }

        int[] center = rooms.last().getCenter();
        tiles[center[0]][center[1]] = Tileset.AVATAR;
        return center;
    }

    private int handleInput(char c) {
        int x = POS[0];
        int y = POS[1];

        switch (c) {
            case 'w' -> {
                if (tiles[x][y + 1] == Tileset.FLOOR) {
                    tiles[x][y] = Tileset.FLOOR;
                    tiles[x][POS[1] = y + 1] = Tileset.AVATAR;
                }
            }
            case 'a' -> {
                if (tiles[x - 1][y] == Tileset.FLOOR) {
                    tiles[x][y] = Tileset.FLOOR;
                    tiles[POS[0] = x - 1][y] = Tileset.AVATAR;
                }
            }
            case 's' -> {
                if (tiles[x][y - 1] == Tileset.FLOOR) {
                    tiles[x][y] = Tileset.FLOOR;
                    tiles[x][POS[1] = y - 1] = Tileset.AVATAR;
                }
            }
            case 'd' -> {
                if (tiles[x + 1][y] == Tileset.FLOOR) {
                    tiles[x][y] = Tileset.FLOOR;
                    tiles[POS[0] = x + 1][y] = Tileset.AVATAR;
                }
            }
            case ':' -> {
                return -1;
            }
            case 'q' -> {
                return -2;
            }
        }
        return 0;
    }

    private void save() throws IOException {
        Path path = Paths.get("byow/Core/savefile.txt");
        Files.write(path, TETile.toString(tiles).getBytes());
    }

    private int[] load() {
        In map = new In("byow/Core/savefile.txt");
        int[] center = new int[2];
        int j = HEIGHT - 1;

        while (!map.isEmpty()) {
            String row = map.readLine();
            for (int i = 0; i < WIDTH; i++) {
                switch (row.charAt(i)) {
                    case '·' -> tiles[i][j] = Tileset.FLOOR;
                    case '#' -> tiles[i][j] = Tileset.WALL;
                    case '@' -> {
                        tiles[i][j] = Tileset.AVATAR;
                        center[0] = i;
                        center[1] = j;
                    }
                    case '█' -> tiles[i][j] = Tileset.LOCKED_DOOR;
                    default -> tiles[i][j] = Tileset.NOTHING;
                }
            }
            j--;
        }

        return center;
    }

    private void start() {
        int[] pos = new int[2];
        welcomeScreen();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                switch (Character.toLowerCase(StdDraw.nextKeyTyped())) {
                    case 'n' -> {
                        String seed = getSeed();
                        Random rand = new Random(Long.parseLong(seed));
                        pos = worldGen(rand);
                    }
                    case 'l' -> pos = load();
                    case 'q' -> System.exit(0);
                    default -> {
                        continue;
                    }
                }
                break;
            }
        }

        POS[0] = pos[0];
        POS[1] = pos[1];
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 14));
        ter.renderFrame(tiles);

        int prev = 0;
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                int out = handleInput(Character.toLowerCase(StdDraw.nextKeyTyped()));
                if (out == -2 && prev == -1) {
                    try {
                        save();
                        System.exit(0);
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }
                prev = out;
            }
            ter.renderFrame(tiles);
            mouseTile();
            StdDraw.pause(10);
        }
    }

    private void welcomeScreen() {
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new Font("Monaco", Font.PLAIN, 35));
        StdDraw.text( 15, 22, "CS61B The Game");
        StdDraw.setFont(new Font("Monaco", Font.PLAIN, 20));
        StdDraw.text(15, 18, "New Game (N)");
        StdDraw.text(15, 16, "Load Game (L)");
        StdDraw.text(15, 14, "Quit (Q)");
        StdDraw.show();
    }

    public String getSeed() {
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 30));
        String seed = "";
        seedScreen(seed);
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toLowerCase(StdDraw.nextKeyTyped());
                if (Character.isDigit(c)) seed += c;
                else if (c == 's') break;
                else if (c == '\b') seed = seed.substring(0, seed.length() - 1);
                seedScreen(seed);
            }
        }
        if (seed.equals("")) return "1";
        return seed;
    }

    public void seedScreen(String s) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setFont(new Font("Monaco", Font.PLAIN, 35));
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(15, (double) HEIGHT / 2, "Seed: " + s);
        StdDraw.show();
    }

    private String mouseTile() {
        int x = (int) StdDraw.mouseX();
        int y = (int) StdDraw.mouseY();
        if (x < WIDTH && y < HEIGHT) {
            StdDraw.text(2, HEIGHT - 2, tiles[x][y].description());
            StdDraw.show();
            return tiles[x][y].description();
        } else return "";
    }

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        start();
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, running both of these:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        input = input.toLowerCase();
        int prev = 0;

        switch (input.substring(0, 1)) {
            case "n" -> {
                int s = input.indexOf('s');
                Random rand = new Random(Long.parseLong(input.substring(1, s)));
                int[] pos = worldGen(rand);
                POS[0] = pos[0];
                POS[1] = pos[1];
                input = input.substring(s + 1);
            }
            case "l" -> load();
        }

        for (int i = 0; i < input.length(); i++) {
            int out = handleInput(input.charAt(i));
            if (out == -2 && prev == -1) {
                try {
                    save();
                } catch (IOException e) {
                    System.out.println(e);
                }
                break;
            }
            prev = out;
        }

        ter.renderFrame(tiles);
        return tiles;
    }
}
