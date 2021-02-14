package aleksander73.doom.weapon_system.weapons;

import aleksander73.doom.animation.SpriteAnimation;
import aleksander73.doom.weapon_system.TypeAWeapon;
import aleksander73.math.linear_algebra.Vector2d;
import aleksander73.vector.core.GameEngine;
import aleksander73.vector.gui.GUIUtility;
import aleksander73.vector.rendering.materials.Texture;

public class Pistol extends TypeAWeapon {
    public Pistol(int ammo) {
        super(
            "Pistol",
            new Vector2d(0.0f, 0.05f),
            GUIUtility.screenToNDC(new Vector2d(1.0f,1.0f)),
            0.7f,
            null,
            GameEngine.getResourceSystem().getTexture("weapons/pistol/idle.png"),
            ammo,
            20,
            0.5f,
            0.5f,
            new SpriteAnimation(0.25f, false, new Texture[] {
                    GameEngine.getResourceSystem().getTexture("weapons/pistol/shoot_1.png"),
                    GameEngine.getResourceSystem().getTexture("weapons/pistol/shoot_2.png"),
                    GameEngine.getResourceSystem().getTexture("weapons/pistol/shoot_3.png")
            }),
            "pistol.wav"
        );
    }
}
