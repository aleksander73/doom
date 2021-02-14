package aleksander73.doom.player;

import aleksander73.vector.core.GameObject;
import aleksander73.vector.core.Transform;
import aleksander73.vector.rendering.Camera;

public class MyCamera extends GameObject {
    public MyCamera(Transform transform) {
        super("Camera");
        Camera camera = Camera.getActiveCamera();
        this.addComponents(transform, camera);
    }
}
