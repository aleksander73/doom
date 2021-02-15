package aleksander73.doom;

import java.util.Arrays;

import aleksander73.doom.collectables.ArmourCollectable;
import aleksander73.doom.collectables.HealthCollectable;
import aleksander73.doom.collectables.ShotgunCollectable;
import aleksander73.doom.collectables.SuperShotgunCollectable;
import aleksander73.doom.enemies.Imp;
import aleksander73.doom.hud.Credits;
import aleksander73.doom.hud.DoomGuy;
import aleksander73.doom.hud.HUDSlot;
import aleksander73.doom.hud.ScrollDownButton;
import aleksander73.doom.hud.ScrollUpButton;
import aleksander73.doom.hud.ShootButton;
import aleksander73.doom.hud.StatusBar;
import aleksander73.doom.input.InputManager;
import aleksander73.doom.managers.AlphaSortingManager;
import aleksander73.doom.managers.GameManager;
import aleksander73.doom.managers.SoundManager;
import aleksander73.doom.other.Floor;
import aleksander73.doom.player.CameraLens;
import aleksander73.doom.player.MyCamera;
import aleksander73.doom.player.Player;
import aleksander73.doom.weapon_system.weapons.Pistol;
import aleksander73.doom.weapon_system.weapons.Shotgun;
import aleksander73.doom.weapon_system.weapons.SuperShotgun;
import aleksander73.math.linear_algebra.Vector2d;
import aleksander73.vector.core.Game;
import aleksander73.vector.core.GameEngine;
import aleksander73.vector.core.Transform;
import aleksander73.vector.rendering.Camera;
import aleksander73.vector.scene.Scene;

public class Doom extends Game {
    public Doom() {
        Camera.getActiveCamera().setFov(65.0f);
    }

    @Override
    protected Scene buildScene() {
        Vector2d playerPosition = new Vector2d(0.0f, 0.0f);

        Skybox skybox = new Skybox(GameEngine.getResourceSystem().getTexture("skyboxes/hell.png"));
        final float u = 3.0f;
        Vector2d square = new Vector2d(2 * u, 2 * u);

        Floor start = Floor.createFloor(new Vector2d(0 * u,  0 * u), new Vector2d(4 * u, 2 * u));
        Floor platform1 = Floor.createFloor(new Vector2d(0 * u,  -6 * u), square);
        Floor platform2 = Floor.createFloor(new Vector2d(6 * u,  -6 * u), square);
        Vector2d platform3Position = new Vector2d(12 * u,  -6 * u);
        Floor platform3 = Floor.createFloor(platform3Position, square);
        Floor platform4 = Floor.createFloor(new Vector2d(0 * u,  -12 * u), square);
        Floor platform5 = Floor.createFloor(new Vector2d(6 * u,  -12 * u), square);
        Vector2d platform6Position = new Vector2d(12 * u,  -12 * u);
        Floor platform6 = Floor.createFloor(platform6Position, square);
        Floor platform7 = Floor.createFloor(new Vector2d(3 * u,  -19 * u), square);
        Floor platform8 = Floor.createFloor(new Vector2d(0 * u,  -26 * u), square);
        Floor platform9 = Floor.createFloor(new Vector2d(12 * u,  -26 * u), square);

        Vector2d platform10Position = new Vector2d(3 * u, -5 * u);
        Floor platform10 = Floor.createFloor(platform10Position, square);
        Vector2d platform11Position = new Vector2d(11 * u, -9 * u);
        Floor platform11 = Floor.createFloor(platform11Position, square);
        Vector2d platform12Position = new Vector2d(1 * u, -19 * u);
        Floor platform12 = Floor.createFloor(platform12Position, square);

        Floor end = Floor.createFloor(new Vector2d(22 * u,  -26 * u), new Vector2d(6 * u, 8 * u));

        float defLen = 4 * u;
        Vector2d vertical = new Vector2d(u, defLen);
        Vector2d horizontal = new Vector2d(defLen, u);
        Floor bridge1 = Floor.createFloor(new Vector2d(0 * u,  -3 * u), vertical);
        Floor bridge2 = Floor.createFloor(new Vector2d(3 * u,  -6 * u), horizontal);
        Floor bridge3 = Floor.createFloor(new Vector2d(9 * u,  -6 * u), horizontal);
        Floor bridge4 = Floor.createFloor(new Vector2d(6 * u,  -9 * u), horizontal);
        Floor bridge5 = Floor.createFloor(new Vector2d(12 * u,  -9 * u), vertical);
        Floor bridge6 = Floor.createFloor(new Vector2d(3 * u,  -12 * u), horizontal);
        Floor bridge7 = Floor.createFloor(new Vector2d(9 * u,  -12 * u), horizontal);
        Floor bridge8 = Floor.createFloor(new Vector2d(0 * u,  -19 * u), new Vector2d(u, 14 * u));
        Floor bridge9 = Floor.createFloor(new Vector2d(6 * u,  -26 * u), new Vector2d(10 * u, u));
        Floor bridge10 = Floor.createFloor(new Vector2d(16 * u,  -26 * u), new Vector2d(6 * u, u));

        Player player = new Player(playerPosition);
        MyCamera camera = new MyCamera(player.getComponent(Transform.class).copy());
        CameraLens cameraLens = new CameraLens();

        Imp imp1 = new Imp(platform10Position.copy().toVector2d());
        Imp imp2 = new Imp(platform11Position.copy().toVector2d());
        Imp imp3 = new Imp(platform12Position.copy().toVector2d());
        Imp imp4 = new Imp(new Vector2d(21 * u, -26 * u));
        Imp imp5 = new Imp(new Vector2d(23 * u, -28 * u));
        Imp imp6 = new Imp(new Vector2d(23 * u, -26 * u));
        Imp imp7 = new Imp(new Vector2d(23 * u, -24 * u));

        Pistol pistol = new Pistol(50);
        Shotgun shotgun = new Shotgun(20);
        Shotgun shotgun2 = new Shotgun(15);
        SuperShotgun superShotgun = new SuperShotgun(15);

        ShotgunCollectable shotgunCollectable = new ShotgunCollectable(shotgun, new Vector2d(0.0f, -4.0f));
        ShotgunCollectable shotgunCollectable2 = new ShotgunCollectable(shotgun2, platform3Position);
        SuperShotgunCollectable superShotgunCollectable = new SuperShotgunCollectable(superShotgun, new Vector2d(0.0f, -8.0f));
        HealthCollectable healthCollectable = new HealthCollectable(platform6Position);

        ArmourCollectable armourCollectable = new ArmourCollectable(new Vector2d(0.0f, -15 * u));
        ArmourCollectable armourCollectable2 = new ArmourCollectable(new Vector2d(0.0f, -18 * u));
        ArmourCollectable armourCollectable3 = new ArmourCollectable(new Vector2d(0.0f, -21 * u));

        ScrollUpButton scrollUpButton = new ScrollUpButton(new Vector2d(-0.85f, 0.5f));
        ScrollDownButton scrollDownButton = new ScrollDownButton(new Vector2d(-0.85f, 0.2f));
        ShootButton shootButton = new ShootButton(new Vector2d(0.75f, 0.0f));

        StatusBar statusBar = new StatusBar(0.4f);
        DoomGuy doomGuy = statusBar.getDoomGuy();
        HUDSlot ammo_100 = statusBar.getAmmo_100();
        HUDSlot ammo_10 = statusBar.getAmmo_10();
        HUDSlot ammo_1 = statusBar.getAmmo_1();
        HUDSlot ammoPlus = statusBar.getAmmoPlus();
        HUDSlot health_100 = statusBar.getHealth_100();
        HUDSlot health_10 = statusBar.getHealth_10();
        HUDSlot health_1 = statusBar.getHealth_1();
        HUDSlot healthPercent = statusBar.getHealthPercent();
        HUDSlot armour_100 = statusBar.getArmour_100();
        HUDSlot armour_10 = statusBar.getArmour_10();
        HUDSlot armour_1 = statusBar.getArmour_1();
        HUDSlot armourPercent = statusBar.getArmourPercent();

        Credits credits = new Credits();

        AlphaSortingManager alphaSortingManager = new AlphaSortingManager();
        GameManager gameManager = new GameManager();
        SoundManager soundManager = new SoundManager();

        return new Scene(Arrays.asList(
            skybox,
            start,
            platform1, platform2, platform3, platform4, platform5, platform6,
            platform7, platform8, platform9, platform10, platform11, platform12,
            end,
            bridge1, bridge2, bridge3, bridge4, bridge5,
            bridge6, bridge7, bridge8, bridge9, bridge10,
            player, camera, cameraLens,
            imp1, imp2, imp3, imp4, imp5, imp6, imp7,
            pistol, shotgun, superShotgun,
            shotgunCollectable, shotgunCollectable2, superShotgunCollectable,
            healthCollectable,
            armourCollectable, armourCollectable2, armourCollectable3,
            scrollUpButton, scrollDownButton, shootButton,
            statusBar,
            doomGuy,
            ammo_100, ammo_10, ammo_1, ammoPlus,
            health_100, health_10, health_1, healthPercent,
            armour_100, armour_10, armour_1, armourPercent,
            credits,
            alphaSortingManager, gameManager, soundManager
        ));
    }

    @Override
    protected void setupInput() {
        InputManager.getInstance().initialize();
    }

    @Override
    protected void clearInput() {
        InputManager.getInstance().clear();
    }
}
