package service;

import exception.AlertNotFoundException;
import model.AdministrativeAlert;
import model.Alert;
import model.SecurityAlert;
import model.enums.AlertPriority;
import model.enums.AlertStatus;
import repository.AlertRepository;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class AlertService {
    private final AlertRepository alertRepository;
    private final AuditService auditService = AuditService.getInstance();

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }
    public void createAlert(Alert alert) {
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
}