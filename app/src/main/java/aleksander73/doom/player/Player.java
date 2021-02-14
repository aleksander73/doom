package aleksander73.doom.player;

import aleksander73.math.linear_algebra.Vector2d;
import aleksander73.math.linear_algebra.Vector3d;
import aleksander73.vector.core.GameObject;
import aleksander73.vector.core.Transform;

public class Player extends GameObject {
    private final Transform transform;
    private final float H = 1.25f;

    public Player(Vector2d position) {
        super("Player");
        transform = new Transform(new Vector3d(position.getX(), H, position.getY()));
        this.addComponent(transform);
    }

    @Override
    public void start() {
        GameObject myCamera = this.getScene().find("Camera");
        myCamera.getComponent(Transform.class).setParent(transform);
    }
}
