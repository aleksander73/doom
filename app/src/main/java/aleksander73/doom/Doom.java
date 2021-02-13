package aleksander73.doom;

import java.util.Arrays;

import aleksander73.vector.core.Game;
import aleksander73.vector.rendering.Camera;
import aleksander73.vector.scene.Scene;

public class Doom extends Game {
    public Doom() {
        Camera.getActiveCamera().setFov(65.0f);
    }

    @Override
    protected Scene buildScene() {
        return new Scene(Arrays.asList());
    }

    @Override
    protected void setupInput() {}

    @Override
    protected void clearInput() {}
}
