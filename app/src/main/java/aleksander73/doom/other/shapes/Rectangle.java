package aleksander73.doom.other.shapes;

import aleksander73.math.linear_algebra.Vector2d;

public class Rectangle extends Shape {
    private final float width;
    private final float height;

    public Rectangle(Vector2d position, float width, float height) {
        super(position);
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean contains(Vector2d v) {
        float x = v.getX();
        float y = v.getY();
        float left = this.getPosition().getX() - width / 2.0f;
        float right = this.getPosition().getX() + width / 2.0f;
        float bottom = this.getPosition().getY() - height / 2.0f;
        float top = this.getPosition().getY() + height / 2.0f;
        return x > left && x < right && y > bottom && y < top;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
