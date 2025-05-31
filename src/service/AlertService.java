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

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }
    public void createAlert(Alert alert) {
        alertRepository.create(alert);
    }

    public void escalateAlert(int id) throws AlertNotFoundException {
        Alert alert = alertRepository.findById(id);
        if (alert == null) throw new AlertNotFoundException("Alert not found");
        alert.escalate();
        alertRepository.updateStatus(id, alert.getStatus());
    }

    public void closeAlert(int id) throws AlertNotFoundException {
        Alert alert = alertRepository.findById(id);
        if (alert == null) throw new AlertNotFoundException("Alert not found");
        alert.setStatus(AlertStatus.CLOSED);
        alertRepository.updateStatus(id, AlertStatus.CLOSED);
    }

    public List<Alert> getByStatus(AlertStatus status) {
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