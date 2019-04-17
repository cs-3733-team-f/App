package edu.wpi.cs3733d19.teamF.sanitation; /**
 * @brief Sanitation API controller.
 */

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    // FXML Objects
    @FXML JFXComboBox priorityComboBox;
    @FXML String LOW;
    @FXML String MEDIUM;
    @FXML String HIGH;
    @FXML JFXTextField descriptionTextField;
    @FXML JFXButton submitBtn;
    @FXML JFXButton cancelBtn;
    @FXML TableView<SanitationRequest> tableView;
    @FXML TableColumn<SanitationRequest, String> columnLocation;
    @FXML TableColumn<SanitationRequest, String> columnPriority;
    @FXML TableColumn<SanitationRequest, String> columnStatus;
    @FXML TableColumn<SanitationRequest, String> columnDescription;
    @FXML TableColumn<SanitationRequest, String> columnRequester;
    @FXML TableColumn<SanitationRequest, String> columnRequestTime;
    @FXML TableColumn<SanitationRequest, String> columnServicer;
    @FXML TableColumn<SanitationRequest, String> columnClaimedTime;
    @FXML TableColumn<SanitationRequest, String> columnCompletedTime;
    @FXML JFXButton claimBtn;
    @FXML JFXButton completeBtn;
    @FXML JFXButton deleteBtn;

    // Requests array
    private ObservableList<SanitationRequest> observableRequests = FXCollections.observableArrayList();

    /**
     * @brief Initializes controller.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Initialize submit button
        submitBtn.setDisable(true);

        // Update table buttons and table
        updateTableButtons();
        updateRequestTable();

        // Initialize table view
        columnLocation.setCellValueFactory(new PropertyValueFactory<>("LocationID"));
        columnPriority.setCellValueFactory(new PropertyValueFactory<>("PriorityString"));
        columnStatus.setCellValueFactory(new PropertyValueFactory<>("StatusString"));
        columnDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        columnRequester.setCellValueFactory(new PropertyValueFactory<>("RequesterUsername"));
        columnRequestTime.setCellValueFactory(new PropertyValueFactory<>("RequestTimeString"));
        columnServicer.setCellValueFactory(new PropertyValueFactory<>("ServicerUsername"));
        columnClaimedTime.setCellValueFactory(new PropertyValueFactory<>("ClaimedTimeString"));
        columnCompletedTime.setCellValueFactory(new PropertyValueFactory<>("CompletedTimeString"));
        tableView.setItems(observableRequests);

        // Update table buttons on every table update
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateTableButtons();
        });
    }

    /**
     * @brief Submit button callback.
     */
    @FXML void submitBtnCallback() {

        // Get request details from UI fields
        String description = descriptionTextField.getText();
        String priority = (String)priorityComboBox.getValue();

        // Submit request to database
        SanitationService.makeRequest(description, priority);

        // Reset the submission UI
        descriptionTextField.setText("");
        submitBtn.setDisable(true);

        // Update the request table
        updateRequestTable();
    }

    /**
     * @brief Cancel button callback.
     */
    @FXML void cancelBtnCallback() {
        SanitationService.close();
    }

    /**
     * @brief Claim button callback.
     */
    @FXML void claimBtnCallback() {
        SanitationRequest selected = tableView.getSelectionModel().getSelectedItem();
        if (selected.getServicerUsername() == null) {
            SanitationService.markRequestClaimed(selected);
        } else {
            SanitationService.markRequestUnclaimed(selected);
        }
        updateTableButtons();
        updateRequestTable();
    }

    /**
     * @brief Complete button callback.
     */
    @FXML void completeBtnCallback() {
        SanitationRequest selected = tableView.getSelectionModel().getSelectedItem();
        if (selected.getStatus() == SanitationRequest.Status.COMPLETE) {
            SanitationService.markRequestIncomplete(selected);
        } else {
            SanitationService.markRequestComplete(selected);
        }
        updateTableButtons();
        updateRequestTable();
    }

    /**
     * @brief Delete button callback.
     */
    @FXML void deleteBtnCallback() {
        SanitationRequest selected = tableView.getSelectionModel().getSelectedItem();
        SanitationTable.deleteRequest(selected);
        updateRequestTable();
    }

    /**
     * @brief Table click callback.
     */
    @FXML void tableClickCallback() {
        updateTableButtons();
    }

    /**
     * @brief Description text input on-key-release callback
     */
    @FXML void descriptionOnKeyReleaseCallback() {
        submitBtn.setDisable(descriptionTextField.getText().length() == 0);
    }

    /**
     * @brief Updates request table from database
     */
    private void updateRequestTable() {
        List<SanitationRequest> requests = SanitationTable.getRequests();
        observableRequests.setAll(requests);
    }

    /**
     * @brief Update table buttons
     */
    private void updateTableButtons() {

        // Get selected sanitation request
        SanitationRequest selected = tableView.getSelectionModel().getSelectedItem();

        // Simple case for nothing selected
        if (selected == null) {
            claimBtn.setDisable(true);
            completeBtn.setDisable(true);
            deleteBtn.setDisable(true);
            return;
        }

        // Determine if delete button is enabled
        boolean deletingEnabled = SanitationService.getDeletingEnabled();
        deleteBtn.setDisable(!deletingEnabled);

        // Determine if current employee is the servicer of the request
        String servicer = selected.getServicerUsername();
        String employee = SanitationService.getCurrentEmployee().getUsername();
        boolean servicerIsEmployee;
        if (servicer == null) servicerIsEmployee = false;
        else servicerIsEmployee = servicer.equals(employee);

        // Set the button statuses
        if (SanitationService.getServicingEnabled()) {
            boolean claimBtnEnabled = servicer == null || (servicerIsEmployee && selected.getStatus() == SanitationRequest.Status.INCOMPLETE);
            claimBtn.setDisable(!claimBtnEnabled);
            boolean completeBtnEnabled = servicerIsEmployee;
            completeBtn.setDisable(!completeBtnEnabled);
        } else {
            claimBtn.setDisable(true);
            completeBtn.setDisable(true);
        }

        // Set the complete button label
        if (tableView.getSelectionModel().getSelectedItem().getStatus() == SanitationRequest.Status.COMPLETE) {
            completeBtn.setText("Mark Incomplete");
        } else {
            completeBtn.setText("Mark Complete");
        }

        // Set the claim button label
        if (tableView.getSelectionModel().getSelectedItem().getServicerUsername() != null) {
            claimBtn.setText("Un-Claim");
        } else {
            claimBtn.setText("Claim");
        }

    }
}