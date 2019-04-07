package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import database.Database;
import database.EdgeTable;
import database.SanitationTable;
import helpers.Constants;
import helpers.MapHelpers;
import helpers.UIHelpers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import map.MapDisplay;
import models.map.Edge;
import models.map.Location;
import models.sanitation.SanitationRequest;

import java.awt.*;

public class AdminMapController extends MapController {
    public JFXButton btnDownload;
    public JFXButton btnBooking;

    public TableView<SanitationRequest> tblData;
    public TableColumn<SanitationRequest,String> tblLocation;
    public TableColumn<SanitationRequest,String> tblPriority;
    public TableColumn<SanitationRequest,String> tblStatus;
    public TableColumn<SanitationRequest,String> tblDescription;
    public TableColumn<SanitationRequest,String> tblRequester;
    public TableColumn<SanitationRequest,String> tblClaimTime;
    public TableColumn<SanitationRequest,String> tblServicer;
    public TableColumn<SanitationRequest,String> tblServiceTime;

    ObservableList<SanitationRequest> spills = FXCollections.observableArrayList();


    private static boolean enableAddNode = false;
    private static boolean enableEditEdge = false;

    public AnchorPane outerTabAnchor;
    public JFXTextField searchBox;

    private String selectedFloor = "1", selectedBuilding = "Shapiro";

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
            if(edgeToggle == Constants.SELECTED) {
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
        panFloor1.getChildren().add(circ);
    }
//    public static void removeCircle(Circle c) {
//        panMap.getChildren().remove(c);
//
//    }


    private void initSanitation(){
        tblLocation.setCellValueFactory(new PropertyValueFactory<>("Location"));
        tblPriority.setCellValueFactory(new PropertyValueFactory<>("Priority"));
        tblStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
        tblDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        //tblUser.setCellValueFactory(new PropertyValueFactory<>("User"));
        tblRequester.setCellValueFactory(new PropertyValueFactory<>("Requester"));
        tblClaimTime.setCellValueFactory(new PropertyValueFactory<>("ClaimedTime"));
        tblServiceTime.setCellValueFactory(new PropertyValueFactory<>("CompletedTime"));
        tblServicer.setCellValueFactory(new PropertyValueFactory<>("Servicer"));
        System.out.println(spills.toString());
        tblData.setItems(spills);
    }
    public void deleteSanitationRequest(){
        SanitationRequest selected = tblData.getSelectionModel().getSelectedItem();
        SanitationTable.deleteSanitationRequest(selected);
    }


    @Override
    public final void logOut(MouseEvent event) throws Exception {
        enableAddNode = false;
        enableEditEdge = false;
        event.consume();
        ScreenController.logOut(btnReturn);
        ScreenController.activate(Constants.Routes.WELCOME);
    }
    @Override
    public void floorOneMapOnMousePressed(MouseEvent event)  {
        this.selectedFloor = "1";
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
