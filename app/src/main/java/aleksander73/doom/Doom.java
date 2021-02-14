package aleksander73.doom;

import java.util.Arrays;

import aleksander73.doom.hud.ScrollDownButton;
import aleksander73.doom.hud.ScrollUpButton;
import aleksander73.doom.hud.ShootButton;
import aleksander73.doom.input.InputManager;
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
        ScrollUpButton scrollUpButton = new ScrollUpButton(new Vector2d(-0.85f, 0.5f));
        ScrollDownButton scrollDownButton = new ScrollDownButton(new Vector2d(-0.85f, 0.2f));
        ShootButton shootButton = new ShootButton(new Vector2d(0.75f, 0.0f));

        return new Scene(Arrays.asList(
            floor, player, camera, scrollUpButton, scrollDownButton, shootButton
        ));
    }

    @Override
    protected void setupInput() {
        InputManager.getInstance().initialize();
    }

    @Override
    protected void clearInput() {
        InputManager.getInstance().clear();
    }
}
