package controllers;

import com.jfoenix.controls.JFXComboBox;
import helpers.Constants;
import helpers.UIHelpers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.map.Location;

import java.net.URL;
import java.util.ResourceBundle;

public class PopUpController implements Initializable {

    public JFXComboBox cmbNodeType;
    private Location loc;

    public void updateNode(MouseEvent event) {
        event.consume();
        String value = (String) cmbNodeType.getValue();

        if(value == null) {
            UIHelpers.createAndShowAlert(
                    ScreenController.getStage(),
                    null,
                    null,
                    Constants.LABEL_NODE_ERROR,
                    Constants.BUTTON_UPDATE_NODE_ERROR
            );

        } else {
            String nType = value.substring(0, value.indexOf(':'));

            loc.setNodeType(Constants.NodeType.valueOf(nType));
            ScreenController.deactivate();
        }

    }

    public void goBack(MouseEvent event) throws Exception{
       // ((Stage) (((Node) event.getSource()).getScene().getWindow())).close();
        event.consume();
        ScreenController.deactivate();
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
