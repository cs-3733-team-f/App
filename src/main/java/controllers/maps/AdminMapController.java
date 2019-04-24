package controllers.maps;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXNodesList;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import controllers.ScreenController;
import controllers.search.SearchEngineController;
import google.FirebaseAPI;
import helpers.Constants;
import helpers.UIHelpers;
import helpers.UserHelpers;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Transform;
import map.MapDisplay;
import models.map.Location;
import models.search.SearchAPI;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminMapController extends MapController {
    private final static String MOVER_EDGE = "";

    public VBox vboxDock;
    public ImageView imgLogOut;
    public JFXButton btnLogOut;
    public AnchorPane roomBooking;
    public AnchorPane UserD;
    public AnchorPane Algo;
    public AnchorPane Clean;
    public AnchorPane TabEdit;

    public JFXTextField search;
    public JFXToggleButton tglSpace;
    public JFXToggleButton tglZone;

    private static Location edgLoc = null;

    static class Delta {
        boolean bolDragged;
    }

    static class Mover {
        Line line;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        //todo ADD in MAX's thing
//        gesMap.setOnMouseMoved( (e) -> {
//                    mouseCnt += 1;
//                    secCnt = 0L;
//                }
//        );
        super.initialize(location, resources);
        SearchEngineController.setParentController(this);
        MapDisplay.displayAdmin(this);

//        SearchAPI searchAPI = new SearchAPI(search, true);
//        searchAPI.searchable();

        Delta deltaDragged = new Delta();
        panMap.setOnMousePressed((e) -> {
            deltaDragged.bolDragged = false;
        });
        panMap.setOnMouseDragged((e) -> {
            deltaDragged.bolDragged = true;
        });
        panMap.setOnMouseReleased((e) -> {
            try {
                if (!deltaDragged.bolDragged && edgLoc == null) {
                    ScreenController.addPopUp(this,(int) e.getX(), (int) e.getY(), map);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        imgMap.setOnMousePressed((e) -> {
            deltaDragged.bolDragged = false;
        });
        imgMap.setOnMouseDragged((e) -> {
            deltaDragged.bolDragged = true;
        });
        imgMap.setOnMouseReleased((e) -> {
            try {
                if (!deltaDragged.bolDragged && edgLoc == null) {
                    ScreenController.addPopUp(this,(int) e.getX(), (int) e.getY(), map);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        Mover mover = new Mover();
        panMap.setOnMouseMoved(e -> {
            Location edgLoc = AdminMapController.getEdgLoc();
            if (edgLoc != null) {
                if (mover.line == null) {
                    mover.line = new Line();
                    mover.line.setStrokeWidth(MapDisplay.edgeWidth);
                    panMap.getChildren().add(0, mover.line);
                    mover.line.setStartX(edgLoc.getxCord());
                    mover.line.setStartY(edgLoc.getyCord());
                    mover.line.setId(MOVER_EDGE);
                }
                mover.line.setEndX(e.getX());
                mover.line.setEndY(e.getY());
            } else if (mover.line != null) {
                panMap.getChildren().remove(mover.line);
                mover.line = null;
            }
        });
        imgMap.setOnMouseMoved(e -> {
            Location edgLoc = AdminMapController.getEdgLoc();
            if (edgLoc != null) {
                if (mover.line == null) {
                    mover.line = new Line();
                    mover.line.setStrokeWidth(MapDisplay.edgeWidth);
                    panMap.getChildren().add(0, mover.line);
                    mover.line.setStartX(edgLoc.getxCord());
                    mover.line.setStartY(edgLoc.getyCord());
                    mover.line.setId(MOVER_EDGE);
                }
                mover.line.setEndX(e.getX());
                mover.line.setEndY(e.getY());
            } else if (mover.line != null) {
                panMap.getChildren().remove(mover.line);
                mover.line = null;
            }
        });
    }

    @Override
    public void btnReturn_Click(MouseEvent mouseEvent) throws Exception {
        ScreenController.logOut(btnLogOut);
        ScreenController.activate(Constants.Routes.LOGIN);
    }

    private void clearEdges() {
        List<Node> lstNodes = new ArrayList<>();
        for (Node n : panMap.getChildren()) {
            if (n instanceof Line && !n.getId().equals(MOVER_EDGE)) {
                lstNodes.add(n);
            }
        }
        panMap.getChildren().removeAll(lstNodes);
    }

    @Override
    public void showFloor(String newFloor) {
        super.showFloorHelper(newFloor);
        clearEdges();
        MapDisplay.displayAdmin(this);
    }

    @Override
    public boolean isAdmin() {
        return true;
    }

    public static Location getEdgLoc() {
        return edgLoc;
    }

    public static void setEdgLoc(Location edgLoc) {
        AdminMapController.edgLoc = edgLoc;
    }

    @Override
    protected void addDoc() {
        ImageView imgUser = new ImageView();
        imgUser.setImage(new Image("images/Icons/user.png"));
        imgUser.setFitHeight(30);
        imgUser.setFitWidth(30);
        imgUser.setPreserveRatio(true);
        imgUser.setPickOnBounds(true);

        JFXButton btnUser = new JFXButton("",imgUser);
        btnUser.setAlignment(Pos.CENTER);
        btnUser.setPrefWidth(60);
        btnUser.setPrefHeight(60);
        btnUser.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnUser.setTextOverrun(OverrunStyle.CLIP);

        ImageView imgSearch = new ImageView();
        imgSearch.setImage(new Image("images/Icons/search.png"));
        imgSearch.setFitHeight(30);
        imgSearch.setFitWidth(30);
        imgSearch.setPreserveRatio(true);
        imgSearch.setPickOnBounds(true);

        JFXButton btnSearch = new JFXButton("",imgSearch);
        btnSearch.setAlignment(Pos.CENTER);
        btnSearch.setPrefWidth(60);
        btnSearch.setPrefHeight(60);
        btnSearch.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnSearch.setTextOverrun(OverrunStyle.CLIP);

        ImageView imgArrow = new ImageView();
        imgArrow.setImage(new Image("images/Icons/arrow.png"));
        imgArrow.setFitHeight(30);
        imgArrow.setFitWidth(30);
        imgArrow.setPreserveRatio(true);
        imgArrow.setPickOnBounds(true);

        JFXButton btnArrow = new JFXButton("",imgArrow);
        btnArrow.setAlignment(Pos.CENTER);
        btnArrow.setPrefWidth(60);
        btnArrow.setPrefHeight(60);
        btnArrow.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnArrow.setTextOverrun(OverrunStyle.CLIP);

        ImageView imgRoom = new ImageView();
        imgRoom.setImage(new Image("images/Icons/room.png"));
        imgRoom.setFitHeight(30);
        imgRoom.setFitWidth(30);
        imgRoom.setPreserveRatio(true);
        imgRoom.setPickOnBounds(true);

        JFXButton btnRoom = new JFXButton("",imgRoom);
        btnRoom.setAlignment(Pos.CENTER);
        btnRoom.setPrefWidth(60);
        btnRoom.setPrefHeight(60);
        btnRoom.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnRoom.setTextOverrun(OverrunStyle.CLIP);

        ImageView imgBookG = new ImageView();
        imgBookG.setImage(new Image("images/Icons/bookG.png"));
        imgBookG.setFitHeight(30);
        imgBookG.setFitWidth(30);
        imgBookG.setPreserveRatio(true);
        imgBookG.setPickOnBounds(true);

        JFXButton btnBookG = new JFXButton("", imgBookG);
        btnBookG.setAlignment(Pos.CENTER);
        btnBookG.setPrefWidth(60);
        btnBookG.setPrefHeight(60);
        btnBookG.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnBookG.setTextOverrun(OverrunStyle.CLIP);

        UIHelpers.btnRaise(btnBookG);

        ImageView imgBookT = new ImageView();
        imgBookT.setImage(new Image("images/Icons/bookT.png"));
        imgBookT.setFitHeight(30);
        imgBookT.setFitWidth(30);
        imgBookT.setPreserveRatio(true);
        imgBookT.setPickOnBounds(true);

        JFXButton btnBookT = new JFXButton("", imgBookT);
        btnBookT.setAlignment(Pos.CENTER);
        btnBookT.setPrefWidth(60);
        btnBookT.setPrefHeight(60);
        btnBookT.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnBookT.setTextOverrun(OverrunStyle.CLIP);

        UIHelpers.btnRaise(btnBookT);

        ImageView imgSpace = new ImageView();
        imgSpace.setImage(new Image("images/Icons/space.png"));
        imgSpace.setFitHeight(30);
        imgSpace.setFitWidth(30);
        imgSpace.setPreserveRatio(true);
        imgSpace.setPickOnBounds(true);

        JFXButton btnSpace = new JFXButton("", imgSpace);
        btnSpace.setAlignment(Pos.CENTER);
        btnSpace.setPrefWidth(60);
        btnSpace.setPrefHeight(60);
        btnSpace.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnSpace.setTextOverrun(OverrunStyle.CLIP);

        UIHelpers.btnRaise(btnSpace);

        ImageView imgZone = new ImageView();
        imgZone.setImage(new Image("images/Icons/zone.png"));
        imgZone.setFitHeight(30);
        imgZone.setFitWidth(30);
        imgZone.setPreserveRatio(true);
        imgZone.setPickOnBounds(true);

        JFXButton btnZone = new JFXButton("", imgZone);
        btnZone.setAlignment(Pos.CENTER);
        btnZone.setPrefWidth(60);
        btnZone.setPrefHeight(60);
        btnZone.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnZone.setTextOverrun(OverrunStyle.CLIP);

        UIHelpers.btnRaise(btnZone);

        ImageView imgEdit = new ImageView();
        imgEdit.setImage(new Image("images/Icons/edit.png"));
        imgEdit.setFitHeight(30);
        imgEdit.setFitWidth(30);
        imgEdit.setPreserveRatio(true);
        imgEdit.setPickOnBounds(true);

        JFXButton btnEdit = new JFXButton("",imgEdit);
        btnEdit.setAlignment(Pos.CENTER);
        btnEdit.setPrefWidth(60);
        btnEdit.setPrefHeight(60);
        btnEdit.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnEdit.setTextOverrun(OverrunStyle.CLIP);

        ImageView imgAlgo = new ImageView();
        imgAlgo.setImage(new Image("images/Icons/algo.png"));
        imgAlgo.setFitHeight(30);
        imgAlgo.setFitWidth(30);
        imgAlgo.setPreserveRatio(true);
        imgAlgo.setPickOnBounds(true);

        JFXButton btnAlgo = new JFXButton("",imgAlgo);
        btnAlgo.setAlignment(Pos.CENTER);
        btnAlgo.setPrefWidth(60);
        btnAlgo.setPrefHeight(60);
        btnAlgo.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnAlgo.setTextOverrun(OverrunStyle.CLIP);

        ImageView imgClean = new ImageView();
        imgClean.setImage(new Image("images/Icons/clean.png"));
        imgClean.setFitHeight(30);
        imgClean.setFitWidth(30);
        imgClean.setPreserveRatio(true);
        imgClean.setPickOnBounds(true);

        JFXButton btnClean = new JFXButton("",imgClean);
        btnClean.setAlignment(Pos.CENTER);
        btnClean.setPrefWidth(60);
        btnClean.setPrefHeight(60);
        btnClean.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnClean.setTextOverrun(OverrunStyle.CLIP);

        ImageView imgTab = new ImageView();
        imgTab.setImage(new Image("images/Icons/tabEdit.png"));
        imgTab.setFitHeight(30);
        imgTab.setFitWidth(30);
        imgTab.setPreserveRatio(true);
        imgTab.setPickOnBounds(true);

        JFXButton btnTab = new JFXButton("",imgTab);
        btnTab.setAlignment(Pos.CENTER);
        btnTab.setPrefWidth(60);
        btnTab.setPrefHeight(60);
        btnTab.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnTab.setTextOverrun(OverrunStyle.CLIP);

        ImageView imgCoffee = new ImageView();
        imgCoffee.setImage(new Image("images/SearchIcons/coffee.png"));
        imgCoffee.setFitHeight(30);
        imgCoffee.setFitWidth(30);
        imgCoffee.setPreserveRatio(true);
        imgCoffee.setPickOnBounds(true);

        JFXButton btnCoffee = new JFXButton("",imgCoffee);
        btnCoffee.setAlignment(Pos.CENTER);
        btnCoffee.setPrefWidth(60);
        btnCoffee.setPrefHeight(60);
        btnCoffee.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnCoffee.setTextOverrun(OverrunStyle.CLIP);

        ImageView imgRest = new ImageView();
        imgRest.setImage(new Image("images/SearchIcons/rest.png"));
        imgRest.setFitHeight(30);
        imgRest.setFitWidth(30);
        imgRest.setPreserveRatio(true);
        imgRest.setPickOnBounds(true);

        JFXButton btnRest = new JFXButton("",imgRest);
        btnRest.setAlignment(Pos.CENTER);
        btnRest.setPrefWidth(60);
        btnRest.setPrefHeight(60);
        btnRest.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnRest.setTextOverrun(OverrunStyle.CLIP);

        ImageView imgExit = new ImageView();
        imgExit.setImage(new Image("images/SearchIcons/exit.png"));
        imgExit.setFitHeight(30);
        imgExit.setFitWidth(30);
        imgExit.setPreserveRatio(true);
        imgExit.setPickOnBounds(true);

        JFXButton btnExit = new JFXButton("",imgExit);
        btnExit.setAlignment(Pos.CENTER);
        btnExit.setPrefWidth(60);
        btnExit.setPrefHeight(60);
        btnExit.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnExit.setTextOverrun(OverrunStyle.CLIP);

        ImageView imgElev = new ImageView();
        imgElev.setImage(new Image("images/SearchIcons/elev.png"));
        imgElev.setFitHeight(30);
        imgElev.setFitWidth(30);
        imgElev.setPreserveRatio(true);
        imgElev.setPickOnBounds(true);

        JFXButton btnElev = new JFXButton("",imgElev);
        btnElev.setAlignment(Pos.CENTER);
        btnElev.setPrefWidth(60);
        btnElev.setPrefHeight(60);
        btnElev.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnElev.setTextOverrun(OverrunStyle.CLIP);

        ImageView imgInfo = new ImageView();
        imgInfo.setImage(new Image("images/Icons/info.png"));
        imgInfo.setFitHeight(30);
        imgInfo.setFitWidth(30);
        imgInfo.setPreserveRatio(true);
        imgInfo.setPickOnBounds(true);

        JFXButton btnInfo = new JFXButton("",imgInfo);
        btnInfo.setAlignment(Pos.CENTER);
        btnInfo.setPrefWidth(60);
        btnInfo.setPrefHeight(60);
        btnInfo.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnInfo.setTextOverrun(OverrunStyle.CLIP);

        btnLogOut.setStyle("-fx-background-radius: 30;" );
        btnLogOut.setButtonType(JFXButton.ButtonType.RAISED);
        imgLogOut.setImage(new Image("images/Icons/signout.png"));

        vboxDock.setSpacing(8);

        Label user = new Label("Logout");
        user.setPrefHeight(50);
        user.setPrefWidth(120);
        user.setTextFill(Color.WHITE);
        user.setStyle("-fx-background-color: radial-gradient(radius 120%, #022D5A, derive(#022D5A, -60%), derive(#022D5A, 60%));" + "-fx-background-radius: 10;" + "-fx-font-size: 24;");
        user.setPadding(new Insets(0, 0, 0, 10));

        HBox userBox = new HBox();

        userBox.getChildren().add(user);
        userBox.getChildren().add(btnLogOut);
        userBox.setStyle( "-fx-background-radius: 20;");
        userBox.setPrefHeight(60);
        userBox.setPrefWidth(180);
        userBox.setAlignment(Pos.CENTER);
        userBox.setSpacing(-20);

        HBox searchBox = new HBox();

        searchBox.getChildren().add(search);
        searchBox.getChildren().add(btnArrow);
        searchBox.setPrefHeight(60);
        searchBox.setPrefWidth(370);
        searchBox.setAlignment(Pos.CENTER);
        searchBox.setSpacing(-20);

        HBox searchIcons = new HBox();
        searchIcons.setSpacing(10);
        searchIcons.getChildren().add(btnCoffee);
        searchIcons.getChildren().add(btnRest);
        searchIcons.getChildren().add(btnExit);
        searchIcons.getChildren().add(btnElev);
        searchIcons.getChildren().add(btnInfo);
        searchIcons.setAlignment(Pos.CENTER);

        VBox searchNear = new VBox();
        searchNear.setPrefWidth(370);
        searchNear.setPrefHeight(150);
        searchNear.setSpacing(5);
        searchNear.getChildren().add(searchBox);
        searchNear.getChildren().add(searchIcons);
        searchNear.setAlignment(Pos.CENTER);

        Label lblRoom = new Label("Conference Room and Workspace Booking");
        lblRoom.setPrefHeight(50);
        lblRoom.setPrefWidth(1200);
        lblRoom.setTextFill(Color.WHITE);
        lblRoom.setAlignment(Pos.CENTER);
        lblRoom.setStyle("-fx-background-color: radial-gradient(radius 120%, #022D5A, derive(#022D5A, -60%), derive(#022D5A, 60%));" +
                "-fx-background-radius: 30;" +
                "-fx-font-size: 24;" +
                "-fx-font-weight: BOLD");
        lblRoom.setPadding(new Insets(10, 10, 10, 10));

        VBox boxRoom = new VBox();
        boxRoom.getChildren().add(lblRoom);
        boxRoom.getChildren().add(roomBooking);
        boxRoom.setAlignment(Pos.CENTER_LEFT);
        boxRoom.setPrefSize(1200,760);
        boxRoom.setSpacing(5);

        HBox boxSpace = new HBox();
        boxSpace.getChildren().add(btnSpace);
        boxSpace.getChildren().add(tglSpace);
        boxSpace.setPrefHeight(60);
        boxSpace.setPrefWidth(150);
        boxSpace.setAlignment(Pos.CENTER);
        boxSpace.setSpacing(-5);

        HBox boxZone = new HBox();
        boxZone.getChildren().add(btnZone);
        boxZone.getChildren().add(tglZone);
        boxZone.setPrefHeight(60);
        boxZone.setPrefWidth(150);
        boxZone.setAlignment(Pos.CENTER);
        boxZone.setSpacing(-5);

        VBox boxWork = new VBox();
        boxWork.getChildren().add(boxZone);
        boxWork.getChildren().add(boxSpace);
        boxWork.setAlignment(Pos.CENTER_LEFT);
        boxWork.setPrefSize(150,150);
        boxWork.setSpacing(5);


        Label lblEdit = new Label("User Dashboard");
        lblEdit.setPrefHeight(50);
        lblEdit.setPrefWidth(1200);
        lblEdit.setTextFill(Color.WHITE);
        lblEdit.setAlignment(Pos.CENTER);
        lblEdit.setStyle("-fx-background-color: radial-gradient(radius 120%, #022D5A, derive(#022D5A, -60%), derive(#022D5A, 60%));" +
                "-fx-background-radius: 30;" +
                "-fx-font-size: 24;" +
                "-fx-font-weight: BOLD");
        lblEdit.setPadding(new Insets(10, 10, 10, 10));

        VBox boxEdit = new VBox();
        boxEdit.getChildren().add(lblEdit);
        boxEdit.getChildren().add(UserD);
        boxEdit.setAlignment(Pos.CENTER_LEFT);
        boxEdit.setPrefSize(1200,760);
        boxEdit.setSpacing(5);

        Label lblAlgo = new Label("Algorithm");
        lblAlgo.setPrefHeight(40);
        lblAlgo.setPrefWidth(150);
        lblAlgo.setTextFill(Color.WHITE);
        lblAlgo.setAlignment(Pos.CENTER);
        lblAlgo.setStyle("-fx-background-color: radial-gradient(radius 120%, #022D5A, derive(#022D5A, -60%), derive(#022D5A, 60%));" +
                "-fx-background-radius: 20;" +
                "-fx-font-size: 18;" +
                "-fx-font-weight: BOLD");
        lblAlgo.setPadding(new Insets(5, 10, 5, 10));

        HBox boxAlgo = new HBox();
        boxAlgo.getChildren().add(Algo);
        boxAlgo.getChildren().add(lblAlgo);
        boxAlgo.setAlignment(Pos.CENTER);
        boxAlgo.setPrefSize(550,40);
        boxAlgo.setSpacing(-30);

        Label lblClean = new Label("Sanitation Requests");
        lblClean.setPrefHeight(50);
        lblClean.setPrefWidth(1200);
        lblClean.setTextFill(Color.WHITE);
        lblClean.setAlignment(Pos.CENTER);
        lblClean.setStyle("-fx-background-color: radial-gradient(radius 120%, #022D5A, derive(#022D5A, -60%), derive(#022D5A, 60%));" +
                "-fx-background-radius: 30;" +
                "-fx-font-size: 24;" +
                "-fx-font-weight: BOLD");
        lblClean.setPadding(new Insets(10, 10, 10, 10));

        VBox boxClean = new VBox();
        boxClean.getChildren().add(lblClean);
        boxClean.getChildren().add(Clean);
        boxClean.setAlignment(Pos.CENTER_LEFT);
        boxClean.setPrefSize(1200,760);
        boxClean.setSpacing(5);

        Label lblTab = new Label("Tabular Map Editor");
        lblTab.setPrefHeight(50);
        lblTab.setPrefWidth(1200);
        lblTab.setTextFill(Color.WHITE);
        lblTab.setAlignment(Pos.CENTER);
        lblTab.setStyle("-fx-background-color: radial-gradient(radius 120%, #022D5A, derive(#022D5A, -60%), derive(#022D5A, 60%));" +
                "-fx-background-radius: 30;" +
                "-fx-font-size: 24;" +
                "-fx-font-weight: BOLD");
        lblTab.setPadding(new Insets(10, 10, 10, 10));

        VBox boxTab = new VBox();
        boxTab.getChildren().add(lblTab);
        boxTab.getChildren().add(TabEdit);
        boxTab.setAlignment(Pos.CENTER_LEFT);
        boxTab.setPrefSize(1200,760);
        boxTab.setSpacing(5);

        JFXNodesList nodeListSearch = new JFXNodesList();
        JFXNodesList nodeListUser = new JFXNodesList();
        JFXNodesList nodeListRoom = new JFXNodesList();
        JFXNodesList nodesListEdit = new JFXNodesList();
        JFXNodesList nodesListAlgo = new JFXNodesList();
        JFXNodesList nodesListClean = new JFXNodesList();
        JFXNodesList nodesListTab = new JFXNodesList();
        JFXNodesList nodesListBook = new JFXNodesList();
        JFXNodesList nodesListWork = new JFXNodesList();

        nodeListSearch.addAnimatedNode(btnSearch);
        nodeListSearch.addAnimatedNode(searchNear);
        nodeListSearch.setRotate(90);
        nodeListSearch.setSpacing(105);

        nodeListUser.addAnimatedNode(btnUser);
        nodeListUser.addAnimatedNode(userBox);
        nodeListUser.setRotate(90);
        nodeListUser.setSpacing(60);

        nodesListBook.addAnimatedNode(btnBookT);
        nodesListBook.addAnimatedNode(boxRoom);
        nodesListBook.setRotate(85);
        nodesListBook.setSpacing(240);

        nodesListWork.addAnimatedNode(btnBookG);
        nodesListWork.addAnimatedNode(boxWork);
        nodesListWork.setRotate(90);
        nodesListWork.setSpacing(-10);

        nodesListEdit.addAnimatedNode(btnEdit);
        nodesListEdit.addAnimatedNode(boxEdit);
        nodesListEdit.setRotate(90);
        nodesListEdit.setSpacing(240);

        nodesListAlgo.addAnimatedNode(btnAlgo);
        nodesListAlgo.addAnimatedNode(boxAlgo);
        nodesListAlgo.setRotate(90);
        nodesListAlgo.setSpacing(250);

        nodesListClean.addAnimatedNode(btnClean);
        nodesListClean.addAnimatedNode(boxClean);
        nodesListClean.setRotate(100);
        nodesListClean.setSpacing(250);

        nodesListTab.addAnimatedNode(btnTab);
        nodesListTab.addAnimatedNode(boxTab);
        nodesListTab.setRotate(105);
        nodesListTab.setSpacing(260);

        VBox boxBook = new VBox();
        boxBook.getChildren().add(nodesListWork);
        boxBook.getChildren().add(nodesListBook);
        boxBook.setAlignment(Pos.CENTER_LEFT);
        boxBook.setPrefSize(60,150);
        boxBook.setSpacing(5);

        nodeListRoom.addAnimatedNode(btnRoom);
        nodeListRoom.addAnimatedNode(boxBook);
        nodeListRoom.setRotate(90);
        nodeListRoom.setSpacing(-30);

        vboxDock.getChildren().add(nodeListUser);
        vboxDock.getChildren().add(nodeListSearch);
        vboxDock.getChildren().add(nodeListRoom);
        vboxDock.getChildren().add(nodesListEdit);
        vboxDock.getChildren().add(nodesListAlgo);
        vboxDock.getChildren().add(nodesListClean);
        vboxDock.getChildren().add(nodesListTab);
    }

    @Override
    public void associateUserWithDirections(Location start, Location end) {
        FirebaseAPI.addDirectionsForUser(UserHelpers.getCurrentUser().getUsername(), start, end);
    }
}

