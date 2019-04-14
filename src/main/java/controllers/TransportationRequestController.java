package controllers;

import com.jfoenix.controls.*;
import database.LocationTable;
import database.TransportationTable;
import helpers.UserHelpers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import models.map.Location;
import models.services.TransportationRequest;
import models.user.User;

import java.net.URL;
import java.sql.Timestamp;
import java.util.*;

public class TransportationRequestController  {

   public JFXComboBox cmbStartLoc;
   public JFXComboBox cmbEndLoc;
   public JFXTextField txtDetails;
   public JFXDatePicker datDate;
   public JFXTimePicker datTime;

    public JFXButton btnSettings;
    public TableView<TransportationRequest> tblData;
    //public TableColumn<SanitationRequest,String> tblRequestID;
    public TableColumn<TransportationRequest,String> tblStartLoc;
    public TableColumn<TransportationRequest,String> tblEndLoc;
    public TableColumn<TransportationRequest,String> tblDetails;
    public TableColumn<TransportationRequest,String> tblTime;
    public TableColumn<TransportationRequest,String> tblDate;
    public TableColumn<TransportationRequest,String> tblRequester;
    public TableColumn<TransportationRequest,String> tblClaimTime;
    public TableColumn<TransportationRequest,String> tblServicer;
    public TableColumn<TransportationRequest,String> tblServiceTime;


    public JFXButton btnSendRequest;
    public JFXButton btnMarkDone;
    public JFXButton btnNavigate;
    public JFXButton btnClaim;
    public JFXTabPane tabMenu;
    public JFXTabPane floorMenu;

    ObservableList<TransportationRequest> transports = FXCollections.observableArrayList();

//    @Override
    public void initialize() {

        System.out.println("initializing ,..");
//        super.initialize(location, resources);
//        MapDisplay.displayCust(this, panes, TextPane);
//        VisualRealtimeController.setPanMap(panFloor1);
//        initSanitation();
//        updateSanitation();

        initTransportation();
        updateTransportation();

        //todo fix navigate btn disable

//        tblData.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
//            btnNavigate.setDisable(false);
//
//        });
        TransportationRequest selected = tblData.getSelectionModel().getSelectedItem();
        if(selected!=null)
        if(selected.getServicer()==null||selected.getServicer().equals(UserHelpers.getCurrentUser())) {//only enable claiming if unclaimed
            btnClaim.setDisable(false);
        }else{
            btnClaim.setDisable(true);
        }

        cmbStartLoc.valueProperty().addListener(((observable, oldValue, newValue) -> {
           // btnSendRequest.setDisable(false);
            updateRequestBTNs();
        }));
        cmbStartLoc.valueProperty().addListener(((observable, oldValue, newValue) -> {
           // btnSendRequest.setDisable(false);
            updateRequestBTNs();
        }));

    }

    private void initTransportation(){
//        tblRequestID.setCellValueFactory(new PropertyValueFactory<>("RequestID"));
        tblStartLoc.setCellValueFactory(new PropertyValueFactory<>("StartLocation"));
        tblEndLoc.setCellValueFactory(new PropertyValueFactory<>("EndLocation"));
        //tblPriority.setCellValueFactory(new PropertyValueFactory<>("Priority"));
        //tblStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
        tblDetails.setCellValueFactory(new PropertyValueFactory<>("Description"));
        tblRequester.setCellValueFactory(new PropertyValueFactory<>("RequesterUserName"));
        tblClaimTime.setCellValueFactory(new PropertyValueFactory<>("ClaimedTime"));
        tblServiceTime.setCellValueFactory(new PropertyValueFactory<>("CompletedTime"));
        tblServicer.setCellValueFactory(new PropertyValueFactory<>("ServicerUserName"));
        System.out.println(transports.toString());
        System.out.println("H");
        if(!transports.isEmpty())
            tblData.setItems(transports);

        HashMap<String, Location> locations = LocationTable.getLocations();

        LinkedList<String> locationNames = new LinkedList<>();
        if(locations != null) {
            Iterator it = locations.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();

                Location location = (Location) pair.getValue();

                locationNames.add(location.getLongName());

                it.remove();
            }
        }

        for(String name : locationNames) {
            cmbStartLoc.getItems().add(name);
            cmbEndLoc.getItems().add(name);
        }
    }

    private void updateTransportation() {
        List<TransportationRequest> lstReqs = TransportationTable.getTransportationRequests();
        if(lstReqs!=null)
         transports.addAll(lstReqs);
    }

    public void navigateTo(){

        //todo update with new floor select btns
//        Location start = map.getLocation(MapController.getTempStart());
//        Location end = tblData.getSelectionModel().getSelectedItem().getLocation();
//
//        PathFinder.printPath(panes, TextPane, map, start, end);
//
//        String floor = start.getFloor();
//        int floorIndex;
//        switch (floor) {
//            case "1":
//                floorIndex = 3;
//                break;
//            case "2":
//                floorIndex = 1;
//                break;
//            case "3":
//                floorIndex = 0;
//                break;
//            case "L1":
//                floorIndex = 3;
//                break;
//            default:
//                floorIndex = 4;
//                break;
//        }
//
//        tabMenu.getSelectionModel().select(0);
//        floorMenu.getSelectionModel().select(floorIndex);
    }

    public void tblClick(){
        updateClaimBtn();
        updateTableBTNs();
    }

    public void requestClick(){updateRequestBTNs();}

    public void updateRequestBTNs(){
        btnSendRequest.setDisable(cmbStartLoc.getValue().equals("")||cmbEndLoc.getValue().equals("")||txtDetails.getText().equals("")||datDate.getValue().equals("")||datTime.getValue().equals(""));
    }

    public void updateTableBTNs(){

        TransportationRequest selected = tblData.getSelectionModel().getSelectedItem();
        User servicer = selected.getServicer();
        boolean btnClaimEnabled = servicer == null || (servicer.equals(UserHelpers.getCurrentUser())&& selected.getStatus() == TransportationRequest.Status.INCOMPLETE);
        btnClaim.setDisable(!btnClaimEnabled);
        boolean btnMarkDoneEnabled = servicer != null && servicer.equals(UserHelpers.getCurrentUser());
        btnMarkDone.setDisable(!btnMarkDoneEnabled);

    }
    public void claimJob(){

        TransportationRequest selected = tblData.getSelectionModel().getSelectedItem();

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

        TransportationTable.editTransportationRequest(selected);

        updateClaimBtn();
        updateTableBTNs();
        tblData.refresh();

    }

    public void markDone(){
        TransportationRequest selected = tblData.getSelectionModel().getSelectedItem();

        if (selected.getStatus() == TransportationRequest.Status.COMPLETE) {
            selected.setStatus(TransportationRequest.Status.INCOMPLETE);
            selected.setCompletedTime(null);
        } else {
            selected.setStatus(TransportationRequest.Status.COMPLETE);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            selected.setCompletedTime(timestamp);
        }
        TransportationTable.editTransportationRequest(selected);
        tblData.refresh();

        updateTableBTNs();
        updateMarkDoneBtn();
    }

    private void updateMarkDoneBtn() {
        if (tblData.getSelectionModel().getSelectedItem().getStatus() == TransportationRequest.Status.COMPLETE) {
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

    public void sendRequest(MouseEvent event) {
        event.consume();

        // Get request data from UI fields
        String description = txtDetails.getText();
        //Location startLoc = (String) cmbStartLoc.getValue();
        Location startLoc = LocationTable.getLocationByID((String) cmbStartLoc.getValue());

        Location endLoc = LocationTable.getLocationByID((String) cmbEndLoc.getValue());

        // Send request to database
        TransportationRequest request = new TransportationRequest(
                startLoc,
                endLoc,
               // ServiceRequest.Status.INCOMPLETE,
                description,
                UserHelpers.getCurrentUser()
               // null,
              //  null


        );
        TransportationTable.addTransportationRequest(request);

        // Deactivate popup
    }

    public void cancelScr(MouseEvent event){
        event.consume();
        //ScreenController.deactivate();//todo figure out appropriate action after UI refactor
    }
}


