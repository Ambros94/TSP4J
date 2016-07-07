package Utility;

public class Coordinate {

    private final float y;
    private final float x;

    public Coordinate(float x, float y) {
        this.x = x;
        this.y = y;
    }

    int distance(Coordinate b) {
        return  (int) Math.round(Math.sqrt((x - b.x) * (x - b.x) + (y - b.y) * (y - b.y)));
    }

    @Override
    public String toString() {
        return "Utility.Coordinate{" +
                "y=" + y +
                ", x=" + x +
                '}';
    }
}
