package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * The interface represents a generator for generating patient data.
 */
public interface PatientDataGenerator {
    /**
     * Generates patient data based on the given patient ID and output strategy.
     *
     * @param patientId      the ID of the patient
     * @param outputStrategy the output strategy to be used for generating the patient data
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
