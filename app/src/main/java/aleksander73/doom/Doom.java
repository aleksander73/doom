package aleksander73.doom;

import java.util.Arrays;

import aleksander73.doom.other.Floor;
import aleksander73.doom.player.MyCamera;
import aleksander73.doom.player.Player;
import aleksander73.math.linear_algebra.Vector2d;
import aleksander73.vector.core.Game;
import aleksander73.vector.core.Transform;
import aleksander73.vector.rendering.Camera;
import aleksander73.vector.scene.Scene;

public class Doom extends Game {
    public Doom() {
        Camera.getActiveCamera().setFov(65.0f);
    }

    @Override
    protected Scene buildScene() {
        final float u = 3.0f;
        Floor floor = Floor.createFloor(new Vector2d(0.0f,  0.0f), new Vector2d(5 * u, 5 * u));
        Player player = new Player(new Vector2d(0.0f, 0.0f));
        MyCamera camera = new MyCamera(player.getComponent(Transform.class).copy());

        return new Scene(Arrays.asList(
            floor, player, camera
        ));
    }

    @Override
    protected void setupInput() {}

    @Override
    protected void clearInput() {}
}
