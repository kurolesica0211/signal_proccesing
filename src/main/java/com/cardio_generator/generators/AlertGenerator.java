package com.cardio_generator.generators;

// Import names should be sorted by ASCII order
// Import statements should be grouped block-like, with a blank line between each group
import com.cardio_generator.outputs.OutputStrategy;
import java.util.Random;

// Block indentations add +2 spaces, not +4, except the cases of line-breaking, where each
// contiuating line should be indented by +4 spaces.

/** The class is used for generating alert data */
public class AlertGenerator implements PatientDataGenerator {

  // The name of the constant should be in UPPER_SNAKE_CASE
  public static final Random RANDOM_GENERATOR = new Random();
  // Non-constant field names should be in lowerCamelCase
  private boolean[] alertStates; // false = resolved, true = pressed

  /** Sets the number of patients
   * @param patientCount the number of patients
   */
  public AlertGenerator(int patientCount) {
    alertStates = new boolean[patientCount + 1];
  }

  @Override
  public void generate(int patientId, OutputStrategy outputStrategy) {
    try {
      if (alertStates[patientId]) {
        if (RANDOM_GENERATOR.nextDouble() < 0.9) { // 90% chance to resolve
          alertStates[patientId] = false;
          // Output the alert
          outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
        }
      } else {
        double lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency
        // One-character variable names are discouraged
        double prob = -Math.expm1(-lambda); // Probability of at least one alert in the period
        boolean alertTriggered = RANDOM_GENERATOR.nextDouble() < prob;

        if (alertTriggered) {
          alertStates[patientId] = true;
          // Output the alert
          outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
        }
      }
    } catch (Exception e) {
      System.err.println("An error occurred while generating alert data for patient " + patientId);
      e.printStackTrace();
    }
  }
}
