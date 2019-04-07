package controllers.map;

import javafx.scene.layout.AnchorPane;
import map.MapDisplay;
import net.kurobako.gesturefx.GesturePane;

public class EmployeeMapController extends MapController {

//    public JFXButton btnSettings;
//    public JFXButton btnReturn;

    public void initialize() {
        toolTip();
        MapDisplay.displayEmployee(new GesturePane[] {panFloorL2, panFloorL1, panFloor1, panFloor2, panFloor3});

    }

    void toolTip() {
        //btnSettings.setTooltip(new Tooltip(Constants.SETTINGS_BUTTON_TOOLTIP));
       // btnReturn.setTooltip(new Tooltip(Constants.LOGOUT_BUTTON_TOOLTIP));
    }
}
