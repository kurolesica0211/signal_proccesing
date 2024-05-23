package com.alerts.alert_types;

public class BloodOxygenAlert extends Alert {

    public BloodOxygenAlert(String patientId, String condition, long timestamp) {
        super(patientId, "Blood Oxygen Alert: " + condition, timestamp);
    }
    
}
