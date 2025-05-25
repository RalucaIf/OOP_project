package service;

import model.AdministrativeAlert;
import model.SecurityAlert;
import model.interfaces.InterfaceAlert;
import repository.AlertRepository;

import java.sql.SQLException;
import java.util.List;

public class AlertService {

    private final AlertRepository alertRepository;

    public AlertService() {
        this.alertRepository = new AlertRepository();
    }

    public void createAlert(InterfaceAlert alert) throws SQLException {
        if (alert instanceof AdministrativeAlert) {
            alertRepository.createAlert(alert, ((AdministrativeAlert) alert).getAdministrativeProblem());
        } else if (alert instanceof SecurityAlert) {
            alertRepository.createAlert(alert, ((SecurityAlert) alert).getSecurityProblem());
        } else {
            throw new IllegalArgumentException("Unsupported alert type");
        }
    }

    public InterfaceAlert getAlert(int id) throws SQLException {
        return alertRepository.getAlert(id);
    }

    public List<InterfaceAlert> getAllAlerts() throws SQLException {
        return alertRepository.getAllAlerts();
    }

    public void updateAlert(InterfaceAlert alert) throws SQLException {
        if (alert instanceof AdministrativeAlert) {
            alertRepository.updateAlert(alert, ((AdministrativeAlert) alert).getAdministrativeProblem());
        } else if (alert instanceof SecurityAlert) {
            alertRepository.updateAlert(alert, ((SecurityAlert) alert).getSecurityProblem());
        } else {
            throw new IllegalArgumentException("Unsupported alert type");
        }
    }

    public void deleteAlertById(int id) throws SQLException {
        alertRepository.deleteAlertById(id);
    }
}