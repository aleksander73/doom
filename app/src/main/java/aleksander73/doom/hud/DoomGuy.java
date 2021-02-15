package aleksander73.doom.hud;

import java.util.Arrays;
import java.util.HashSet;

import aleksander73.doom.player.Player;
import aleksander73.math.linear_algebra.Vector2d;
import aleksander73.vector.adt.StateMachine;
import aleksander73.vector.core.GameEngine;
import aleksander73.vector.gui.GUIElement;
import aleksander73.vector.rendering.materials.Material;
import aleksander73.vector.rendering.materials.Texture;
import aleksander73.vector.time.Timer;

public class DoomGuy extends GUIElement {
    private final String IDLE = "IDLE";
    private final String TIMEOUT = "TIMEOUT";
    private final StateMachine stateMachine;
    private final Texture[] idleTextures;
    private final Texture[] smileTextures;
    private final Texture[] hurtTextures;
    private final Texture deadTexture;
    private Texture[] currentTextureSet;
    private final Timer timer = new Timer();
    private final float timeout = 1.0f;

    public DoomGuy(Vector2d position, Vector2d dimensions) {
        super(
            "DoomGuy",
            position,
            dimensions,
            2,
            null,
            GameEngine.getResourceSystem().getTexture("hud/doom_guy/idle_0.png")
        );
        idleTextures = new Texture[] {
            GameEngine.getResourceSystem().getTexture("hud/doom_guy/idle_0.png"),
            GameEngine.getResourceSystem().getTexture("hud/doom_guy/idle_1.png"),
            GameEngine.getResourceSystem().getTexture("hud/doom_guy/idle_2.png"),
            GameEngine.getResourceSystem().getTexture("hud/doom_guy/idle_3.png"),
            GameEngine.getResourceSystem().getTexture("hud/doom_guy/idle_4.png")
        };
        smileTextures = new Texture[] {
            GameEngine.getResourceSystem().getTexture("hud/doom_guy/smile_0.png"),
            GameEngine.getResourceSystem().getTexture("hud/doom_guy/smile_1.png"),
            GameEngine.getResourceSystem().getTexture("hud/doom_guy/smile_2.png"),
            GameEngine.getResourceSystem().getTexture("hud/doom_guy/smile_3.png"),
            GameEngine.getResourceSystem().getTexture("hud/doom_guy/smile_4.png"),
        };
        hurtTextures = new Texture[] {
            GameEngine.getResourceSystem().getTexture("hud/doom_guy/hurt_0.png"),
            GameEngine.getResourceSystem().getTexture("hud/doom_guy/hurt_1.png"),
            GameEngine.getResourceSystem().getTexture("hud/doom_guy/hurt_2.png"),
            GameEngine.getResourceSystem().getTexture("hud/doom_guy/hurt_3.png"),
            GameEngine.getResourceSystem().getTexture("hud/doom_guy/hurt_4.png"),
        };
        deadTexture = GameEngine.getResourceSystem().getTexture("hud/doom_guy/dead.png");
        currentTextureSet = idleTextures;
        stateMachine = new StateMachine(new HashSet<>(Arrays.asList(IDLE, TIMEOUT)), IDLE);
        stateMachine.enableTransition(IDLE, TIMEOUT);
        stateMachine.enableTransition(TIMEOUT, TIMEOUT);
        stateMachine.enableTransition(TIMEOUT, IDLE);
        stateMachine.setOnEnter(IDLE, () -> currentTextureSet = idleTextures);
        stateMachine.setOnEnter(TIMEOUT, timer::start);
        stateMachine.setAction(TIMEOUT, () -> {
            if(timer.elapsedTime() > timeout) {
                stateMachine.changeState(IDLE);
            }
        });
        stateMachine.setOnExit(TIMEOUT, timer::stop);
    }

    @Override
    public void update() {
        stateMachine.update();
        Texture texture = DoomGuy.this.getTextureFrom(currentTextureSet);
        DoomGuy.this.getComponent(Material.class).setTexture(texture);
    }

    public void smile() {
        currentTextureSet = smileTextures;
        stateMachine.changeState(TIMEOUT);
    }

    public void hurt() {
        currentTextureSet = hurtTextures;
        stateMachine.changeState(TIMEOUT);
    }

    private Texture getTextureFrom(Texture[] textureSet) {
        Texture texture;
        Player player = (Player)this.getScene().find("Player");
        int index = ((100 - player.getHealth()) / 20);
        if(index < 5) {
            texture = textureSet[index];
        } else {
            texture = deadTexture;
        }
        return texture;
    }
}
