package controllers;

import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import models.map.Location;
import models.map.Map;

public abstract class PopUpController implements Initializable {

    protected Location loc;
    protected Map map;
    protected AnchorPane[] panes;
    protected AnchorPane pane;

    public abstract void setLoc(Location loc);

    public final void setMap(Map map) {
        this.map = map;
    };

    public final void setPane(AnchorPane pane) {
        this.pane = pane;
    }

    public void setPanes(AnchorPane[] panes) {
        this.panes = panes;
    }
}
