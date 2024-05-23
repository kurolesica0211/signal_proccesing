package com.alerts.alert_decoration;

import com.alerts.alert_types.Alert;

public class PriorityAlertDecorator extends AlertDecorator{
    int priority;

    /**
     * Decorates an alert with a priority;
     * 1 - highest priority,
     * 5 - lowest priority;
     * this constructor if you want to creat an entirely new alert
     * @param patientId
     * @param condition
     * @param timestamp
     * @param priority
     */
    public PriorityAlertDecorator(String patientId, String condition, long timestamp, int priority) {
        super(patientId, condition, timestamp);
        this.priority = priority;
    }

    /**
     * Decorates an alert with a priority;
     * 1 - highest priority,
     * 5 - lowest priority
     * @param alert
     * @param priority
     */
    public PriorityAlertDecorator(Alert alert, int priority) {
        super(alert.getPatientId(), alert.getCondition(), alert.getTimestamp());
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
