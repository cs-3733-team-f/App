package models.requests;


import java.text.SimpleDateFormat;

import java.util.Date;

public class Security {

    public enum ThreatLevel {
        LOW, MEDIUM, HIGH, MIDNIGHT
    }
    private ThreatLevel threatLevel;
    private String description;
    private String time;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");

    public Security(ThreatLevel threatLevel, String description, String time) {
        this.threatLevel = threatLevel;
        this.description = description;
        this.time = getTime();
    }

    public String getThreatLevel() {
        return threatLevel.name();
    }

    public java.util.Date getDate() {
        java.util.Date date = new java.util.Date();
        return date;
    }

    public void setTime(String time) {
        this.time = getTime();

    }

    public String getTime() {
        Date date = getDate();
        String dateString = simpleDateFormat.format(date);
        return dateString;
    }

    public String getDescription() {
        return description;
    }
}
