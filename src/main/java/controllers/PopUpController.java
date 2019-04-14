package controllers;

import controllers.maps.MapController;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import models.map.Location;
import models.map.Map;
import models.map.Workspace;
import java.time.LocalDate;
import java.time.LocalTime;

public abstract class PopUpController implements Initializable {

    protected Location loc;
    protected Map map;
    protected Pane pane;
    public ScrollPane txtPane;
    protected Circle circle;
    protected Location kiosk;
    protected Workspace ws;
    protected LocalDate StartDate;
    protected LocalTime StartTime;
    protected LocalDate EndDate;
    protected LocalTime EndTime;

    public abstract void setLoc(Location loc);

    public final void setMap(Map map) {
        this.map = map;
        kiosk = map.getLocation(MapController.getTempStart());
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public void setPane(Pane pane) {
        this.pane = pane;
    }

    public void setTxtPane(ScrollPane txtPane) {
        this.txtPane = txtPane;
    }

    public void setWorkspace(Workspace ws) {
        this.ws = ws;
    }

    public void setStartDate(LocalDate StartDate) { this.StartDate = StartDate; }

    public void setStartTime(LocalTime StartTime) { this.StartTime = StartTime; }

    public void setEndDate(LocalDate EndDate) { this.EndDate = EndDate; }

    public void setEndTime(LocalTime EndTime) { this.EndTime = EndTime; }
}
