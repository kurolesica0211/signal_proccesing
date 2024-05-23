package com.alerts.alert_types;

import com.alerts.MonitoringSystem;

// Represents an alert
public class Alert {
    private String patientId;
    private String condition;
    private long timestamp;

    public Alert(String patientId, String condition, long timestamp) {
        this.patientId = patientId;
        this.condition = condition;
        this.timestamp = timestamp;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getCondition() {
        return condition;
    }

    public long getTimestamp() {
        return timestamp;
    }

    protected void triggerAlert(Alert alert) {
        // Basically, this method should call everything that is needed to handle the alert from other system
        MonitoringSystem.notifyStaff(alert);
        MonitoringSystem.logAlert(alert);
        // Additional alert handling logic can be added here
    }
}
