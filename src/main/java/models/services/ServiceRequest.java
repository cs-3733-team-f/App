package models.services;

import models.map.Location;
import models.user.User;
import java.sql.Timestamp;
import java.util.Date;

public abstract class ServiceRequest {

    /**
     * Enumerations
     */
    public enum Status {
        INCOMPLETE, COMPLETE
    }

    /**
     * Class fields.
     */
    protected int requestID;              // Request ID (unique)
    protected Location location;          // Origin location
    protected String description;         // Textual description of request
    protected Status status;              // Status of request
    protected User requester;             // Employee who made request
    protected Timestamp requestTime;      // Request timestamp
    protected User servicer;              // Custodian who serviced request
    protected Timestamp claimedTime;      // Time employee claimed request
    protected Timestamp completedTime;    // Time employee marked request complete

    /**
     * @brief Constructs new service request to add to database.
     * @param location Origin location of the request
     * @param description Textual description of request
     * @param requester Employee who made request
     */
    public ServiceRequest(Location location, String description, User requester)
    {
        this.requestID = 0;
        this.location = location;
        this.status = Status.INCOMPLETE;
        this.description = description;
        this.requester = requester;
        this.requestTime = new Timestamp(new Date().getTime());
        this.servicer = null;
        this.claimedTime = null;
        this.completedTime = null;
    }

    /**
     * @brief Constructs service request with all fields.
     * @param requestID Request ID (generated by database)
     * @param location Origin location of the request
     * @param status Status of request
     * @param description Textual description of request
     * @param requester Employee who made request
     * @param requestTime Time request was made
     * @param servicer Employee who fulfilled the request
     * @param claimedTime Time service was claimed by an employee
     * @param completedTime Time service was marked completed
     */
    public ServiceRequest(
            int requestID,
            Location location,
            Status status,
            String description,
            User requester,
            Timestamp requestTime,
            User servicer,
            Timestamp claimedTime,
            Timestamp completedTime)
    {
        this.requestID = requestID;
        this.location = location;
        this.status = status;
        this.description = description;
        this.requester = requester;
        this.requestTime = requestTime;
        this.servicer = servicer;
        this.claimedTime = claimedTime;
        this.completedTime = completedTime;
    }

    /**
     * Attribute getters
     */
    public int getRequestID() {
        return requestID;
    }
    public Location getLocation() {
        return location;
    }
    public String getLocationShortName() {
        if(location==null) return null;
        return location.getShortName();
    }
    public Status getStatus() {
        return status;
    }
    public String getDescription() {
        return description;
    }
    public User getRequester() {
        return requester;
    }
    public Integer getRequesterID() {
        if (requester == null) return null;
        return requester.getUserID();
    }
    public String getRequesterUserName() {
        if(requester==null) return null;
        return requester.getUsername();
    }
    public String getServicerUserName() {
        if (servicer == null) return null;
        return servicer.getUsername();
    }
    public Timestamp getRequestTime() {
        return requestTime;
    }
    public User getServicer() {
        return servicer;
    }
    public Integer getServicerID() {
        if (servicer == null) return null;
        return servicer.getUserID();
    }
    public Timestamp getClaimedTime() {
        return claimedTime;
    }
    public Timestamp getCompletedTime() {
        return completedTime;
    }

    /**
     * Attribute setters
     */
    public void setStatus(Status status) {
        this.status = status;
    }
    public void setServicer(User servicer) {
        this.servicer = servicer;
    }
    public void setClaimedTime(Timestamp serviceTime) {
        this.claimedTime = serviceTime;
    }
    public void setCompletedTime(Timestamp completedTime) {
        this.completedTime = completedTime;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SanitationRequest) {
            if (((ServiceRequest) obj).getRequestID() == (this.getRequestID())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
