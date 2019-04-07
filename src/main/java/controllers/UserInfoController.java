package controllers;

import com.jfoenix.controls.JFXButton;
import helpers.Constants;
import helpers.DatabaseHelpers;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.util.Duration;
import map.MapDisplay;
import map.PathFinder;
import models.map.Location;
import models.map.SubPath;

import java.net.URL;
import java.util.*;

public class UserInfoController extends PopUpController implements Initializable {
    public Label lblNodeID;
    public Label lblLocation;
    public Label lblFloor;
    public Label lblBuilding;
    public Label lblNodeType;
    public Label lblLongName;
    public Label lblShortName;
    public JFXButton btnDirections;
    public JFXButton btnCancel;

    private final double lineWidth = 3.5;
    private final double lineLength = 5.0;
    private final double lineGap = 10.0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setLoc(Location loc) {
        this.loc = loc;
        lblNodeID.setText(loc.getNodeID());
        lblLocation.setText("(" + loc.getxCord() + ", " + loc.getyCord() + ")");
        lblFloor.setText(loc.getFloor());
        lblBuilding.setText(loc.getBuilding());
        lblNodeType.setText(DatabaseHelpers.enumToString(loc.getNodeType()));
        lblLongName.setText(loc.getLongName());
        lblShortName.setText(loc.getShortName());
    }

    public void btnDirections_OnClick(MouseEvent event) throws Exception {
        event.consume();

        panDirections.setVisible(true);

        for (AnchorPane pane : panes) {
            List<Node> lstNodes1 = new ArrayList<>();
            for (Node n : pane.getChildren()) {
                if (n instanceof Line) {
                    lstNodes1.add(n);
                }
            }
            for (Node n : lstNodes1) {
                pane.getChildren().remove(n);
            }
        }

        Stack<Location> path = PathFinder.findPath(defLocation, loc);
        String directions = PathFinder.txtDirections((Stack<Location>) path.clone());
        Label lblDirections = (Label) panDirections.lookup("#lblDirections");
        lblDirections.setText(directions);

        HashMap<String, Location> lstLocations = map.getAllLocations();
        Location prev = null;
        while (!path.isEmpty()) {
            Location curr = path.pop();
            if (prev != null) {
                Line line = new Line(MapDisplay.scaleX(prev.getxCord()), MapDisplay.scaleY(prev.getyCord()), MapDisplay.scaleX(curr.getxCord()), MapDisplay.scaleY(curr.getyCord()));
                line.setStroke(Color.BLACK);
                line.getStrokeDashArray().setAll(lineLength, lineGap);
                line.setStrokeWidth(lineWidth);
                line.setStrokeLineCap(StrokeLineCap.ROUND);
                line.setStrokeLineJoin(StrokeLineJoin.ROUND);
                final double maxOffset =
                        line.getStrokeDashArray().stream()
                                .reduce(
                                        0d,
                                        (a, b) -> a - b
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
                                Duration.seconds(3),
                                new KeyValue(
                                        line.strokeDashOffsetProperty(),
                                        maxOffset,
                                        Interpolator.LINEAR
                                )
                        )
                );
                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.play();
                if (curr.getFloor().equals("L2") && prev.getFloor().equals("L2")) {
                    panes[0].getChildren().add(1, line);
                } else if (curr.getFloor().equals("L1") && prev.getFloor().equals("L1")) {
                    panes[1].getChildren().add(1, line);
                } else if (curr.getFloor().equals("1") && prev.getFloor().equals("1")) {
                    panes[2].getChildren().add(1, line);
                } else if (curr.getFloor().equals("2") && prev.getFloor().equals("2")) {
                    panes[3].getChildren().add(1, line);
                } else {
                    panes[4].getChildren().add(1, line);
                }
            }
            prev = curr;
        }
        ScreenController.deactivate();
    }

    public void btnCancel_OnClick(MouseEvent event) {
        event.consume();
        ScreenController.deactivate();
    }
}
