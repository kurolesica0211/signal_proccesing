package com.alerts.alert_checking;

import java.util.ArrayList;

import com.alerts.alert_creating.*;
import com.alerts.alert_types.Alert;
import com.data_management.PatientRecord;

public class BloodPressureStrategy implements AlertStrategy {

    @Override
    public Alert checkAlert(ArrayList<PatientRecord> patientRecords) {
        if (patientRecords.size() == 0) {
            return null;
        }
        AlertFactory alertFactory = new BloodPressureAlertFactory();
        String id = String.valueOf(patientRecords.get(0).getPatientId());
        for (int i = 0; i < patientRecords.size(); i++) {
            PatientRecord record = patientRecords.get(i);
            if (record.getRecordType().equals("DiastolicPressure")) {
                // Pressure level check
                if (record.getMeasurementValue() > 120) {
                    return alertFactory.createAlert(id, "diastolic pressure high", record.getTimestamp());
                } else if (record.getMeasurementValue() < 60) {
                    return alertFactory.createAlert(id, "diastolic pressure low", record.getTimestamp());
                }
                

                // Blood Pressure trend check
                if (i >= 2) {
                    PatientRecord record01 = record;
                    PatientRecord record11 = patientRecords.get(i - 1);
                    PatientRecord record21 = patientRecords.get(i - 2);
                    if ((record11.getMeasurementValue() - record01.getMeasurementValue()) > 10
                            && (record21.getMeasurementValue() - record11.getMeasurementValue()) > 10) {
                        return alertFactory.createAlert(id, "diastolic trend alert", record.getTimestamp());
                    }
                    if ((record01.getMeasurementValue() - record11.getMeasurementValue()) > 10
                            && (record11.getMeasurementValue() - record21.getMeasurementValue()) > 10) {
                        return alertFactory.createAlert(id, "diastolic trend alert", record.getTimestamp());
                    }
                }
            } else if (record.getRecordType().equals("SystolicPressure")) {
                if (record.getMeasurementValue() > 180) {
                    return alertFactory.createAlert(id, "systolic pressure high", record.getTimestamp());
                } else if (record.getMeasurementValue() < 90) {
                    return alertFactory.createAlert(id, "systolic pressure low", record.getTimestamp());
                }
                if (i < 2) {
                    continue;
                }
                PatientRecord record00 = record;
                PatientRecord record10 = patientRecords.get(i - 1);
                PatientRecord record20 = patientRecords.get(i - 2);
                if ((record10.getMeasurementValue() - record00.getMeasurementValue()) > 10
                        && (record20.getMeasurementValue() - record10.getMeasurementValue()) > 10) {
                    return alertFactory.createAlert(id, "systolic trend alert", record00.getTimestamp());
                }
                if ((record00.getMeasurementValue() - record10.getMeasurementValue()) > 10
                        && (record10.getMeasurementValue() - record20.getMeasurementValue()) > 10) {
                    return alertFactory.createAlert(id, "systolic trend alert", record00.getTimestamp());
                }
            }
        }
        return null;
    }

}
