package model;

import model.enums.AlertPriority;
import model.enums.AlertStatus;

public abstract class Alert  {
    private int id;
    private AlertPriority priority;
    private AlertStatus status;

    public Alert(int id, AlertPriority priority, AlertStatus status) {
        this.id = id;
        this.priority = priority;
        this.status = status;
    }

    // getters
    public int getId() {
        return id;
    }
    public AlertPriority getPriority() {
        return priority;
    }
    public AlertStatus getStatus() {
        return status;
    }

    // setters
    public void setPriority(AlertPriority priority) {
        this.priority = priority;
    }

    public void setStatus(AlertStatus status) {
        this.status = status;
    }

    // methods
    public abstract void escalate();
    public abstract String getDetails();

    @Override
    public String toString() {
        return getDetails();
    }

    public void setId(int id) {
        this.id = id;
    }
}