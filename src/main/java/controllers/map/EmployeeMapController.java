package controllers.map;

import javafx.scene.layout.AnchorPane;
import map.MapDisplay;
import net.kurobako.gesturefx.GesturePane;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeMapController extends MapController {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MapDisplay.displayEmployee(new AnchorPane[] {panFloorL2, panFloorL1, panFloor1, panFloor2, panFloor3});
    }
}
