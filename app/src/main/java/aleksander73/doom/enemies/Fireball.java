package aleksander73.doom.enemies;

import java.util.Arrays;
import java.util.HashSet;

import aleksander73.doom.animation.SpriteAnimation;
import aleksander73.doom.game_object.Model;
import aleksander73.doom.player.Player;
import aleksander73.math.linear_algebra.Vector3d;
import aleksander73.vector.adt.StateMachine;
import aleksander73.vector.core.GameEngine;
import aleksander73.vector.core.Transform;
import aleksander73.vector.rendering.Shaders;
import aleksander73.vector.rendering.materials.Texture;
import aleksander73.vector.time.Time;
import aleksander73.vector.time.Timer;

public class Fireball extends Model {
    private final int damage = 40;
    private final float velocity = 5.0f;
    private final Timer timer = new Timer();
    private static final float timeToLive = 10.0f;
    private final SpriteAnimation idleAnimation;
    private final SpriteAnimation explodingAnimation;
    private final Player player;
    private final StateMachine stateMachine;
    private static final String IDLE = "IDLE";
    private static final String EXPLODING = "EXPLODING";
    private static final float EXPLOSION_DISTANCE = 1.0f;

    public Fireball(final Transform transform, final Vector3d direction, final Player player) {
        super(
            "Fireball",
            transform,
            GameEngine.getResourceSystem().loadMesh("fireball.mesh"),
            Shaders.getStandardShader(),
            null,
            GameEngine.getResourceSystem().getTexture("effects/fireball/idle_1.png")
        );
        this.player = player;
        idleAnimation = new SpriteAnimation(0.2f, true, new Texture[] {
            GameEngine.getResourceSystem().getTexture("effects/fireball/idle_1.png"),
            GameEngine.getResourceSystem().getTexture("effects/fireball/idle_2.png")
        });
        explodingAnimation = new SpriteAnimation(0.3f, false, new Texture[] {
            GameEngine.getResourceSystem().getTexture("effects/fireball/fireball_explode_1.png"),
            GameEngine.getResourceSystem().getTexture("effects/fireball/fireball_explode_2.png"),
            GameEngine.getResourceSystem().getTexture("effects/fireball/fireball_explode_3.png")
        });
        this.addComponents(idleAnimation, explodingAnimation);
        stateMachine = new StateMachine(new HashSet<>(Arrays.asList(IDLE, EXPLODING)), IDLE);
        stateMachine.enableTransition(IDLE, EXPLODING);
        stateMachine.setAction(IDLE, () -> {
            if(timer.elapsedTime() > timeToLive) {
                stateMachine.changeState(EXPLODING);
            }
            Vector3d position = Fireball.this.getComponent(Transform.class).getPosition();
            Vector3d playerPosition = player.getComponent(Transform.class).getPosition();
            if(position.distance(playerPosition) < EXPLOSION_DISTANCE) {
                stateMachine.changeState(EXPLODING);
                if(player.isAlive()) {
                    player.hurt(damage);
                }
                return;
            }
            Vector3d delta = direction.mul(velocity * Time.getDeltaTime()).toVector3d();
            transform.translate(delta);
            idleAnimation.update();
        });
        stateMachine.setOnEnter(EXPLODING, explodingAnimation::start);
        stateMachine.setAction(EXPLODING, () -> {
            explodingAnimation.update();
            if(explodingAnimation.hasFinished()) {
                Fireball.this.destroy();
            }
        });
    }

    @Override
    public void start() {
        idleAnimation.start();
        timer.start();
    }

    @Override
    public void update() {
        stateMachine.update();
        Transform transform = this.getComponent(Transform.class);
        Vector3d playerPosition = player.getComponent(Transform.class).getPosition();
        transform.lookAt(playerPosition);
    }
}
