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
        ArrayList<PatientRecord> systolicBloodPressure = new ArrayList<>();
        ArrayList<PatientRecord> diastolicBloodPressure = new ArrayList<>();
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
                    diastolicBloodPressure.add(record);
                    break;
                case "RedBloodCells":
                    redBloodCells.add(record);
                    break;
                case "Saturation":
                    saturation.add(record);
                    break;
                case "SystolicPressure":
                    systolicBloodPressure.add(record);
                    break;
                case "WhiteBloodCells":
                    whiteBloodCells.add(record);
                    break;
                default:
                    break;
            }
        }
        evaluateBloodPressure(systolicBloodPressure, diastolicBloodPressure);
        evaluateECG(ecgRecords);
        evaluateSaturation(saturation);
        evaluateHypoxemia(saturation, systolicBloodPressure);
    }

    private void evaluateHypoxemia(ArrayList<PatientRecord> saturation, ArrayList<PatientRecord> bloodPressure) {
        for (int i = 0; i<saturation.size(); i++) {
            String id = String.valueOf(saturation.get(0).getPatientId());
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
            if (triger1 && triger2)
                triggerAlert(new Alert(id,"Hypotensive Hypoxemia", record.getTimestamp()));
        }
    }

    private void evaluateSaturation(ArrayList<PatientRecord> saturation) {
        for (int i = 0; i<saturation.size(); i++) {
            String id = String.valueOf(saturation.get(0).getPatientId());
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

    private void evaluateBloodPressure(ArrayList<PatientRecord> systolicBloodPressure, ArrayList<PatientRecord> diastolicBloodPressure) {
        for (int i = 0; i<systolicBloodPressure.size(); i++) {
            // Pressure level check
            PatientRecord temp0 = systolicBloodPressure.get(i);
            PatientRecord temp1 = diastolicBloodPressure.get(i);
            String id = String.valueOf(temp0.getPatientId());
            if (temp1.getMeasurementValue() > 120) {
                triggerAlert(new Alert(id, "Diastolic Pressure High", temp1.getTimestamp()));
            } else if (temp1.getMeasurementValue() < 60) {
                triggerAlert(new Alert(id, "Diastolic Pressure Low", temp1.getTimestamp()));
            }
            if (temp0.getMeasurementValue() > 180) {
                triggerAlert(new Alert(id, "Systolic Pressure High", temp0.getTimestamp()));
            } else if (temp0.getMeasurementValue() < 90) {
                triggerAlert(new Alert(id, "Systolic Pressure Low", temp0.getTimestamp()));
            }

            // Blood Pressure trend check
            if (i >= 2) {
                PatientRecord record00 = systolicBloodPressure.get(i);
                PatientRecord record10 = systolicBloodPressure.get(i-1);
                PatientRecord record20 = systolicBloodPressure.get(i-2);
                PatientRecord record01 = diastolicBloodPressure.get(i);
                PatientRecord record11 = diastolicBloodPressure.get(i-1);
                PatientRecord record21 = diastolicBloodPressure.get(i-2);
                if ((record10.getMeasurementValue()-record00.getMeasurementValue())>10
                    && (record20.getMeasurementValue()-record10.getMeasurementValue())>10) {
                    triggerAlert(new Alert(id, "Blood Pressure Trend Alert", record00.getTimestamp()));
                }
                if ((record00.getMeasurementValue()-record10.getMeasurementValue())>10
                    && (record10.getMeasurementValue()-record20.getMeasurementValue())>10) {
                    triggerAlert(new Alert(id, "Blood Pressure Trend Alert", record00.getTimestamp()));
                }
                if ((record11.getMeasurementValue()-record01.getMeasurementValue())>10
                    && (record21.getMeasurementValue()-record11.getMeasurementValue())>10) {
                    triggerAlert(new Alert(id, "Blood Pressure Trend Alert", record00.getTimestamp()));
                }
                if ((record01.getMeasurementValue()-record11.getMeasurementValue())>10
                    && (record11.getMeasurementValue()-record21.getMeasurementValue())>10) {
                    triggerAlert(new Alert(id, "Blood Pressure Trend Alert", record00.getTimestamp()));
                }
            }
        }
    }

    private void evaluateECG(ArrayList<PatientRecord> ecgRecords) {
        double lastDiff = 0;
        for (int i = 0; i<ecgRecords.size(); i++) {
            String id = String.valueOf(ecgRecords.get(0).getPatientId());
            double diff = 0;
            PatientRecord temp1 = ecgRecords.get(i);
            if (i>0) {
                diff = (temp1.getTimestamp()-ecgRecords.get(i-1).getTimestamp())*0.001;
                double heartRate = 60/(diff);
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
                if (Math.abs(lastDiff-diff) > Math.abs(lastDiff)) {
                    triggerAlert(new Alert(id, "Heart Rate Trend Alert", temp1.getTimestamp()));
                }
                lastDiff = diff;
            }
            lastDiff = diff;
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
