package com.alerts.alert_decoration;

import com.alerts.alert_types.*;;

public class AlertDecorator extends Alert {

    public AlertDecorator(String patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
    }
    
}
