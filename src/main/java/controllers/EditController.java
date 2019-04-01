package controllers;

import com.jfoenix.controls.JFXComboBox;
import database.Database;
import helpers.Constants;
import helpers.MapHelpers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import models.map.Location;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class EditController extends PopUpController implements Initializable {

    private String selectedFloor = "1", selectedBuilding = "Tower";

    public JFXComboBox cmbNodeType;
    public String BATH;
    public String CONF;
    public String DEPT;
    public String ELEV;
    public String EXIT;
    public String HALL;
    public String INFO;
    public String LABS;
    public String REST;
    public String RETL;
    public String SERV;
    public String STAI;

    public void updateNode(MouseEvent event) {
        event.consume();
        String value = (String) cmbNodeType.getValue();
        String nType = value.substring(0, value.indexOf(':'));
        loc.setNodeType(Constants.NodeType.valueOf(nType));
        if(loc.getNodeID() == null) Database.addNewLocation(loc);

        ScreenController.deactivate();
    }



    public void goBack(MouseEvent event) throws Exception{
       // ((Stage) (((Node) event.getSource()).getScene().getWindow())).close();
        event.consume();
        ScreenController.deactivate();
    }

    public void deleteNode(MouseEvent event) {
        event.consume();
//        loc.deleteCurrNode();
        ScreenController.deactivate();
    }

    public Location chooseLocation(MouseEvent event) throws Exception {
        Point selectedPoint = new Point((int)event.getX(), (int)event.getY());
        Point nodeCoordinate = MapHelpers.mapPointToMapCoordinates(selectedPoint);
        Circle circ = MapHelpers.generateNode(nodeCoordinate);
//        Point nodeDisplayCoordinate = MapHelpers.mapPointToMapCoordinates()
        Location loc = new Location(null, nodeCoordinate.x, nodeCoordinate.y,
                this.selectedFloor, this.selectedBuilding, Constants.NodeType.HALL,
                "RECENT_ADDITION", "RECENT_ADDITION");
        circ.setOnMouseClicked(evt -> {
            try {
                evt.consume();
                ScreenController.popUp("edit", loc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return loc;
    }


    public void setLoc(Location loc) {
        this.loc =  loc;
        switch (loc.getNodeType()) {
            case BATH:
                cmbNodeType.setValue(BATH);
                break;
            case SERV:
                cmbNodeType.setValue(SERV);
                break;
            case RETL:
                cmbNodeType.setValue(RETL);
                break;
            case REST:
                cmbNodeType.setValue(REST);
                break;
            case LABS:
                cmbNodeType.setValue(LABS);
                break;
            case INFO:
                cmbNodeType.setValue(INFO);
                break;
            case HALL:
                cmbNodeType.setValue(HALL);
                break;
            case EXIT:
                cmbNodeType.setValue(EXIT);
                break;
            case ELEV:
                cmbNodeType.setValue(ELEV);
                break;
            case DEPT:
                cmbNodeType.setValue(DEPT);
                break;
            case CONF:
                cmbNodeType.setValue(CONF);
                break;
            default:
                cmbNodeType.setValue(STAI);
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
