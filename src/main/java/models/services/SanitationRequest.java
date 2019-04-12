/**
 * @brief Class for implementing sanitation requests.
 */

package models.services;
import models.map.Location;
import models.user.User;
import java.util.Date;
import java.sql.Timestamp;

public class SanitationRequest extends ServiceRequest {

    /**
     * @brief Constructs new sanitation request to add to database.
     * @param location Location of the spill
     * @param priority Priority of the request
     * @param description Textual description of request
     * @param requester Employee who made request
     */
    public SanitationRequest(Location location, Priority priority, String description, User requester)
    {
        super(
                0, location, priority, Status.INCOMPLETE, description,
                requester, new Timestamp(new Date().getTime()),
                null, null, null
        );
    }

    /**
     * @brief Constructs sanitation request with all fields.
     * @param requestID Request ID (generated by database)
     * @param location Location of the spill
     * @param priority Priority of the request
     * @param status Status of request
     * @param description Textual description of request
     * @param requester Employee who made request
     * @param requestTime Time request was made
     * @param servicer Custodian who fulfilled the request
     * @param claimedTime Time service was claimed by a custodian
     * @param completedTime Time service was marked completed
     */
    public SanitationRequest(
            int requestID,
            Location location,
            Priority priority,
            Status status,
            String description,
            User requester,
            Timestamp requestTime,
            User servicer,
            Timestamp claimedTime,
            Timestamp completedTime) {
        super(
                requestID, location, priority, status, description,
                requester, requestTime,
                servicer, claimedTime, completedTime
        );
    }
}