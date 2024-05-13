package com.alerts;

// A system through which staff can monitor patients
public class MonitoringSystem {
    // Flags are needed for tests
    public static boolean alertTriggered = false;
    public static boolean alertAcknowledged = false;

    public static void notifyStaff(Alert alert) {
        // Code to notify staff of an alert
        alertTriggered = true;
        System.out.println("Alert triggered: " + alert.getCondition());
    }

    public static void logAlert(Alert alert) {
        // Code to log an alert
        alertAcknowledged = true;
        System.out.println("Alert logged: " + alert.getCondition());
    }
}
