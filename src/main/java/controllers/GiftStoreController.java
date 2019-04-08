/**
 * @brief Controller for submitting sanitation requests.
 */

package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import database.SanitationTable;
import javafx.scene.input.MouseEvent;
import models.map.Location;
import models.sanitation.SanitationRequest;

import java.net.URL;
import java.util.ResourceBundle;

public class GiftStoreController extends PopUpController {

    public JFXComboBox cmbPriority;
    public JFXTextField txtDescription;
    public JFXButton submitBTN;
    public JFXComboBox cmbItem;
    public JFXComboBox cmbSize;
    public JFXTextField txtRecipient;
    public JFXTextField txtMessage;
    public JFXTextField txtSender;
    public String FLOWERS;
    public String CHOCOLATE;
    public String STUFFEDBEAR;
    public String SMALL;
    public String MEDIUM;
    public String LARGE;


    /**
     * @brief Sends sanitation request to database.
     * @param event MouseEvent for 'Submit' button press.
     */
    public void sendRequest(MouseEvent event) {
        event.consume();

        // Get request data from UI fields
        String description = txtDescription.getText();
        String priority = (String) cmbPriority.getValue();

        // Send request to database
        SanitationRequest request = new SanitationRequest(
                loc,
                SanitationRequest.Priority.valueOf(priority),
                description
        );
        SanitationTable.addSanitationRequest(request);

        // Deactivate popup
        ScreenController.deactivate();
    }

    /**
     * @brief Exits the sanitation request popup.
     * @param event MouseEvent of 'Back' button press.
     */
    public void goBack(MouseEvent event) throws Exception{
       // ((Stage) (((Node) event.getSource()).getScene().getWindow())).close();
        event.consume();
        ScreenController.deactivate();
    }

    /**
     * @brief Sets location of sanitation request.
     * @param loc Location of request.
     */
    public void setLoc(Location loc){
        this.loc = loc;
    }

    /**
     * @brief Empty initialize script.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbPriority.valueProperty().addListener(((observable, oldValue, newValue) -> {
            submitBTN.setDisable(false);
        }));
    }
}
