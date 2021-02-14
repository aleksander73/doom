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

public abstract class TypeAWeapon extends Weapon {
    public TypeAWeapon(String name, Vector2d equippedPosition, Vector2d dimensions, float h, Colour colour, Texture mainTexture, int ammo, int damage, float equipTime, float hideTime, SpriteAnimation shootAnimation, String shootSoundFilename) {
        super(name, equippedPosition, dimensions, h, colour, mainTexture, ammo, damage, equipTime, hideTime, shootAnimation, shootSoundFilename);
    }

    @Override
    public StateMachine buildBehaviourStateMachine() {
        final String IDLE = "IDLE";
        final String SHOOTING = "SHOOTING";
        final StateMachine stateMachine = new StateMachine(new HashSet<>(Arrays.asList(IDLE, SHOOTING)), IDLE);
        stateMachine.enableTransition(IDLE, SHOOTING);
        stateMachine.enableTransition(SHOOTING, IDLE);
        stateMachine.setOnEnter(IDLE, new Runnable() {
            @Override
            public void run() {
                Texture mainTexture = TypeAWeapon.this.getMainTexture();
                TypeAWeapon.this.getComponent(Material.class).setTexture(mainTexture);
            }
        });
        stateMachine.setAction(IDLE, new Runnable() {
            @Override
            public void run() {
                Vector2d click = InputManager.getInstance().findSector(InputManager.SHOOT_SECTOR).getInput().getTouchDown();
                if(click != null) {
                    if(TypeAWeapon.this.canShoot()) {
                        stateMachine.changeState(SHOOTING);
                    } else {
                        GameEngine.getResourceSystem().playSound(TypeAWeapon.this.getEmptySound(), false);
                    }
                }
            }
        });
        stateMachine.setOnEnter(SHOOTING, () -> {
            TypeAWeapon.this.shoot();
            TypeAWeapon.this.getShootAnimation().start();
        });
        stateMachine.setAction(SHOOTING, () -> {
            TypeAWeapon.this.getShootAnimation().update();
            if(TypeAWeapon.this.getShootAnimation().hasFinished()) {
                stateMachine.changeState(IDLE);
            }
        });
        return stateMachine;
    }
}
