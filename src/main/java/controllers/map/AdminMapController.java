package controllers.map;

import com.jfoenix.controls.JFXButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import map.MapDisplay;
import net.kurobako.gesturefx.GesturePane;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminMapController extends MapController {
    public JFXButton btnDownload;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MapDisplay.displayAdmin(new AnchorPane[] {panFloorL2, panFloorL1, panFloor1, panFloor2, panFloor3});
    }
}
