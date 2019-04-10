package models.mapEditor;

import com.jfoenix.controls.JFXToggleButton;
import controllers.ScreenController;
import controllers.VisualRealtimeController;
import database.EdgeTable;
import helpers.Constants;
import helpers.MapHelpers;
import helpers.UIHelpers;
import javafx.scene.shape.Line;
import models.map.Edge;
import models.map.Location;

import java.util.ArrayList;

public class EdgeAddController extends MapEditorActionController {
    static Location primaryNodeLocation;
    private static ArrayList<Location> selectedLocations = new ArrayList<Location>();
    public void reset() {
        clearSprites();
        clearLocations();
    }

    @Override
    public void updateGraphics() {

    }

    @Override
    public void updateDatabase() {

    }

    @Override
    public void clearSprites() {
        VisualRealtimeController.visuallyDeselectEdgeAddSprites(selectedLocations);
        if(primaryNodeLocation != null) VisualRealtimeController.visuallyDeselectCircle(primaryNodeLocation);
    }
    public static void clearLocations() {
        selectedLocations.clear();
        primaryNodeLocation = null;
    }
    public void nodeSelectEvent(Location loc) throws Exception {
        if(getEnabledFromButton()) { //If edge adding is enabled
            if (primaryNodeLocation != loc) { // if the selected location isn't clicked again
                performEdgeToggle(loc); // add the new location
            } else {
                reset();
            }
        }
    }
    public void performEdgeToggle(Location newLocation) {
        if(primaryNodeLocation != null) {
            // Create edge from the primary location and the newest location
            Edge localEdge = MapHelpers.generateEdgeBetweenLocations(primaryNodeLocation, newLocation);
            // Add edge to database if it exists, remove if it it doesn't.
            boolean edgeAddedOrRemoved = EdgeTable.addOrRemoveEdge(localEdge);
            // If it was added, display the new edge
            if(edgeAddedOrRemoved == Constants.ADDED) addEdgeAndDisplay(localEdge);
            // If it was removed, don't display the edge
            else if(edgeAddedOrRemoved == Constants.REMOVED) deleteEdgeConnection(localEdge);
            // If nothing is selected for primary node, make it the new primary node.
        } else setStartNodeLocationAndDisplay(newLocation);
    }
    public static void addEdgeAndDisplay(Edge localEdge) {
        if(MapHelpers.EdgeBetweenLocationsOnSameFloor(localEdge)) {
            addLineToEdge(localEdge);
            addEdgeAndLineToVisualController(localEdge);
        } else {
            displayNodeConnectionBetweenFloors(localEdge);
        }
    }
    public static void addLineToEdge(Edge localEdge) {
        Line line = UIHelpers.generateLineFromEdge(localEdge);
        localEdge.setLine(line);
    }
    public static void addEdgeAndLineToVisualController(Edge localEdge) {
        VisualRealtimeController.getLocalMap().addEdge(localEdge);
        VisualRealtimeController.addLine(localEdge.getLine());

        VisualRealtimeController.pushCircleToFront(localEdge.getStart());

        VisualRealtimeController.pushCircleToFront(localEdge.getEnd());
        selectedLocations.add(localEdge.getEnd());
        VisualRealtimeController.visuallySelectCircle(localEdge.getEnd());
    }
    public static void displayNodeConnectionBetweenFloors(Edge localEdge) {
        selectedLocations.add(localEdge.getEnd());
        VisualRealtimeController.visuallySelectCircle(localEdge.getEnd());
    }
    public static void deleteEdgeConnection(Edge localEdge) {
        VisualRealtimeController.removeLine(localEdge.getLine());
        VisualRealtimeController.getLocalMap().getAllEdges().remove(localEdge.getEdgeID());
    }
    public static void setStartNodeLocationAndDisplay(Location loc) {
        primaryNodeLocation = loc;
        VisualRealtimeController.visuallySelectPrimaryLocation(loc);
    }
    private static JFXToggleButton enableButton;
    public static void setEnabledFromButton() {
        enabled = enableButton.selectedProperty().get();
    }
    public static boolean getEnabledFromButton() {
        setEnabledFromButton();
        return enabled;
    }
    public void setEnableButton(JFXToggleButton button) {
        enableButton = button;
    }
    private static boolean enabled = false;

    public void setEnabled(boolean enable) {
        enabled = enable;
    }

    public boolean getEnabled() {
        return enabled;
    }
    public static JFXToggleButton getEnableButton() {
        return enableButton;
    }
}
