package com.alerts.alert_checking;

import java.util.ArrayList;

import com.alerts.alert_types.Alert;
import com.data_management.PatientRecord;

public interface AlertStrategy {
    /**
     * Checks specific set of patient records for dangerous conditions;
     * implementations of this method vary in the specific conditions they check for
     * @param patientRecords set of patient records to check
     * @return Alert object if dangerous condition is found, null otherwise
     */
    public Alert checkAlert(ArrayList<PatientRecord> patientRecords);
}
