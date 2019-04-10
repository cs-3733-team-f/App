package helpers;

import controllers.AdminMapController;
import controllers.VisualRealtimeController;
import database.LocationTable;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Screen;
import map.MapDisplay;
import models.map.Edge;
import models.map.Location;

import java.awt.*;

public class UIHelpers {

    public static final double MIN_WIDTH = 400.0;
    public static final double MIN_PIXELS = 600.0;

    public static double getScreenWidth() {
        return Screen.getPrimary().getBounds().getWidth();
    }

    public static double getScreenHeight() {
        return Screen.getPrimary().getBounds().getHeight();
    }

    public static void setAdminNodeClickEvent(Circle c, Location loc) {
        c.setOnMouseReleased(evt -> {
            try {
                evt.consume();
                AdminMapController.setDraggingNode(false);
            } catch (Exception e) {
                throw new UnsupportedOperationException(e);
            }
        });
        c.setOnMouseDragged(evt -> {
            try {
                evt.consume();
                AdminMapController.nodeDragEvent(loc, evt);
            } catch (Exception e) {
                throw new UnsupportedOperationException(e);
            }
        });
        c.setOnMouseClicked(evt -> {
            try {
                evt.consume();
                AdminMapController.locationSelectEvent(loc);
            } catch (Exception e) {
                throw new UnsupportedOperationException(e);
            }
        });
    }

    public static Line generateLineFromEdge(Edge e) {
        Location start = e.getStart();
        Location end = e.getEnd();
        Point startPoint = generateLocationCoordinates(start);
        Point endPoint = generateLocationCoordinates(end);
        Line line = new Line(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
        line.setStroke(Color.BLACK);
        line.setStrokeWidth(MapDisplay.getEdgeWidth());
        line.setId(e.getEdgeID());
//        lstLines.put(edge.getEdgeID(), line);
        return line;
//        pane.getChildren().add(line);
    }

    public static Point generateLocationCoordinates(Location loc) {
        double x = MapDisplay.scaleX(loc.getxCord());
        double y = MapDisplay.scaleY(loc.getyCord());
        return new Point((int) x, (int) y);
    }

    public static Circle updateCircleForNodeType(Location loc) {
        Circle c = loc.getNodeCircle();
        if(loc.getNodeType() == Constants.NodeType.HALL) {
            c.setRadius(MapDisplay.getHallRadius());
            c.setFill(Color.GRAY);
        } else if (loc.getNodeType() == Constants.NodeType.WORK) {
            c.setRadius(MapDisplay.getLocRadius()*1.2);
            c.setFill(Color.GRAY);
        }else {
            c.setRadius(MapDisplay.getLocRadius());
            c.setFill(Color.WHITE);
        }
        return c;
    }

}
