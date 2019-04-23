package controllers;

import com.querydsl.sql.types.Null;
import controllers.requests.LogoutController;
import helpers.Caretaker;
import helpers.Constants;
import helpers.Memento;
import helpers.Originator;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.map.Location;
import models.map.Map;
import models.map.Workspace;
import models.user.User;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class ScreenController {
    private static HashMap<String, String> screenMap = new HashMap<>();
    public volatile static Stage stage;
    public static long mouseCnt = 0;
    private static long theCnt = 0;
    private static long secCnt = 0;
    static Originator org = new Originator();
    static Caretaker car = new Caretaker();

    public ScreenController(Stage stage) {
        this.stage = stage;

        try {
            // Initialize screens
            this.initializeScreens(this.stage);

            // Activate beginning screen
            this.activate(Constants.Routes.WELCOME);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Thread t = new Thread(() -> {
            System.out.println("here");
            while(true) {
                LogoutController lc = new LogoutController() {
                    @Override
                    public void initialize(URL location, ResourceBundle resources) {
                    }
                };
                int timeCnt = lc.getTime();
                car.add(org.saveStateToMemento());
                theCnt = mouseCnt;
                if (secCnt == timeCnt*60) {
                        Platform.runLater(() -> {
                            ScreenController.deactivate();
                            try {
                                ScreenController.activate(Constants.Routes.WELCOME);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    mouseCnt = 0L;
                    secCnt = 0;
                }
                try {
                    Thread.sleep(1000L);
                    System.out.println("SavedCount");
                    System.out.println(theCnt);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (car.get(car.mementoList.size() - 1) == theCnt) {
                    secCnt += 1;
                    System.out.println("secCnt");
                    System.out.println(secCnt);
                }
                else {
                    secCnt = 0;
                }
            }
        });
        t.setDaemon(true);
        t.start();

    }

    public void initializeScreens(Stage stage) throws Exception {
        // Initialize screen controller to switch between different scenes
        this.addScreen(Constants.Routes.LOGO,"/Logo.fxml");
        this.addScreen(Constants.Routes.WELCOME,"/Welcome.fxml");
        this.addScreen(Constants.Routes.USER_MAP, "/UserMap.fxml");
        this.addScreen(Constants.Routes.LOGIN,"/Login.fxml");
        this.addScreen(Constants.Routes.ADMIN_MAP, "/AdminMap.fxml");
        this.addScreen(Constants.Routes.EDIT_LOCATION, "/EditLocation.fxml");
        this.addScreen(Constants.Routes.DOWNLOAD, "/Download.fxml");
        this.addScreen(Constants.Routes.DOWNLOADED, "/Downloaded.fxml");
        this.addScreen(Constants.Routes.USER_INFO, "/UserInfo.fxml");
        this.addScreen(Constants.Routes.EMPLOYEE_INFO, "/EmployeeInfo.fxml");
        this.addScreen(Constants.Routes.EMPLOYEE_MAP, "/EmployeeMap.fxml");
        this.addScreen(Constants.Routes.SANITATION_REQUEST, "/SanitationRequest.fxml");
        this.addScreen(Constants.Routes.DIRECTIONS, "/Directions.fxml");
        this.addScreen(Constants.Routes.CUSTODIAN_MAP, "/CustodianMap.fxml");
        this.addScreen(Constants.Routes.CUSTODIAN_INFO, "/CustodianInfo.fxml");
        this.addScreen(Constants.Routes.BOOKING_WINDOW, "/RoomBookingWindow.fxml");
        this.addScreen(Constants.Routes.CREATE_USER, "/CreateUser.fxml");
        this.addScreen(Constants.Routes.USER_POPUP, "/CreateUserPopUp.fxml");
        this.addScreen(Constants.Routes.EDIT_POPUP, "/EditUserPopUp.fxml");
        this.addScreen(Constants.Routes.WORKSPACE, "/Workspace.fxml");
        this.addScreen(Constants.Routes.WORKSPACE_POPUP, "/WorkspacePopUp.fxml");
        this.addScreen(Constants.Routes.REQUESTS, "/requests/Requests.fxml");
        this.addScreen(Constants.Routes.IT, "/requests/ITServiceRequest.fxml");
        this.addScreen(Constants.Routes.PERSCRIPTION, "/requests/Prescription.fxml");
        this.addScreen(Constants.Routes.INTERPRETER, "/requests/Interpreter.fxml");
        this.addScreen(Constants.Routes.INTERNAL_TRANS, "/requests/InternalTrans.fxml");
        this.addScreen(Constants.Routes.GIFT_STORE, "/requests/GiftStore.fxml");
        this.addScreen(Constants.Routes.FLOURIST, "/requests/Flourist.fxml");
        this.addScreen(Constants.Routes.SECURITY, "/requests/Security.fxml");
        this.addScreen(Constants.Routes.VISUAL_AUDIO, "/requests/VisualAudio.fxml");
        this.addScreen(Constants.Routes.EXTERNAL_TRANS, "/requests/ExternalTrans.fxml");
        this.addScreen(Constants.Routes.PATIENT_INFO, "/requests/PatientInfo.fxml");
    }

    public void addScreen(Constants.Routes route, String layout) {
        screenMap.put(route.name(), layout);
    }

    private static void addStyles(Scene scene) {
        scene.getStylesheets().add(ScreenController.class.getResource("/css/jfoenix-components.css").toExternalForm());
        scene.getStylesheets().add(ScreenController.class.getResource("/css/custom.css").toExternalForm());
        scene.getStylesheets().add(ScreenController.class.getResource("/css/colorScheme.css").toExternalForm());
    }

    public static void deactivate() {
        stage.close();
    }

    public static EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            mouseCnt += 1;
            secCnt = 0;
            System.out.println(mouseCnt);
        }
    };


    public static void activate(Constants.Routes route) throws Exception {
        stage = new Stage();
        URL url = routeToURL(route);

        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();

        Scene s = new Scene(root);
        addStyles(s);
        stage.setTitle("Brigham and Women's Pathfinder Application");
        autoLog(s);
        stage.setScene(s);
        stage.setResizable(true);
        // stage.setMaximized(true);
        stage.show();
    }


    public static void autoLog(Scene scene) throws Exception {
        scene.setOnMouseClicked(mouseHandler);
        scene.setOnMouseMoved(mouseHandler);
        scene.setOnMousePressed(mouseHandler);
        scene.setOnMouseReleased(mouseHandler);
    }




    public static void popUp(Constants.Routes route) throws Exception {
        stage = new Stage();
        URL url = routeToURL(route);

        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        displayPopUp(root);
    }

    public static void popUpUser(Constants.Routes route, User user) throws Exception {
        stage = new Stage();
        URL url = routeToURL(route);
        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        PopUpControllerUser pcu = loader.getController();
        pcu.setUser(user);
        displayPopUp(root);
    }

    public static void popUp(Constants.Routes route, Location loc) throws Exception {
        stage = new Stage();
        URL url = routeToURL(route);

        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        PopUpController pc = loader.getController();

        pc.setLoc(loc);

        displayPopUp(root);
    }

    public static void popUp(Constants.Routes route, Workspace ws, Circle circle, LocalTime StartTime, LocalDate StartDate, LocalTime EndTime, LocalDate EndDate) throws Exception {
        stage = new Stage();
        URL url = routeToURL(route);

        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        PopUpController pc = loader.getController();

        pc.setCircle(circle);
        pc.setWorkspace(ws);
        pc.setStartDate(StartDate);
        pc.setStartTime(StartTime);
        pc.setEndDate(EndDate);
        pc.setEndTime(EndTime);
        displayPopUp(root);
    }

    public static void popUp(Constants.Routes route, Location loc, Map map, AnchorPane[] panes) throws Exception {
        stage = new Stage();
        URL url = routeToURL(route);

        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        PopUpController pc = loader.getController();

        pc.setLoc(loc);
        pc.setMap(map);
        pc.setPanes(panes);

        displayPopUp(root);
    }

    public static void popUp(Constants.Routes route, Location loc, Map map, AnchorPane[] panes, Circle circle) throws Exception {
        stage = new Stage();
        URL url = routeToURL(route);

        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        PopUpController pc = loader.getController();

        pc.setLoc(loc);
        pc.setMap(map);
        pc.setPanes(panes);
        pc.setCircle(circle);

        displayPopUp(root);
    }

    public static void popUp(Constants.Routes route, Location loc, Map map, AnchorPane[] panes, Circle circle, ScrollPane TextPane) throws Exception {
        stage = new Stage();
        URL url = routeToURL(route);

        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        PopUpController pc = loader.getController();

        pc.setLoc(loc);
        pc.setMap(map);
        pc.setPanes(panes);
        pc.setCircle(circle);
        pc.setTextPane(TextPane);

        displayPopUp(root);
    }

    public static void popUp(Constants.Routes route, Location loc1, Location loc2, Map map, AnchorPane[] panes) throws Exception {
        stage = new Stage();
        URL url = routeToURL(route);

        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        DirectionsController pc = loader.getController();

        pc.setLoc(loc1);
        pc.setLoc2(loc2);
        pc.setMap(map);
        pc.setPanes(panes);

        displayPopUp(root);
    }

    private static void displayPopUp(Parent root) {
        Scene s = new Scene(root);
        addStyles(s);
        stage.setScene(s);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    /**
     * Closes out of a current window with selected
     * @param n Node from the window the logout is from
     */
    public static void logOut(Node n) {
        ((Stage) n.getScene().getWindow()).close();
    }

    private static URL routeToURL(Constants.Routes route) throws MalformedURLException {
        return new URL(ScreenController.class.getResource(screenMap.get(route.name())).toString().replaceAll("%20", " "));
    }

}
