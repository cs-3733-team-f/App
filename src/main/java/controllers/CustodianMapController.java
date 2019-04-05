package controllers;

import com.jfoenix.controls.JFXButton;
import database.SanitationTable;
import helpers.Constants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import map.MapDisplay;
import models.sanitation.SanitationRequest;

import java.util.List;

public class CustodianMapController extends MapController {

    public JFXButton btnSettings;
    public TableView<SanitationRequest> tblData;
    public TableColumn<SanitationRequest,String> tblLocation;
    public TableColumn<SanitationRequest,String> tblPriority;
    public TableColumn<SanitationRequest,String> tblStatus;
    public TableColumn<SanitationRequest,String> tblUser;
    public TableColumn<SanitationRequest,String> tblDescription;

    public JFXButton btnMarkDone;

    ObservableList<SanitationRequest> spills = FXCollections.observableArrayList();

    public void initialize() {
        toolTip();
        MapDisplay.displayCust(new AnchorPane[] {panFloorL2, panFloorL1, panFloor1, panFloor2, panFloor3});
        initSanitation();
        updateSanitation();

        tblData.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnMarkDone.setDisable(false);
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
        List<SanitationRequest> lstReqs = SanitationTable.getSanitationRequests();
        spills.addAll(lstReqs);
    }

    public void markDone(){
        SanitationRequest selected = tblData.getSelectionModel().getSelectedItem();
        if (selected.getStatusObj() == SanitationRequest.Status.COMPLETE) {
            selected.setStatus(SanitationRequest.Status.INCOMPLETE);
        } else {
            selected.setStatus(SanitationRequest.Status.COMPLETE);
        }
        SanitationTable.editSanitationRequest(selected);
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


