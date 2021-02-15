package aleksander73.doom.collectables;

import aleksander73.doom.player.Player;
import aleksander73.math.linear_algebra.Vector2d;
import aleksander73.vector.core.GameEngine;

public class HealthCollectable extends Collectable {
    public static final int HEALTH = 25;

    public HealthCollectable(Vector2d position) {
        super(position, GameEngine.getResourceSystem().loadMesh("health_collectable.mesh"), GameEngine.getResourceSystem().getTexture("items/health.png"));
    }

    @Override
    protected void onCollected() {
        super.onCollected();
        this.getPlayer().heal(HEALTH);
    }

    @Override
    protected boolean shouldBeUsed() {
        return HealthCollectable.this.getPlayer().getHealth() < Player.MAX_HEALTH;
    }
}
