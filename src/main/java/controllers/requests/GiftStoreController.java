package controllers.requests;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.sun.javafx.scene.control.TableColumnComparatorBase;
import controllers.ScreenController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import models.map.Location;
import models.requests.GIFT;
import models.sanitation.SanitationRequest;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GiftStoreController {

    public JFXComboBox getCmbItem;
    public JFXComboBox getCmbSize;

    public JFXButton submitBTN;
    public JFXComboBox cmbItem;
    public JFXComboBox cmbSize;
    public JFXTextField txtRecipient;
    public JFXTextField txtMessage;
    public JFXTextField txtSender;
    public String FLOWERS1;
    public String CHOCOLATE1;
    public String STUFFEDBEAR1;
    public String SMALL;
    public String MEDIUM;
    public String LARGE;
    public Text price;

    public TableView<GIFT> tblGifts;
    public TableColumn<GIFT, String> tblRequestID;
    public TableColumn<GIFT, String> tblGift;
    public TableColumn<GIFT, String> tblType;
    public TableColumn<GIFT, String> tblSize;
    public TableColumn<GIFT, String> tblNote;
    public TableColumn<GIFT, String> tblRecipient;
    public TableColumn<GIFT, String> tblSender;
    public int requestID = 1;
    boolean A =false;
    boolean B= false;

    ObservableList<GIFT> giftsREGS = FXCollections.observableArrayList();
    public void initialize() {

        initGift();

        getCmbSize.valueProperty().addListener(((observable, oldValue, newValue) -> {
            submitBTN.setDisable(false);
            A=true;
            calculate();
        }));
        getCmbItem.valueProperty().addListener(((observable, oldValue, newValue) -> {
            submitBTN.setDisable(false);
            B=true;
            calculate();
        }));
    }

    public void calculate(){
        if (A&&B){
            double cost=0.0;

            String gift=(String) cmbItem.getValue();
            String size=(String) cmbSize.getValue();

            if(gift.equals("FLOWERS1")){
                cost=2.3;
            }else if (gift.equals("STUFFEDBEAR1")){
                cost=1.9;
            }else if (gift.equals("CHOCOLATES1")){
                cost=4.3;
            }
            if(size.equals("LARGE")){
                cost=cost*3;
            }else if (size.equals("MEDIUM")){
                cost=cost*2;
            }else if(size.equals("SMALL")){
                cost=cost*1;
            }


            price.setText("$"+cost);

        }else{
            price.setText("$0.00");
        }
    }
    public void sendRequest(MouseEvent event) {

        event.consume();
        String recipient = txtRecipient.getText();
        String note = txtMessage.getText();
        String sender = txtSender.getText();

        String type = (String) cmbItem.getValue();
        String size = (String) cmbSize.getValue();

        GIFT req = new GIFT(requestID++, GIFT.Type.valueOf(type), GIFT.Size.valueOf(size),note,recipient,sender);
        GIFTdetails.add(req);
        giftsREGS.clear();
        giftsREGS.addAll(GIFTdetails);
        tblGifts.refresh();
        event.consume();
    }


    List<GIFT> GIFTdetails = new ArrayList<>();
    private void initGift() {
        tblRequestID.setCellValueFactory(new PropertyValueFactory<>("RequestID"));
        tblType.setCellValueFactory(new PropertyValueFactory<>("Type"));
        tblSize.setCellValueFactory(new PropertyValueFactory<>("Size"));
        tblNote.setCellValueFactory(new PropertyValueFactory<>("Note"));
        tblRecipient.setCellValueFactory(new PropertyValueFactory<>("Recipient"));
        tblSender.setCellValueFactory(new PropertyValueFactory<>("Sender"));
        tblGifts.setItems(giftsREGS);
        if(GIFTdetails != null) {
            giftsREGS.clear();
            giftsREGS.addAll(GIFTdetails);
            tblGifts.refresh();
        }
    }
    public void completeReq(MouseEvent event) {
//        event.consume();
//        GIFT selected = tblGift.getSelectionModel().getSelectedItem();
//        System.out.println(selected.toString());
//        GIFTdetails.remove(selected);
//        giftsREGS.clear();
//        giftsREGS.addAll(GIFTdetails);
//        tblGift.refresh();
    }
    public void cancelScr(MouseEvent event) {
        event.consume();
        ScreenController.deactivate();
    }

}
