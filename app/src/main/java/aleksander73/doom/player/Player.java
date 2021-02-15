package aleksander73.doom.player;

import java.util.Arrays;
import java.util.HashSet;

import aleksander73.doom.hud.StatusBar;
import aleksander73.doom.input.InputManager;
import aleksander73.doom.input.Sector;
import aleksander73.doom.managers.SoundManager;
import aleksander73.doom.other.shapes.Rectangle;
import aleksander73.doom.weapon_system.TypeBWeapon;
import aleksander73.doom.weapon_system.Weapon;
import aleksander73.doom.weapon_system.weapons.Pistol;
import aleksander73.doom.weapon_system.weapons.Shotgun;
import aleksander73.doom.weapon_system.weapons.SuperShotgun;
import aleksander73.math.linear_algebra.Vector2d;
import aleksander73.math.linear_algebra.Vector3d;
import aleksander73.vector.adt.StateMachine;
import aleksander73.vector.animation.ValueAnimation;
import aleksander73.vector.core.GameEngine;
import aleksander73.vector.core.GameObject;
import aleksander73.vector.core.Transform;
import aleksander73.vector.rendering.renderers.Renderer;
import aleksander73.vector.time.Time;

public class Player extends GameObject {
    private final Transform transform;
    private final float H = 1.25f;

    public static final int MAX_HEALTH = 100;
    private int health;
    public static final int MAX_ARMOUR = 200;
    private int armour;

    private float moveSpeed = 20.0f;
    private float maxMoveSpeed = 0.2f;
    private float rotateSpeed = 500.0f;
    private float maxRotateSpeed = 6.0f;

    private final StateMachine stateMachine;
    private static final String IDLE = "IDLE";
    private static final String HIDING_WEAPON = "HIDING_WEAPON";
    private static final String SWITCHING_WEAPON = "SWITCHING_WEAPON";
    private static final String EQUIPPING_WEAPON = "EQUIPPING_WEAPON";

    private StatusBar statusBar;

    private final Inventory inventory;

    private final String hurtSound = "player_hurt.wav";
    private final String deathSound = "player_death.wav";

    public Player(Vector2d position) {
        super("Player");
        transform = new Transform(new Vector3d(position.getX(), H, position.getY()));
        this.addComponent(transform);
        health = MAX_HEALTH;
        armour = 100;
        inventory = new Inventory();
        stateMachine = new StateMachine(new HashSet<>(Arrays.asList(IDLE, HIDING_WEAPON, SWITCHING_WEAPON, EQUIPPING_WEAPON)), IDLE);
        stateMachine.enableTransition(IDLE, HIDING_WEAPON);
        stateMachine.enableTransition(HIDING_WEAPON, SWITCHING_WEAPON);
        stateMachine.enableTransition(IDLE, SWITCHING_WEAPON);
        stateMachine.enableTransition(SWITCHING_WEAPON, EQUIPPING_WEAPON);
        stateMachine.enableTransition(EQUIPPING_WEAPON, IDLE);
        stateMachine.setOnEnter(IDLE, () -> inventory.getEquippedWeapon().setActive(true));
        stateMachine.setAction(IDLE, () -> {
            if(inventory.getEquippedWeapon() == null || !inventory.getEquippedWeapon().getBehaviourStateMachine().currentState().equals(IDLE) || inventory.size() < 2) {
                return;
            }
            if(InputManager.getInstance().findSector(InputManager.SCROLL_UP_SECTOR).getInput().getTouchDown() != null) {
                Player.this.equip(inventory.weaponDown());
            }
            if(InputManager.getInstance().findSector(InputManager.SCROLL_DOWN_SECTOR).getInput().getTouchDown() != null) {
                Player.this.equip(inventory.weaponUp());
            }
        });
        stateMachine.setOnExit(IDLE, () -> {
            if(inventory.getEquippedWeapon() != null) {
                inventory.getEquippedWeapon().setActive(false);
            }
        });
        stateMachine.setOnEnter(HIDING_WEAPON, () -> {
            inventory.getEquippedWeapon().getHideAnimation().start();
        });
        stateMachine.setAction(HIDING_WEAPON, () -> {
            ValueAnimation<Vector2d> hideAnimation = inventory.getEquippedWeapon().getHideAnimation();
            hideAnimation.update();
            inventory.getEquippedWeapon().getComponent(Transform.class).setPosition(hideAnimation.value().toVector3d());
            if(hideAnimation.hasFinished()) {
                stateMachine.changeState(SWITCHING_WEAPON);
            }
        });
        stateMachine.setAction(SWITCHING_WEAPON, () -> {
            if(inventory.getEquippedWeapon() != null) {
                inventory.getEquippedWeapon().getComponent(Renderer.class).setActive(false);
            }
            inventory.switchWeapons();
            inventory.getEquippedWeapon().getComponent(Renderer.class).setActive(true);
            statusBar.updateAmmo(inventory.getEquippedWeapon().getAmmo());
            stateMachine.changeState(EQUIPPING_WEAPON);
        });
        stateMachine.setOnEnter(EQUIPPING_WEAPON, () -> {
            inventory.getEquippedWeapon().getEquipAnimation().start();
        });
        stateMachine.setAction(EQUIPPING_WEAPON, () -> {
            ValueAnimation<Vector2d> equipAnimation = inventory.getEquippedWeapon().getEquipAnimation();
            equipAnimation.update();
            inventory.getEquippedWeapon().getComponent(Transform.class).setPosition(equipAnimation.value().toVector3d());
            if(equipAnimation.hasFinished()) {
                stateMachine.changeState(IDLE);
            }
        });
        Sector moveSector = new Sector(InputManager.MOVE_SECTOR, new Rectangle(new Vector2d(-0.5f, 0.0f), 1.0f, 2.0f), 0);
        InputManager.getInstance().addSector(moveSector);
        Sector rotateSector = new Sector(InputManager.ROTATE_SECTOR, new Rectangle(new Vector2d(0.5f, 0.0f), 1.0f, 2.0f), 0);
        InputManager.getInstance().addSector(rotateSector);
    }

    @Override
    public void start() {
        GameObject myCamera = this.getScene().find("Camera");
        myCamera.getComponent(Transform.class).setParent(transform);
        statusBar = (StatusBar)Player.this.getScene().find("StatusBar");
        statusBar.updateHealth(health);
        statusBar.updateArmour(armour);
        Pistol pistol = (Pistol)this.getScene().find("Pistol");
        this.collect(pistol);
        Shotgun shotgun = (Shotgun)this.getScene().find("Shotgun");
        this.collect(shotgun);
        SuperShotgun superShotgun = (SuperShotgun)this.getScene().find("SuperShotgun");
        this.collect(superShotgun);
        this.equip(pistol);
    }

    @Override
    public void update() {
        Vector2d move_r = InputManager.getInstance().findSector(InputManager.MOVE_SECTOR).getInput().r();
        Vector3d moveDelta = new Vector3d(move_r.getX(), 0.0f, -move_r.getY()).mul(moveSpeed * Time.getDeltaTime()).toVector3d();
        if(moveDelta.magnitude() > maxMoveSpeed) {
            moveDelta = moveDelta.resize(maxMoveSpeed).toVector3d();
        }
        transform.translate(transform, moveDelta);

        Vector2d rotate_r = InputManager.getInstance().findSector(InputManager.ROTATE_SECTOR).getInput().r();
        float rotateDelta = -rotate_r.getX() * rotateSpeed * Time.getDeltaTime();
        if(rotateDelta > maxRotateSpeed) {
            rotateDelta = maxRotateSpeed;
        } else if(rotateDelta < -maxRotateSpeed) {
            rotateDelta = -maxRotateSpeed;
        }
        transform.rotate(Vector3d.yUnitVector, rotateDelta);

        stateMachine.update();
    }

    public void collect(Weapon weapon) {
        Weapon possessedWeapon = inventory.findWeaponOfType(weapon);
        if(possessedWeapon != null) {
            if(possessedWeapon instanceof TypeBWeapon && possessedWeapon.getAmmo() == 0) {
                ((TypeBWeapon)possessedWeapon).queueReload();
            }
            possessedWeapon.addAmmo(weapon.getAmmo());
            if(possessedWeapon == inventory.getEquippedWeapon()) {
                statusBar.updateAmmo(inventory.getEquippedWeapon().getAmmo());
            }
        } else {
            weapon.getComponent(Transform.class).setPosition(weapon.getHidePosition().toVector3d());
            inventory.addWeapon(weapon);
        }
        statusBar.getDoomGuy().smile();
    }

    public void equip(Weapon weapon) {
        if(weapon == inventory.getEquippedWeapon() || inventory.findWeaponOfType(weapon)== null) {
            return;
        }
        inventory.setWeaponToEquip(weapon);
        if(inventory.getEquippedWeapon() != null) {
            stateMachine.changeState(HIDING_WEAPON);
        } else {
            stateMachine.changeState(SWITCHING_WEAPON);
        }
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void hurt(int points) {
        int dArmour = (int)((armour / (float)MAX_ARMOUR) * points);
        int dHealth = points - dArmour;
        if(dArmour > armour) {
            dHealth += dArmour - armour;
        }
        this.strengthen(-dArmour);
        this.heal(-dHealth);
        CameraLens cameraLens = (CameraLens)this.getScene().find("CameraLens");
        if(this.isAlive()) {
            cameraLens.onHurt();
            GameEngine.getResourceSystem().playSound(hurtSound, false);
            statusBar.getDoomGuy().hurt();
        } else {
            inventory.getEquippedWeapon().reset();
            this.setActive(false);
            cameraLens.onDied();
            GameEngine.getResourceSystem().playSound(deathSound, false);
            SoundManager soundManager = (SoundManager)this.getScene().find("SoundManager");
            soundManager.fadeOut();
        }
    }

    public void heal(int points) {
        health += points;
        if(health > Player.MAX_HEALTH) {
            health = Player.MAX_HEALTH;
        } else if(health <= 0) {
            health = 0;
        }
        statusBar.updateHealth(health);
    }

    public void strengthen(int points) {
        armour += points;
        if(armour > Player.MAX_ARMOUR) {
            armour = Player.MAX_ARMOUR;
        } else if(armour < 0) {
            armour = 0;
        }
        statusBar.updateArmour(armour);
    }

    public int getHealth() {
        return health;
    }

    public int getArmour() {
        return armour;
    }
}
