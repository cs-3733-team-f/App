/**
 * @brief edu.wpi.cs3733d19.teamF.sanitation.Employee class for testing.
 */

package edu.wpi.cs3733d19.teamF.sanitation;

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
        return "edu.wpi.cs3733d19.teamF.sanitation.Employee(Username: " + username + ")";
    }
}