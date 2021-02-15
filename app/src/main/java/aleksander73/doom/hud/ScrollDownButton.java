package aleksander73.doom.hud;

import aleksander73.doom.input.InputManager;
import aleksander73.doom.input.Sector;
import aleksander73.doom.other.shapes.Rectangle;
import aleksander73.math.linear_algebra.Vector2d;
import aleksander73.vector.core.GameEngine;
import aleksander73.vector.gui.GUIElement;
import aleksander73.vector.gui.GUIUtility;

public class ScrollDownButton extends GUIElement {
    public ScrollDownButton(Vector2d position) {
        super(
            "ScrollDownButton",
            position,
            GUIUtility.screenToNDC(new Vector2d(0.2f, 0.1f)),
            HUDUtility.hudColour,
            GameEngine.getResourceSystem().getTexture("hud/scroll_down.png")
        );
        Sector scrollDown = new Sector(InputManager.SCROLL_DOWN_SECTOR, new Rectangle(position, this.getWidth(), this.getHeight()), 1);
        InputManager.getInstance().addSector(scrollDown);
    }
}
