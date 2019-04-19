package controllers.AboutPage;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controllers.node.PopUpController;
import controllers.ScreenController;
import helpers.UserHelpers;
import javafx.scene.input.MouseEvent;
import models.map.Location;
import java.net.URL;
import java.util.ResourceBundle;


public class LogoutPopupController {

    public JFXTextField txtTimeoutPeriod;
    public JFXButton selectBTN;
    public JFXButton cancelBTN;

    /**
     * @brief Sends sanitation request to database.
     * @param event MouseEvent for 'Select' button press.
     */
    public void sendRequest(MouseEvent event) {
        event.consume();

        // Get request data from UI fields
        String description = txtTimeoutPeriod.getText();
    }

    /**
     * @brief Exits the sanitation request popup.
     * @param event MouseEvent of 'Cancel' button press.
     */
    public void goBack(MouseEvent event) throws Exception{
        // ((Stage) (((Node) event.getSource()).getScene().getWindow())).close();
        event.consume();
        ScreenController.deactivate();
    }

    /**
     * @brief Empty initialize script.
     */
//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//        cmbPriority.valueProperty().addListener(((observable, oldValue, newValue) -> {
//            submitBTN.setDisable(false);
//        }));
    }

