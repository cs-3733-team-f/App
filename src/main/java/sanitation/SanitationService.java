/**
 * @brief Sanitation service facade interface.
 */

package sanitation;

public class SanitationService {

    /**
     * @brief Initializes sanitation service and databases.
     * @return Boolean indicating if init was successful.
     */
    public static boolean initialize() {
        return false;
    }

    /**
     * @brief Adds given sanitation request to database.
     * @param request Sanitation request to add.
     * @return Boolean indicating if add was successful.
     */
    public static boolean makeRequest(SanitationRequest request) {
        return false;   // TODO
    }

    /**
     * @brief Marks sanitation request as claimed by a user.
     * @param requestID ID of the request to be claimed.
     * @param username Username of the claiming employee.
     * @return Boolean indicating if the claim was successful.
     */
    public static boolean claimRequest(int requestID, String username) {
        return false;   // TODO
    }

    /**
     * @brief Marks sanitation request as completed by a user.
     * @param requestID ID of the request to be marked complete.
     * @param username Username of the employee who completed it.
     * @return Boolean indicating if the update was successful.
     */
    public static boolean completeRequest(int requestID, String username) {
        return false;   // TODO
    }

    /**
     * @brief Deletes sanitation request from the database.
     * @param requestID ID of the request to be deleted.
     * @return Boolean indicating if request was found and deleted.
     */
    public static boolean deleteRequest(int requestID)
    {
        return false;   // TODO
    }

    /**
     * @brief Adds employee to the database.
     * @param username Username of new employee
     * @return Boolean indicating if employee was added successfully.
     */
    public static boolean addEmployee(String username) {
        return false;   // TODO
    }

    /**
     * @brief Deletes employee from the database.
     * @param username Username of employee to delete.
     * @return Boolean indicating if employee was found and deleted.
     */
    public static boolean deleteEmployee(String username) {
        return false;   // TODO
    }
}
