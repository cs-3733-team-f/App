package models.mapEditor;

import com.jfoenix.controls.JFXToggleButton;
import controllers.ScreenController;
import controllers.VisualRealtimeController;
import helpers.Constants;
import helpers.MapHelpers;
import helpers.UIHelpers;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import models.map.Location;

import java.awt.*;


public class NodeAddController extends MapEditorActionController {
    public void reset() {
        clearSprites();
    }
    public void updateGraphics() {

    }

    @Override
    public void updateDatabase() {

    }

    @Override
    public void clearSprites() {

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
    public static void addNodeEvent(MouseEvent event, String selectedFloor, String selectedBuilding,
                                    AnchorPane addToPane) throws Exception {
        if(getEnabledFromButton()) {
            Point selectedPoint = new Point((int) event.getX(), (int) event.getY());
            Point nodeCoordinate = MapHelpers.mapPointToMapCoordinates(selectedPoint);
            Circle circ = MapHelpers.generateNode(nodeCoordinate);
//        Point nodeDisplayCoordinate = MapHelpers.mapPointToMapCoordinates()
            Location loc = new Location(null, nodeCoordinate.x, nodeCoordinate.y,
                    selectedFloor, selectedBuilding, Constants.NodeType.HALL,
                    "RECENT_ADDITION", "RECENT_ADDITION");

            ScreenController.popUp(Constants.Routes.EDIT_LOCATION, loc);
            UIHelpers.setAdminNodeClickEvent(circ, loc);
            circ.setId(loc.getNodeID());
            loc.setNodeCircle(circ);
            VisualRealtimeController.getLocalMap().addLocation(loc);
            addToPane.getChildren().add(circ);
        }
    }
    public static JFXToggleButton getEnableButton() {
        return enableButton;
    }
}

