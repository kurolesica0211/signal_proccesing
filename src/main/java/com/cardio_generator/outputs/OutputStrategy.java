package com.cardio_generator.outputs;

/**
 * The interface represents a strategy for outputting the generated data.
 */
public interface OutputStrategy {
    /**
     * Outputs the data related to a patient.
     *
     * @param patientId the ID of the patient
     * @param timestamp the timestamp of the data
     * @param label     the label associated with the data
     * @param data      the actual data to be outputted
     */
    void output(int patientId, long timestamp, String label, String data);
}
