package aleksander73.doom.weapon_system;

import aleksander73.doom.animation.SpriteAnimation;
import aleksander73.math.linear_algebra.Vector2d;
import aleksander73.vector.adt.StateMachine;
import aleksander73.vector.animation.ValueAnimation;
import aleksander73.vector.core.GameEngine;
import aleksander73.vector.gui.GUIElement;
import aleksander73.vector.rendering.materials.Colour;
import aleksander73.vector.rendering.materials.Texture;
import aleksander73.vector.rendering.renderers.Renderer;
import aleksander73.vector.utility.functional_interface.Function;

public abstract class Weapon extends GUIElement {
    private int ammo;
    private final int damage;
    private final Vector2d hidePosition;
    private final Texture mainTexture;

    private final ValueAnimation<Vector2d> equipAnimation;
    private final ValueAnimation<Vector2d> hideAnimation;
    private final SpriteAnimation shootAnimation;

    private final String shootSound;
    private static final String emptySound = "weapon_empty.wav";

    private final StateMachine behaviourStateMachine;

    public Weapon(String name, final Vector2d equippedPosition, Vector2d dimensions, float h, Colour colour, Texture mainTexture, int ammo, int damage, final float equipTime, final float hideTime, SpriteAnimation shootAnimation, String shootSound) {
        super(name, equippedPosition, dimensions, colour, mainTexture);
        this.ammo = ammo;
        this.damage = damage;
        this.mainTexture = mainTexture;
        this.shootSound = shootSound;
        hidePosition = equippedPosition.add(new Vector2d(0.0f, -h)).toVector2d();
        equipAnimation = new ValueAnimation<>(equipTime, false, new Function<Float, Vector2d>() {
            @Override
            public Vector2d accept(Float t) {
                return hidePosition.lerp(equippedPosition, t / equipTime).toVector2d();
            }
        });
        hideAnimation = new ValueAnimation<>(hideTime, false, new Function<Float, Vector2d>() {
            @Override
            public Vector2d accept(Float t) {
                return equippedPosition.lerp(hidePosition, t / hideTime).toVector2d();
            }
        });
        this.shootAnimation = shootAnimation;
        this.addComponents(equipAnimation, hideAnimation, shootAnimation);
        this.setActive(false);
        this.getComponent(Renderer.class).setActive(false);
        behaviourStateMachine = this.buildBehaviourStateMachine();
    }

    protected abstract StateMachine buildBehaviourStateMachine();

    @Override
    public void update() {
        behaviourStateMachine.update();
    }

    public void shoot() {
        ammo--;
        GameEngine.getResourceSystem().playSound(shootSound, false);
    }

    public boolean canShoot() {
        return ammo > 0;
    }

    public void addAmmo(int ammo) {
        this.ammo += ammo;
    }

    public void reset() {
        behaviourStateMachine.reset();
    }

    // --------------------------------------------------

    public int getAmmo() {
        return ammo;
    }

    public int getDamage() {
        return damage;
    }

    public Vector2d getHidePosition() {
        return hidePosition;
    }

    public ValueAnimation<Vector2d> getEquipAnimation() {
        return equipAnimation;
    }

    public ValueAnimation<Vector2d> getHideAnimation() {
        return hideAnimation;
    }

    public SpriteAnimation getShootAnimation() {
        return shootAnimation;
    }

    public StateMachine getBehaviourStateMachine() {
        return behaviourStateMachine;
    }

    public Texture getMainTexture() {
        return mainTexture;
    }

    public String getEmptySound() {
        return emptySound;
    }
}
