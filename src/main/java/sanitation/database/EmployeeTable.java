/**
 * @brief Minimalist employee database.
 */

package sanitation.database;

import sanitation.models.IEmployee;
import java.sql.*;
import java.util.ArrayList;

public class EmployeeTable {

    // Database connection
    private static Connection connection = null;

    /**
     * @brief Sets database connection.
     * @param databaseConnection Database connection.
     */
    public static void setConnection(Connection databaseConnection) {
        connection = databaseConnection;
    }

    /**
     * @brief Creates employee table.
     * @return Boolean indicating if creation was successful.
     */
    public static boolean create() {

        // Make create statement
        Statement statement;
        try {
            statement = connection.createStatement();
        } catch (SQLException exception) {
            System.out.println("Failed to make create statement.");
            exception.printStackTrace();
            return false;
        }

        // Execute create statement
        String createString = "CREATE TABLE tab_employees(username VARCHAR(100) PRIMARY KEY)";
        try {
            statement.execute(createString);
        } catch (SQLException exception) {
            System.out.println("Failed to execute create statement.");
            exception.printStackTrace();
            return false;
        }

        // Creation was successful
        return true;
    }

    /**
     * @brief Adds employee to database.
     * @return Boolean indicating if add was successful.
     */
    public static boolean addEmployee(IEmployee employee) {

        // Check if employee already exists
        if (containsEmployee(employee)) {
            return false;
        }

        // Add employee
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO tab_employees (username) VALUES(?)");
            statement.setString(1, employee.getUsername());
            return statement.execute();
        } catch (SQLException exception) {
            System.out.println("Failed to add employee.");
            exception.printStackTrace();
            return false;
        }
    }

    /**
     * @brief Deletes employee from database.
     * @return Boolean indicating if the delete was successful.
     */
    public static boolean deleteEmployee(IEmployee employee) {

        // Check if employee actually exists
        if (!containsEmployee(employee)) {
            return false;
        }

        // Delete employee
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM tab_employees WHERE username=?");
            statement.setString(1, employee.getUsername());
            statement.execute();
            return true;
        } catch (SQLException exception) {
            System.out.println("Failed to delete employee");
            exception.printStackTrace();
            return false;
        }
    }

    /**
     * @brief Returns list of employee usernames registered in the database.
     */
    public static ArrayList<String> getEmployeeUsernames() {
        try {
            // Execute query to get all usernames
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM tab_employees";
            ResultSet resultSet = statement.executeQuery(query);

            // Build username list from query results
            ArrayList<String> usernames = new ArrayList<>();
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                usernames.add(username);
            }

            // Return list
            return usernames;

        } catch (SQLException exception) {
            System.out.println("Failed to get employee usernames.");
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * @brief Returns boolean indicating if given employee exists in the database.
     */
    public static boolean containsEmployee(IEmployee employee) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM tab_employees WHERE username=?");
            statement.setString(1, employee.getUsername());
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException exception) {
            System.out.println("Failed to check employee existence.");
            exception.printStackTrace();
            return false;
        }
    }

    /**
     * @brief Drops employee table.
     * @return Boolean indicating if drop was successful.
     */
    public static boolean drop() {
        try {
            Statement statement = connection.createStatement();
            return statement.execute("DROP TABLE tab_employees");
        } catch (SQLException exception) {
            System.out.println("Failed to drop employee table.");
            exception.printStackTrace();
            return false;
        }
    }
}
