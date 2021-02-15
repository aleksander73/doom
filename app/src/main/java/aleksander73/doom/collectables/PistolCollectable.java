package aleksander73.doom.collectables;

import aleksander73.doom.weapon_system.Weapon;
import aleksander73.math.linear_algebra.Vector2d;
import aleksander73.vector.core.GameEngine;

public class PistolCollectable extends WeaponCollectable {
    public PistolCollectable(Weapon weapon, Vector2d position) {
        super(weapon, position, GameEngine.getResourceSystem().getTexture("weapons/pistol/icon.png"));
    }
}
