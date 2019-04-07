package controllers.map;

import com.jfoenix.controls.JFXButton;
import controllers.ScreenController;
import helpers.Constants;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import map.MapDisplay;
import net.kurobako.gesturefx.GesturePane;

import java.net.URL;
import java.util.ResourceBundle;

public class MapController implements Initializable {
    public JFXButton btnReturn;
    public GesturePane panFloor3;
    public GesturePane panFloor2;
    public GesturePane panFloor1;
    public GesturePane panFloorL1;
    public GesturePane panFloorL2;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GesturePane[] panes = new GesturePane[] {panFloorL2, panFloorL1, panFloor1, panFloor2, panFloor3};
        for (GesturePane pane : panes) {
            pane.setVBarEnabled(false);
            pane.setHBarEnabled(false);
        }
        MapDisplay.displayUser(panes);
    }

    public void logOut(MouseEvent event) throws Exception {
        event.consume();
        ScreenController.logOut(btnReturn);
        ScreenController.activate(Constants.Routes.LOGIN);
    }
}
