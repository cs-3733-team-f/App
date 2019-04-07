package controllers.map;

import com.jfoenix.controls.JFXButton;
import map.MapDisplay;
import net.kurobako.gesturefx.GesturePane;

public class AdminMapController extends MapController {
    public JFXButton btnDownload;

    public void initialize() {
        MapDisplay.displayAdmin(new GesturePane[] {panFloorL2, panFloorL1, panFloor1, panFloor2, panFloor3});
    }
}
