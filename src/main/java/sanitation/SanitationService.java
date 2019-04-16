/**
 * @brief Sanitation service facade interface.
 */

package sanitation;

import sanitation.database.EmployeeTable;
import sanitation.database.SanitationTable;
import sanitation.models.Employee;
import sanitation.models.IEmployee;
import sanitation.models.SanitationRequest;
import sanitation.models.SanitationRequest.Priority;

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

    // Recreate database flag
    private static final boolean recreateDatabase = true;

    // Fields
    private static Stage stage;                 // JFX stage
    private static IEmployee currentEmployee;   // Current username
    private static String currentLocationID;    // Location ID of request
    private static String thisCssPath;          // Path to CSS Style sheet

    /**
     * @brief Runs service request as a standalone application.
     * @param args Arguments to launch with.
     */
    public static void main(String[] args) {
        initDatabases();
        currentEmployee = new Employee("DefaultUser");
        currentLocationID = "DefaultLocation";
        thisCssPath = "/css/jfoenix-components.css";
        addEmployee(currentEmployee);
        launch(args);
    }

    /**
     * @brief Runs service request from within another application.
     * @param x X-coordinate of screen position
     * @param y Y-coordinate of screen position
     * @param width Width of window
     * @param height Height of window
     * @param cssPath Path to CSS cascading style sheets
     * @param locationID ID of the location of the request
     * @param username Username of logged-in employee
     */
    public static void run(int x, int y, int width, int height, String cssPath, String locationID, String username) {
        // TODO place application on the screen... but how?
        currentEmployee = new Employee(username);
        currentLocationID = locationID;
        thisCssPath = cssPath;
        initDatabases();
    }

    /**
     * @brief Starts the application.
     * @param primaryStage primary stage.
     */
    public void start(Stage primaryStage) {
        stage = new Stage();
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
     * @brief Initializes databases.
     * @return Boolean indicating if init was successful.
     */
    public static boolean initDatabases() {

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
        if (recreateDatabase) {
            SanitationTable.drop();
            EmployeeTable.drop();
            EmployeeTable.create();
            SanitationTable.create();
        }

        // Return true
        return true;
    }

    /**
     * @brief Returns current employee logged in.
     */
    public static IEmployee getCurrentEmployee() {
        return currentEmployee;
    }

    /**
     * @brief Adds given sanitation request to database.
     * @param description Description of request
     * @return priority Priority of request (LOW, MEDIUM, HIGH)
     */
    public static boolean makeRequest(String description, String priority) {
        Priority priorityEnum = Priority.valueOf(priority);
        SanitationRequest request = new SanitationRequest(currentLocationID, priorityEnum, description, currentEmployee);
        return SanitationTable.addRequest(request);
    }

    /**
     * @brief Marks sanitation request as claimed by current employee.
     * @param request Sanitation request to be claimed.
     * @return Boolean indicating if the claim was successful.
     */
    public static boolean markRequestClaimed(SanitationRequest request) {
        request.markClaimedBy(currentEmployee);
        return SanitationTable.updateRequest(request);
    }

    /**
     * @brief Marks sanitation request as unclaimed.
     * @return Boolean indicating if the unclaim was successful.
     */
    public static boolean markRequestUnclaimed(SanitationRequest request) {
        request.markUnclaimed();
        return SanitationTable.updateRequest(request);
    }

    /**
     * @brief Marks sanitation request as completed by a user.
     * @param request Sanitation request to be marked complete.
     * @return Boolean indicating if the update was successful.
     */
    public static boolean markRequestComplete(SanitationRequest request) {
        request.markComplete();
        return SanitationTable.updateRequest(request);
    }

    /**
     * @brief Marks sanitation request as incomplete.
     * @param request Sanitation request to be marked incomplete.
     * @return Boolean indicating if update was successful.
     */
    public static boolean markRequestIncomplete(SanitationRequest request) {
        request.markIncomplete();
        return SanitationTable.updateRequest(request);
    }

    /**
     * @brief Deletes sanitation request from the database.
     * @param request Sanitation request to be deleted.
     * @return Boolean indicating if request was found and deleted.
     */
    public static boolean deleteRequest(SanitationRequest request) {
        return SanitationTable.deleteRequest(request);
    }

    /**
     * @brief Returns full list of sanitation requests from database.
     */
    public static ArrayList<SanitationRequest> getRequests() {
        return SanitationTable.getRequests();
    }

    /**
     * @brief Adds employee to the database.
     * @param employee New employee to add
     * @return Boolean indicating if employee was added successfully.
     */
    public static boolean addEmployee(IEmployee employee) {
        return EmployeeTable.addEmployee(employee);
    }

    /**
     * @brief Deletes employee from the database.
     * @param employee Employee to delete.
     * @return Boolean indicating if employee was found and deleted.
     */
    public static boolean deleteEmployee(IEmployee employee) {
        return EmployeeTable.deleteEmployee(employee);
    }

    /**
     * @brief Returns list of employee usernames registered in the database.
     */
    public static ArrayList<String> getEmployeeUsernames() {
        return EmployeeTable.getEmployeeUsernames();
    }

    /**
     * @brief Closes application
     */
    public static void close() {
        stage.close();
    }
}
