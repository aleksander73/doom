package aleksander73.doom.input;

import aleksander73.math.linear_algebra.Vector2d;

public class Input {
    private Vector2d touchDown;
    private Vector2d lastTouchDown;
    private Vector2d touchCurrent;

    public void clear() {
        touchDown = null;
    }

    public Vector2d r() {
        Vector2d r;
        if(touchCurrent != null && lastTouchDown != null) {
            r = touchCurrent.sub(lastTouchDown).toVector2d();
        } else {
            r = new Vector2d(0.0f, 0.0f);
        }

        return r;
    }

    public Vector2d getTouchDown() {
        return touchDown;
    }

    public void setTouchDown(Vector2d touchDown) {
        this.touchDown = touchDown;
    }

    public Vector2d getLastTouchDown() {
        return lastTouchDown;
    }

    public void setLastTouchDown(Vector2d lastTouchDown) {
        this.lastTouchDown = lastTouchDown;
    }

    public Vector2d getTouchCurrent() {
        return touchCurrent;
    }

    public void setTouchCurrent(Vector2d touchCurrent) {
        this.touchCurrent = touchCurrent;
    }
}
