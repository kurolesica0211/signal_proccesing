package data_management;

import org.junit.Test;
import static org.junit.Assert.*;

import com.data_management.DataStorage;
import com.data_management.FileReader;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import com.alerts.AlertGenerator;
import com.alerts.MonitoringSystem;
import com.cardio_generator.HealthDataSimulator;

import java.util.List;

import javax.xml.crypto.Data;

public class tests {

    public tests() {
    }

    @Test
    public void testFileReaderAndGetRecords() {
        DataStorage dataStorage = new DataStorage();
        FileReader fileReader = new FileReader();
        fileReader.readData(dataStorage);
        List<Patient> patients = dataStorage.getAllPatients();
        List<PatientRecord> patient1 = patients.get(0).getRecords(0, Long.MAX_VALUE);
        List<PatientRecord> patient2 = patients.get(1).getRecords(0, Long.parseLong("1715545367974"));
        assertEquals(patient1.get(0).getMeasurementValue(), 0.5272823916128154, 0);
        assertEquals(patient1.get(0).getRecordType(), "ECG");
        assertEquals(patient1.get(0).getTimestamp(), Long.parseLong("1715545367974"));
        assertTrue(patient2.isEmpty());
    }

    @Test
    public void testSystolicBloodPressureLevelAlert() {
        DataStorage dataStorage = new DataStorage();
        dataStorage.addPatientData(0, 190.0, "SystolicPressure", Long.parseLong("1"));
        dataStorage.addPatientData(0, 80.0, "DiastolicPressure", Long.parseLong("2"));
        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);
        alertGenerator.evaluateData(dataStorage.getAllPatients().get(0));
        assertTrue(MonitoringSystem.alertTriggered);
    }

    @Test
    public void testDistolicBloodPressureLevelAlert() {
        DataStorage dataStorage = new DataStorage();
        dataStorage.addPatientData(0, 100.0, "SystolicPressure", Long.parseLong("1"));
        dataStorage.addPatientData(0, 140.0, "DiastolicPressure", Long.parseLong("2"));
        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);
        alertGenerator.evaluateData(dataStorage.getAllPatients().get(0));
        System.out.println(dataStorage.getAllPatients().get(0));
        assertTrue(MonitoringSystem.alertTriggered);
    }

    @Test
    public void testSaturationLevelAlert() {
        DataStorage dataStorage = new DataStorage();
        dataStorage.addPatientData(0, 90, "Saturation", Long.parseLong("1"));
        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);
        alertGenerator.evaluateData(dataStorage.getAllPatients().get(0));
        System.out.println(dataStorage.getAllPatients().get(0));
        assertTrue(MonitoringSystem.alertTriggered);
    }

    @Test
    public void testSaturaitonTrendAlert() {
        DataStorage dataStorage = new DataStorage();
        dataStorage.addPatientData(0, 93, "Saturation", Long.parseLong("1"));
        dataStorage.addPatientData(0, 99, "Saturation", Long.parseLong("1000"));
        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);
        alertGenerator.evaluateData(dataStorage.getAllPatients().get(0));
        System.out.println(dataStorage.getAllPatients().get(0));
        assertTrue(MonitoringSystem.alertTriggered);
    }

    @Test
    public void testSaturationTrendAlert1() {
        DataStorage dataStorage = new DataStorage();
        dataStorage.addPatientData(0, 93, "Saturation", Long.parseLong("1"));
        dataStorage.addPatientData(0, 99, "Saturation", Long.parseLong("6000000"));
        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);
        alertGenerator.evaluateData(dataStorage.getAllPatients().get(0));
        System.out.println(dataStorage.getAllPatients().get(0));
        assertTrue(MonitoringSystem.alertTriggered == false);
    }

    @Test
    public void testECGLevelAlert() {
        DataStorage dataStorage = new DataStorage();
        dataStorage.addPatientData(0, 100.0, "SystolicPressure", Long.parseLong("1"));
        dataStorage.addPatientData(0, 100.0, "DiastolicPressure", Long.parseLong("2"));
        dataStorage.addPatientData(0, 94, "Saturation", Long.parseLong("3"));
        dataStorage.addPatientData(0, 0.1, "ECG", Long.parseLong("3"));
        dataStorage.addPatientData(0, 0.1, "ECG", Long.parseLong("1002"));
        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);
        alertGenerator.evaluateData(dataStorage.getAllPatients().get(0));
        System.out.println(dataStorage.getAllPatients().get(0));
        assertTrue(MonitoringSystem.alertTriggered);
    }

    @Test
    public void testBloodPressureTrendAlert() {
        DataStorage dataStorage = new DataStorage();
        dataStorage.addPatientData(0, 90.0, "SystolicPressure", Long.parseLong("1"));
        dataStorage.addPatientData(0, 80.0, "DiastolicPressure", Long.parseLong("2"));
        dataStorage.addPatientData(0, 101.0, "SystolicPressure", Long.parseLong("1"));
        dataStorage.addPatientData(0, 92.0, "DiastolicPressure", Long.parseLong("2"));
        dataStorage.addPatientData(0, 122.0, "SystolicPressure", Long.parseLong("1"));
        dataStorage.addPatientData(0, 103.0, "DiastolicPressure", Long.parseLong("2"));
        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);
        alertGenerator.evaluateData(dataStorage.getAllPatients().get(0));
        System.out.println(dataStorage.getAllPatients().get(0));
        assertTrue(MonitoringSystem.alertTriggered);
    }

    @Test
    public void testECGTrendAlert() {
        DataStorage dataStorage = new DataStorage();
        dataStorage.addPatientData(0, 90.0, "SystolicPressure", Long.parseLong("1"));
        dataStorage.addPatientData(0, 80.0, "DiastolicPressure", Long.parseLong("2"));
        dataStorage.addPatientData(0, 101.0, "ECG", Long.parseLong("1"));
        dataStorage.addPatientData(0, 101.0, "ECG", Long.parseLong("995"));
        dataStorage.addPatientData(0, 101.0, "ECG", Long.parseLong("4000"));
        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);
        alertGenerator.evaluateData(dataStorage.getAllPatients().get(0));
        System.out.println(dataStorage.getAllPatients().get(0));
        assertTrue(MonitoringSystem.alertTriggered);
    }

    @Test
    public void testHypoxemiaAlert() {
        DataStorage dataStorage = new DataStorage();
        dataStorage.addPatientData(0, 70.0, "SystolicPressure", Long.parseLong("1"));
        dataStorage.addPatientData(0, 80.0, "DiastolicPressure", Long.parseLong("2"));
        dataStorage.addPatientData(0, 90, "Saturation", Long.parseLong("3"));
        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);
        alertGenerator.evaluateData(dataStorage.getAllPatients().get(0));
        System.out.println(dataStorage.getAllPatients().get(0));
        assertTrue(MonitoringSystem.alertTriggered);
    }
}
