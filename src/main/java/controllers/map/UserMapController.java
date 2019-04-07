package controllers.map;

import com.jfoenix.controls.JFXButton;
import controllers.ScreenController;
import helpers.Constants;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.javafx.IconFontFX;
import jiconfont.javafx.IconNode;
import map.MapDisplay;
import net.kurobako.gesturefx.GesturePane;

import java.net.URL;
import java.util.ResourceBundle;

public class UserMapController extends MapController {
    public JFXButton btnSettings;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        IconFontFX.register(FontAwesome.getIconFont());

        IconNode icon = new IconNode(FontAwesome.COG);
        icon.setIconSize(25);
        icon.setFill(Color.WHITE);
        btnSettings.setText("");
        btnSettings.setGraphic(icon);
    }

    @Override
    public void logOut(MouseEvent event) throws Exception {
        event.consume();
        ScreenController.logOut(btnReturn);
        ScreenController.activate(Constants.Routes.WELCOME);
    }
}
