package controllers.requests;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import models.requests.Security;
import controllers.ScreenController;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class SecurityController implements Initializable {
    public JFXComboBox cmdThreatLevel;
    public JFXTextField txtDescription;
    public JFXButton btnSubmit;
    public JFXButton btnCancel;
    public JFXTextField txtTime;
    public TableView<Security> tblSecurity;
    public TableColumn<Security, String> colThreatLevel;
    public TableColumn<Security, String> colDescription;

    private ObservableList<Security> listRequests;


    public void btnSubmit_Clicked(MouseEvent mouseEvent) {
        mouseEvent.consume();
        if (cmdThreatLevel.getValue()!= null) {
            Security.ThreatLevel threatLv = Security.ThreatLevel.valueOf((String) cmdThreatLevel.getValue());
            String desc = txtDescription.getText();
            String time = "";
            Security secOb = new Security(threatLv, desc, time);
            listRequests.add(secOb);
            tblSecurity.refresh();
            cmdThreatLevel.setValue(null);
            txtDescription.setText("");
        }
    }

    public void btnRefresh_Clicked(MouseEvent mouseEvent) {
        mouseEvent.consume();
        Security.ThreatLevel threatLv = Security.ThreatLevel.valueOf((String) cmdThreatLevel.getValue());
        String desc = txtDescription.getText();
        String time = "";
        Security secOb = new Security(threatLv, desc, time);
        String theTime = secOb.getTime();
        txtTime.setText(theTime);
    }

    public void btnCancel_Clicked(MouseEvent mouseEvent) {
        mouseEvent.consume();
        ScreenController.deactivate();
    }

    public SecurityController() {
        listRequests = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colThreatLevel.setCellValueFactory(new PropertyValueFactory<>("ThreatLevel"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        tblSecurity.setItems(listRequests);
    }
}
