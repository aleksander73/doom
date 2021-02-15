package aleksander73.doom.managers;

import aleksander73.vector.animation.ValueAnimation;
import aleksander73.vector.core.GameEngine;
import aleksander73.vector.core.GameObject;

public class SoundManager extends GameObject {
    private boolean fadingOut;
    private final ValueAnimation<Float> fadeOutAnimation;

    public SoundManager() {
        super("SoundManager");
        final float duration = 5.0f;
        fadeOutAnimation = new ValueAnimation<>(duration, false, t -> 1.0f - t / duration);
        this.addComponent(fadeOutAnimation);
    }

    @Override
    public void update() {
        if(fadingOut) {
            fadeOutAnimation.update();
            float volume = fadeOutAnimation.value();
            GameEngine.getResourceSystem().getMediaPlayer().setVolume(volume, volume);
        }
    }

    public void fadeOut() {
        fadeOutAnimation.start();
        fadingOut = true;
    }
}
