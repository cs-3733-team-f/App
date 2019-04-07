package controllers.popup;

import com.jfoenix.controls.JFXButton;
import controllers.ScreenController;
import controllers.popup.PopUpController;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import map.MapDisplay;
import map.PathFinder;
import models.map.Location;
import models.map.SubPath;
import javafx.animation.*;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;

import java.net.URL;
import java.util.*;

public class DirectionsController extends PopUpController implements Initializable {
    public Label lblStart;
    public Label lblEnd;
    public JFXButton btnCancel;
    public JFXButton btnGo;

    private Location loc2;

    private final double lineWidth = 3.5;
    private final double lineLength = 5.0;
    private final double lineGap = 10.0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setLoc(Location loc) {
        this.loc = loc;
        lblEnd.setText(loc.getLongName());
    }

    public void setLoc2(Location loc2) {
        this.loc2 = loc2;
        lblStart.setText(loc2.getLongName());
    }

    public void btnCancel_OnClick(MouseEvent event) {
        event.consume();
        ScreenController.deactivate();
    }

    public void btnGo_OnClick(MouseEvent event) {
        event.consume();

        for (GesturePane pane : panes) {
//            List<Node> lstNodes1 = new ArrayList<>();
//            for (Node n : pane.getChildren()) {
//                if (n instanceof Line) {
//                    lstNodes1.add(n);
//                }
//            }
//            for (Node n : lstNodes1) {
//                pane.getChildren().remove(n);
//            }
        }

        Stack<SubPath> path = PathFinder.findPath(loc, loc2);
        HashMap<String, Location> lstLocations = map.getAllLocations();
        for (SubPath sub : path) {
            String id = sub.getEdgeID();
            if (!id.equals("")) {
                String locID1 = id.substring(0, id.indexOf("_"));
                String locID2 = id.substring(id.indexOf("_") + 1);
                String endLoc = sub.getLocation().getNodeID();
                String start;
                String end;
                if (locID1.equals(endLoc)) {
                    start = locID2;
                    end = locID1;
                } else {
                    start = locID1;
                    end = locID2;
                }
                Location loc1 = lstLocations.get(start);
                Location loc2 = lstLocations.get(end);
                Line line = new Line(MapDisplay.scaleX(loc1.getxCord()), MapDisplay.scaleY(loc1.getyCord()), MapDisplay.scaleX(loc2.getxCord()), MapDisplay.scaleY(loc2.getyCord()));
                line.setStroke(Color.BLACK);
                line.getStrokeDashArray().setAll(lineLength, lineGap);
                line.setStrokeWidth(lineWidth);
                line.setStrokeLineCap(StrokeLineCap.ROUND);
                line.setStrokeLineJoin(StrokeLineJoin.ROUND);
                final double maxOffset =
                        line.getStrokeDashArray().stream()
                                .reduce(
                                        0d,
                                        (a, b) -> a + b
                                );

                Timeline timeline = new Timeline(
                        new KeyFrame(
                                Duration.ZERO,
                                new KeyValue(
                                        line.strokeDashOffsetProperty(),
                                        0,
                                        Interpolator.LINEAR
                                )
                        ),
                        new KeyFrame(
                                Duration.seconds(2),
                                new KeyValue(
                                        line.strokeDashOffsetProperty(),
                                        maxOffset,
                                        Interpolator.LINEAR
                                )
                        )
                );
                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.play();
//                if (loc1.getFloor().equals("L2") && loc2.getFloor().equals("L2")) {
//                    panes[0].getChildren().add(1, line);
//                } else if (loc1.getFloor().equals("L1") && loc2.getFloor().equals("L1")) {
//                    panes[1].getChildren().add(1, line);
//                } else if (loc1.getFloor().equals("1") && loc2.getFloor().equals("1")) {
//                    panes[2].getChildren().add(1, line);
//                } else if (loc1.getFloor().equals("2") && loc2.getFloor().equals("2")) {
//                    panes[3].getChildren().add(1, line);
//                } else {
//                    panes[4].getChildren().add(1, line);
//                }
            }
        }
        ScreenController.deactivate();
    }
}