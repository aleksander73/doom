package aleksander73.doom.enemies;

import java.util.Arrays;
import java.util.HashSet;

import aleksander73.doom.animation.SpriteAnimation;
import aleksander73.doom.weapon_system.AttackInfo;
import aleksander73.doom.weapon_system.TypeBWeapon;
import aleksander73.doom.weapon_system.Weapon;
import aleksander73.math.linear_algebra.Vector2d;
import aleksander73.math.linear_algebra.Vector3d;
import aleksander73.vector.adt.StateMachine;
import aleksander73.vector.core.GameEngine;
import aleksander73.vector.core.GameObject;
import aleksander73.vector.core.Transform;
import aleksander73.vector.rendering.materials.Material;
import aleksander73.vector.rendering.materials.Texture;

public class Imp extends Enemy {
    private SpriteAnimation castingFireballAnimation;

    private static final float FIREBALL_RANGE = 25.0f;
    private static final float FIREBALL_Z_OFFSET = 0.25f;
    private static final float EXPLOSION_DISTANCE = 5.0f;

    private final String fireballSound = "imp_fireball.wav";

    public Imp(Vector2d position) {
        super(
            "Imp",
            position,
            GameEngine.getResourceSystem().loadMesh("imp.mesh"),
            GameEngine.getResourceSystem().getTexture("enemies/imp/walking_1.png"),
            100,
            new SpriteAnimation(0.5f, false, new Texture[] {
                GameEngine.getResourceSystem().getTexture("enemies/imp/dying_1.png"),
                GameEngine.getResourceSystem().getTexture("enemies/imp/dying_2.png")
            }),
            new SpriteAnimation(1.0f, false, new Texture[] {
                GameEngine.getResourceSystem().getTexture("enemies/imp/dying_1.png"),
                GameEngine.getResourceSystem().getTexture("enemies/imp/dying_2.png"),
                GameEngine.getResourceSystem().getTexture("enemies/imp/dying_3.png"),
                GameEngine.getResourceSystem().getTexture("enemies/imp/dying_4.png"),
                GameEngine.getResourceSystem().getTexture("enemies/imp/dying_5.png")
            }),
            "imp_hurt.wav",
            "imp_death.wav"
        );

        this.enableExplosion(
            new SpriteAnimation(1.0f, false, new Texture[] {
                GameEngine.getResourceSystem().getTexture("enemies/imp/exploding_1.png"),
                GameEngine.getResourceSystem().getTexture("enemies/imp/exploding_2.png"),
                GameEngine.getResourceSystem().getTexture("enemies/imp/exploding_3.png"),
                GameEngine.getResourceSystem().getTexture("enemies/imp/exploding_4.png"),
                GameEngine.getResourceSystem().getTexture("enemies/imp/exploding_5.png"),
                GameEngine.getResourceSystem().getTexture("enemies/imp/exploding_6.png"),
                GameEngine.getResourceSystem().getTexture("enemies/imp/exploding_7.png"),
                GameEngine.getResourceSystem().getTexture("enemies/imp/exploding_8.png")}
            ),
            attackInfo -> {
                Weapon weapon = attackInfo.getWeapon();
                Vector3d position1 = Imp.this.getComponent(Transform.class).getPosition();
                Vector3d origin = attackInfo.getOrigin();

                return weapon instanceof TypeBWeapon && position1.distance(origin) < EXPLOSION_DISTANCE;
            },
            "imp_explosion.wav"
        );


        castingFireballAnimation = new SpriteAnimation(1.0f, false, new Texture[] {
                GameEngine.getResourceSystem().getTexture("enemies/imp/casting_fireball_1.png"),
                GameEngine.getResourceSystem().getTexture("enemies/imp/casting_fireball_2.png"),
                GameEngine.getResourceSystem().getTexture("enemies/imp/casting_fireball_3.png")
        });
        this.addComponents(castingFireballAnimation);
    }

    @Override
    public StateMachine buildBehaviourStateMachine() {
        final String THINKING = "THINKING";
        final String CASTING_FIREBALL = "CASTING_FIREBALL";

        final StateMachine stateMachine = new StateMachine(new HashSet<>(Arrays.asList(THINKING, CASTING_FIREBALL)), THINKING) {
            @Override
            public void reset() {
                castingFireballAnimation.reset();
                this.changeState(THINKING);
            }
        };

        stateMachine.enableTransition(THINKING, THINKING);
        stateMachine.enableTransition(THINKING, CASTING_FIREBALL);
        stateMachine.enableTransition(CASTING_FIREBALL, THINKING);

        stateMachine.setOnEnter(THINKING, () -> Imp.this.getComponent(Material.class).setTexture(Imp.this.getMainTexture()));
        stateMachine.setAction(THINKING, () -> {
            Vector3d position = Imp.this.getComponent(Transform.class).getPosition();
            Vector3d playerPosition = Imp.this.getPlayer().getComponent(Transform.class).getPosition();
            float distance = position.distance(playerPosition);
            if(distance < FIREBALL_RANGE) {
                stateMachine.changeState(CASTING_FIREBALL);
            }
        });
        stateMachine.setOnEnter(CASTING_FIREBALL, () -> castingFireballAnimation.start());
        stateMachine.setAction(CASTING_FIREBALL, () -> {
            castingFireballAnimation.update();
            if(castingFireballAnimation.hasFinished()) {
                GameObject.instantiate(Imp.this.createFireball());
                GameEngine.getResourceSystem().playSound(fireballSound, false);
                stateMachine.changeState(THINKING);
            }
        });

        return stateMachine;
    }

    private Fireball createFireball() {
        Transform transform = this.getComponent(Transform.class).copy();
        Vector3d direction = transform.getForward();
        transform.translate(direction.mul(FIREBALL_Z_OFFSET).toVector3d());
        return new Fireball(transform, direction, this.getPlayer());
    }
}
