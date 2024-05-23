package com.alerts.alert_checking;

import java.util.ArrayList;

import com.alerts.alert_creating.*;
import com.alerts.alert_types.Alert;
import com.data_management.PatientRecord;

public class HeartRateStrategy implements AlertStrategy{

    @Override
    public Alert checkAlert(ArrayList<PatientRecord> patientRecords) {
        if (patientRecords.size() == 0) {
            return null;
        }
        AlertFactory alertFactory = new ECGAlertFactory();
        String id = String.valueOf(patientRecords.get(0).getPatientId());
        double lastDiff = 0;
        for (int i = 0; i < patientRecords.size(); i++) {
            PatientRecord record = patientRecords.get(i);
            double diff = 0;
            if (i>0) {
                diff = (record.getTimestamp()-patientRecords.get(i-1).getTimestamp())*0.001;
                double heartRate = 60/(diff);
                // ECG level check
                if (heartRate < 50) {
                    return alertFactory.createAlert(id, "high heart rate", record.getTimestamp());
                }
                if (heartRate > 100) {
                    return alertFactory.createAlert(id, "low heart rate", record.getTimestamp());
                }
            }
            if (i>1) {
                // ECG trend check
                /*
                 * The requirements are ambiguous, so I assumed that the alert should be triggered
                 * when the difference between the current heart rate and the previous heart rate is greater than the previous difference.
                 * (odd assumption, but just for the sake of the example)
                 */
                if (Math.abs(lastDiff-diff) > Math.abs(lastDiff)) {
                    return alertFactory.createAlert(id, "dangerous trend", record.getTimestamp());
                }
                lastDiff = diff;
            }
            lastDiff = diff;
        }
        return null;
    }
    
}
