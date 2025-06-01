package repository;

import config.DBConfig;
import model.AdministrativeAlert;
import model.Alert;
import model.SecurityAlert;
import model.enums.AlertPriority;
import model.enums.AlertStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlertRepository implements CRUDInterface<Alert> {

    @Override
     public Alert create(Alert alert) {
         String sql = """
                 INSERT INTO alerts (priority, status, type, problem)
                 VALUES (?, ?, ?, ?) RETURNING id;
                 """;
         try (Connection connection = DBConfig.getConnection();
              PreparedStatement preparedStatement = connection.prepareStatement(sql)){

             // preparedStatement.setInt(1, alert.getId());
             preparedStatement.setString(1, alert.getPriority().toString());
             preparedStatement.setString(2, alert.getStatus().toString());

             if (alert instanceof AdministrativeAlert) {
                 preparedStatement.setString(3, "Administrative");
                 preparedStatement.setString(4, ((AdministrativeAlert) alert).getAdministrativeProblem());
             } else if (alert instanceof SecurityAlert){
                 preparedStatement.setString(3, "Security");
                 preparedStatement.setString(4, ((SecurityAlert) alert).getSecurityProblem());
             } else {
                 throw new IllegalArgumentException("Unsupported alert type");
             }
             try (ResultSet resultSet = preparedStatement.executeQuery()) {
                 if (resultSet.next()) {
                     alert.setId(resultSet.getInt("id")); // Retrieve and set the generated ID
                 }
             }
             return alert;

         } catch (SQLException e) {
             throw new RuntimeException(e);
         }
    }

    @Override
    public Alert findById(int id) {
        String sql = "SELECT * FROM alerts WHERE id = ?";
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    int alertId = resultSet.getInt(1);
                    String priority = resultSet.getString(2);
                    String status = resultSet.getString(3);
                    String type = resultSet.getString(4);
                    String problem = resultSet.getString(5);

                    AlertPriority alertPriority = AlertPriority.valueOf(priority.toUpperCase());
                    AlertStatus alertStatus = AlertStatus.valueOf(status.toUpperCase());
                    if (type.equals("Administrative")) {
                        return new AdministrativeAlert(alertId, alertPriority, alertStatus, problem);
                    } else if (type.equals("Security")){
                        return new SecurityAlert(alertId, alertPriority, alertStatus, problem);
                    } else {throw new IllegalArgumentException("Unsupported alert type");}
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Alert> getAll() {
        String sql = "SELECT * FROM alerts";
        List<Alert> alerts = new ArrayList<>();
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()){
            while(resultSet.next()) {
                int alertId = resultSet.getInt(1);
                String priority = resultSet.getString(2);
                String status = resultSet.getString(3);
                String type = resultSet.getString(4);
                String problem = resultSet.getString(5);

                AlertPriority alertPriority = AlertPriority.valueOf(priority.toUpperCase());
                AlertStatus alertStatus = AlertStatus.valueOf(status.toUpperCase());
                if (type.equalsIgnoreCase("Administrative")) {
                    alerts.add(new AdministrativeAlert(alertId, alertPriority, alertStatus, problem));
                } else if(type.equalsIgnoreCase("Security")){
                    alerts.add(new SecurityAlert(alertId, alertPriority, alertStatus, problem));
                } else {throw new IllegalArgumentException("Unsupported alert type");}
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return alerts;

    }

    @Override
    public void delete(int id){
        String sql = "DELETE FROM alerts WHERE id = ?";
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            int rowsDeleted = preparedStatement.executeUpdate();

            if(rowsDeleted > 0) {
                System.out.println("Alert with id " + id + " deleted.");
            } else{
                System.out.println("No alert with id " + id + " found.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting alert " + e.getMessage());
        }
    }

    public void updateStatus(int id, AlertStatus status) {
        String sql = "UPDATE alerts SET status = ? WHERE id = ?";
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, status.name());
            preparedStatement.setInt(2, id);

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated == 0) {
                throw new RuntimeException("No alert found with ID: " + id);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error updating the status of alert with ID: " + id, e);
        }
    }

    public List<Alert> getByStatus(AlertStatus status) {
        String sql = "SELECT * FROM alerts WHERE status = ?";
        List<Alert> alerts = new ArrayList<>();

        try (Connection connection = DBConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, status.name());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int alertId = resultSet.getInt("id");
                    String priority = resultSet.getString("priority");
                    String alertStatus = resultSet.getString("status");
                    String type = resultSet.getString("type");
                    String problem = resultSet.getString("problem");

                    AlertPriority alertPriority = AlertPriority.valueOf(priority.toUpperCase());
                    AlertStatus alertStatusEnum = AlertStatus.valueOf(alertStatus.toUpperCase());

                    if (type.equalsIgnoreCase("Administrative")) {
                        alerts.add(new AdministrativeAlert(alertId, alertPriority, alertStatusEnum, problem));
                    } else if (type.equalsIgnoreCase("Security")) {
                        alerts.add(new SecurityAlert(alertId, alertPriority, alertStatusEnum, problem));
                    } else {
                        throw new IllegalArgumentException("Unsupported alert type: " + type);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving alerts by status: " + status, e);
        }
        return alerts;
    }
}