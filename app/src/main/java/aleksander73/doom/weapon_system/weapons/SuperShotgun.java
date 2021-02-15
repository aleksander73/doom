package aleksander73.doom.weapon_system.weapons;

import aleksander73.doom.animation.SpriteAnimation;
import aleksander73.doom.weapon_system.TypeBWeapon;
import aleksander73.math.linear_algebra.Vector2d;
import aleksander73.vector.core.GameEngine;
import aleksander73.vector.gui.GUIUtility;
import aleksander73.vector.rendering.materials.Texture;

public class SuperShotgun extends TypeBWeapon {
    public SuperShotgun(int ammo) {
        super(
            "SuperShotgun",
            new Vector2d(0.0f, 0.075f),
            GUIUtility.screenToNDC(new Vector2d(1.0f,1.0f)),
            0.7f,
            null,
            GameEngine.getResourceSystem().getTexture("weapons/super_shotgun/idle.png"),
            ammo,
            75,
            0.75f,
            0.75f,
            new SpriteAnimation(0.2f, false, new Texture[] {
                GameEngine.getResourceSystem().getTexture("weapons/super_shotgun/shoot_1.png"),
                GameEngine.getResourceSystem().getTexture("weapons/super_shotgun/shoot_2.png")
            }),
            "super_shotgun_shoot.wav",
            new SpriteAnimation(1.65f, false, new Texture[] {
                GameEngine.getResourceSystem().getTexture("weapons/super_shotgun/reload_1.png"),
                GameEngine.getResourceSystem().getTexture("weapons/super_shotgun/reload_2.png"),
                GameEngine.getResourceSystem().getTexture("weapons/super_shotgun/reload_3.png"),
                GameEngine.getResourceSystem().getTexture("weapons/super_shotgun/reload_4.png"),
                GameEngine.getResourceSystem().getTexture("weapons/super_shotgun/reload_5.png"),
                GameEngine.getResourceSystem().getTexture("weapons/super_shotgun/reload_3.png"),
                GameEngine.getResourceSystem().getTexture("weapons/super_shotgun/reload_6.png")
            }),
            "super_shotgun_reload.wav"
        );
    }
}
