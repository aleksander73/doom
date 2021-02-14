package aleksander73.doom.input;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aleksander73.math.linear_algebra.Vector2d;
import aleksander73.vector.core.GameEngine;
import aleksander73.vector.utility.ListUtility;

public class InputManager {
    public static final String MOVE_SECTOR = "MOVE_SECTOR";
    public static final String ROTATE_SECTOR = "ROTATE_SECTOR";

    private boolean enabled = true;
    private final List<Sector> sectors = new ArrayList<>();
    private final Comparator<Sector> sectorComparator;
    private final Map<Integer, Sector> pointerIdToLockedSector = new HashMap<>();
    private static InputManager instance;

    private InputManager() {
        sectorComparator = new Comparator<Sector>() {
            @Override
            public int compare(Sector sector1, Sector sector2) {
                return Integer.compare(sector2.getLevel(), sector1.getLevel());
            }
        };
    }

    public static InputManager getInstance() {
        if(instance == null) {
            instance = new InputManager();
        }
        return instance;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initialize() {
        GameEngine.getInputSystem().setOnTouchListener((view, motionEvent) -> InputManager.getInstance().isEnabled() && InputManager.getInstance().handleInputEvent(view, motionEvent));
    }

    public void addSector(Sector sector) {
        sectors.add(sector);
        Collections.sort(sectors, sectorComparator);
    }

    public Sector findSector(String name) {
        return ListUtility.first(sectors, (sector) -> sector.getName().equals(name));
    }

    public void clear() {
        for(int i = 0; i < sectors.size(); i++) {
            sectors.get(i).clear();
        }
    }

    public boolean handleInputEvent(View view, MotionEvent motionEvent) {
        int pointerIndex = motionEvent.getActionIndex();
        int pointerID = motionEvent.getPointerId(pointerIndex);
        Vector2d pos = GameEngine.getInputSystem().screenToNDC(new Vector2d(
                motionEvent.getX(pointerIndex),
                motionEvent.getY(pointerIndex)
        ));
        Sector sector = this.sectorAt(pos);
        // If interaction with no sections or a foreign pointer tries to act upon the already locked sector
        if(sector == null || (sector.isLocked() && sector != pointerIdToLockedSector.get(pointerID))) {
            return false;
        }
        switch(motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN :
            case MotionEvent.ACTION_POINTER_DOWN : {
                this.onTouchDown(pointerID, sector, pos);
                break;
            }
            case MotionEvent.ACTION_MOVE : {
                this.onMove(motionEvent);
                break;
            }
            case MotionEvent.ACTION_UP :
            case MotionEvent.ACTION_POINTER_UP : {
                this.onTouchUp(pointerID);
                break;
            }
        }
        return true;
    }

    private Sector sectorAt(final Vector2d v) {
        return ListUtility.first(sectors, (sector) -> sector.contains(v));
    }

    private void onTouchDown(int pointerID, Sector sector, Vector2d v) {
        if(sector.isLocked()) {
            return;
        }
        sector.setLocked(true);
        pointerIdToLockedSector.put(pointerID, sector);
        sector.getInput().setTouchDown(v);
        sector.getInput().setLastTouchDown(v);
        sector.getInput().setTouchCurrent(v);
    }

    private void onMove(MotionEvent motionEvent) {
        int n = motionEvent.getPointerCount();
        for(int index = 0; index < n; index++) {
            Sector lockedSector = pointerIdToLockedSector.get(motionEvent.getPointerId(index));
            if(lockedSector != null) {
                Vector2d v = GameEngine.getInputSystem().screenToNDC(new Vector2d(motionEvent.getX(index), motionEvent.getY(index)));
                lockedSector.getInput().setTouchCurrent(v);
            }
        }
    }

    private void onTouchUp(int pointerID) {
        Sector lockedSector = pointerIdToLockedSector.remove(pointerID);
        if(lockedSector != null) {
            lockedSector.getInput().setLastTouchDown(null);
            lockedSector.getInput().setTouchCurrent(null);
            lockedSector.setLocked(false);
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
