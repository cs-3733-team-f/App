package edu.wpi.cs3733d19.teamF.sanitation; /**
 * @brief Sanitation request database.
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

public class SanitationTable {

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
     * @brief Creates empty request table.
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

        // Attempt to create table
        String createString = "CREATE TABLE tab_requests(" +
                "requestID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
                "nodeID VARCHAR(100)," +
                "priority VARCHAR(10), " +
                "status VARCHAR(100), " +
                "description VARCHAR(100), " +
                "requesterUsername VARCHAR(100) REFERENCES tab_employees(username), " +
                "requestTime TIMESTAMP, " +
                "servicerUsername VARCHAR(100) REFERENCES tab_employees(username), " +
                "claimedTime TIMESTAMP, " +
                "completedTime TIMESTAMP, " +
                "CONSTRAINT priority_enum CHECK (priority in ('LOW', 'MEDIUM', 'HIGH')), " +
                "CONSTRAINT status_enum CHECK (status in ('INCOMPLETE', 'COMPLETE')))";
        try {
            statement.execute(createString);
        } catch (SQLException | NullPointerException e) {
            return false;
        }

        // Creation was successful
        return true;
    }

    /**
     * @brief Adds new sanitation request to the database.
     * @param request Sanitation request to add.
     * Expects servicer-related fields to be null as the request is brand-new.
     */
    public static boolean addRequest(SanitationRequest request) {

        // Get data from request
        String nodeID = request.getLocationID();
        String priority = request.getPriority().name();
        String status = request.getStatus().name();
        String description = request.getDescription();
        String requesterUsername = request.getRequesterUsername();
        Timestamp requestTime = request.getRequestTime();

        // Attempt to add request to database
        try {
            String statementString = "INSERT INTO tab_requests " +
                    "(nodeID, priority, status, description, requesterUsername, requestTime)" +
                    " VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(statementString);
            statement.setString(1, nodeID);
            statement.setString(2, priority);
            statement.setString(3, status);
            statement.setString(4, description);
            statement.setString(5, requesterUsername);
            statement.setTimestamp(6, requestTime);
            statement.execute();
        } catch (SQLException exception) {
            System.out.println("Failed to add request with description [" + description + "].");
            exception.printStackTrace();
            return false;
        }

        // Get auto-generated request ID and assign to sanitation request.
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT MAX(requestID) FROM tab_requests";
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            int requestID = resultSet.getInt(1);
            request.setRequestID(requestID);
            return true;
        } catch (SQLException exception) {
            System.out.println("Failed to get auto-generated ID for description [" + description + "].");
            exception.printStackTrace();
            return false;
        }
    }

    /**
     * @brief Deletes sanitation request with same ID as given request.
     * @param request Request to be deleted.
     * @return Boolean indicating if request was deleted.
     */
    public static boolean deleteRequest(SanitationRequest request) {
        int requestID = request.getRequestID();
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM tab_requests WHERE requestID=?");
            statement.setInt(1, requestID);
            return statement.execute();
        } catch (SQLException exception) {
            String description = request.getDescription();
            System.out.println("Failed to delete request with description [" + description + "].");
            exception.printStackTrace();
            return false;
        }
    }

    /**
     * @brief Updates sanitation request in database.
     * @param request Request to be updated.
     * @return Boolean indicating if update was successful
     * Only updates sericer-related fields.
     */
    public static boolean updateRequest(SanitationRequest request) {

        // Get updated data from request
        int requestID = request.getRequestID();
        String status = request.getStatus().name();
        String servicerUsername = request.getServicerUsername();
        Timestamp claimedTime = request.getClaimedTime();
        Timestamp completedTime = request.getCompletedTime();

        // Attempt to update request in database
        try {
            String updateString ="UPDATE tab_requests SET " +
                    "status=?, " +
                    "servicerUsername=?, " +
                    "claimedTime=?, " +
                    "completedTime=? " +
                    "WHERE requestID=?";
            PreparedStatement statement = connection.prepareStatement(updateString);
            statement.setString(1, status);
            // Not needed for strings?
            // if (servicerUsername == null) statement.setNull(2, Types.VARCHAR);
            // else statement.setString(2, servicerUsername);
            statement.setString(2, servicerUsername);
            statement.setTimestamp(3, claimedTime);
            statement.setTimestamp(4, completedTime);
            statement.setInt(5, requestID);
            return statement.execute();
        } catch (SQLException exception) {
            String description = request.getDescription();
            System.out.println("Failed to update sanitation request with description [" + description + "].");
            exception.printStackTrace();
            return false;
        }
    }

    /**
     * @brief Returns list of sanitation requests from the database.
     */
    public static ArrayList<SanitationRequest> getRequests() {
        try {
            // Execute query to get all sanitation requests
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM tab_requests";
            ResultSet resultSet = statement.executeQuery(query);

            // Build request list from query
            ArrayList<SanitationRequest> sanitationRequests = new ArrayList<>();
            while (resultSet.next()) {

                // Build sanitation request fields from resultSet
                int requestID = resultSet.getInt("requestID");
                String locationID = resultSet.getString("nodeID");
                SanitationRequest.Priority priority =
                        SanitationRequest.Priority.valueOf(
                                resultSet.getString("priority"));
                SanitationRequest.Status status =
                        SanitationRequest.Status.valueOf(
                                resultSet.getString("status"));
                String description = resultSet.getString("description");
                String requesterUsername = resultSet.getString("requesterUsername");
                Timestamp requestTime = resultSet.getTimestamp("requestTime");
                String servicerUsername = resultSet.getString("servicerUsername");
                Timestamp claimedTime = resultSet.getTimestamp("claimedTime");
                Timestamp completedTime = resultSet.getTimestamp("completedTime");

                // Create and add sanitation request to list
                SanitationRequest sanitationRequest = new SanitationRequest(
                        requestID, locationID, priority, status, description,
                        requesterUsername, requestTime,
                        servicerUsername, claimedTime, completedTime);
                sanitationRequests.add(sanitationRequest);
            }

            // Sort the list by priority and time
            Collections.sort(sanitationRequests);

            // Return the list
            return sanitationRequests;

        } catch (SQLException exception) {
            System.out.println("Failed to get sanitation requests.");
            exception.printStackTrace();
            System.out.println();
            return null;
        }
    }

    /**
     * @brief Drops sanitation table.
     * @return Boolean indicating if drop was successful.
     */
    public static boolean drop() {
        try {
            Statement statement = connection.createStatement();
            return statement.execute("DROP TABLE tab_requests");
        } catch (SQLException exception) {
            System.out.println("Failed to drop requests table.");
            exception.printStackTrace();
            return false;
        }
    }
}