package aleksander73.doom.hud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aleksander73.math.linear_algebra.Vector2d;
import aleksander73.vector.core.GameEngine;
import aleksander73.vector.gui.GUIElement;
import aleksander73.vector.rendering.materials.Material;
import aleksander73.vector.rendering.materials.Texture;
import aleksander73.vector.rendering.renderers.Renderer;

public class StatusBar extends GUIElement {
    private final float height;

    private final float H_LINE;
    private final float HUD_SLOT_WIDTH;
    private final float HUD_SLOT_HEIGHT;
    private final Map<Character, Texture> mapCharacterToTexture;

    private final HUDSlot ammo_100;
    private final HUDSlot ammo_10;
    private final HUDSlot ammo_1;
    private final HUDSlot ammoPlus;

    private final HUDSlot health_100;
    private final HUDSlot health_10;
    private final HUDSlot health_1;
    private final HUDSlot healthPercent;

    private final HUDSlot armour_100;
    private final HUDSlot armour_10;
    private final HUDSlot armour_1;
    private final HUDSlot armourPercent;

    public StatusBar(float height) {
        super(
            "StatusBar",
            new Vector2d(0.0f, -1.0f + height / 2.0f),
            new Vector2d(2.0f, height),
            1,
            null,
            GameEngine.getResourceSystem().getTexture("hud/status.png")
        );

        this.height = height;

        mapCharacterToTexture = new HashMap<>();
        mapCharacterToTexture.put('0', GameEngine.getResourceSystem().getTexture("hud/characters/0.png"));
        mapCharacterToTexture.put('1', GameEngine.getResourceSystem().getTexture("hud/characters/1.png"));
        mapCharacterToTexture.put('2', GameEngine.getResourceSystem().getTexture("hud/characters/2.png"));
        mapCharacterToTexture.put('3', GameEngine.getResourceSystem().getTexture("hud/characters/3.png"));
        mapCharacterToTexture.put('4', GameEngine.getResourceSystem().getTexture("hud/characters/4.png"));
        mapCharacterToTexture.put('5', GameEngine.getResourceSystem().getTexture("hud/characters/5.png"));
        mapCharacterToTexture.put('6', GameEngine.getResourceSystem().getTexture("hud/characters/6.png"));
        mapCharacterToTexture.put('7', GameEngine.getResourceSystem().getTexture("hud/characters/7.png"));
        mapCharacterToTexture.put('8', GameEngine.getResourceSystem().getTexture("hud/characters/8.png"));
        mapCharacterToTexture.put('9', GameEngine.getResourceSystem().getTexture("hud/characters/9.png"));
        mapCharacterToTexture.put('%', GameEngine.getResourceSystem().getTexture("hud/characters/percent.png"));
        mapCharacterToTexture.put('+', GameEngine.getResourceSystem().getTexture("hud/characters/plus.png"));

        H_LINE = -1.0f + 0.6f * height;
        HUD_SLOT_WIDTH = 0.075f;
        HUD_SLOT_HEIGHT = 0.6f * height;

        ammo_100 = this.createDigit(-0.85f);
        ammo_10 = this.createDigit(-0.77f);
        ammo_1 = this.createDigit(-0.69f);
        ammoPlus = this.createSign(-0.61f, '+');
        ammo_1.getComponent(Renderer.class).setActive(true);
        ammoPlus.getComponent(Renderer.class).setActive(false);

        health_100 = this.createDigit(-0.35f);
        health_10 = this.createDigit(-0.27f);
        health_1 = this.createDigit(-0.19f);
        healthPercent = this.createSign(-0.11f, '%');
        health_1.getComponent(Renderer.class).setActive(true);
        healthPercent.getComponent(Renderer.class).setActive(true);

        armour_100 = this.createDigit(0.61f);
        armour_10 = this.createDigit(0.69f);
        armour_1 = 	this.createDigit(0.77f);
        armourPercent =	this.createSign(0.85f, '%');
        armour_1.getComponent(Renderer.class).setActive(true);
        armourPercent.getComponent(Renderer.class).setActive(true);
    }

    private HUDSlot createDigit(float x) {
        HUDSlot HUDSlot = new HUDSlot(new Vector2d(x, H_LINE), new Vector2d(HUD_SLOT_WIDTH, HUD_SLOT_HEIGHT), mapCharacterToTexture.get('0'));
        HUDSlot.getComponent(Renderer.class).setActive(false);
        return HUDSlot;
    }

    private HUDSlot createSign(float x, char c) {
        return new HUDSlot(new Vector2d(x, H_LINE), new Vector2d(HUD_SLOT_WIDTH, HUD_SLOT_HEIGHT), mapCharacterToTexture.get(c));
    }

    private void fill(HUDSlot hudSlot100, HUDSlot hudSlot10, HUDSlot hudSlot1, int n) {
        List<Texture> textures = new ArrayList<>(3);
        int n100 = n / 100;
        int n10 = (n % 100) / 10;
        int n1 = n % 10;
        if(n100 == 0) {
            textures.add(null);
        } else {
            textures.add(this.texture(n100));
        }
        if(n10 == 0 && n100 == 0) {
            textures.add(null);
        } else {
            textures.add(this.texture(n10));
        }
        textures.add(texture(n1));
        Texture tex100 = textures.get(0);
        if(tex100 == null) {
            hudSlot100.getComponent(Renderer.class).setActive(false);
        } else {
            hudSlot100.getComponent(Material.class).setTexture(tex100);
            hudSlot100.getComponent(Renderer.class).setActive(true);
        }
        Texture tex10 = textures.get(1);
        if(tex10 == null) {
            hudSlot10.getComponent(Renderer.class).setActive(false);
        } else {
            hudSlot10.getComponent(Material.class).setTexture(tex10);
            hudSlot10.getComponent(Renderer.class).setActive(true);
        }
        Texture tex1 = textures.get(2);
        hudSlot1.getComponent(Material.class).setTexture(tex1);
    }

    public void updateAmmo(int ammo) {
        if(ammo > 999) {
            ammo_100.getComponent(Material.class).setTexture(mapCharacterToTexture.get('9'));
            ammo_100.getComponent(Renderer.class).setActive(true);
            ammo_10.getComponent(Material.class).setTexture(mapCharacterToTexture.get('9'));
            ammo_10.getComponent(Renderer.class).setActive(true);
            ammo_1.getComponent(Material.class).setTexture(mapCharacterToTexture.get('9'));
            ammoPlus.getComponent(Renderer.class).setActive(true);
        } else {
            this.fill(ammo_100, ammo_10, ammo_1, ammo);
            ammoPlus.getComponent(Renderer.class).setActive(false);
        }
    }

    public void updateHealth(int health) {
        this.fill(health_100, health_10, health_1, health);
    }

    public void updateArmour(int armour) {
        this.fill(armour_100, armour_10, armour_1, armour);
    }

    private Texture texture(int n) {
        char c = Integer.toString(n).charAt(0);
        return  mapCharacterToTexture.get(c);
    }

    @Override
    public float getHeight() {
        return height;
    }

    public HUDSlot getAmmo_100() {
        return ammo_100;
    }

    public HUDSlot getAmmo_10() {
        return ammo_10;
    }

    public HUDSlot getAmmo_1() {
        return ammo_1;
    }

    public HUDSlot getAmmoPlus() {
        return ammoPlus;
    }

    public HUDSlot getHealth_100() {
        return health_100;
    }

    public HUDSlot getHealth_10() {
        return health_10;
    }

    public HUDSlot getHealth_1() {
        return health_1;
    }

    public HUDSlot getHealthPercent() {
        return healthPercent;
    }

    public HUDSlot getArmour_100() {
        return armour_100;
    }

    public HUDSlot getArmour_10() {
        return armour_10;
    }

    public HUDSlot getArmour_1() {
        return armour_1;
    }

    public HUDSlot getArmourPercent() {
        return armourPercent;
    }
}
