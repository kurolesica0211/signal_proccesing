package com.alerts.alert_creating;

import com.alerts.alert_types.*;

public class ECGAlertFactory extends AlertFactory {
    /**
     * Create a new ECGAlert object
     * @param patientID the ID of the patient
     * @param condition the condition of the patient, input only the most specific information
     * @param timestamp the time the alert was created
     * @return a new ECGAlert object
     */
    @Override
    public Alert createAlert(String patientID, String condition, long timestamp) {
        return new ECGAlert(patientID, condition, timestamp);
    }
}
