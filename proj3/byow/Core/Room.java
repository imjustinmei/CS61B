package byow.Core;

public class Room {
    private int x;
    private int y;
    private int width;
    private int height;

    public Room(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int[] getCenter() {
        return new int[] {x + width / 2, y + height / 2};
    }

    public boolean intersects(Room other) {
        int[] vals = other.getValues();
        return x <= vals[0] + vals[2] && x + width >= vals[0] && y <= vals[1] + vals[3] && y + width >=  vals[1];
    }

    public int distanceSquare(Room other) {
        int[] vals = other.getValues();
        int xDiff = x - vals[0];
        int yDiff = y - vals[1];

        return xDiff * xDiff + yDiff * yDiff;
    }

    public int[] getValues() {
        return new int[] {x, y, width, height};
    }
}
