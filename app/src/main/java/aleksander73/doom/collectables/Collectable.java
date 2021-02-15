package aleksander73.doom.collectables;

import aleksander73.doom.game_object.Model;
import aleksander73.doom.player.CameraLens;
import aleksander73.doom.player.Player;
import aleksander73.math.linear_algebra.Vector2d;
import aleksander73.math.linear_algebra.Vector3d;
import aleksander73.vector.core.GameEngine;
import aleksander73.vector.core.Transform;
import aleksander73.vector.rendering.Shaders;
import aleksander73.vector.rendering.materials.Texture;
import aleksander73.vector.rendering.mesh.Mesh;
import aleksander73.vector.time.Time;

public abstract class Collectable extends Model {
    private static final float H = 0.5f;
    private Player player;
    private static final float MIN_DISTANCE = 1.0f;
    private final String collectSound = "item_collected.wav";

    public Collectable(Vector2d position, Mesh mesh, Texture texture) {
        super(
            "Collectable",
            new Transform(new Vector3d(position.getX(), H, position.getY())),
            mesh,
            Shaders.getStandardShader(),
            null,
            texture
        );
    }

    @Override
    public void start() {
        player = (Player)this.getScene().find("Player");
    }

    @Override
    public void update() {
        Transform transform = this.getComponent(Transform.class);
        Vector3d playerPosition = player.getComponent(Transform.class).getPosition();
        transform.rotate(Vector3d.yUnitVector, 100.0f * Time.getDeltaTime());
        if(this.shouldBeUsed()) {
            Vector2d position2d = new Vector2d(transform.getPosition().getX(), transform.getPosition().getZ());
            Vector2d playerPosition2d = new Vector2d(playerPosition.getX(), playerPosition.getZ());
            if(position2d.distance(playerPosition2d) < MIN_DISTANCE) {
                this.onCollected();
                this.destroy();
            }
        }
    }

    protected boolean shouldBeUsed() {
        return true;
    }

    protected void onCollected() {
        GameEngine.getResourceSystem().playSound(this.getCollectSound(), false);
        CameraLens cameraLens = (CameraLens)this.getScene().find("CameraLens");
        cameraLens.onCollected();
    }

    public Player getPlayer() {
        return player;
    }

    public String getCollectSound() {
        return collectSound;
    }
}
