package aleksander73.doom.animation;

import aleksander73.vector.animation.ValueAnimation;
import aleksander73.vector.rendering.materials.Material;
import aleksander73.vector.rendering.materials.Texture;

public class SpriteAnimation extends ValueAnimation<Integer> {
    private final Texture[] textures;

    public SpriteAnimation(final float duration, boolean looped, final Texture[] textures) {
        super(duration, looped);
        this.textures = textures;
        this.setF(t -> {
            int index;
            int n = textures.length;
            if(t <= 0.0f) {
                index = 0;
            } else {
                float end = duration * ((n - 1) / (float)n);
                if(t < end) {
                    float a = (n - 1) / end;
                    index = (int)(a * t);
                } else {
                    index = n - 1;
                }
            }
            return index;
        });
    }

    @Override
    public void update() {
        super.update();
        Material material = this.getGameObject().getComponent(Material.class);
        Texture currentTexture = material.getTexture();
        int index = this.value();
        Texture newTexture = textures[index];
        if(newTexture != currentTexture) {
            material.setTexture(newTexture);
        }
    }
}
