package controllers.map;

import com.jfoenix.controls.JFXButton;
import controllers.ScreenController;
import helpers.Constants;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import map.MapDisplay;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class MapController implements Initializable {
    public JFXButton btnReturn;
    public AnchorPane panFloor3;
    public AnchorPane panFloor2;
    public AnchorPane panFloor1;
    public AnchorPane panFloorL1;
    public AnchorPane panFloorL2;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    //    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//        AnchorPane[] panes = new AnchorPane[] {panFloorL2, panFloorL1, panFloor1, panFloor2, panFloor3};
//        for (GesturePane pane : panes) {
//            pane.setVBarEnabled(false);
//            pane.setHBarEnabled(false);
//        }
//    }

    public void logOut(MouseEvent event) throws Exception {
        event.consume();
        ScreenController.logOut(btnReturn);
        ScreenController.activate(Constants.Routes.LOGIN);
    }
}
