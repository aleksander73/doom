package aleksander73.doom.hud;

import aleksander73.math.linear_algebra.Vector2d;
import aleksander73.vector.gui.GUIElement;
import aleksander73.vector.rendering.materials.Texture;

public class HUDSlot extends GUIElement {
    public HUDSlot(Vector2d position, Vector2d dimensions, Texture texture) {
        super(
            "HUDSlot",
            position,
            dimensions,
            2,
            null,
            texture
        );
    }
}
