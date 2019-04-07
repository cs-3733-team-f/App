package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import database.Database;
import database.EdgeTable;
import helpers.Constants;
import helpers.MapHelpers;
import helpers.UIHelpers;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import map.MapDisplay;
import models.map.Edge;
import models.map.Location;
import java.awt.*;

public class AdminMapController extends MapController {
    public JFXButton btnDownload;
    public JFXButton btnBooking;

    private static boolean enableAddNode = false;
    private static boolean enableEditEdge = false;

    public AnchorPane outerTabAnchor;
    public JFXTextField searchBox;

    public static String selectedFloor = "1", selectedBuilding = "Shapiro";

    private static Location selectedLocation; // Location that is being modified or created

    public static void locationSelectEvent(Location loc) {

        if(enableEditEdge) {
            if (selectedLocation != loc) {
                selectLocation(loc);

            }
            else {
                deselectLocation();
                VisualRealtimeController.visuallyDeselectCircle(loc);
            }
        }
    }

    public static boolean isEnableAddNode() {
        return enableAddNode;
    }

    public static boolean isEnableEditEdge() {
        return enableEditEdge;
    }

    public static void selectLocation(Location loc) {
        if(selectedLocation != null) {
            Edge edge = MapHelpers.generateEdge(selectedLocation, loc);
            boolean edgeToggle = EdgeTable.toggleEdge(edge);
            if(edgeToggle == Constants.SELECTED &&
                    edge.getStart().getFloor().equals(edge.getEnd().getFloor())) {
                Line line = UIHelpers.generateLineFromEdge(edge);
                edge.setLine(line);
                VisualRealtimeController.addLine(line);
            } else {
                VisualRealtimeController.removeLine(edge.getLine());
            }
        }
        else {
            selectedLocation = loc;
            VisualRealtimeController.visuallySelectCircle(loc);
        }
    }

    public static void deselectLocation() {
        selectedLocation = null;
    }

    public void enableEdgeEditor() {
        try {
            VisualRealtimeController.visuallyDeselectCircle(selectedLocation);
        } catch(Exception e) {
            // Circle is null
        }

        selectedLocation = null;

        enableEditEdge = !enableEditEdge;
    }
    public void enableNodeCreation() {
        enableAddNode = !enableAddNode;
    }

    public void initialize() {
        // Set tooltip
        toolTip();

        MapDisplay.displayAdmin(new AnchorPane[] {panFloorL2, panFloorL1, panFloor1, panFloor2, panFloor3});
        VisualRealtimeController.setPanMap(panFloor1);
        selectedLocation = null;

        // Initialize search box
        searchBox = new JFXTextField();
        searchBox.setMaxWidth(UIHelpers.getScreenWidth());
        searchBox.setPadding(new Insets(5));

        // Add search box to layout
        outerTabAnchor.getChildren().add(searchBox);

        // Add event listener for search box
        searchBox.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {



            }
        });
    }

    void toolTip() {
        btnDownload.setTooltip(new Tooltip(Constants.DOWNLOAD_BUTTON_TOOLTIP));
        btnReturn.setTooltip(new Tooltip(Constants.LOGOUT_BUTTON_TOOLTIP));
    }

    public void clickDownload(MouseEvent event) throws Exception {
        event.consume();
        ScreenController.deactivate();
        ScreenController.activate(Constants.Routes.DOWNLOAD);
    }

    public void displayBooking(MouseEvent event) throws Exception {
        event.consume();
        ScreenController.deactivate();
        ScreenController.activate(Constants.Routes.BOOKING_WINDOW);
    }
    public void addNode(MouseEvent event) throws Exception {
        Point selectedPoint = new Point((int)event.getX(), (int)event.getY());
        Point nodeCoordinate = MapHelpers.mapPointToMapCoordinates(selectedPoint);
        Circle circ = MapHelpers.generateNode(nodeCoordinate);
//        Point nodeDisplayCoordinate = MapHelpers.mapPointToMapCoordinates()
        Location loc = new Location(null, nodeCoordinate.x, nodeCoordinate.y,
                this.selectedFloor, this.selectedBuilding, Constants.NodeType.HALL,
                "RECENT_ADDITION", "RECENT_ADDITION");

        ScreenController.popUp(Constants.Routes.EDIT_LOCATION, loc);
//        String locID = Database.generateUniqueNodeID(loc);
//        loc.setNodeID(locID);
//        loc.addCurrNode();
        UIHelpers.setAdminNodeClickEvent(circ, loc);
        loc.setNodeCircle(circ);
        AnchorPane addToPane = panFloor1;
        switch(loc.getFloor()) {
            case "1":
                addToPane = panFloor1;
                break;
            case "2":
                addToPane = panFloor2;
                break;
            case "3":
                addToPane = panFloor3;
                break;
            case "L1":
                addToPane = panFloorL1;
                break;
            case "L2":
                addToPane = panFloorL2;
                break;
        }
        addToPane.getChildren().add(circ);
    }
//    public static void removeCircle(Circle c) {
//        panMap.getChildren().remove(c);
//
//    }


    @Override
    public final void logOut(MouseEvent event) throws Exception {
        enableAddNode = false;
        enableEditEdge = false;
        event.consume();
        ScreenController.logOut(btnReturn);
        ScreenController.activate(Constants.Routes.WELCOME);
    }
    public void floorL2MapOnMousePressed(MouseEvent event)  {
        this.selectedFloor = "L2";
        VisualRealtimeController.setPanMap(panFloorL2);
        mapOnMousePressed(event);
    }
    public void floorL1MapOnMousePressed(MouseEvent event)  {
        this.selectedFloor = "L1";
        VisualRealtimeController.setPanMap(panFloorL1);
        mapOnMousePressed(event);
    }
    public void floorThreeMapOnMousePressed(MouseEvent event)  {
        VisualRealtimeController.setPanMap(panFloor3);
        this.selectedFloor = "3";
        mapOnMousePressed(event);
    }
    public void floorTwoMapOnMousePressed(MouseEvent event)  {
        VisualRealtimeController.setPanMap(panFloor2);
        this.selectedFloor = "2";
        mapOnMousePressed(event);
    }
    @Override
    public void floorOneMapOnMousePressed(MouseEvent event)  {
        VisualRealtimeController.setPanMap(panFloor1);
        this.selectedFloor = "1";
        mapOnMousePressed(event);
    }
    public void mapOnMousePressed(MouseEvent event)  {
//        this.selectedFloor = "1";
        try {
            if (enableAddNode && !enableEditEdge)
                addNode(event);
        } catch(Exception e) {
            e.printStackTrace();
        }

        // Handle onMousePressed event
        sceneX = event.getSceneX();
        sceneY = event.getSceneY();

        translateX = ((AnchorPane) event.getSource()).getTranslateX();
        translateY = ((AnchorPane) event.getSource()).getTranslateY();
    }
}
