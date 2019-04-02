/**
 * @brief Controller for submitting sanitation requests.
 */

package controllers;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import database.Database;
import javafx.beans.value.ChangeListener;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import models.map.Location;
import models.sanitation.SanitationRequest;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SanitationController extends PopUpController {

    public JFXComboBox cmbPriority;
    public JFXTextField txtDescription;
    public JFXButton submitBTN;
    public String LOW;
    public String MEDIUM;
    public String HIGH;

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
        Database.addSanitationRequest(request);

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
     * @brief Enables 'Submit' button when all fields are filled in.
     */
    public void keyReleased(){
        String des = txtDescription.getText();
        String pri = (String)cmbPriority.getValue();

        boolean isDisabled = des.isEmpty() || pri.isEmpty();
        submitBTN.setDisable(isDisabled);

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
        return;
    }
}
