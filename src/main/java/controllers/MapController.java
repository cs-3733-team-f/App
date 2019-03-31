package controllers;

import com.jfoenix.controls.JFXButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.AnchorPane;

public abstract class MapController {
    public JFXButton btnReturn;
    public ImageView floorOneMap;
    public AnchorPane panMap;
//    public JFXButton zoomIn;
//    public JFXButton zoomOut;

    private double sceneX, sceneY;
    private double translateX, translateY;

    public abstract void initialize();

    public final void floorOneMapOnMousePressed(MouseEvent event) {

        // Handle onMousePressed event
        sceneX = event.getSceneX();
        sceneY = event.getSceneY();

        translateX = ((AnchorPane) event.getSource()).getTranslateX();
        translateY = ((AnchorPane) event.getSource()).getTranslateY();
    }

    public final void floorOneMapOnMouseDragged(MouseEvent event) {

        // Handle onMouseDragged event
        double offsetX = event.getSceneX() - sceneX;
        double offsetY = event.getSceneY() - sceneY;
        double newTranslateX = translateX + offsetX;
        double newTranslateY = translateY + offsetY;

        ((AnchorPane) event.getSource()).setTranslateX(newTranslateX);
        ((AnchorPane) event.getSource()).setTranslateY(newTranslateY);
    }

    public final void floorOneMapScroll(ScrollEvent event) {
        ((AnchorPane) event.getSource()).setTranslateX(((AnchorPane) event.getSource()).getTranslateX() + event.getDeltaX());
        ((AnchorPane) event.getSource()).setTranslateY(((AnchorPane) event.getSource()).getTranslateY() + event.getDeltaY());

        if (((AnchorPane) event.getSource()).getTranslateX() <= -250) {
            ((AnchorPane) event.getSource()).setTranslateX(-250);
        }

        if (((AnchorPane) event.getSource()).getTranslateX() >= 250) {
            ((AnchorPane) event.getSource()).setTranslateX(250);
        }

        if (((AnchorPane) event.getSource()).getTranslateY() <= -350) {
            ((AnchorPane) event.getSource()).setTranslateY(-350);
        }

        if (((AnchorPane) event.getSource()).getTranslateY() >= 350) {
            ((AnchorPane) event.getSource()).setTranslateY(350);
        }
    }

    public final void floorOneMapZoom(ZoomEvent event) {
        ((AnchorPane) event.getSource()).setScaleX(((AnchorPane) event.getSource()).getScaleX() * event.getZoomFactor());
        ((AnchorPane) event.getSource()).setScaleY(((AnchorPane) event.getSource()).getScaleY() * event.getZoomFactor());
        if(((AnchorPane) event.getSource()).getScaleX() <= 0.85 && ((AnchorPane) event.getSource()).getScaleY() <= 0.85) {
            ((AnchorPane) event.getSource()).setScaleX(0.85);
            ((AnchorPane) event.getSource()).setScaleY(0.85);
        }
        if(((AnchorPane) event.getSource()).getScaleX() >= 8 && ((AnchorPane) event.getSource()).getScaleY() >= 8) {
            ((AnchorPane) event.getSource()).setScaleX(8);
            ((AnchorPane) event.getSource()).setScaleY(8);
        }
    }

    public final void floorOneMapZoomDone(ZoomEvent event) {
        if(((AnchorPane) event.getSource()).getScaleX() <= 1 && ((AnchorPane) event.getSource()).getScaleY() <= 1) {
            ((AnchorPane) event.getSource()).setScaleX(1);
            ((AnchorPane) event.getSource()).setScaleY(1);
        }
        if(((AnchorPane) event.getSource()).getScaleX() >= 4 && ((AnchorPane) event.getSource()).getScaleY() >= 4) {
            ((AnchorPane) event.getSource()).setScaleX(4);
            ((AnchorPane) event.getSource()).setScaleY(4);
        }
    }

  /*  public void floorOneMapZoomIn(MouseEvent event) {
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
    }*/

    /**
     * Logs out back to the welcome screen
     * @param event
     * @throws Exception
     */
    public final void logOut(MouseEvent event) throws Exception {
        event.consume();
        ScreenController.logOut(btnReturn);
        ScreenController.activate("welcome");
    }

    /**
     * Adds the tooltips
     */
    abstract void toolTip();
}
