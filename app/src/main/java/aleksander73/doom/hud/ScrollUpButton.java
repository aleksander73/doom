package aleksander73.doom.hud;

import aleksander73.doom.input.InputManager;
import aleksander73.doom.input.Sector;
import aleksander73.doom.other.shapes.Rectangle;
import aleksander73.math.linear_algebra.Vector2d;
import aleksander73.vector.core.GameEngine;
import aleksander73.vector.gui.GUIElement;
import aleksander73.vector.gui.GUIUtility;

public class ScrollUpButton extends GUIElement {
    public ScrollUpButton(Vector2d position) {
        super(
            "ScrollUpButton",
            position,
            GUIUtility.screenToNDC(new Vector2d(0.2f, 0.1f)),
            HUDUtility.hudColour,
            GameEngine.getResourceSystem().getTexture("hud/scroll_up.png")
        );
        Sector scrollUp = new Sector(InputManager.SCROLL_UP_SECTOR, new Rectangle(position, this.getWidth(), this.getHeight()), 1);
        InputManager.getInstance().addSector(scrollUp);
    }
}
