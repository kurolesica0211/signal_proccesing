package com.alerts.alert_checking;

import java.util.ArrayList;

import com.alerts.alert_creating.*;
import com.alerts.alert_types.Alert;
import com.data_management.PatientRecord;

public class OxygenSaturationStrategy implements AlertStrategy{

    @Override
    public Alert checkAlert(ArrayList<PatientRecord> patientRecords) {
        if (patientRecords.size() == 0) {
            return null;
        }
        String id = String.valueOf(patientRecords.get(0).getPatientId());
        AlertFactory alertFactory = new BloodOxygenAlertFactory();
        for (PatientRecord patientRecord : patientRecords) {
            if (patientRecord.getMeasurementValue() < 92) {
                return alertFactory.createAlert(id, "low saturation", patientRecord.getTimestamp());
            }
        }
        return null;
    }
    
}
