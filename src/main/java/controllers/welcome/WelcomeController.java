package controllers.welcome;

import com.jfoenix.controls.JFXButton;
import controllers.ScreenController;
import helpers.Constants;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class WelcomeController {

    public void welcomeLogin() throws Exception{
//        ScreenController.deactivate();
        ScreenController.activate(Constants.Routes.USER_MAP);
    }

    public void employeeLogin() throws Exception{
//        ScreenController.deactivate();
        ScreenController.activate(Constants.Routes.LOGIN);
        Thread t = new Thread(() -> {
            try {
                ScreenController.preLoad(Constants.Routes.ADMIN_MAP);
                ScreenController.preLoad(Constants.Routes.CUSTODIAN_MAP);
                ScreenController.preLoad(Constants.Routes.EMPLOYEE_MAP);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t.setDaemon(true);
        t.start();
    }
}
