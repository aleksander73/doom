package aleksander73.doom.input;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;

import aleksander73.vector.core.GameEngine;

public class InputManager {
    private boolean enabled = true;
    private static InputManager instance;

    public static InputManager getInstance() {
        if(instance == null) {
            instance = new InputManager();
        }
        return instance;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initialize() {
        GameEngine.getInputSystem().setOnTouchListener((view, motionEvent) -> {
            boolean result = false;
            if(InputManager.getInstance().isEnabled()) {
                result = InputManager.getInstance().handleInputEvent(view, motionEvent);
            }
            return result;
        });
    }

    public void clear() {}

    public boolean handleInputEvent(View view, MotionEvent motionEvent) {
        return false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
