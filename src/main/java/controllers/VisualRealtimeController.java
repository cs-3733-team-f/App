package controllers;

import helpers.MapHelpers;
import helpers.UIHelpers;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import models.map.Edge;
import models.map.Location;
import models.map.Map;
import models.map.Workspace;

import java.util.ArrayList;

public class VisualRealtimeController {
    public static ArrayList<AnchorPane> mapPanes = new ArrayList<AnchorPane>();

    public static AnchorPane selectedFloorMap;

    // used to deselect and select circles when adding edges
//    public static ArrayList<Location> selectedLocations = new ArrayList<Location>();

    // for dragging node in real time
    private static Location draggingLocation;
    private static ArrayList<Edge> selectedEdgeList = new ArrayList<Edge>();

    private static final Color PRIMARY_NODE_COLOR = Color.RED;
    private static final Color SECONDARY_NODE_COLOR = Color.ORANGE;
    private static final Color DRAG_NODE_COLOR = Color.CRIMSON;
    private static final Color DRAG_LINE_COLOR = Color.RED;



    private static Map localMap;
    public static void setSelectedFloorMap(AnchorPane selectedFloorMap) {
        VisualRealtimeController.selectedFloorMap = selectedFloorMap;
    }
    public static void removeLinesAssociatedWithLocation(Location c) {
        setSelectedFloorFromLocation(c);
        for (Edge e : localMap.getAllEdges().values()) {
            if (e.getStart().equals(c) || e.getEnd().equals(c)) {
                try {
                    localMap.getAllEdges().remove(e);
                    if(MapHelpers.EdgeBetweenLocationsOnSameFloor(e))
                    selectedFloorMap.getChildren().remove(e.getLine());
                } catch(Exception exc) {

                }
            }
        }
    }
    public static void removeCircle(Location c) {

        selectedFloorMap.getChildren().remove(c.getNodeCircle());

    }
    public static void removeCircle(Workspace c) {

        selectedFloorMap.getChildren().remove(c.getNodeCircle());

    }
    public static void pushCircleToFront(Location c) {
        c.getNodeCircle().toFront();
    }
    public static void setLocalMap(Map m) {
        localMap = m;
    }
    public static Map getLocalMap() {
        return localMap;
    }
    public static void visuallySelectLine(Line l) {
        l.setStroke(DRAG_LINE_COLOR);
    }
    public static void visuallyDeselectNodeDragSprites() {
        for(Edge l : selectedEdgeList) {
            if(l.getLine() != null) visuallyDeselectLine(l.getLine());
        }
        selectedEdgeList.clear();
//        visuallySelectCircle(draggingLocation);
        draggingLocation = null;
    }
    public static void deselectDraggingLocation() {}
    public static void visuallyDeselectLine(Line l) {
        l.setStroke(Color.BLACK);
    }
    public static void setLineConnections(Location c) {
        if(!c.equals(draggingLocation)) {
            visuallyDeselectNodeDragSprites();
            draggingLocation = c;
            visuallySelectDragCircle(c);
            for (Edge e : localMap.getAllEdges().values()) {
                if (e.getStart().equals(c) || e.getEnd().equals(c)) {
                    selectedEdgeList.add(e);
                    if(e.getLine() != null) visuallySelectLine(e.getLine());
                }
            }
        }
    }
    public static void updateLineConnections(Location c) {
        setSelectedFloorFromLocation(c);
        setLineConnections(c);
        for(Edge e : selectedEdgeList) {
            try {
                if (e.getStart().equals(c)) {
                    e.getLine().setStartX(c.getNodeCircle().getCenterX());
                    e.getLine().setStartY(c.getNodeCircle().getCenterY());
                } else if (e.getEnd().equals(c)) {
                    e.getLine().setEndX(c.getNodeCircle().getCenterX());
                    e.getLine().setEndY(c.getNodeCircle().getCenterY());
                }
            } catch(Exception exc) {
                // probably connected through elevator
            }
        }
    }
    public static void addCircle(Circle c) {
        selectedFloorMap.getChildren().add(c);

    }
    public static void addPanMap(AnchorPane panMap) {
        mapPanes.add(panMap);
    }
    public static void visuallyDeselectEdgeAddSprites(ArrayList<Location> selectedLocations) {
        for(Location c : selectedLocations) {
            setSelectedFloorFromLocation(c);
            UIHelpers.updateCircleForNodeType(c);
        }
    }
    public static void visuallyDeselectCircle(Location c) {
        setSelectedFloorFromLocation(c);
        UIHelpers.updateCircleForNodeType(c);
    }
    public static void setSelectedFloorFromLocation(Location c) {
        for(AnchorPane p : mapPanes) {
            selectedFloorMap = p.getId().equals(c.getFloor()) ? p : selectedFloorMap;
        }
    }
    public static void visuallySelectPrimaryLocation(Location loc) {
        setSelectedFloorFromLocation(loc);
        changeCircleColor(loc, PRIMARY_NODE_COLOR);
    }
    public static void visuallySelectCircle(Location c) {
        setSelectedFloorFromLocation(c);
        changeCircleColor(c, SECONDARY_NODE_COLOR);
    }
    public static void visuallySelectDragCircle(Location c) {
        setSelectedFloorFromLocation(c);
        changeCircleColor(c, DRAG_NODE_COLOR);
    }
    public static void changeCircleColor(Location c, Color color) {
        setSelectedFloorFromLocation(c);
        c.getNodeCircle().setFill(color);
    }
    public static void addLine(Line l) {
        selectedFloorMap.getChildren().add(l);

    }
    public static void removeLine(Line l) {
        selectedFloorMap.getChildren().remove(l);
    }

}
