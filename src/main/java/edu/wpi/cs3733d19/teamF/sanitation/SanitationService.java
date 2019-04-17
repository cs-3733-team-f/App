/**
 * @brief Sanitation service facade interface.
 */

package edu.wpi.cs3733d19.teamF.sanitation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class SanitationService extends Application {

    // Private fields
    private static Stage stage = new Stage();       // JFX stage
    private static IEmployee currentEmployee;       // Current username
    private static String currentLocationID;        // Location ID of request
    private static String thisCssPath;              // Path to CSS Style sheet
    private static boolean servicingEnabled = true; // Servicing requests enabled
    private static boolean deletingEnabled = true;  // Deleting requests enabled

    /**
     * Runs service request demo as a standalone application.
     * @param args Arguments to launch with.
     */
    public static void main(String[] args) {
        currentEmployee = new Employee("DefaultUser");
        currentLocationID = "DefaultLocation";
        thisCssPath = "/css/jfoenix-components.css";
        initDatabases();
        addEmployee(currentEmployee);
        setServicingEnabled(true);
        setDeletingEnabled(true);
        launch(args);
    }

    /**
     * Runs service request from within another application.
     * @param x X-coordinate of screen position
     * @param y Y-coordinate of screen position
     * @param width Width of window
     * @param height Height of window
     * @param cssPath Path to CSS cascading style sheet
     * @param locationID ID of the location of the request
     * @param username Username of logged-in employee
     */
    public static void run(int x, int y, int width, int height, String cssPath, String locationID, String username) {
        currentEmployee = new Employee(username);
        currentLocationID = locationID;
        thisCssPath = cssPath;
        stage.setX(x);
        stage.setY(y);
        stage.setWidth(width);
        stage.setHeight(height);
        initDatabases();
        launch();
    }

    /**
     * Starts the application.
     */
    public void start(Stage primaryStage) {
        try {
            URL url;
            String route = "/SanitationRequestAPI.fxml";
            url = new URL(SanitationService.class.getResource(route).toString().replaceAll("%20", " "));
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(SanitationService.class.getResource(thisCssPath).toExternalForm());
            stage.setTitle("Sanitation Service");
            stage.setScene(scene);
            stage.show();
        } catch (Exception exception) {
            exception.printStackTrace();
            return;
        }
    }

    /**
     * Enables or disables servicing requests through the UI.
     */
    public static void setServicingEnabled(boolean enabled) {
        servicingEnabled = enabled;
    }

    /**
     * Enables or disables deleting requests through the UI.
     */
    public static void setDeletingEnabled(boolean enabled) {
        deletingEnabled = enabled;
    }

    /**
     * Returns boolean indicating if servicing requests is enabled.
     */
    static boolean getServicingEnabled() {
        return servicingEnabled;
    }

    /**
     * Returns boolean indicating if deleting requests is enabled.
     */
    static boolean getDeletingEnabled() {
        return deletingEnabled;
    }

    /**
     * Initializes databases.
     * @return Boolean indicating if init was successful.
     */
    private static boolean initDatabases() {

        // Register database
        try {
            DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
        } catch (SQLException exception) {
            System.out.println("Failed to register database.");
            exception.printStackTrace();
            return false;
        }

        // Create database connection
        Connection connection;
        try {
            connection = DriverManager.getConnection("jdbc:derby:database_sanitation;create=true");
        } catch (SQLException exception) {
            System.out.println("Failed to connect to database.");
            exception.printStackTrace();
            return false;
        }

        // Send database connection to tables
        EmployeeTable.setConnection(connection);
        SanitationTable.setConnection(connection);

        // Create database
        EmployeeTable.create();
        SanitationTable.create();

        // Return true
        return true;
    }

    /**
     * Returns current employee logged in.
     */
    static IEmployee getCurrentEmployee() {
        return currentEmployee;
    }

    /**
     * Adds given sanitation request to database.
     * @param description Description of request
     * @return priority Priority of request (LOW, MEDIUM, HIGH)
     */
    static boolean makeRequest(String description, String priority) {
        SanitationRequest.Priority priorityEnum = SanitationRequest.Priority.valueOf(priority);
        SanitationRequest request = new SanitationRequest(currentLocationID, priorityEnum, description, currentEmployee);
        return SanitationTable.addRequest(request);
    }

    /**
     * Marks sanitation request as claimed by current employee.
     * @param request Sanitation request to be claimed.
     * @return Boolean indicating if the claim was successful.
     */
    static boolean markRequestClaimed(SanitationRequest request) {
        request.markClaimedBy(currentEmployee);
        return SanitationTable.updateRequest(request);
    }

    /**
     * Marks sanitation request as unclaimed.
     * @return Boolean indicating if the unclaim was successful.
     */
    static boolean markRequestUnclaimed(SanitationRequest request) {
        request.markUnclaimed();
        return SanitationTable.updateRequest(request);
    }

    /**
     * Marks sanitation request as completed by a user.
     * @param request Sanitation request to be marked complete.
     * @return Boolean indicating if the update was successful.
     */
    static boolean markRequestComplete(SanitationRequest request) {
        request.markComplete();
        return SanitationTable.updateRequest(request);
    }

    /**
     * Marks sanitation request as incomplete.
     * @param request Sanitation request to be marked incomplete.
     * @return Boolean indicating if update was successful.
     */
    static boolean markRequestIncomplete(SanitationRequest request) {
        request.markIncomplete();
        return SanitationTable.updateRequest(request);
    }

    /**
     * Deletes sanitation request from the database.
     * @param request Sanitation request to be deleted.
     * @return Boolean indicating if request was found and deleted.
     */
    static boolean deleteRequest(SanitationRequest request) {
        return SanitationTable.deleteRequest(request);
    }

    /**
     * Returns full list of sanitation requests from database.
     */
    static ArrayList<SanitationRequest> getRequests() {
        return SanitationTable.getRequests();
    }

    /**
     * Adds employee to the database.
     * @param employee New employee to add
     * @return Boolean indicating if employee was added successfully.
     */
    public static boolean addEmployee(IEmployee employee) {
        return EmployeeTable.addEmployee(employee);
    }

    /**
     * Deletes employee from the database.
     * @param employee edu.wpi.cs3733d19.teamF.sanitation.Employee to delete.
     * @return Boolean indicating if employee was found and deleted.
     */
    public static boolean deleteEmployee(IEmployee employee) {
        return EmployeeTable.deleteEmployee(employee);
    }

    /**
     * @brief Closes application
     */
    static void close() {
        stage.close();
    }
}