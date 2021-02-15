package aleksander73.doom.managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import aleksander73.doom.game_object.Model;
import aleksander73.doom.other.Floor;
import aleksander73.math.linear_algebra.Vector3d;
import aleksander73.vector.core.GameObject;
import aleksander73.vector.core.Transform;
import aleksander73.vector.gui.GUIElement;
import aleksander73.vector.utility.functional_interface.Condition;

public class AlphaSortingManager extends GameObject {
    private final Runnable alphaSort;
    private final Condition<GameObject> isModel;
    private final Condition<GameObject> isGUIElement;
    private final Condition<GameObject> isOther;
    private final Comparator<GameObject> modelComparator;
    private final Comparator<GameObject> guiComparator;
    private final Comparator<GameObject> otherComparator;
    private Transform cameraTransform;

    public AlphaSortingManager() {
        super("AlphaSortingManager");
        isModel = element -> element instanceof Model;
        isGUIElement = element -> element instanceof GUIElement;
        isOther = element -> !isModel.test(element) && !isGUIElement.test(element);
        modelComparator = (o1, o2) -> {
            if(o1 instanceof Floor) {
                return -1;
            } else if(o2 instanceof Floor) {
                return 1;
            }
            Vector3d cameraPosition = cameraTransform.getPosition();
            Vector3d o1Pos = o1.getComponent(Transform.class).getPosition();
            Vector3d o2Pos = o2.getComponent(Transform.class).getPosition();
            float d1 = cameraPosition.distance(o1Pos);
            float d2 = cameraPosition.distance(o2Pos);
            return Float.compare(d2, d1);
        };
        guiComparator = (o1, o2) -> {
            GUIElement gui1 = (GUIElement)o1;
            GUIElement gui2 = (GUIElement)o2;
            return Integer.compare(gui1.getLayer(), gui2.getLayer());
        };
        otherComparator = (o1, o2) -> {
            if(o1 instanceof AlphaSortingManager) {
                return 1;
            } else if(o2 instanceof AlphaSortingManager) {
                return -1;
            }
            return 0;
        };
        alphaSort = () -> AlphaSortingManager.this.getScene().sortByGrouping(
            new ArrayList<>(Arrays.asList(isModel, isGUIElement, isOther)),
            new ArrayList<>(Arrays.asList(modelComparator, guiComparator, otherComparator))
        );
    }

    @Override
    public void start() {
        cameraTransform = this.getScene().find("Camera").getComponent(Transform.class);
    }

    @Override
    public void update() {
        this.getScene().getOnUpdated().queueRunnable(alphaSort);
    }
}
