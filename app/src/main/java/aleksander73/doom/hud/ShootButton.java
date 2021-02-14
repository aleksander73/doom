package aleksander73.doom.hud;

import aleksander73.doom.input.InputManager;
import aleksander73.doom.input.Sector;
import aleksander73.doom.other.shapes.Ellipse;
import aleksander73.math.linear_algebra.Vector2d;
import aleksander73.vector.core.GameEngine;
import aleksander73.vector.gui.GUIElement;
import aleksander73.vector.gui.GUIUtility;

public class ShootButton extends GUIElement {
    public ShootButton(Vector2d position) {
        super(
            "ShootButton",
            position,
            GUIUtility.screenToNDC(new Vector2d(0.2f, 0.2f)),
            HUDUtility.hudColour,
            GameEngine.getResourceSystem().getTexture("hud/shoot_btn.png")
        );
        Sector shoot = new Sector(InputManager.SHOOT_SECTOR, new Ellipse(position, this.getWidth() / 2.0f, this.getHeight() / 2.0f), 1);
        InputManager.getInstance().addSector(shoot);
    }
}
