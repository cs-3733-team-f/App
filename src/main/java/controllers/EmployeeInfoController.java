package controllers;

import com.jfoenix.controls.JFXButton;
import helpers.DatabaseHelpers;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import models.map.Location;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeInfoController extends PopUpController implements Initializable {
    public Label lblNodeID;
    public Label lblLocation;
    public Label lblFloor;
    public Label lblBuilding;
    public Label lblNodeType;
    public Label lblLongName;
    public Label lblShortName;

    public JFXButton btnRequest;
    public JFXButton btnDirections;
    public JFXButton btnCancel;

    private static boolean bolSelectedEmp = false;
    private static Location locSelectedEmp;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setLoc(Location loc) {
        this.loc = loc;
        lblNodeID.setText(loc.getNodeID());
        lblLocation.setText("(" + loc.getxCord() + ", " + loc.getyCord() + ")");
        lblFloor.setText(loc.getFloor());
        lblBuilding.setText(loc.getBuilding());
        lblNodeType.setText(DatabaseHelpers.enumToString(loc.getNodeType()));
        lblLongName.setText(loc.getLongName());
        lblShortName.setText(loc.getShortName());
    }

    public void btnCancel_OnClick(MouseEvent event) {
        event.consume();
        ScreenController.deactivate();
    }

    public void btnReportSpill_OnClick(MouseEvent event) {
        event.consume();
        ScreenController.deactivate();
        try {
            ScreenController.popUp("sanitation-request" , loc);
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }

    private void checkSelected() throws Exception {
        if (bolSelectedEmp) { // Two locations
            if (!loc.equals(locSelectedEmp)) {
                ScreenController.popUp("directions", loc, locSelectedEmp, map, pane);
            }
            locSelectedEmp = null;
            bolSelectedEmp = false;
        } else { // One location
            locSelectedEmp = loc;
            bolSelectedEmp = true;
        }
    }

    public void btnDirections_OnClick(MouseEvent mouseEvent) throws Exception {
        mouseEvent.consume();
        ScreenController.deactivate();
        checkSelected();
    }
}
