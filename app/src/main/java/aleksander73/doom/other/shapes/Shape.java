package aleksander73.doom.other.shapes;

import aleksander73.math.linear_algebra.Vector2d;

public abstract class Shape {
    private Vector2d position;

    public Shape(Vector2d position) {
        this.position = position;
    }

    public abstract boolean contains(Vector2d v);

    public Vector2d getPosition() {
        return position;
    }
}
