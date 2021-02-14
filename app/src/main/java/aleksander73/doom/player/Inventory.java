package aleksander73.doom.player;

import java.util.ArrayList;
import java.util.List;

import aleksander73.doom.weapon_system.Weapon;

public class Inventory {
    private final List<Weapon> weapons = new ArrayList<>();
    private Weapon equippedWeapon = null;
    private Weapon weaponToEquip = null;

    public int size() {
        return weapons.size();
    }

    public void addWeapon(Weapon weapon) {
        weapons.add(weapon);
    }

    public void switchWeapons() {
        equippedWeapon = weaponToEquip;
    }

    public Weapon findWeaponOfType(Weapon weapon) {
        Weapon result = null;
        String weaponType = weapon.getClass().getSimpleName();
        for(Weapon w : weapons) {
            if(w.getClass().getSimpleName().equals(weaponType)) {
                result = w;
                break;
            }
        }
        return result;
    }

    public Weapon weaponUp() {
        int index = weapons.indexOf(equippedWeapon) + 1;
        if(index >= weapons.size()) {
            index = 0;
        }
        return weapons.get(index);
    }

    public Weapon weaponDown() {
        int index = weapons.indexOf(equippedWeapon) - 1;
        if(index < 0) {
            index = weapons.size() - 1;
        }
        return weapons.get(index);
    }

    public Weapon getEquippedWeapon() {
        return equippedWeapon;
    }

    public void setEquippedWeapon(Weapon equippedWeapon) {
        this.equippedWeapon = equippedWeapon;
    }

    public Weapon getWeaponToEquip() {
        return weaponToEquip;
    }

    public void setWeaponToEquip(Weapon weaponToEquip) {
        this.weaponToEquip = weaponToEquip;
    }
}
