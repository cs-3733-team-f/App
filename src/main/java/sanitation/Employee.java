/**
 * @brief Employee class for testing.
 */

package sanitation;

public class Employee implements IEmployee {

    // Employees only have a username
    private String username;

    // Constructor
    public Employee(String username) {
        this.username = username;
    }

    // Username getter
    public String getUsername() {
        return username;
    }

    // Converts employee to string
    public String toString() {
        return "Employee(Username: " + username + ")";
    }
}
