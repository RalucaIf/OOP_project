package model;

import model.enums.AlertPriority;
import model.enums.AlertStatus;

import java.time.LocalDateTime;

public class SecurityAlert extends Alert {
    private final String securityProblem;

    public SecurityAlert(int id, AlertPriority priority, AlertStatus status, String securityProblem) {
        super(id, priority, status);
        this.securityProblem = String.valueOf(securityProblem);
    }


    public String getSecurityProblem() {
        return securityProblem;
    }

    @Override
    public void escalate(){
        if(this.getPriority() == AlertPriority.HIGH)
            this.setPriority(AlertPriority.CRITICAL);
         else
            this.setPriority(AlertPriority.HIGH);
    }
    @Override
    public String getDetails() {
        return "Security Alert: " + getId() + " affecting: " + securityProblem +
                " [Priority: " + getPriority() + ", Status: " + getStatus() + "]";
    }

}
