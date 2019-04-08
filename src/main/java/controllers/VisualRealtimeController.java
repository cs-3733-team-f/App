package controllers;

import database.EdgeTable;
import helpers.UIHelpers;
import javafx.scene.Node;
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

    public static AnchorPane panMap;

    // used to deselect and select circles when adding edges
    public static ArrayList<Location> selectedLocations = new ArrayList<Location>();

    // for dragging node in real time
    private static Location draggingLocation;
    private static ArrayList<Edge> selectedEdgeList = new ArrayList<Edge>();

    private static Map localMap;
    public static void setPanMap(AnchorPane panMap) {
        VisualRealtimeController.panMap = panMap;
    }
    public static void removeCircle(Location c) {

        panMap.getChildren().remove(c.getNodeCircle());

    }
    public static void removeCircle(Workspace c) {

        panMap.getChildren().remove(c.getNodeCircle());

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
        l.setStroke(Color.ORANGE);
    }
    public static void deselectAllLines() {
        for(Edge l : selectedEdgeList) {
            if(l.getLine() != null) visuallyDeselectLine(l.getLine());
        }
        selectedEdgeList.clear();
//        visuallySelectCircle(draggingLocation);
        draggingLocation = null;
    }
    public static void visuallyDeselectLine(Line l) {
        l.setStroke(Color.BLACK);
    }
    public static void setLineConnections(Location c) {
        if(!c.equals(draggingLocation)) {
            deselectAllLines();
            draggingLocation = c;
            visuallySelectCircle(c);
            for (Edge e : localMap.getAllEdges().values()) {
                if (e.getStart().equals(c) || e.getEnd().equals(c)) {
                    selectedEdgeList.add(e);
                    if(e.getLine() != null) visuallySelectLine(e.getLine());
                }
            }
        }
    }
    public static void updateLineConnections(Location c) {
        setSelectedPane(c);
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
        panMap.getChildren().add(c);

    }
    public static void addPanMap(AnchorPane panMap) {
        mapPanes.add(panMap);
    }
    public static void visuallyDeselectAll() {
        for(Location c : selectedLocations) {
            UIHelpers.updateCircleForNodeType(c);
        }
        selectedLocations.clear();
    }
    public static void visuallyDeselectCircle(Location c) {
        setSelectedPane(c);
        UIHelpers.updateCircleForNodeType(c);
    }
    public static void setSelectedPane(Location c) {
        for(AnchorPane p : mapPanes) {
            panMap = p.getId().equals(c.getFloor()) ? p : panMap;
        }
    }

    public static void visuallySelectCircle(Location c) {
        setSelectedPane(c);
        selectedLocations.add(c);
        if(selectedLocations.size() == 1) {
            changeCircleColor(c, Color.RED);
        } else {
            changeCircleColor(c, Color.ORANGE);
        }

    }
    public static void changeCircleColor(Location c, Color color) {
        setSelectedPane(c);
        c.getNodeCircle().setFill(color);
    }
    public static void addLine(Line l) {
        panMap.getChildren().add(l);

    }
    public static void removeLine(Line l) {
        panMap.getChildren().remove(l);
    }

}
