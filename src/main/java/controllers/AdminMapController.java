package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import helpers.Constants;
import helpers.UIHelpers;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import map.MapDisplay;
import models.map.Location;
import models.mapEditor.EdgeAddController;
import models.mapEditor.NodeAddController;
import models.mapEditor.NodeDragController;
import models.search.SearchEngine;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminMapController extends MapController {
    public JFXButton btnDownload;
    public JFXButton btnBooking;
    @FXML
    private JFXToggleButton enableEdge, enableNode;

    static NodeDragController nodeDragController;
    static NodeAddController nodeAddController;
    static EdgeAddController edgeAddController;
    private static boolean lastActionDrag_DontDisplayController = false;

    public AnchorPane outerTabAnchor;
    public JFXTextField searchBox;

    public static String selectedFloor = "1", selectedBuilding = "Shapiro";

    private static Location selectedLocation; // Location that is being modified or created

    /** Called when a circle associated with location is pressed
     * */
    public static void locationSelectEvent(Location loc) throws Exception {
        edgeAddController.nodeSelectEvent(loc);
        if(!edgeAddController.getEnabledFromButton()) {
            if(lastActionDrag_DontDisplayController) {
                lastActionDrag_DontDisplayController = false;
            } else {
                ScreenController.popUp(Constants.Routes.EDIT_LOCATION, loc);
            }
        }
    }
    public static void nodeCreationEvent(Location loc) throws Exception {

    }

    public static void setDraggingNode(boolean draggingNode) {
        if(draggingNode) {
            edgeAddController.setEnabled(false);
            nodeAddController.setEnabled(false);
            nodeDragController.setEnabled(true);
        } else {
            if(nodeDragController.getEnabled()) {
                lastActionDrag_DontDisplayController = true;
                edgeAddController.setEnabledFromButton();
                nodeAddController.setEnabledFromButton();
                nodeDragController.setEnabled(false);
                nodeDragController.reset();
            }
        }
    }



    public static void clearEditorControllers() {
        nodeDragController.reset();
        nodeAddController.reset();
        edgeAddController.reset();
        nodeDragController.setEnabled(false);
        nodeAddController.setEnabled(false);
        edgeAddController.setEnabled(false);
    }
    public void enableEdgeEditorButtonEvent() {

        enableNode.setSelected(false); // turn off other JFX toggle
        clearEditorControllers();
        edgeAddController.setEnabledFromButton();
    }
    public void enableNodeCreationButtonEvent() {
        enableEdge.setSelected(false);
        clearEditorControllers();
        nodeAddController.setEnabled(true);
    }
    public static void nodeDragEvent(Location loc, MouseEvent event) {
        nodeAddController.getEnableButton().setSelected(false);
        edgeAddController.getEnableButton().setSelected(false);
        clearEditorControllers();
        nodeDragController.setEnabled(true);
        nodeDragController.nodeDragEvent(loc, event);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        MapDisplay.displayAdmin(panes);
        configVisualRealtimeController();
        VisualRealtimeController.setSelectedFloorMap(panFloor1);
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

                SearchEngine searchEngine = new SearchEngine(searchBox.getText());

            }
        });
        nodeAddController = new NodeAddController();
        nodeAddController.setEnableButton(enableNode);
        nodeDragController = new NodeDragController();
        edgeAddController = new EdgeAddController();
        edgeAddController.setEnableButton(enableEdge);
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


    @Override
    public void logOut(MouseEvent event) {
        clearEditorControllers();
        event.consume();
        ScreenController.logOut(btnReturn);
        try {
            ScreenController.activate(Constants.Routes.WELCOME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void floorL2MapOnMousePressed(MouseEvent event)  {
        this.selectedFloor = "L2";
        VisualRealtimeController.setSelectedFloorMap(panFloorL2);
        mapOnMousePressed(event);
    }
    public void floorL1MapOnMousePressed(MouseEvent event)  {
        this.selectedFloor = "L1";
        VisualRealtimeController.setSelectedFloorMap(panFloorL1);
        mapOnMousePressed(event);
    }
    public void floorThreeMapOnMousePressed(MouseEvent event)  {
        VisualRealtimeController.setSelectedFloorMap(panFloor3);
        this.selectedFloor = "3";
        mapOnMousePressed(event);
    }
    public void floorTwoMapOnMousePressed(MouseEvent event)  {
        VisualRealtimeController.setSelectedFloorMap(panFloor2);
        this.selectedFloor = "2";
        mapOnMousePressed(event);
    }
    @Override
    public void floorOneMapOnMousePressed(MouseEvent event)  {
        VisualRealtimeController.setSelectedFloorMap(panFloor1);
        this.selectedFloor = "1";
        mapOnMousePressed(event);
    }
//    public void floorGroundOnMousePressed(MouseEvent event)  {
//        VisualRealtimeController.setSelectedFloorMap();
//        this.selectedFloor = "G";
//        mapOnMousePressed(event);
//    }
    public void mapOnMousePressed(MouseEvent event)  {

        try {
            if (nodeAddController.getEnabledFromButton() && !edgeAddController.getEnabledFromButton()) {
//                addNode(event);
                NodeAddController.addNodeEvent(event, selectedFloor, selectedBuilding,
                        determinePanMapFromFloor(selectedFloor));

            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        // Handle onMousePressed event
        sceneX = event.getSceneX();
        sceneY = event.getSceneY();

        translateX = ((AnchorPane) event.getSource()).getTranslateX();
        translateY = ((AnchorPane) event.getSource()).getTranslateY();
    }
    public void configVisualRealtimeController() {
        panFloor1.setId("1");
        panFloor2.setId("2");
        panFloor3.setId("3");
        panFloorL1.setId("L1");
        panFloorL2.setId("L2");
        VisualRealtimeController.addPanMap(panFloor1);
        VisualRealtimeController.addPanMap(panFloor2);
        VisualRealtimeController.addPanMap(panFloor3);
        VisualRealtimeController.addPanMap(panFloorL1);
        VisualRealtimeController.addPanMap(panFloorL2);

    }
}
