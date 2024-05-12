package com.alerts;

import java.util.ArrayList;
import java.util.List;

import com.data_management.*;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    private DataStorage dataStorage;

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {
        List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);
        ArrayList<PatientRecord> ecgRecords = new ArrayList<>();
        ArrayList<PatientRecord> cholesterol = new ArrayList<>();
        ArrayList<PatientRecord> bloodPressure = new ArrayList<>();
        ArrayList<PatientRecord> redBloodCells = new ArrayList<>();
        ArrayList<PatientRecord> saturation = new ArrayList<>();
        ArrayList<PatientRecord> whiteBloodCells = new ArrayList<>();

        for (PatientRecord record : records) {
            switch (record.getRecordType()) {
                case "ECG":
                    ecgRecords.add(record);
                    break;
                case "Cholesterol":
                    cholesterol.add(record);
                    break;
                case "DiastolicPressure":
                    bloodPressure.add(record);
                    break;
                case "RedBloodCells":
                    redBloodCells.add(record);
                    break;
                case "Saturation":
                    saturation.add(record);
                    break;
                case "SystolicPressure":
                    bloodPressure.add(record);
                    break;
                case "WhiteBloodCells":
                    whiteBloodCells.add(record);
                    break;
                default:
                    break;
            }
        }
        evaluateBloodPressure(bloodPressure);
        evaluateECG(ecgRecords);
        evaluateSaturation(saturation);
        evaluateHypoxemia(saturation, bloodPressure);
    }

    private void evaluateHypoxemia(ArrayList<PatientRecord> saturation, ArrayList<PatientRecord> bloodPressure) {
        String id = String.valueOf(saturation.get(0).getPatientId());
        for (int i = 0; i<saturation.size(); i++) {
            boolean triger1 = false; boolean triger2 = false;
            PatientRecord record = saturation.get(i);
            // Saturation level check
            if (record.getMeasurementValue() < 92) {
                triger1 = true;
            }

            // Blood pressure level check
            if ((bloodPressure.get(i).getRecordType() == "SystolicPressure") && (bloodPressure.get(i).getMeasurementValue() < 90)) {
                triger2 = true;
            }

            // Hypoxemia check
            if (triger1 && triger2) triggerAlert(new Alert(id,"Hypotensive Hypoxemia", record.getTimestamp()));
        }
    }

    private void evaluateSaturation(ArrayList<PatientRecord> saturation) {
        String id = String.valueOf(saturation.get(0).getPatientId());
        for (int i = 0; i<saturation.size(); i++) {
            PatientRecord record = saturation.get(i);
            // Saturation level check
            if (record.getMeasurementValue() < 92) {
                triggerAlert(new Alert(id, "Low Saturation", record.getTimestamp()));
            }

            // Saturation trend check
            int j = 1;
            while ((i-j) >= 0 &&  (600000 > record.getTimestamp()-saturation.get(i-j).getTimestamp())) {
                if (saturation.get(i-j).getMeasurementValue() - record.getMeasurementValue() > 5) {
                    triggerAlert(new Alert(id, "Saturation Trend Alert", record.getTimestamp()));
                }
                j++;
            }
        }
    }

    private void evaluateBloodPressure(ArrayList<PatientRecord> bloodPressure) {
        PatientRecord record0 = bloodPressure.get(0);
        PatientRecord record1 = bloodPressure.get(1);
        PatientRecord record2 = bloodPressure.get(2);
        String id = String.valueOf(record0.getPatientId());
        for (int i = 0; i<bloodPressure.size(); i++) {
            // Pressure level check
            PatientRecord temp = bloodPressure.get(i);
            switch (temp.getRecordType()) {
                case "DiastolicPressure":
                    if (temp.getMeasurementValue() > 120) {
                        triggerAlert(new Alert(id, "Diastolic Pressure High", temp.getTimestamp()));
                    } else if (temp.getMeasurementValue() < 60) {
                        triggerAlert(new Alert(id, "Diastolic Pressure Low", temp.getTimestamp()));
                    }
                    break;
                case "SystolicPressure":
                    if (temp.getMeasurementValue() > 180) {
                        triggerAlert(new Alert(id, "Systolic Pressure High", temp.getTimestamp()));
                    } else if (temp.getMeasurementValue() < 90) {
                        triggerAlert(new Alert(id, "Systolic Pressure Low", temp.getTimestamp()));
                    }
                    break;
                default:
                    break;
            }

            // Blood Pressure trend check
            if (i > 2) {
                record0 = record1;
                record1 = record2;
                record2 = temp;
            }
            if ((record1.getMeasurementValue()-record0.getMeasurementValue())>10
                && (record2.getMeasurementValue()-record1.getMeasurementValue())>10) {
                triggerAlert(new Alert(id, "Blood Pressure Trend Alert", record0.getTimestamp()));
            }
            if ((record0.getMeasurementValue()-record1.getMeasurementValue())>10
                && (record1.getMeasurementValue()-record2.getMeasurementValue())>10) {
                triggerAlert(new Alert(id, "Blood Pressure Trend Alert", record0.getTimestamp()));
            }
        }
    }

    private void evaluateECG(ArrayList<PatientRecord> ecgRecords) {
        String id = String.valueOf(ecgRecords.get(0).getPatientId());
        for (int i = 0; i<ecgRecords.size(); i++) {
            double lastDiff = 0;
            double diff = 0;
            PatientRecord temp1 = ecgRecords.get(i);
            if (i>0) {
                diff = (temp1.getMeasurementValue()-ecgRecords.get(i-1).getMeasurementValue());
                double heartRate = 60/(diff*1000);
                // ECG level check
                if (heartRate < 50) {
                    triggerAlert(new Alert(id, "Heart Rate Low", temp1.getTimestamp()));
                }
                if (heartRate > 100) {
                    triggerAlert(new Alert(id, "Heart Rate High", temp1.getTimestamp()));
                }
            }
            if (i>1) {
                // ECG trend check
                if (Math.abs(lastDiff-diff) > Math.abs(diff)) {
                    triggerAlert(new Alert(id, "Heart Rate Trend Alert", temp1.getTimestamp()));
                }
                lastDiff = diff;
            } else {
                lastDiff = diff;
            }
        }
    }

    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(Alert alert) {
        MonitoringSystem.notifyStaff(alert);
        MonitoringSystem.logAlert(alert);
        // Additional alert handling logic can be added here
    }
}
