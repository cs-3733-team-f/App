package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import controllers.MapController;
import database.Database;
import helpers.Constants;
import helpers.SpillModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import map.MapDisplay;
import models.map.Location;
import models.sanitation.SanitationRequest;

import javax.xml.crypto.Data;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CustodianMapController extends MapController {

    public JFXButton btnSettings;
    public TableView<SanitationRequest> tblData;
    public TableColumn<SanitationRequest,String> tblLocation;
    public TableColumn<SanitationRequest,String> tblPriority;
    public TableColumn<SanitationRequest,String> tblStatus;
    public TableColumn<SanitationRequest,String> tblUser;
    public TableColumn<SanitationRequest,String> tblDescription;

    public JFXButton btnMarkDone;
    public JFXButton btnNavigate;
    public JFXTabPane tabFloorPane;

    ObservableList<SanitationRequest> spills = FXCollections.observableArrayList();

    public void initialize() {
        toolTip();
        MapDisplay.displayCust(panMap, "Tower", "1");
        initSanitation();
        updateSanitation();

        tblData.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnMarkDone.setDisable(false);
            btnNavigate.setDisable(false);
            updateBtn();
        });

    }

    void toolTip() {
        btnSettings.setTooltip(new Tooltip(Constants.SETTINGS_BUTTON_TOOLTIP));
        btnReturn.setTooltip(new Tooltip(Constants.LOGOUT_BUTTON_TOOLTIP));
    }

    private void initSanitation(){
        tblLocation.setCellValueFactory(new PropertyValueFactory<>("Location"));
        tblPriority.setCellValueFactory(new PropertyValueFactory<>("Priority"));
        tblStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
        tblDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        tblUser.setCellValueFactory(new PropertyValueFactory<>("User"));
        tblData.setItems(spills);
    }

    private void updateSanitation() {
        List<SanitationRequest> lstReqs = Database.getSanitationRequests();
        spills.addAll(lstReqs);
    }

    public void navigateTo(){

        //Location start = Database.getKioskLoc();
        Location end = tblData.getSelectionModel().getSelectedItem().getLocationObj();
        //map.navigate(start,end);

        //String floor = start.getFloor();
        //tabFloorPane.getSelectionModel().select(floor);

    }

    public void markDone(){
        SanitationRequest selected = tblData.getSelectionModel().getSelectedItem();
        if (selected.getUser().equals("")) {
            selected.setUser("user_temp");
        } else {
            selected.setUser("");
        }
        if (selected.getStatusObj() == SanitationRequest.Status.COMPLETE) {
            selected.setStatus(SanitationRequest.Status.INCOMPLETE);
        } else {
            selected.setStatus(SanitationRequest.Status.COMPLETE);
        }
        Database.editSanitationRequest(selected);
        tblData.refresh();
        updateBtn();
    }

    private void updateBtn() {
        if (tblData.getSelectionModel().getSelectedItem().getStatusObj() == SanitationRequest.Status.COMPLETE) {
            btnMarkDone.setText("Mark Incomplete");
        } else {
            btnMarkDone.setText("Mark Complete");
        }
    }
}


