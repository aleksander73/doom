package aleksander73.doom.other.shapes;

import aleksander73.math.linear_algebra.Vector2d;

public class Ellipse extends Shape {
    private final float a;
    private final float b;

    public Ellipse(Vector2d position, float a, float b) {
        super(position);
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean contains(Vector2d v) {
        Vector2d r = v.sub(this.getPosition()).toVector2d();
        Vector2d v_resized = new Vector2d(r.getX(), r.getY() * (a / b));
        return v_resized.magnitude() < a;
    }
}
