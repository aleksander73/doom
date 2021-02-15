package aleksander73.doom.enemies;

import java.util.Arrays;
import java.util.HashSet;

import aleksander73.doom.animation.SpriteAnimation;
import aleksander73.doom.game_object.Model;
import aleksander73.doom.hud.StatusBar;
import aleksander73.doom.player.Player;
import aleksander73.doom.weapon_system.AttackInfo;
import aleksander73.doom.weapon_system.Weapon;
import aleksander73.math.linear_algebra.Vector2d;
import aleksander73.math.linear_algebra.Vector3d;
import aleksander73.vector.adt.StateMachine;
import aleksander73.vector.core.GameEngine;
import aleksander73.vector.core.Transform;
import aleksander73.vector.rendering.Shaders;
import aleksander73.vector.rendering.materials.Material;
import aleksander73.vector.rendering.materials.Texture;
import aleksander73.vector.rendering.mesh.Mesh;
import aleksander73.vector.time.Time;
import aleksander73.vector.utility.functional_interface.Function;

public abstract class Enemy extends Model {
    private int health;
    protected static final float H = 1.0f;

    private final StateMachine conditionStateMachine;
    private static final String IDLE = "IDLE";
    private static final String HURT = "HURT";
    private static final String DYING = "DYING";
    private static final String EXPLODING = "EXPLODING";
    private static final String DEAD = "DEAD";

    private boolean explosionEnabled;
    private Function<AttackInfo, Boolean> shouldExplode = input -> false;

    private final StateMachine behaviourStateMachine;
    private Player player;

    private final String hurtSound;
    private final String dieSound;
    private String explosionSound;

    public Enemy(String name, Vector2d position, Mesh mesh, Texture mainTexture, final int health, final SpriteAnimation hurtAnimation, final SpriteAnimation dyingAnimation, String hurtSound, String dieSound) {
        super(
            name,
            new Transform(new Vector3d(position.getX(), H, position.getY())),
            mesh,
            Shaders.getStandardShader(),
            null,
            mainTexture
        );
        this.addComponents(hurtAnimation, dyingAnimation);
        this.health = health;
        behaviourStateMachine = this.buildBehaviourStateMachine();
        conditionStateMachine = new StateMachine(new HashSet<>(Arrays.asList(IDLE, HURT, DYING, EXPLODING, DEAD)), IDLE);
        conditionStateMachine.enableTransition(IDLE, HURT);
        conditionStateMachine.enableTransition(HURT, IDLE);
        conditionStateMachine.enableTransition(HURT, HURT);
        conditionStateMachine.enableTransition(HURT, DYING);
        conditionStateMachine.enableTransition(IDLE, DYING);
        conditionStateMachine.enableTransition(DYING, DEAD);
        conditionStateMachine.setOnEnter(IDLE, () -> {
            Texture mainTexture1 = Enemy.this.getMainTexture();
            Enemy.this.getComponent(Material.class).setTexture(mainTexture1);
        });
        conditionStateMachine.setOnExit(IDLE, behaviourStateMachine::reset);
        conditionStateMachine.setOnEnter(HURT, () -> {
            hurtAnimation.reset();
            hurtAnimation.start();
        });
        conditionStateMachine.setAction(HURT, () -> {
            hurtAnimation.update();
            if(hurtAnimation.hasFinished()) {
                conditionStateMachine.changeState(IDLE);
            }
        });
        conditionStateMachine.setOnEnter(DYING, dyingAnimation::start);
        conditionStateMachine.setAction(DYING, () -> {
            dyingAnimation.update();
            if(dyingAnimation.hasFinished()) {
                conditionStateMachine.changeState(DEAD);
            }
        });
        this.hurtSound = hurtSound;
        this.dieSound = dieSound;
    }

    @Override
    public void start() {
        player = (Player)this.getScene().find("Player");
    }

    @Override
    public void update() {
        Transform transform = this.getComponent(Transform.class);
        transform.rotate( Vector3d.yUnitVector, 60.0f * Time.getDeltaTime());
        String currentState = conditionStateMachine.currentState();
        conditionStateMachine.update();
        if(currentState.equals(IDLE)) {
            behaviourStateMachine.update();
        }
    }

    protected abstract StateMachine buildBehaviourStateMachine();

    public boolean isAlive() {
        return health > 0;
    }

    public void applyDamage(AttackInfo attackInfo) {
        Weapon weapon = attackInfo.getWeapon();
        health -= weapon.getDamage();

        if(health > 0) {
            conditionStateMachine.changeState(HURT);
            GameEngine.getResourceSystem().playSound(hurtSound, false);
        } else {
            health = 0;
            if(explosionEnabled && this.shouldExplode.accept(attackInfo)) {
                conditionStateMachine.changeState(EXPLODING);
                GameEngine.getResourceSystem().playSound(explosionSound, false);
            } else {
                conditionStateMachine.changeState(DYING);
                GameEngine.getResourceSystem().playSound(dieSound, false);
            }

            StatusBar statusBar = (StatusBar)this.getScene().find("StatusBar");
            statusBar.getDoomGuy().smile();
        }
    }

    public void enableExplosion(final SpriteAnimation explosionAnimation, Function<AttackInfo, Boolean> shouldExplode, String explosionSound) {
        if(explosionEnabled) {
            return;
        }
        explosionEnabled = true;
        this.addComponent(explosionAnimation);
        this.shouldExplode = shouldExplode;
        this.explosionSound = explosionSound;
        conditionStateMachine.enableTransition(IDLE, EXPLODING);
        conditionStateMachine.enableTransition(HURT, EXPLODING);
        conditionStateMachine.enableTransition(EXPLODING, DEAD);
        conditionStateMachine.setOnEnter(EXPLODING, explosionAnimation::start);
        conditionStateMachine.setAction(EXPLODING, () -> {
            if(explosionAnimation.hasFinished()) {
                conditionStateMachine.changeState(DEAD);
                return;
            }
            explosionAnimation.update();
        });
    }

    public Player getPlayer() {
        return player;
    }
}
