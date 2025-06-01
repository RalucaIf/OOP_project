package service;

import exception.AlertNotFoundException;
import exception.InvalidDataException;
import model.*;
import model.enums.AlertStatus;
import repository.AlertRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class AlertService {
    private final AlertRepository alertRepository;
    private final AuditService auditService = AuditService.getInstance(); // singleton

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }
    public void createAlert(Alert alert) throws InvalidDataException {
        if (alert == null || alert.getPriority() == null || alert.getDetails() == null) {
            throw new InvalidDataException("Invalid alert data");
        }
        alertRepository.create(alert);
        auditService.writeToCSV("Alert created: " + alert.getDetails());
    }

    public void escalateAlert(int id) throws AlertNotFoundException {
        Alert alert = alertRepository.findById(id);
        if (alert == null) throw new AlertNotFoundException("Alert not found");
        alert.escalate();
        alertRepository.updateStatus(id, alert.getStatus());
        auditService.writeToCSV("Alert escalated: " + alert.getDetails());
    }

    public void closeAlert(int id) throws AlertNotFoundException {
        Alert alert = alertRepository.findById(id);
        if (alert == null) throw new AlertNotFoundException("Alert not found");
        alert.setStatus(AlertStatus.CLOSED);
        alertRepository.updateStatus(id, AlertStatus.CLOSED);
        auditService.writeToCSV("Alert closed: " + alert.getDetails());
    }

    public List<Alert> getByStatus(AlertStatus status) {
        auditService.writeToCSV("Get alerts by status");
        return alertRepository.getByStatus(status);
    }

    public Set<Alert> getSortedAlertsByPriorityThenTime() {
        TreeSet<Alert> sorted = new TreeSet<>(
                Comparator.comparing(Alert::getPriority).reversed()
        );
        sorted.addAll(alertRepository.getAll());
        return sorted;
    }
    // create alert using records
    public void createAlert(Alert alert, Device sourceDevice) throws InvalidDataException {
        if (alert == null || alert.getPriority() == null || alert.getDetails() == null) {
            throw new InvalidDataException("Invalid alert data");
        }
        alertRepository.create(alert);

        Event event = new Event("ALERT_CREATED", sourceDevice, LocalDateTime.now());
        auditService.writeToCSV("Event: " + event.type() + ", Source: "
                + sourceDevice.name() + " (" + sourceDevice.ip() + ")");
        System.out.println("Alert created: " + alert.getDetails());
        System.out.println("Event logged: " + event);
    }
}