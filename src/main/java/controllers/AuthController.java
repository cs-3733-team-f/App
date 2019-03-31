package controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import helpers.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AuthController {
    public JFXTextField emailField;
    public JFXPasswordField passwordField;
    public Label errorMessage;

    private Constants.Auth authenticate(String username, String password) {
        String TEMP_ADMIN_USERNAME = "root";
        String TEMP_ADMIN_PASSWORD = "";
        String TEMP_EMPLOYEE_USERNAME = "employee";
        String TEMP_EMPLOYEE_PASSWORD = "";
        String TEMP_CUSTODIAN_USERNAME = "custodian";
        String TEMP_CUSTODIAN_PASSWORD = "";


        if (username.equals(TEMP_ADMIN_USERNAME) && password.equals(TEMP_ADMIN_PASSWORD)) {
            return Constants.Auth.ADMIN;
        } else if (username.equals(TEMP_EMPLOYEE_USERNAME) && password.equals(TEMP_EMPLOYEE_PASSWORD)) {
            return Constants.Auth.EMPLOYEE;
        } else if (username.equals(TEMP_CUSTODIAN_USERNAME) && password.equals(TEMP_CUSTODIAN_PASSWORD)) {
            return Constants.Auth.CUSTODIAN;
        } else {
            return Constants.Auth.USER;
        }
    }

    public void handleLogin(ActionEvent actionEvent) throws Exception {
        Constants.Auth authType = authenticate(emailField.getText(), passwordField.getText());
        if(authType == Constants.Auth.ADMIN) {
            errorMessage.setVisible(false);
            errorMessage.setManaged(false);

            ScreenController.deactivate();
            ScreenController.activate("admin-map");
        } else if (authType == Constants.Auth.EMPLOYEE) {
            errorMessage.setVisible(false);
            errorMessage.setManaged(false);

            ScreenController.deactivate();
            ScreenController.activate("employee-map");
        } else if (authType == Constants.Auth.CUSTODIAN) {
            errorMessage.setVisible(false);
            errorMessage.setManaged(false);

            ScreenController.deactivate();
            ScreenController.activate("custodian-map");

        }else {
            errorMessage.setText("Invalid credentials");
            errorMessage.setManaged(true);
            errorMessage.setVisible(true);
        }

    }

    public void goBack(ActionEvent actionEvent) throws Exception {
        actionEvent.consume();
        ScreenController.deactivate();
        ScreenController.activate("welcome");
    }

}
