package com.alerts.alert_decoration;

import com.alerts.alert_types.Alert;

public class RepeatedAlertDecoration extends AlertDecorator{
    Alert alert;

    /**
     * Decorates an alert with a repeated alert;
     * this constructor if you want to creat an entirely new alert
     * @param patientId
     * @param condition
     * @param timestamp
     */
    public RepeatedAlertDecoration(String patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
        alert = this;
        RepeatAlertThread repeatAlertThread = new RepeatAlertThread();
        repeatAlertThread.start();
    }

    /**
     * Decorates an alert with a repeated alert
     * @param alert
     */
    public RepeatedAlertDecoration(Alert alert) {
        super(alert.getPatientId(), alert.getCondition(), alert.getTimestamp());
        this.alert = alert;
        RepeatAlertThread repeatAlertThread = new RepeatAlertThread();
        repeatAlertThread.start();
    }
    
    class RepeatAlertThread extends Thread {
        public void run() {
            long timestamp = System.currentTimeMillis();
            while (System.currentTimeMillis() - timestamp < 5000){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                triggerAlert(alert);
            }
        }
    }
}
