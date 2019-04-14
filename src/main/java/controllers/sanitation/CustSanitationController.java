package controllers.sanitation;

import com.jfoenix.controls.JFXButton;
import database.SanitationTable;
import helpers.UserHelpers;
import javafx.scene.input.MouseEvent;
import models.sanitation.SanitationRequest;
import models.user.User;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class CustSanitationController extends SanitationController {
    public JFXButton btnMarkDone;
    public JFXButton btnNavigate;
    public JFXButton btnClaim;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        tblData.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnNavigate.setDisable(false);
        });
    }

    public void tblClick(MouseEvent mouseEvent) {
        updateClaimBtn();
        updateBtns();
    }

    private void updateBtns(){
        SanitationRequest selected = tblData.getSelectionModel().getSelectedItem();
        if (selected != null) {
            User servicer = selected.getServicer();

            boolean btnClaimEnabled = (servicer == null || (servicer.equals(UserHelpers.getCurrentUser()) &&
                    (selected.getStatus() == SanitationRequest.Status.INCOMPLETE)));
            btnClaim.setDisable(!btnClaimEnabled);

            boolean btnMarkDoneEnabled = (servicer != null && servicer.equals(UserHelpers.getCurrentUser()));
            btnMarkDone.setDisable(!btnMarkDoneEnabled);
        }
    }

    public void claimJob(MouseEvent mouseEvent) {
        SanitationRequest selected = tblData.getSelectionModel().getSelectedItem();

        if (selected.getServicer() != null) {
            selected.setServicer(null);
            selected.setClaimedTime(null);
        } else {
            selected.setServicer(UserHelpers.getCurrentUser());
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            selected.setClaimedTime(timestamp);
        }

        SanitationTable.editSanitationRequest(selected);
        tblData.refresh();
        updateClaimBtn();
        updateBtns();
    }

    private void updateClaimBtn() {
        if (tblData.getSelectionModel().getSelectedItem() != null && tblData.getSelectionModel().getSelectedItem().getServicer() != null) {
            btnClaim.setText("Un-Claim");
        } else {
            btnClaim.setText("Claim");
        }
    }

    public void markDone(MouseEvent mouseEvent) {
        SanitationRequest selected = tblData.getSelectionModel().getSelectedItem();

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
        updateBtns();
        updateMarkDoneBtn();
    }

    private void updateMarkDoneBtn() {
        if (tblData.getSelectionModel().getSelectedItem().getStatus() == SanitationRequest.Status.COMPLETE) {
            btnMarkDone.setText("Mark Incomplete");
        } else {
            btnMarkDone.setText("Mark Complete");
        }
    }

    public void navigateTo(MouseEvent mouseEvent) {
        //        Location start = map.getLocation(MapController1.getTempStart());
//        Location end = tblData.getSelectionModel().getSelectedItem().getLocation();
//        PathFinder.printPath(panes, TextPane, map, start, end);
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
//        tabMenu.getSelectionModel().select(0);
//        floorMenu.getSelectionModel().select(floorIndex);
    }
}
