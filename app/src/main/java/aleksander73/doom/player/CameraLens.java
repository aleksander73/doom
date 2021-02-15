package aleksander73.doom.player;

import java.util.Arrays;
import java.util.HashSet;

import aleksander73.math.linear_algebra.Vector2d;
import aleksander73.vector.adt.StateMachine;
import aleksander73.vector.animation.ValueAnimation;
import aleksander73.vector.core.GameEngine;
import aleksander73.vector.gui.GUIElement;
import aleksander73.vector.rendering.materials.Colour;
import aleksander73.vector.rendering.materials.Material;
import aleksander73.vector.rendering.materials.Texture;
import aleksander73.vector.utility.functional_interface.Function;

public class CameraLens extends GUIElement {
    private static final Colour DEFAULT_LENS_COLOUR = new Colour(0, 0, 0, 0);
    private final ValueAnimation<Float> collectAnimation;
    private static final Colour COLLECT_LENS_COLOUR = new Colour(255, 255, 0, 0);
    private static final int COLLECT_ALPHA_MAX = 32;
    private final float COLLECT_ANIMATION_TIME = 0.5f;
    private ValueAnimation<Float> hurtAnimation;
    private static final Colour HURT_LENS_COLOUR = new Colour(255, 0, 0, 0);
    private static final int HURT_ALPHA_MAX = 128;
    private final float HURT_ANIMATION_TIME = 1.0f;
    private ValueAnimation<Float> dieAnimation;
    private static final Colour DIE_LENS_COLOUR = new Colour(255, 0, 0, 0);
    private static final int DIE_ALPHA_MAX = 255;
    private final float DIE_ANIMATION_TIME = 5.0f;
    private ValueAnimation<Float> animationToPlay;
    private final StateMachine stateMachine;
    private final String IDLE = "IDLE";
    private final String RUNNING = "RUNNING";
    private Material material;
    private final Texture defaultTex;
    private final Texture gameOverTex;

    public CameraLens() {
        super("CameraLens", new Vector2d(0.0f, 0.0f), new Vector2d(2.0f, 2.0f), 3, DEFAULT_LENS_COLOUR, null);
        collectAnimation = new ValueAnimation<>(COLLECT_ANIMATION_TIME, false, t -> (float)(COLLECT_ALPHA_MAX * Math.sin((Math.PI / COLLECT_ANIMATION_TIME) * t)));
        hurtAnimation = new ValueAnimation<>(HURT_ANIMATION_TIME, false, t -> (float)(HURT_ALPHA_MAX * Math.sin((Math.PI / HURT_ANIMATION_TIME) * t)));
        dieAnimation = new ValueAnimation<>(DIE_ANIMATION_TIME, false, t -> (float)(DIE_ALPHA_MAX * Math.sin((Math.PI / (2.0f * DIE_ANIMATION_TIME)) * t)));
        this.addComponents(collectAnimation);
        defaultTex = GameEngine.getResourceSystem().getDefaultTex();
        gameOverTex = GameEngine.getResourceSystem().getTexture("hud/game_over.png");
        stateMachine = new StateMachine(new HashSet<>(Arrays.asList(IDLE, RUNNING)), IDLE);
        stateMachine.enableTransition(IDLE, RUNNING);
        stateMachine.enableTransition(RUNNING, RUNNING);
        stateMachine.enableTransition(RUNNING, IDLE);
        stateMachine.setOnEnter(RUNNING, () -> {
            animationToPlay.reset();
            animationToPlay.start();
        });
        stateMachine.setAction(RUNNING, () -> {
            animationToPlay.update();
            int alpha = (int)(animationToPlay.value().floatValue());
            material.getColour().setAlpha(alpha);
            if(animationToPlay.hasFinished()) {
                stateMachine.changeState(IDLE);
            }
        });
    }

    @Override
    public void start() {
        material = this.getComponent(Material.class);
    }

    @Override
    public void update() {
        stateMachine.update();
    }

    public void onCollected() {
        material.setColour(COLLECT_LENS_COLOUR);
        material.setTexture(defaultTex);
        animationToPlay = collectAnimation;
        stateMachine.changeState(RUNNING);
    }

    public void onHurt() {
        material.setTexture(defaultTex);
        animationToPlay = hurtAnimation;
        material.setColour(HURT_LENS_COLOUR);
        stateMachine.changeState(RUNNING);
    }

    public void onDied() {
        material.setTexture(gameOverTex);
        animationToPlay = dieAnimation;
        material.setColour(DIE_LENS_COLOUR);
        stateMachine.changeState(RUNNING);
    }
}
