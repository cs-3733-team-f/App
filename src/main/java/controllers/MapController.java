package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXNodesList;
import helpers.Constants;
import javafx.fxml.FXML;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import map.Map;
import map.MapDisplay;
import map.MapParser;

public class MapController {

    /**
     * Side menu
     */
    @FXML
    JFXButton settingsButton;

    @FXML
    JFXButton logoutButton;

    @FXML
    JFXButton settings2Button;

    @FXML
    JFXNodesList nodesList;




    /**
     * Map
     */
    @FXML
    ImageView floorOneMap;

    @FXML
    AnchorPane panMap;

    @FXML
    JFXButton zoomIn;

    @FXML
    JFXButton zoomOut;

    double sceneX, sceneY;
    double translateX, translateY;

    public void initialize() {
        // Set tooltip for sidemenu buttons
        settingsButton.setTooltip(new Tooltip(Constants.SETTINGS_BUTTON_TOOLTIP));
        logoutButton.setTooltip(new Tooltip(Constants.LOGOUT_BUTTON_TOOLTIP));
       // settings2Button.setTooltip(new Tooltip(Constants.SETTINGS2_BUTTON_TOOLTIP));


        JFXButton btnMenu = settingsButton;

        JFXButton btnOption2 = new JFXButton("Settings");

        JFXButton btnCollapse = new JFXButton("<<");
        btnCollapse.setTooltip(new Tooltip("Collapse menu"));
        //btnCollapse.setOnAction(e->nodesList.animateList(false));

        nodesList.setSpacing(10);

       // nodesList.addAnimatedNode(settingsButton);
      //  nodesList.addAnimatedNode(btnOption2);
      //  nodesList.addAnimatedNode(logoutButton);

       // nodesList.addAnimatedNode(btnCollapse);


        // Set icons for sidemenu buttons
//        settingsButton.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.COG));
//        logoutButton.setGraphic(new FontAwesomeIconView(FontAwesomeIcon.SIGN_OUT));

        Map map = MapParser.parse("/data/nodes.csv", "/data/edges.csv");
        MapDisplay.display(map, panMap, "Tower", "1");
    }

    public void floorOneMapOnMousePressed(MouseEvent event) {

        // Handle onMousePressed event
        sceneX = event.getSceneX();
        sceneY = event.getSceneY();

        translateX = ((AnchorPane) event.getSource()).getTranslateX();
        translateY = ((AnchorPane) event.getSource()).getTranslateY();
    }

    public void floorOneMapOnMouseDragged(MouseEvent event) {

        // Handle onMouseDragged event
        double offsetX = event.getSceneX() - sceneX;
        double offsetY = event.getSceneY() - sceneY;
        double newTranslateX = translateX + offsetX;
        double newTranslateY = translateY + offsetY;

        ((AnchorPane) event.getSource()).setTranslateX(newTranslateX);
        ((AnchorPane) event.getSource()).setTranslateY(newTranslateY);
    }

    public void floorOneMapScroll(ScrollEvent event) {
        ((AnchorPane) event.getSource()).setTranslateX(((AnchorPane) event.getSource()).getTranslateX() + event.getDeltaX());
        ((AnchorPane) event.getSource()).setTranslateY(((AnchorPane) event.getSource()).getTranslateY() + event.getDeltaY());

        if (((AnchorPane) event.getSource()).getTranslateX() <= 0) {
            ((AnchorPane) event.getSource()).setTranslateX(0);
        }

        if (((AnchorPane) event.getSource()).getTranslateX() >= 180) {
            ((AnchorPane) event.getSource()).setTranslateX(180);
        }

        if (((AnchorPane) event.getSource()).getTranslateY() <= 0) {
            ((AnchorPane) event.getSource()).setTranslateY(0);
        }

        if (((AnchorPane) event.getSource()).getTranslateY() >= 180) {
            ((AnchorPane) event.getSource()).setTranslateY(180);
        }
    }

    public void floorOneMapZoom(ZoomEvent event) {
        ((AnchorPane) event.getSource()).setScaleX(((AnchorPane) event.getSource()).getScaleX() * event.getZoomFactor());
        ((AnchorPane) event.getSource()).setScaleY(((AnchorPane) event.getSource()).getScaleY() * event.getZoomFactor());
        if(((AnchorPane) event.getSource()).getScaleX() <= 0.55 && ((AnchorPane) event.getSource()).getScaleY() <= 0.55) {
            ((AnchorPane) event.getSource()).setScaleX(0.55);
            ((AnchorPane) event.getSource()).setScaleY(0.55);
        }
        if(((AnchorPane) event.getSource()).getScaleX() >= 8 && ((AnchorPane) event.getSource()).getScaleY() >= 8) {
            ((AnchorPane) event.getSource()).setScaleX(8);
            ((AnchorPane) event.getSource()).setScaleY(8);
        }
    }

    public void floorOneMapZoomDone(ZoomEvent event) {
        if(((AnchorPane) event.getSource()).getScaleX() <= 0.85 && ((AnchorPane) event.getSource()).getScaleY() <= 0.85) {
            ((AnchorPane) event.getSource()).setScaleX(0.85);
            ((AnchorPane) event.getSource()).setScaleY(0.85);
        }
        if(((AnchorPane) event.getSource()).getScaleX() >= 4 && ((AnchorPane) event.getSource()).getScaleY() >= 4) {
            ((AnchorPane) event.getSource()).setScaleX(4);
            ((AnchorPane) event.getSource()).setScaleY(4);
        }
    }

    public void floorOneMapZoomIn(MouseEvent event) {
        System.out.println("Zoom In function under construction: " + event.toString());
        floorOneMapZoom(new ZoomEvent(ZoomEvent.ZOOM, 0, 0, 0, 0, false, false, false, false,
                false, false, 4.2, 4.2, null));
        event.consume();
    }

    public void floorOneMapZoomOut(MouseEvent event) {
        System.out.println("Zoom Out function under construction: " + event.toString());
        System.out.println("Zoom In function under construction: " + event.toString());
        floorOneMapZoom(new ZoomEvent(ZoomEvent.ZOOM, 0, 0, 0, 0, false, false, false, false,
                false, false, 0.2, 0.2, null));
        event.consume();
    }

    public void logOut(MouseEvent event) throws Exception {
        event.consume();
        ScreenController.deactivate();
        ScreenController.activate("welcome");
    }
}
