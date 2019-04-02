/**
 * @brief Class for implementing sanitation requests.
 */

package models.sanitation;
import models.map.Location;

public class SanitationRequest implements Comparable<SanitationRequest> {

    /**
     * Enumeration for request priority.
     */
    public enum Priority {
        LOW, MEDIUM, HIGH
    }

    public enum Status {
        INCOMPLETE, COMPLETE
    }

    /**
     * Class fields.
     */
    private Location location;  // Location of the spill
    private Priority priority;  // Priority of the request
    private String description; // Textual description of request

    /**
     * @brief Constructs new sanitation request.


    /**
     * Class fields.
     */
    private int requestID;      // Request ID (unique)
    private Location location;  // Location of the spill
    private Priority priority;  // Priority of the request
    private String description; // Textual description of request
    private Status status;
    private String user;

    /**
     * @brief Constructs new sanitation request without ID
     * @param location Location of the spill
     * @param priority Priority of the request
     * @param description Textual description of request
     */
    public SanitationRequest(Location location, Priority priority, String description) {
        this.requestID = 0;
        this.location = location;
        this.priority = priority;
        this.description = description;
        this.status = Status.INCOMPLETE;
        this.user = "";
    }

    /**
     * @brief Constructs new sanitation request with ID
     * @param requestID Request ID (generated by database)
     * @param location Location of the spill
     * @param priority Priority of the request
     * @param description Textual description of request
     */
    public SanitationRequest(int requestID, Location location, Priority priority, String description, Status status, String user) {
        this.requestID = requestID;
        this.location = location;
        this.priority = priority;
        this.description = description;
        this.status = status;
        this.user = user;
    }

    /**
     * Converts priority to integer
     */
    private Integer priorityToInt() {
        switch(priority) {
            case HIGH: return 1;
            case MEDIUM: return 2;
            case LOW: return 3;
            default: return 0;
        }
    }

    /**
     * @brief Compares sanitation requests based on priority.
     */
    public int compareTo(SanitationRequest request) {
        return priorityToInt().compareTo(request.priorityToInt());
    }

    /**
     * @brief Converts sanitation request to string.
     */
    public String toString() {
        String string = "Request: " +
                "Location: " + location.getNodeID() + ", " +
                "Priority: " + priority.name() + ", " +
                "Description: " + description;
        return string;
    }

    /**
     * Attribute getters
     */
    public int getRequestID()
    {
        return requestID;
    }

    public String getLocation() {
        return location.getLongName();
    }

    public Location getLocationObj() {
        return location;
    }

    public String getPriority() { return priority.name(); }

    public Priority getPriorityObj() { return priority; }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status.name();
    }

    public Status getStatusObj() {
        return status;
    }

    public String getUser() {
        return user;
    }
}