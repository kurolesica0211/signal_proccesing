package com.cardio_generator.outputs;

// Import names should be sorted by ASCII order
import java.util.concurrent.ConcurrentHashMap;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.file.Paths;
import java.io.PrintWriter;
import java.nio.file.StandardOpenOption;

// The name of the class should be FileOutputStrategy, so to match the name of the file 
// (it was appropriately changed in the other files).
// Block indentations add +2 spaces, not +4, except the cases of line-breaking, where each
// contiuating line should be indented by +4 spaces.

/** The class is used for writing the output in a file */
public class FileOutputStrategy implements OutputStrategy {

  // Non-constant field names should be in lowerCamelCase
  private String baseDirectory;

  // The name of the constant should be in UPPER_SNAKE_CASE
  public final ConcurrentHashMap<String, String> FILE_MAP = new ConcurrentHashMap<>();

  /** Sets the derictory, where the file should be stored
   * @param baseDirectory the directory, where the file should be stored
   */
  public FileOutputStrategy(String baseDirectory) {

    // I think such blank lines are permitted, as it improves readability
    this.baseDirectory = baseDirectory;
  }

  // The method should not be documented, as Override annotation is present
  @Override
  public void output(int patientId, long timestamp, String label, String data) {

    try {
      // Create the directory
      Files.createDirectories(Paths.get(baseDirectory));
    } catch (IOException e) {
      System.err.println("Error creating base directory: " + e.getMessage());
      return;
    }
    // Set the FilePath variable
    // The line is too long, so it should be broken into two lines (break is justified, because 
    // the body of the lambda consists of a single unbraced expression).
    String FilePath = FILE_MAP.computeIfAbsent(label, k -> 
        Paths.get(baseDirectory, label + ".txt").toString());

    // Write the data to the file
    try (PrintWriter out = new PrintWriter(
        Files.newBufferedWriter(Paths.get(FilePath),
            StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
      out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n",
          patientId, timestamp, label, data);
    } catch (Exception e) {
      System.err.println("Error writing to file " + FilePath + ": " + e.getMessage());
    }

  }
}