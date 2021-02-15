package aleksander73.doom.managers;

import java.util.List;

import aleksander73.doom.enemies.Enemy;
import aleksander73.doom.hud.StatusBar;
import aleksander73.doom.input.InputManager;
import aleksander73.doom.player.CameraLens;
import aleksander73.doom.player.Player;
import aleksander73.vector.core.GameObject;
import aleksander73.vector.utility.ListUtility;

public class GameManager extends GameObject {
    private final Runnable checkGameWon;

    public GameManager() {
        super("GameManager");
        checkGameWon = () -> {
            List<GameObject> gameObjects = GameManager.this.getScene().getGameObjects();
            List<GameObject> activeEnemies = ListUtility.filter(gameObjects, gameObject -> gameObject instanceof Enemy && ((Enemy)gameObject).isAlive());
            if(activeEnemies.size() == 0)  {
                GameManager.this.onGameWon();
                GameManager.this.setActive(false);
            }
        };
    }

    @Override
    public void update() {
        this.getScene().getOnUpdated().queueRunnable(checkGameWon);
    }

    private void onGameWon() {
        Player player = (Player)this.getScene().find("Player");
        player.setActive(false);
        InputManager.getInstance().setEnabled(false);
        InputManager.getInstance().clear();
        StatusBar statusBar = (StatusBar)this.getScene().find("StatusBar");
        statusBar.getDoomGuy().smile();
        CameraLens cameraLens = (CameraLens)this.getScene().find("CameraLens");
        cameraLens.onGameWon();
    }
}
