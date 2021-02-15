package aleksander73.doom.hud;

import aleksander73.math.linear_algebra.Vector2d;
import aleksander73.vector.core.GameEngine;
import aleksander73.vector.gui.GUIElement;
import aleksander73.vector.gui.GUIUtility;
import aleksander73.vector.rendering.materials.Colour;

public class Credits extends GUIElement {
    public Credits() {
        super(
            "Credits",
            new Vector2d(0.575f, -0.55f),
            GUIUtility.screenToNDC(new Vector2d(0.8f, 0.075f)),
            new Colour(128, 128, 128, 255),
            GameEngine.getResourceSystem().getTexture("hud/credits.png")
        );
    }
}
