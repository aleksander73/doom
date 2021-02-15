package aleksander73.doom;

import java.util.Arrays;

import aleksander73.doom.collectables.ArmourCollectable;
import aleksander73.doom.collectables.HealthCollectable;
import aleksander73.doom.collectables.PistolCollectable;
import aleksander73.doom.collectables.ShotgunCollectable;
import aleksander73.doom.collectables.SuperShotgunCollectable;
import aleksander73.doom.hud.DoomGuy;
import aleksander73.doom.hud.HUDSlot;
import aleksander73.doom.hud.ScrollDownButton;
import aleksander73.doom.hud.ScrollUpButton;
import aleksander73.doom.hud.ShootButton;
import aleksander73.doom.hud.StatusBar;
import aleksander73.doom.input.InputManager;
import aleksander73.doom.managers.AlphaSortingManager;
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
        Skybox skybox = new Skybox(GameEngine.getResourceSystem().getTexture("skyboxes/hell.png"));
        final float u = 3.0f;
        Floor floor = Floor.createFloor(new Vector2d(0.0f,  0.0f), new Vector2d(5 * u, 5 * u));
        Player player = new Player(new Vector2d(0.0f, 0.0f));
        MyCamera camera = new MyCamera(player.getComponent(Transform.class).copy());
        CameraLens cameraLens = new CameraLens();
        Pistol pistol = new Pistol(5);
        PistolCollectable pistolCollectable = new PistolCollectable(new Pistol(5), new Vector2d(0.0f, 5.0f));
        Shotgun shotgun = new Shotgun(6);
        SuperShotgun superShotgun = new SuperShotgun((7));
        HealthCollectable healthCollectable = new HealthCollectable(new Vector2d(0.0f, -5.0f));
        ArmourCollectable armourCollectable = new ArmourCollectable(new Vector2d(5.0f, -5.0f));
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
        AlphaSortingManager alphaSortingManager = new AlphaSortingManager();

        return new Scene(Arrays.asList(
            skybox, floor, player, camera, cameraLens,
            pistol, pistolCollectable, shotgun, superShotgun,
            healthCollectable, armourCollectable,
            scrollUpButton, scrollDownButton, shootButton,
            statusBar, doomGuy,
            ammo_100, ammo_10, ammo_1, ammoPlus,
            health_100, health_10, health_1, healthPercent,
            armour_100, armour_10, armour_1, armourPercent,
            alphaSortingManager
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
