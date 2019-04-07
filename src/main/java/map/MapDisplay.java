package map;

import controllers.ScreenController;
import helpers.Constants;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.javafx.IconFontFX;
import jiconfont.javafx.IconNode;
import models.map.Edge;
import models.map.Location;
import models.map.Map;
import net.kurobako.gesturefx.GesturePane;

import java.util.HashMap;

public class MapDisplay {
    private final static double CIRCLE_SIZE = 20;
    private final static double hallRadius = 2.5;
    private final static double locWidth = 2.0;
    private final static double edgeWidth = 1.5;
    private final static double xShift = -2110.0;
    private final static double yShift = 730.0;
    private final static double scale = 0.475;
    private final static Color nodeFill = Color.WHITE;
    private final static Color hallFill = Color.GRAY;
    private final static Color nodeOutline = Color.BLACK;
    private final static Color edgeFill = Color.BLACK;

    private final static int ICON_SIZE = 15;

    /**
     * Display the graph on a map for the default user (no halls, info boxes)
     */
    public static void displayUser(GesturePane[] panes) {
        Map map = MapParser.parse();
        displayNodesUser(map, panes);
    }

    /**
     * Display the graph of a map for employees (halls, info boxes with spill reporting)
     * @param panes
     */
    public static void displayEmployee(GesturePane[] panes) {
        Map map = MapParser.parse();
        displayNodesEmployee(map, panes);
    }

    /**
     * Display the graph on a map for the admin (halls, edit boxes)
     * @param panes
     */
    public static void displayAdmin(GesturePane[] panes) {
        Map map = MapParser.parse();
        displayEdges(map, panes);
        displayNodesAdmin(map, panes);
    }

    /**
     * Display the graph on a map for the custodian (no halls)
     * @param panes
     */
    public static void displayCust(GesturePane[] panes) {
        Map map = MapParser.parse();
        displayNodesCust(map, panes);
    }

    private static void displayNodesUser(Map map, GesturePane[] panes) {
        IconFontFX.register(FontAwesome.getIconFont());

        HashMap<String, Location> lstLocations = map.getAllLocations();
        for (Location loc : lstLocations.values()) {
            if (loc.getNodeType() != Constants.NodeType.HALL) {
                double xLoc = scaleX(loc.getxCord());
                double yLoc = scaleY(loc.getyCord());
                Button circle = new Button();
                circle.setMinWidth(CIRCLE_SIZE);
                circle.setMinHeight(CIRCLE_SIZE);
                circle.setMaxWidth(CIRCLE_SIZE);
                circle.setMaxHeight(CIRCLE_SIZE);
                circle.setTranslateX(xLoc);
                circle.setTranslateY(yLoc);
                circle.setGraphic(getIcon(loc.getNodeType()));
                circle.setStyle("-fx-focus-color: black;");
                circle.setOnMouseClicked(event -> {
                    try {
                        event.consume();
                        panes[0].requestFocus();
                        ScreenController.popUp(Constants.Routes.USER_INFO, loc, map, panes);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                System.out.println(findPane(panes, loc.getFloor()).getContent());
            }
        }
    }

    private static void displayNodesCust(Map map, GesturePane[] panes) {
        HashMap<String, Location> lstLocations = map.getAllLocations();
        for (Location loc : lstLocations.values()) {
            if (loc.getNodeType() != Constants.NodeType.HALL) {
                double xLoc = scaleX(loc.getxCord());
                double yLoc = scaleY(loc.getyCord());
                Circle circle = new Circle(xLoc, yLoc, CIRCLE_SIZE, nodeFill);
                circle.setStroke(nodeOutline);
                circle.setStrokeWidth(locWidth);
                circle.setOnMouseClicked(event -> {
                    try {
                        event.consume();
                        ScreenController.popUp(Constants.Routes.CUSTODIAN_INFO, loc, map, panes);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                //findPane(panes, loc.getFloor()).getChildren().add(circle);
            }
        }
    }

    private static void displayNodesEmployee(Map map, GesturePane[] panes) {
        HashMap<String, Location> lstLocations = map.getAllLocations();
        for (Location loc : lstLocations.values()) {
            if (loc.getNodeType() != Constants.NodeType.HALL) {
                double xLoc = scaleX(loc.getxCord());
                double yLoc = scaleY(loc.getyCord());
                Circle circle = new Circle(xLoc, yLoc, CIRCLE_SIZE, nodeFill);
                circle.setStroke(nodeOutline);
                circle.setStrokeWidth(locWidth);
                circle.setOnMouseClicked(event -> {
                    try {
                        event.consume();
                        ScreenController.popUp(Constants.Routes.EMPLOYEE_INFO, loc, map, panes);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                //findPane(panes, loc.getFloor()).getChildren().add(circle);
            }
        }
    }

    private static void displayNodesAdmin(Map map, GesturePane[] panes) {
        HashMap<String, Location> lstLocations = map.getAllLocations();
        for (Location loc : lstLocations.values()) {
            double xLoc = scaleX(loc.getxCord());
            double yLoc = scaleY(loc.getyCord());
            Circle circle;
            if (loc.getNodeType() != Constants.NodeType.HALL) {
                circle = new Circle(xLoc, yLoc, CIRCLE_SIZE, nodeFill);
            } else {
                circle = new Circle(xLoc, yLoc, hallRadius, hallFill);
            }
            circle.setStroke(nodeOutline);
            circle.setStrokeWidth(locWidth);
            //findPane(panes, loc.getFloor()).getChildren().add(circle);
        }
    }

    private static void displayEdges(Map map, GesturePane[] panes) {
        HashMap<String, Edge> lstEdges = map.getAllEdges();
        for (Edge edge : lstEdges.values()) {
            Location start = edge.getStart();
            Location end = edge.getEnd();
            if (start.getFloor().equals(end.getFloor())) {
                double x1 = scaleX(start.getxCord());
                double x2 = scaleX(end.getxCord());
                double y1 = scaleY(start.getyCord());
                double y2 = scaleY(end.getyCord());
                Line line = new Line(x1, y1, x2, y2);
                line.setStroke(edgeFill);
                line.setStrokeWidth(edgeWidth);
                //findPane(panes, start.getFloor()).getChildren().add(line);
            }
        }
    }

    private static GesturePane findPane(GesturePane[] panes, String floor) {
        switch (floor) {
            case "L2":
                return panes[0];
            case "L1":
                return panes[1];
            case "1":
                return panes[2];
            case "2":
                return panes[3];
            default:
                return panes[4];
        }
    }

    private static IconNode getIcon(Constants.NodeType nodeType) {
        IconNode icon;
        switch (nodeType) {
            case BATH:
                icon = new IconNode(FontAwesome.BATH);
                break;
            case CONF:
                icon = new IconNode(FontAwesome.BATH);
                break;
            case DEPT:
                icon = new IconNode(FontAwesome.BATH);
                break;
            case ELEV:
                icon = new IconNode(FontAwesome.BATH);
                break;
            case EXIT:
                icon = new IconNode(FontAwesome.BATH);
                break;
            case HALL:
                icon = new IconNode(FontAwesome.BATH);
                break;
            case INFO:
                icon = new IconNode(FontAwesome.BATH);
                break;
            case LABS:
                icon = new IconNode(FontAwesome.BATH);
                break;
            case REST:
                icon = new IconNode(FontAwesome.BATH);
                break;
            case RETL:
                icon = new IconNode(FontAwesome.BATH);
                break;
            case SERV:
                icon = new IconNode(FontAwesome.BATH);
                break;
            default:
                icon = new IconNode(FontAwesome.BATH);
                break;
        }
        icon.setFill(Color.GRAY);
        icon.setIconSize(ICON_SIZE);
        return icon;
    }

    public static double scaleX(double x) {
        return (x - xShift) * scale;
    }

    public static double scaleY(double y) {
        return (y - yShift) * scale;
    }

    public static double revScaleX(double x) {
        return x / scale + xShift;
    }

    public static double revScaleY(double y) {
        return y / scale + yShift;
    }

    public static double getLocRadius() {
        return CIRCLE_SIZE;
    }

    public static double getHallRadius() {
        return hallRadius;
    }

    public static double getLocWidth() {
        return locWidth;
    }

    public static double getEdgeWidth() {
        return edgeWidth;
    }
}
