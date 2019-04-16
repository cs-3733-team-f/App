/**
 * @brief Sanitation service facade interface.
 */

package sanitation;

import sanitation.database.EmployeeTable;
import sanitation.database.SanitationTable;
import sanitation.models.IEmployee;
import sanitation.models.SanitationRequest;

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
    private Stage stage;

    /**
     * @brief Main function which starts the application.
     * @param args Arguments to launch with.
     */
    public static void main(String[] args) {
        launch(args);
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
            // TODO addStyles(scene)
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
     * @brief Adds given sanitation request to database.
     * @param request Sanitation request to add.
     * @return Boolean indicating if add was successful.
     */
    public static boolean makeRequest(SanitationRequest request) {
        return SanitationTable.addRequest(request);
    }

    /**
     * @brief Marks sanitation request as claimed by an employee.
     * @param request Sanitation request to be claimed.
     * @param employee Employee claiming the request.
     * @return Boolean indicating if the claim was successful.
     */
    public static boolean markRequestClaimed(SanitationRequest request, IEmployee employee) {
        request.markClaimedBy(employee);
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
     * @brief Marks sanitationrequest as incomplete.
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

}
