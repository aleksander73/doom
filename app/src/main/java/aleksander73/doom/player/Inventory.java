package aleksander73.doom.player;

import java.util.ArrayList;
import java.util.List;

import aleksander73.doom.weapon_system.Weapon;

public class Inventory {
    private List<Weapon> weapons = new ArrayList<>();

    public int size() {
        return weapons.size();
    }

    public void addWeapon(Weapon weapon) {
        weapons.add(weapon);
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

    public Weapon weaponUp(Weapon equippedWeapon) {
        int index = weapons.indexOf(equippedWeapon) + 1;
        if(index >= weapons.size()) {
            index = 0;
        }
        return weapons.get(index);
    }

    public Weapon weaponDown(Weapon equippedWeapon) {
        int index = weapons.indexOf(equippedWeapon) - 1;
        if(index < 0) {
            index = weapons.size() - 1;
        }
        return weapons.get(index);
    }
}
