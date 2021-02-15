package aleksander73.doom.collectables;

import aleksander73.doom.weapon_system.Weapon;
import aleksander73.math.linear_algebra.Vector2d;
import aleksander73.vector.core.GameEngine;
import aleksander73.vector.rendering.materials.Texture;

public class WeaponCollectable extends Collectable {
    private final Weapon weapon;
    private static final String weaponCollectSound = "weapon_collected.wav";

    public WeaponCollectable(Weapon weapon, Vector2d position, Texture texture) {
        super(position, GameEngine.getResourceSystem().loadMesh("weapon_collectable.mesh"), texture);
        this.weapon = weapon;
    }

    @Override
    protected void onCollected() {
        super.onCollected();
        this.getPlayer().collect(weapon);
    }

    @Override
    public String getCollectSound() {
        return weaponCollectSound;
    }
}
