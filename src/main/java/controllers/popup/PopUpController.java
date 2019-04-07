package controllers.popup;

import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import models.map.Location;
import models.map.Map;
import models.user.User;
import net.kurobako.gesturefx.GesturePane;

import javax.jws.soap.SOAPBinding;

public abstract class PopUpController implements Initializable {

    protected Location loc;
    protected Map map;
    protected GesturePane[] panes;

    public abstract void setLoc(Location loc);

    public final void setMap(Map map) {
        this.map = map;
    };

    public void setPanes(GesturePane[] panes) {
        this.panes = panes;
    }
}
