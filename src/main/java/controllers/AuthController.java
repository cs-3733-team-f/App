package controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AuthController {

    @FXML
    JFXTextField emailField;

    @FXML
    JFXPasswordField passwordField;

    @FXML
    Label errorMessage;


    final String TEMP_USERNAME = "root";
    final String TEMP_PASSWORD = "";

    /**
     * Pseudo-authentication method used for checking user credentials
     * @param username
     * @param password
     * @return boolean indicating if the credentials were correct
     */
    private boolean authenticate(String username, String password) {
        return username.equals(TEMP_USERNAME) && password.equals(TEMP_PASSWORD);
    }

    /**
     * Method to switch
     * @param actionEvent
     * @throws Exception
     */
    public void handleLogin(ActionEvent actionEvent) throws Exception {

        // Button was clicked, check credentials
        // Username: emailField.getText()
        // Password: passwordField.getText()
        if(authenticate(emailField.getText(), passwordField.getText())) {
            errorMessage.setVisible(false);
            errorMessage.setManaged(false);

            // Hide window
            ScreenController.deactivate();

            // Move to another scene
            ScreenController.moveTo("main");

        } else {
            errorMessage.setText("Invalid credentials");
            errorMessage.setManaged(true);
            errorMessage.setVisible(true);
        }

    }

    public void goBack(ActionEvent actionEvent) throws Exception {
        actionEvent.consume();
        ScreenController.deactivate();
        ScreenController.moveTo("welcome");
    }

}
