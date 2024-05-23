package com.alerts.alert_creating;

import com.alerts.alert_types.Alert;

public class AlertFactory {
    // Alert constructor requires not only condition, but also patientID and timestamp
    public Alert createAlert(String patientID, String condition, long timestamp) {
        return new Alert(patientID, condition, timestamp);
    }
}
