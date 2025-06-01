package model;

import model.enums.AlertPriority;
import model.enums.AlertStatus;

public class AdministrativeAlert extends Alert {
    private final String administrativeProblem;

    public AdministrativeAlert(int id, AlertPriority priority, AlertStatus status, String administrativeProblem) {
        super(id, priority, status);
        this.administrativeProblem = administrativeProblem;
    }
    public String getAdministrativeProblem() {
        return administrativeProblem;
    }
    @Override
    public void escalate(){
        if(this.getPriority() == AlertPriority.MEDIUM)
            this.setPriority(AlertPriority.HIGH);
        else
            if(this.getPriority() == AlertPriority.HIGH)
                this.setPriority(AlertPriority.CRITICAL);
    }
   @Override
    public String getDetails() {
        return "Administrative Alert: " + getId() + " affecting: " + administrativeProblem +
                " [Priority: " + getPriority() + ", Status: " + getStatus() + "]";
    }
    @Override
    public String toString() {
        return getDetails();
    }
}
