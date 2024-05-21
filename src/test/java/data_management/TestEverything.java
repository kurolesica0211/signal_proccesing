package data_management;

import org.java_websocket.WebSocket;
import org.junit.Test;
import static org.junit.Assert.*;

import com.data_management.*;
import com.alerts.AlertGenerator;
import com.alerts.MonitoringSystem;

import java.net.InetSocketAddress;
import java.util.List;

public class TestEverything {

    public TestEverything() {
    }

    // Running all the tests together somehow make testSaturationTrendAlert1 fail, it should be run separately
    // THERE ARE TEST FILES IN BIN FOLDER, MAKE SURE THEY ARE IN PATEINTDATA FODLER BEFORE RUNNING THE TESTS
    @Test
    public void testFileReaderAndGetRecords() {
        DataStorage dataStorage = new DataStorage();
        FileReader fileReader = new FileReader("patientData");
        fileReader.readData(dataStorage);
        List<Patient> patients = dataStorage.getAllPatients();
        List<PatientRecord> patient1 = patients.get(0).getRecords(0, Long.MAX_VALUE);
        List<PatientRecord> patient2 = patients.get(1).getRecords(0, Long.parseLong("1715545367974"));
        assertEquals(patient1.get(0).getMeasurementValue(), 0.5272823916128154, 0);
        assertEquals(patient1.get(0).getRecordType(), "ECG");
        assertEquals(patient1.get(0).getTimestamp(), Long.parseLong("1715545367974"));
        assertTrue(patient2.isEmpty());
        MonitoringSystem.alertTriggered = false;
    }

    @Test
    public void testSystolicBloodPressureLevelAlert() {
        DataStorage dataStorage = new DataStorage();
        dataStorage.addPatientData(0, 190.0, "SystolicPressure", Long.parseLong("1"));
        dataStorage.addPatientData(0, 80.0, "DiastolicPressure", Long.parseLong("2"));
        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);
        alertGenerator.evaluateData(dataStorage.getAllPatients().get(0), 1);
        assertTrue(MonitoringSystem.alertTriggered);
        MonitoringSystem.alertTriggered = false;
    }

    @Test
    public void testDistolicBloodPressureLevelAlert() {
        DataStorage dataStorage = new DataStorage();
        dataStorage.addPatientData(0, 100.0, "SystolicPressure", Long.parseLong("1"));
        dataStorage.addPatientData(0, 140.0, "DiastolicPressure", Long.parseLong("2"));
        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);
        alertGenerator.evaluateData(dataStorage.getAllPatients().get(0), 1);
        System.out.println(dataStorage.getAllPatients().get(0));
        assertTrue(MonitoringSystem.alertTriggered);
        MonitoringSystem.alertTriggered = false;
    }

    @Test
    public void testSaturationLevelAlert() {
        DataStorage dataStorage = new DataStorage();
        dataStorage.addPatientData(0, 90, "Saturation", Long.parseLong("1"));
        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);
        alertGenerator.evaluateData(dataStorage.getAllPatients().get(0), 3);
        System.out.println(dataStorage.getAllPatients().get(0));
        assertTrue(MonitoringSystem.alertTriggered);
        MonitoringSystem.alertTriggered = false;
    }

    @Test
    public void testSaturaitonTrendAlert() {
        DataStorage dataStorage = new DataStorage();
        dataStorage.addPatientData(0, 93, "Saturation", Long.parseLong("1"));
        dataStorage.addPatientData(0, 99, "Saturation", Long.parseLong("1000"));
        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);
        alertGenerator.evaluateData(dataStorage.getAllPatients().get(0), 3);
        System.out.println(dataStorage.getAllPatients().get(0));
        assertTrue(MonitoringSystem.alertTriggered);
        MonitoringSystem.alertTriggered = false;
    }

    @Test
    public void testECGLevelAlert() {
        DataStorage dataStorage = new DataStorage();
        dataStorage.addPatientData(0, 0.1, "ECG", Long.parseLong("3"));
        dataStorage.addPatientData(0, 0.1, "ECG", Long.parseLong("1500"));
        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);
        alertGenerator.evaluateData(dataStorage.getAllPatients().get(0), 2);
        System.out.println(dataStorage.getAllPatients().get(0));
        assertTrue(MonitoringSystem.alertTriggered);
        MonitoringSystem.alertTriggered = false;
        dataStorage = new DataStorage();
        dataStorage.addPatientData(0, 0.1, "ECG", Long.parseLong("3"));
        dataStorage.addPatientData(0, 0.1, "ECG", Long.parseLong("4"));
        alertGenerator = new AlertGenerator(dataStorage);
        alertGenerator.evaluateData(dataStorage.getAllPatients().get(0), 2);
        System.out.println(dataStorage.getAllPatients().get(0));
        assertTrue(MonitoringSystem.alertTriggered);
    }

    @Test
    public void testSaturationTrendAlert1() {
        DataStorage dataStorage = new DataStorage();
        dataStorage.addPatientData(0, 93, "Saturation", Long.parseLong("1"));
        dataStorage.addPatientData(0, 99, "Saturation", Long.parseLong("6000000"));
        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);
        alertGenerator.evaluateData(dataStorage.getAllPatients().get(0), 3);
        System.out.println(dataStorage.getAllPatients().get(0));
        assertTrue(MonitoringSystem.alertTriggered==false);
        MonitoringSystem.alertTriggered = false;
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
        alertGenerator.evaluateData(dataStorage.getAllPatients().get(0), 1);
        System.out.println(dataStorage.getAllPatients().get(0));
        assertTrue(MonitoringSystem.alertTriggered);
        MonitoringSystem.alertTriggered = false;
    }

    @Test
    public void testECGTrendAlert() {
        DataStorage dataStorage = new DataStorage();
        dataStorage.addPatientData(0, 101.0, "ECG", Long.parseLong("1"));
        dataStorage.addPatientData(0, 101.0, "ECG", Long.parseLong("995"));
        dataStorage.addPatientData(0, 101.0, "ECG", Long.parseLong("4000"));
        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);
        alertGenerator.evaluateData(dataStorage.getAllPatients().get(0), 2);
        System.out.println(dataStorage.getAllPatients().get(0));
        assertTrue(MonitoringSystem.alertTriggered);
        MonitoringSystem.alertTriggered = false;
    }

    @Test
    public void testHypoxemiaAlert() {
        DataStorage dataStorage = new DataStorage();
        dataStorage.addPatientData(0, 70.0, "SystolicPressure", Long.parseLong("1"));
        dataStorage.addPatientData(0, 80.0, "DiastolicPressure", Long.parseLong("2"));
        dataStorage.addPatientData(0, 90, "Saturation", Long.parseLong("3"));
        AlertGenerator alertGenerator = new AlertGenerator(dataStorage);
        alertGenerator.evaluateData(dataStorage.getAllPatients().get(0), 4);
        System.out.println(dataStorage.getAllPatients().get(0));
        assertTrue(MonitoringSystem.alertTriggered);
        MonitoringSystem.alertTriggered = false;
    }

    @Test
    public void testWebSocketReaderBasic() throws InterruptedException {
        String URI = "ws://localhost:1024";
        DataStorage dataStorage = new DataStorage();
        DataReader webSocket = new WebSocketReader();
        TestWebSocketServer server = new TestWebSocketServer(1024);
        server.start();
        webSocket.listenForData(dataStorage, URI);
        try {
            System.out.println("Number of connections: "+server.getConnections().size());
            for (WebSocket conn : server.getConnections()) {
                System.out.println(conn.getRemoteSocketAddress());
                conn.send("0: 60, 1: 70, 2: 80, 3: 90");
            }
            Thread.sleep(1000);
            PatientRecord rec = dataStorage.getAllPatients().get(0).getRecords(0, Long.MAX_VALUE).get(0);
            assertEquals(rec.getMeasurementValue(), 90, 0);
            assertEquals(rec.getRecordType(), "80");
            assertEquals(rec.getTimestamp(), 70);
            assertEquals(rec.getPatientId(), 60);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            webSocket.stopListening();
            server.stop();
        }
    }

    @Test
    public void testWebSocketReaderCorruptTextHandling() throws InterruptedException {
        String URI = "ws://localhost:1024";
        DataStorage dataStorage = new DataStorage();
        DataReader webSocket = new WebSocketReader();
        TestWebSocketServer server = new TestWebSocketServer(1024);
        server.start();
        webSocket.listenForData(dataStorage, URI);
        try {
            System.out.println("Number of connections: "+server.getConnections().size());
            for (WebSocket conn : server.getConnections()) {
                System.out.println(conn.getRemoteSocketAddress());
                conn.send("sofsd;jfnb: klnffl, dgsgdf: dlnfgdlf");
            }
            Thread.sleep(1000);
            assertTrue(dataStorage.getAllPatients().isEmpty());
            webSocket.stopListening();
            server.stop();
        } catch (Exception e) {
            webSocket.stopListening();
            server.stop();
            e.printStackTrace();
        }
    }
}
