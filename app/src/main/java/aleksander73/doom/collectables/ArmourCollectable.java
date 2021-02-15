package aleksander73.doom.collectables;

import aleksander73.doom.animation.SpriteAnimation;
import aleksander73.doom.player.Player;
import aleksander73.math.linear_algebra.Vector2d;
import aleksander73.vector.core.GameEngine;
import aleksander73.vector.rendering.materials.Texture;

public class ArmourCollectable extends Collectable {
    public static final int ARMOUR = 5;
    private final SpriteAnimation shining;

    public ArmourCollectable(Vector2d position) {
        super(position, GameEngine.getResourceSystem().loadMesh("armour_collectable.mesh"), GameEngine.getResourceSystem().getTexture("items/armour_1.png"));
        shining = new SpriteAnimation(2.0f, true, new Texture[] {
            GameEngine.getResourceSystem().getTexture("items/armour_1.png"),
            GameEngine.getResourceSystem().getTexture("items/armour_2.png"),
            GameEngine.getResourceSystem().getTexture("items/armour_3.png"),
            GameEngine.getResourceSystem().getTexture("items/armour_4.png"),
            GameEngine.getResourceSystem().getTexture("items/armour_4.png"),
            GameEngine.getResourceSystem().getTexture("items/armour_4.png"),
            GameEngine.getResourceSystem().getTexture("items/armour_3.png"),
            GameEngine.getResourceSystem().getTexture("items/armour_2.png")
        });
        this.addComponent(shining);
    }

    @Override
    public void start() {
        super.start();
        shining.start();
    }

    @Override
    public void update() {
        super.update();
        shining.update();
    }

    @Override
    protected void onCollected() {
        super.onCollected();
        ArmourCollectable.this.getPlayer().strengthen(ARMOUR);
    }

    @Override
    protected boolean shouldBeUsed() {
        return ArmourCollectable.this.getPlayer().getArmour() < Player.MAX_ARMOUR;
    }
}
