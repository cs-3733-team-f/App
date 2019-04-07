package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import database.Database;
import database.LocationTable;
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
import map.PathFinder;
import models.map.Location;
import models.sanitation.SanitationRequest;
import models.user.User;

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
    public JFXTabPane tabMenu;
    public JFXTabPane floorMenu;
    //public JFXTab tabPathFinder;

    ObservableList<SanitationRequest> spills = FXCollections.observableArrayList();

    public void initialize() {



        MapDisplay.displayCust(new AnchorPane[] {panFloorL2, panFloorL1, panFloor1, panFloor2, panFloor3});
        VisualRealtimeController.setPanMap(panFloor1);
        toolTip();

//        toolTip();
//        MapDisplay.displayCust(panMap, "1");
        initSanitation();
        updateSanitation();

        tblData.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {// only enable btns if item selected

            /*
            if(selected.getServicer()==null||selected.getServicer().equals(UserHelpers.getCurrentUser())) {
                btnClaim.setDisable(false);
           }else{
                btnClaim.setDisable(true);
            }
            if(selected.getServicer()!=null && selected.getServicer().equals(UserHelpers.getCurrentUser())) {
                btnMarkDone.setDisable(false);
            }else{
                btnMarkDone.setDisable(true);
            }
            */


            btnNavigate.setDisable(false);

        });
        SanitationRequest selected = tblData.getSelectionModel().getSelectedItem();
        if(selected!=null)
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
        tblLocation.setCellValueFactory(new PropertyValueFactory<>("LocationShortName"));
        tblPriority.setCellValueFactory(new PropertyValueFactory<>("Priority"));
        tblStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
        tblDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        //tblUser.setCellValueFactory(new PropertyValueFactory<>("User"));
        tblRequester.setCellValueFactory(new PropertyValueFactory<>("RequesterUserName"));
        tblClaimTime.setCellValueFactory(new PropertyValueFactory<>("ClaimedTime"));
        tblServiceTime.setCellValueFactory(new PropertyValueFactory<>("CompletedTime"));
        tblServicer.setCellValueFactory(new PropertyValueFactory<>("ServicerUserName"));
        System.out.println(spills.toString());
        tblData.setItems(spills);
    }

    private void updateSanitation() {
        List<SanitationRequest> lstReqs = SanitationTable.getSanitationRequests();
        if(lstReqs!=null)
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
        //Location start= LocationTable.getLocationByID("");

        //Location start = Database.getLocationbyID();
        Location end = tblData.getSelectionModel().getSelectedItem().getLocation();
        //map.navigate(start,end);


        //PathFinder.findPath(start,end);
        String floor = end.getFloor();
        int floorIndex=3;
        if(floor.equals("1")){
            floorIndex=3;
        }else if(floor.equals("2")){
            floorIndex=1;
        }else if (floor.equals("3")){
            floorIndex=0;
        }else if (floor.equals("L1")){
            floorIndex=3;
        }else if (floor.equals(("L2"))){
            floorIndex=4;
        }

        tabMenu.getSelectionModel().select(0);
        floorMenu.getSelectionModel().select(floorIndex);


    }

    public void tblClick(){
        updateClaimBtn();
        updateAllBTNS();
    }

    public void updateAllBTNS(){

        SanitationRequest selected = tblData.getSelectionModel().getSelectedItem();
        User servicer = selected.getServicer();
        boolean btnClaimEnabled = servicer == null || (servicer.equals(UserHelpers.getCurrentUser())&& selected.getStatus() == SanitationRequest.Status.INCOMPLETE);
        btnClaim.setDisable(!btnClaimEnabled);
        boolean btnMarkDoneEnabled = servicer != null && servicer.equals(UserHelpers.getCurrentUser());
        btnMarkDone.setDisable(!btnMarkDoneEnabled);

    }
    public void claimJob(){

        SanitationRequest selected = tblData.getSelectionModel().getSelectedItem();

//        if (selected.getServicer() != null) {
//        }else{
//            selected.setServicer(null);
//        }


        if (selected.getServicer() != null) {
           //if(selected.getServicerUserName().equals(UserHelpers.getCurrentUser())){
               selected.setServicer(null);
               selected.setClaimedTime(null);
           //}

        } else {
            selected.setServicer(UserHelpers.getCurrentUser());
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            selected.setClaimedTime(timestamp);
        }

        SanitationTable.editSanitationRequest(selected);

        updateClaimBtn();

        updateAllBTNS();

        tblData.refresh();

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
            selected.setCompletedTime(null);
        } else {
            selected.setStatus(SanitationRequest.Status.COMPLETE);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            selected.setCompletedTime(timestamp);
        }
        SanitationTable.editSanitationRequest(selected);
        tblData.refresh();

        updateAllBTNS();

        updateMarkDoneBtn();
    }

    private void updateMarkDoneBtn() {
        if (tblData.getSelectionModel().getSelectedItem().getStatus() == SanitationRequest.Status.COMPLETE) {
            btnMarkDone.setText("Mark Incomplete");
        } else {
            btnMarkDone.setText("Mark Complete");
        }
    }

    private void updateClaimBtn() {
        if (tblData.getSelectionModel().getSelectedItem().getServicer()!=null) {
            btnClaim.setText("Un-Claim");
        } else {
            btnClaim.setText("Claim");
        }
    }
}


