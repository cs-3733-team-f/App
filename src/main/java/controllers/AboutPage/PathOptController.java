package controllers.AboutPage;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import controllers.node.PopUpController;
import controllers.ScreenController;
import helpers.UserHelpers;
import javafx.scene.input.MouseEvent;
import models.map.Location;
import models.sanitation.SanitationRequest;
import java.net.URL;
import java.util.ResourceBundle;


public class PathOptController {

    public JFXComboBox cmbPathFindingMethod;
    public JFXButton selectBTN;
    public JFXButton cancelBTN;
    public String AStar;
    public String BreadthFirst;
    public String DepthFirst;
    public String BestFirst;
    public String DijkstrasAlgorithm;
}
