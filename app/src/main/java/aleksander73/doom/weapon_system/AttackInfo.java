package aleksander73.doom.weapon_system;

import aleksander73.math.linear_algebra.Vector3d;

public class AttackInfo {
    private final Weapon weapon;
    private final Vector3d origin;

    public AttackInfo(Weapon weapon, Vector3d origin) {
        this.weapon = weapon;
        this.origin = origin;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public Vector3d getOrigin() {
        return origin;
    }
}
