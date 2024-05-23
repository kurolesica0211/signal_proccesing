package com.alerts.alert_types;

public class HypoxemiaAlert extends Alert {

    public HypoxemiaAlert(String patientId, String condition, long timestamp) {
        super(patientId, "Hypotensive Hypoxemia Alert: " + condition, timestamp);
    }
    
}
