package model;

import model.enums.AlertPriority;
import model.enums.AlertStatus;
import model.interfaces.InterfaceAlert;

public abstract class Alert implements InterfaceAlert {
    private final int id;
    private AlertPriority priority;
    private AlertStatus status;

    public Alert(int id, AlertPriority priority, AlertStatus status) {
        this.id = id;
        this.priority = priority;
        this.status = status;
    }

    // getters
    @Override
    public int getId() {
        return id;
    }
    @Override
    public AlertPriority getPriority() {
        return priority;
    }
    @Override
    public AlertStatus getStatus() {
        return status;
    }
    // setters
    @Override
    public void setPriority(AlertPriority priority) {
        this.priority = priority;
    }
    @Override
    public void setStatus(AlertStatus status) {
        this.status = status;
    }
    // methods
    @Override
    public abstract void escalate();

    @Override
    public abstract String getDetails();
}