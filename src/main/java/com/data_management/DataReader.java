package com.data_management;

import java.io.IOException;

public interface DataReader {
    /**
     * Reads data from a specified source and stores it in the data storage.
     * 
     * @param dataStorage the storage where data will be stored
     * @throws IOException if there is an error reading the data
     */
    void readData(DataStorage dataStorage) throws IOException;

    /**
     * Listens for data from a specified WebSocket server and stores it in the data storage.
     * 
     * @param dataStorage the storage where data will be stored
     * @param address the address of the WebSocket server
     */
    void listenForData(DataStorage dataStorage, String address);

    /**
     * Stops listening for data from the WebSocket server.
     */
    void stopListening();
}
