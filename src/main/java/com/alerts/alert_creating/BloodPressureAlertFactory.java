package com.alerts.alert_creating;

import com.alerts.alert_types.*;

public class BloodPressureAlertFactory extends AlertFactory {
    /**
     * Create a new BloodPressureAlert object
     * @param patientID the ID of the patient
     * @param condition the condition of the patient, input only the most specific information
     * @param timestamp the time the alert was created
     * @return a new BloodPressureAlert object
     */
    @Override
    public Alert createAlert(String patientID, String condition, long timestamp) {
        return new BloodPressureAlert(patientID, condition, timestamp);
    }
}
