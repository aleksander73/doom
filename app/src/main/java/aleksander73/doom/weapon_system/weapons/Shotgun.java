package aleksander73.doom.weapon_system.weapons;

import aleksander73.doom.animation.SpriteAnimation;
import aleksander73.doom.weapon_system.TypeBWeapon;
import aleksander73.math.linear_algebra.Vector2d;
import aleksander73.vector.core.GameEngine;
import aleksander73.vector.gui.GUIUtility;
import aleksander73.vector.rendering.materials.Texture;

public class Shotgun extends TypeBWeapon {
    public Shotgun(int ammo) {
        super(
            "Shotgun",
            new Vector2d(0.0f, -0.075f),
            GUIUtility.screenToNDC(new Vector2d(0.65f,0.75f)),
            0.7f,
            null,
            GameEngine.getResourceSystem().getTexture("weapons/shotgun/idle.png"),
            ammo,
            45,
            0.75f,
            0.75f,
            new SpriteAnimation(0.2f, false, new Texture[] {
                    GameEngine.getResourceSystem().getTexture("weapons/shotgun/shoot_1.png"),
                    GameEngine.getResourceSystem().getTexture("weapons/shotgun/shoot_2.png")
            }),
            "shotgun_shoot.wav",
            new SpriteAnimation(0.8f, false, new Texture[] {
                    GameEngine.getResourceSystem().getTexture("weapons/shotgun/reload_1.png"),
                    GameEngine.getResourceSystem().getTexture("weapons/shotgun/reload_2.png"),
                    GameEngine.getResourceSystem().getTexture("weapons/shotgun/reload_3.png"),
                    GameEngine.getResourceSystem().getTexture("weapons/shotgun/reload_2.png"),
                    GameEngine.getResourceSystem().getTexture("weapons/shotgun/reload_1.png")
            }),
            "shotgun_reload.wav"
        );
    }
}
