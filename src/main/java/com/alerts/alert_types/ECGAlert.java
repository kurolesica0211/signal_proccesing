package com.alerts.alert_types;

public class ECGAlert extends Alert {

    public ECGAlert(String patientId, String condition, long timestamp) {
        super(patientId, "ECG Alert: " + condition, timestamp);
    }
    
}
