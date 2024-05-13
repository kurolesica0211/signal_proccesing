package com.alerts;

// A system through which staff can monitor patients
public class MonitoringSystem {
    public static boolean alertTriggered = false;
    public static boolean alertAcknowledged = false;

    public static void notifyStaff(Alert alert) {
        // Code to notify staff of an alert
        alertTriggered = true;
    }

    public static void logAlert(Alert alert) {
        // Code to log an alert
        alertAcknowledged = true;
    }
}
