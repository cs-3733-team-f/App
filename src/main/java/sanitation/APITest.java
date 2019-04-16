/**
 * @brief Test program for the sanitation API.
 */

package sanitation;

import sanitation.models.Employee;
import sanitation.models.SanitationRequest;
import sanitation.models.SanitationRequest.Priority;

import java.util.ArrayList;

public class APITest {

    /**
     * @brief Main API test
     * @param args Ignored
     */
    public static void main(String[] args) {

        // Initialize databasess
        System.out.println("Initializing Service...");
        SanitationService.initialize();

        // Delete existing employees
        for (String username : SanitationService.getEmployeeUsernames()) {
            SanitationService.deleteEmployee(new Employee(username));
        }

        // Delete existing sanitation requests
        for (SanitationRequest request : SanitationService.getRequests()) {
            SanitationService.deleteRequest(request);
        }

        // Print employees before adds
        System.out.println("Employees before adds:");
        printEmployees();

        // Create and add employees
        Employee emp1 = new Employee("emp1");
        Employee emp2 = new Employee("emp2");
        Employee emp3 = new Employee("emp3");
        SanitationService.addEmployee(emp1);
        SanitationService.addEmployee(emp2);
        SanitationService.addEmployee(emp3);

        // Print employees after adds
        System.out.println("Employees after adds:");
        printEmployees();

        // Print employees after deleting emp2
        SanitationService.deleteEmployee(emp2);
        System.out.println("Employees after deleting emp2:");
        printEmployees();

        // Print sanitation requests before adds
        System.out.println("Requests before adds:");
        printRequests();

        // Create and add sanitation requests
        SanitationRequest req1 = new SanitationRequest(
                "HALL-1", Priority.HIGH, "Spill-1", emp1);
        SanitationRequest req2 = new SanitationRequest(
                "HALL-3", Priority.LOW, "Spill-3", emp3);
        SanitationService.makeRequest(req1);
        SanitationService.makeRequest(req2);

        // Print request handles
        System.out.println("Request handles");
        System.out.println("\treq1: " + req1.toString());
        System.out.println("\treq2: " + req2.toString());

        // Print sanitation after adds
        System.out.println("Requests after adds:");
        printRequests();

        // Mark claimed test
        System.out.println("emp3 marks req1 as claimed:");
        SanitationService.markRequestClaimed(req1, emp3);
        printRequests();

        // Mark unclaimed test
        System.out.println("emp3 marks req1 as unclaimed:");
        SanitationService.markRequestUnclaimed(req1);
        printRequests();

        // Mark complete test
        System.out.println("emp1 claims and marks req2 as complete:");
        SanitationService.markRequestClaimed(req2, emp1);
        SanitationService.markRequestComplete(req2);
        printRequests();

        // Mark incomplete test
        System.out.println("emp1 marks req2 as incomplete");
        SanitationService.markRequestIncomplete(req2);
        printRequests();
    }

    /**
     * @brief Prints list of employees in database
     */
    public static void printEmployees() {
        ArrayList<String> usernames = SanitationService.getEmployeeUsernames();
        if (usernames == null || usernames.size() == 0) {
            System.out.println("\tNone");
        } else {
            for (String username : usernames) {
                Employee employee = new Employee(username);
                System.out.println("\t" + employee.toString());
            }
        }
    }

    /**
     * @brief Prints list of sanitation requests from database.
     */
    public static void printRequests() {
        ArrayList<SanitationRequest> requests = SanitationService.getRequests();
        if (requests == null || requests.size() == 0) {
            System.out.println("\tNone");
        } else {
            for (SanitationRequest request : requests) {
                System.out.println("\t" + request.toString());
            }
        }
    }
}