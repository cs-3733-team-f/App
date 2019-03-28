package helpers;

import com.jfoenix.animation.alert.JFXAlertAnimation;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class UIHelpers {

    public static final double MIN_WIDTH = 400.0;
    public static final double MIN_PIXELS = 600.0;

    public static double getScreenWidth() {
        return Screen.getPrimary().getBounds().getWidth();
    }

    public static double getScreenHeight() {
        return Screen.getPrimary().getBounds().getHeight();
    }

    public static void createAndShowAlert(Stage stage, String title, String header, String message, String buttonText) {

        JFXDialogLayout layout = new JFXDialogLayout();

        Button btn = new JFXButton(buttonText);
        btn.setStyle("-fx-text-fill: WHITE;");

        Label msg = new Label(message);

        VBox box = new VBox(20);

        box.getChildren().addAll(msg, btn);

        layout.setBody(box);
        JFXAlert<Void> alert = new JFXAlert<>(stage);

        alert.setOverlayClose(true);
        alert.setAnimation(JFXAlertAnimation.CENTER_ANIMATION);
        alert.setContent(layout);
        alert.initModality(Modality.NONE);

        btn.setOnAction(action-> alert.hideWithAnimation());

        alert.show();
    }

}
