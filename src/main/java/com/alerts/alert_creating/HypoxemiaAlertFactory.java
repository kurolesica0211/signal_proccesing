package com.alerts.alert_creating;

import com.alerts.alert_types.*;

public class HypoxemiaAlertFactory extends AlertFactory {
    @Override
    public Alert createAlert(String patientId, String condition, long timestamp) {
        return new HypoxemiaAlert(patientId, condition, timestamp);
    }
}
