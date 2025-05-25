package model.interfaces;

import model.enums.AlertPriority;
import model.enums.AlertStatus;

import java.time.LocalDateTime;

public interface InterfaceAlert {
    // getters
    int getId();
    AlertPriority getPriority();
    AlertStatus getStatus();

    // setters
    void setPriority(AlertPriority priority);
    void setStatus(AlertStatus status);

    // methods
    void escalate();
    String getDetails();
}
