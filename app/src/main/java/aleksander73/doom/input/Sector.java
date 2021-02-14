package aleksander73.doom.input;

import aleksander73.doom.other.shapes.Shape;
import aleksander73.math.linear_algebra.Vector2d;

public class Sector {
    private final String name;
    private final Shape shape;
    private final Input input = new Input();

    private final int level;
    private boolean locked;

    public Sector(String name, Shape shape, int level) {
        this.name = name;
        this.shape = shape;
        this.level = level;
    }

    public boolean contains(Vector2d v) {
        return shape.contains(v);
    }

    public void clear() {
        input.clear();
    }

    // --------------------------------------------------

    public String getName() {
        return name;
    }

    public Input getInput() {
        return input;
    }

    public int getLevel() {
        return level;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
