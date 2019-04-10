package models.mapEditor;

import controllers.AdminMapController;
import controllers.VisualRealtimeController;
import database.LocationTable;
import javafx.scene.input.MouseEvent;
import map.MapDisplay;
import models.map.Edge;
import models.map.Location;

import java.util.ArrayList;

public class NodeDragController extends MapEditorActionController {
    private static final int PRIMARY_NODE_INDEX = 0;
    private static Location draggingLocation;
    private static ArrayList<Edge> selectedEdgeList = new ArrayList<Edge>();

    public NodeDragController() {
        this.draggingLocation = null;
    }
    public void reset() {
        clearSprites();
        draggingLocation = null;
        selectedEdgeList.clear();

    }
    public void updateGraphics() {

    }
    public void updateDatabase() {

    }

    @Override
    public void clearSprites() {
        if(draggingLocation != null)
            VisualRealtimeController.visuallyDeselectCircle(draggingLocation);

        VisualRealtimeController.visuallyDeselectNodeDragSprites();
    }
    private static boolean enabled = false;

    public void setEnabled(boolean enable) {
        enabled = enable;
    }

    public boolean getEnabled() {
        return enabled;
    }
    public void nodeDragEvent(Location loc, MouseEvent mouseEvent) {
        if(enabled) {
            loc.getNodeCircle().setCenterX(mouseEvent.getX());
            loc.setxCord((int) MapDisplay.revScaleX(mouseEvent.getX()));
            loc.setyCord((int)MapDisplay.revScaleY(mouseEvent.getY()));

            LocationTable.updateLocation(loc);
            loc.getNodeCircle().setCenterY(mouseEvent.getY());
            VisualRealtimeController.updateLineConnections(loc);
            draggingLocation = loc;
        }
    }

}
