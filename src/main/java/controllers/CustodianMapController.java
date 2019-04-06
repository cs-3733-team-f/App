package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import database.Database;
import database.SanitationTable;
import helpers.Constants;
import helpers.UserHelpers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import map.MapDisplay;
import models.map.Location;
import models.sanitation.SanitationRequest;

import java.util.HashMap;
import java.util.List;
import java.sql.Timestamp;

import static controllers.VisualRealtimeController.panMap;

public class CustodianMapController extends MapController {

    public JFXButton btnSettings;
    public TableView<SanitationRequest> tblData;
    public TableColumn<SanitationRequest,String> tblLocation;
    public TableColumn<SanitationRequest,String> tblPriority;
    public TableColumn<SanitationRequest,String> tblStatus;
    public TableColumn<SanitationRequest,String> tblDescription;
    public TableColumn<SanitationRequest,String> tblRequester;
    public TableColumn<SanitationRequest,String> tblClaimTime;
    public TableColumn<SanitationRequest,String> tblServicer;
    public TableColumn<SanitationRequest,String> tblServiceTime;


    public JFXButton btnMarkDone;
    public JFXButton btnNavigate;
    public JFXButton btnClaim;
    public JFXTabPane tabFloorPane;

    ObservableList<SanitationRequest> spills = FXCollections.observableArrayList();

    public void initialize() {


        toolTip();

        MapDisplay.displayAdmin(new AnchorPane[] {panFloorL2, panFloorL1, panFloor1, panFloor2, panFloor3});
        VisualRealtimeController.setPanMap(panFloor1);

//        toolTip();
//        MapDisplay.displayCust(panMap, "1");
        initSanitation();
        updateSanitation();

        tblData.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {// only enable btns if item selected
            btnMarkDone.setDisable(false);
            btnNavigate.setDisable(false);
        });
        SanitationRequest selected = tblData.getSelectionModel().getSelectedItem();
        if(selected.getServicer()==null||selected.getServicer().equals(UserHelpers.getCurrentUser())) {//only enable claiming if unclaimed
            btnClaim.setDisable(false);
        }else{
            btnClaim.setDisable(true);
        }

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
        //tblUser.setCellValueFactory(new PropertyValueFactory<>("User"));
        tblRequester.setCellValueFactory(new PropertyValueFactory<>("Requester"));
        tblClaimTime.setCellValueFactory(new PropertyValueFactory<>("ClaimedTime"));
        tblServiceTime.setCellValueFactory(new PropertyValueFactory<>("CompletedTime"));
        tblServicer.setCellValueFactory(new PropertyValueFactory<>("Servicer"));

        tblData.setItems(spills);
    }

    private void updateSanitation() {
        List<SanitationRequest> lstReqs = SanitationTable.getSanitationRequests();
        spills.addAll(lstReqs);
    }

    public void navigateTo(){
        
        String locID;

        //HashMap<String, Location> locations = Database.getLocations();



//        for (locations.Entry<String, Object> entry : map.entrySet()) {
//            String key = entry.getKey();
//            Object value = entry.getValue();
//            // ...
//        }

       
        //

        //Location start = Database.getLocationbyID();
        Location end = tblData.getSelectionModel().getSelectedItem().getLocation();
        //map.navigate(start,end);

        //String floor = start.getFloor();
        //tabFloorPane.getSelectionModel().select(floor);

    }

    public void claimJob(){

        SanitationRequest selected = tblData.getSelectionModel().getSelectedItem();

        if (selected.getServicer() != null) {
            selected.setServicer(UserHelpers.getCurrentUser());
        }else{
            selected.setServicer(null);
        }
        updateClaimBtn();

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        selected.setClaimedTime(timestamp);


    }

    public void markDone(){
        SanitationRequest selected = tblData.getSelectionModel().getSelectedItem();
//        if (selected.getUser().equals("")) {
//            selected.setUser("user_temp");
//        } else {
//            selected.setUser("");
//        }
        if (selected.getStatus() == SanitationRequest.Status.COMPLETE) {
            selected.setStatus(SanitationRequest.Status.INCOMPLETE);
        } else {
            selected.setStatus(SanitationRequest.Status.COMPLETE);
        }
        SanitationTable.editSanitationRequest(selected);
        tblData.refresh();

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        selected.setCompletedTime(timestamp);
        updateMarkDoneBtn();
    }

    private void updateMarkDoneBtn() {
        if (tblData.getSelectionModel().getSelectedItem().getStatus() == SanitationRequest.Status.COMPLETE) {
            btnClaim.setText("Un-Claim");
        } else {
            btnClaim.setText("Claim");
        }
    }

    private void updateClaimBtn() {
        if (tblData.getSelectionModel().getSelectedItem().getServicer()==null) {
            btnMarkDone.setText("Mark Incomplete");
        } else {
            btnMarkDone.setText("Mark Complete");
        }
    }
}


