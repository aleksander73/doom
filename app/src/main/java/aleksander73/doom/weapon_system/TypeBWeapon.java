package aleksander73.doom.weapon_system;

import java.util.Arrays;
import java.util.HashSet;

import aleksander73.doom.animation.SpriteAnimation;
import aleksander73.doom.input.InputManager;
import aleksander73.math.linear_algebra.Vector2d;
import aleksander73.vector.adt.StateMachine;
import aleksander73.vector.core.GameEngine;
import aleksander73.vector.rendering.materials.Colour;
import aleksander73.vector.rendering.materials.Material;
import aleksander73.vector.rendering.materials.Texture;

public class TypeBWeapon extends Weapon {
    private static final String IDLE = "IDLE";
    private static final String SHOOTING = "SHOOTING";
    private static final String RELOADING = "RELOADING";
    private StateMachine stateMachine;

    private final SpriteAnimation reloadAnimation;
    private final String reloadSound;

    private boolean reloadQueued;

    public TypeBWeapon(String name, Vector2d equippedPosition, Vector2d dimensions, float h, Colour colour, Texture mainTexture, int ammo, int damage, float equipTime, float hideTime, SpriteAnimation shootAnimation, String shootSound, SpriteAnimation reloadAnimation, String reloadSound) {
        super(name, equippedPosition, dimensions, h, colour, mainTexture, ammo, damage, equipTime, hideTime, shootAnimation, shootSound);
        this.reloadAnimation = reloadAnimation;
        this.reloadSound = reloadSound;
        this.addComponent(reloadAnimation);
    }

    @Override
    public StateMachine buildBehaviourStateMachine() {
        stateMachine = new StateMachine(new HashSet<>(Arrays.asList(IDLE, SHOOTING, RELOADING)), IDLE);
        stateMachine.enableTransition(IDLE, RELOADING);
        stateMachine.enableTransition(IDLE, SHOOTING);
        stateMachine.enableTransition(SHOOTING, RELOADING);
        stateMachine.enableTransition(SHOOTING, IDLE);
        stateMachine.enableTransition(RELOADING, IDLE);
        stateMachine.setOnEnter(IDLE, () -> {
            Texture mainTexture = TypeBWeapon.this.getMainTexture();
            TypeBWeapon.this.getComponent(Material.class).setTexture(mainTexture);
        });
        stateMachine.setAction(IDLE, () -> {
            Vector2d click = InputManager.getInstance().findSector(InputManager.SHOOT_SECTOR).getInput().getTouchDown();
            if(click != null) {
                if(TypeBWeapon.this.canShoot()) {
                    stateMachine.changeState(SHOOTING);
                } else {
                    GameEngine.getResourceSystem().playSound(TypeBWeapon.this.getEmptySound(), false);
                }
            }
            if(reloadQueued) {
                stateMachine.changeState(RELOADING);
                reloadQueued = false;
            }
        });
        stateMachine.setOnEnter(SHOOTING, () -> {
            TypeBWeapon.this.shoot();
            TypeBWeapon.this.getShootAnimation().start();
        });
        stateMachine.setAction(SHOOTING, () -> {
            TypeBWeapon.this.getShootAnimation().update();
            if(TypeBWeapon.this.getShootAnimation().hasFinished()) {
                if(TypeBWeapon.this.canShoot()) {
                    stateMachine.changeState(RELOADING);
                } else {
                    stateMachine.changeState(IDLE);
                }
            }
        });
        stateMachine.setOnEnter(RELOADING, () -> {
            reloadAnimation.start();
            GameEngine.getResourceSystem().playSound(reloadSound, false);
        });
        stateMachine.setAction(RELOADING, () -> {
            reloadAnimation.update();
            if(reloadAnimation.hasFinished()) {
                stateMachine.changeState(IDLE);
            }
        });
        return stateMachine;
    }

    public void queueReload() {
        reloadQueued = true;
    }
}
