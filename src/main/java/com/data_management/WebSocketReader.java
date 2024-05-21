package com.data_management;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;

import org.java_websocket.handshake.ServerHandshake;

/*
 * Reads data from a WebSocket server and stores it in the data storage.
 */
public class WebSocketReader implements DataReader {
    private DataStorage dataStorage;
    private WebSocketClient client;
    private final CountDownLatch latch = new CountDownLatch(1);

    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        throw new UnsupportedOperationException("Unrelevant method 'readData'");
    }

    @Override
    public void listenForData(DataStorage dataStorage, String address) {
        this.client = new WebSocketClient(URI.create(address));
        this.dataStorage = dataStorage;
        client.connect();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Listening for data from WebSocket server at: " + address);
    }

    @Override
    public void stopListening() {
        client.close();
        System.out.println("Stopped listening for data from WebSocket server");
    }

    private void addData(String message) {
        System.out.println("Received data: " + message);
        try {
            String[] data = message.split(", ");
            int patientId = Integer.parseInt(data[0].split(": ")[1]);
            long timestamp = Long.parseLong(data[1].split(": ")[1]);
            String label = data[2].split(": ")[1];
            double value = formatValue(data[3].split(": ")[1]);
            dataStorage.addPatientData(patientId, value, label, timestamp);
        } catch (Exception e) {
            System.out.println("Error adding data: " + message);
            //e.printStackTrace();
        }
    }

    private double formatValue(String value) {
        char[] valueChars = value.toCharArray();
        // Any new case of writing data value should be added here to avoid errors
        if (valueChars[valueChars.length - 1] == '%') {
            return Double.parseDouble(value.substring(0, valueChars.length - 1));
        }

        return Double.parseDouble(value);
    }

    private class WebSocketClient extends org.java_websocket.client.WebSocketClient {

        public WebSocketClient(URI serverUri) {
            super(serverUri);
        }

        @Override
        public void onOpen(ServerHandshake handshakedata) {
            System.out.println("Connected to server: " + getURI());
            latch.countDown();
        }

        @Override
        public void onMessage(String message) {
            addData(message);
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            System.out.println("Connection closed: " + code + ", " + reason);
        }

        @Override
        public void onError(Exception ex) {
            System.err.println("An error occurred during the connection process:");
            ex.printStackTrace();
        }
    }
    
}
