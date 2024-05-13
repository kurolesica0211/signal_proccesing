package com.data_management;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class FileReader implements DataReader {
    private String path = " ";
    public FileReader(String path) {
        this.path = path;
    }

    public void readData(DataStorage dataStorage) {
        String path = this.path.isBlank() ? "patientData" : this.path;
        try {
            String[] fileNames = getFileNames(path);
            for (String fileName : fileNames) {
                if (fileName.endsWith("Alert.txt")) {
                    continue;
                }
                File currentFile = new File(fileName);
                Scanner myReader = new Scanner(currentFile);
                while (myReader.hasNextLine()) {
                    String[] data = myReader.nextLine().split(", ");
                    int patientId = Integer.parseInt(data[0].split(": ")[1]);
                    long timestamp = Long.parseLong(data[1].split(": ")[1]);
                    String label = data[2].split(": ")[1];
                    double value = formatValue(data[3].split(": ")[1]);
                    dataStorage.addPatientData(patientId, value, label, timestamp);
                }
                myReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double formatValue(String value) {
        char[] valueChars = value.toCharArray();
        
        if (valueChars[valueChars.length - 1] == '%') {
            return Double.parseDouble(value.substring(0, valueChars.length - 1));
        }

        return Double.parseDouble(value);
    }

    private String[] getFileNames(String path) {
        ArrayList<String> fileNames = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
            paths.filter(Files::isRegularFile).filter(f -> f.toString().endsWith(".txt")).forEach(f -> fileNames.add(f.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileNames.toArray(String[]::new);
    }

    public static void main(String[] args) {
        DataStorage dataStorage = new DataStorage();
        FileReader fileReader = new FileReader("patientData");
        fileReader.readData(dataStorage);
        List<Patient> patients = dataStorage.getAllPatients();
        for (Patient patient : patients) {
            List<PatientRecord> records = patient.getRecords(0, Long.MAX_VALUE);
            for (PatientRecord record : records) {
                System.out.println("Patient ID: " + record.getPatientId() + " - Record: " + record.getRecordType() + " - " + record.getMeasurementValue() + " - " + record.getTimestamp());
            }
        }
    }
}
