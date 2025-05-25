package repository;

import config.DBConfig;
import model.AdministrativeAlert;
import model.SecurityAlert;
import model.enums.AlertPriority;
import model.enums.AlertStatus;
import model.interfaces.InterfaceAlert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlertRepository {

    public void createAlert(InterfaceAlert alert, String specificDetail) {
        String sql = """
                    insert into alerts 
                    (id, priority, status, type, specific_detail) 
                    values (?, ?, ?, ?, ?)
                    """;

        try (Connection connection = DBConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, alert.getId());
            preparedStatement.setString(2, alert.getPriority().name());
            preparedStatement.setString(3, alert.getStatus().name());
            preparedStatement.setString(4, alert instanceof AdministrativeAlert ? "ADMINISTRATIVE" : "SECURITY");
            preparedStatement.setString(5, specificDetail);

            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public InterfaceAlert getAlert(int id) throws SQLException {
        String sql = "select * from alerts where id = ?";

        try (Connection connection = DBConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return parseAlertFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new SQLException("Alert with ID " + id + " not found.");
    }

    public List<InterfaceAlert> getAllAlerts() throws SQLException {
        String sql = "select * from alerts";
        List<InterfaceAlert> alerts = new ArrayList<>();
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                alerts.add(parseAlertFromResultSet(resultSet));
            }
        }
        return alerts;
    }

    public void updateAlert(InterfaceAlert alert, String specificDetail) throws SQLException {
        String query = "update alerts set priority = ?, status = ?, type = ?, specific_detail = ? where id = ?";

        try (Connection connection = DBConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, alert.getPriority().name());
            preparedStatement.setString(2, alert.getStatus().name());
            preparedStatement.setString(3, alert instanceof AdministrativeAlert ? "ADMINISTRATIVE" : "SECURITY");
            preparedStatement.setString(4, specificDetail);
            preparedStatement.setInt(5, alert.getId());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("No alert found with ID " + alert.getId());
            }
        }
    }

    public void deleteAlertById(int id) throws SQLException {
        String sql = "delete from alerts where id = ?";

        try (Connection connection = DBConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted == 0) {
                throw new SQLException("No alert found with ID " + id);
            }
        }
    }
    private InterfaceAlert parseAlertFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        AlertPriority priority = AlertPriority.valueOf(resultSet.getString("priority"));
        AlertStatus status = AlertStatus.valueOf(resultSet.getString("status"));
        String type = resultSet.getString("type");
        String specificDetail = resultSet.getString("specific_detail");

        if (type.equals("ADMINISTRATIVE")) {
            return new AdministrativeAlert(id, priority, status, specificDetail);
        } else if (type.equals("SECURITY")) {
            return new SecurityAlert(id, priority, status, specificDetail);
        }
        throw new SQLException("Unknown alert type: " + type);
    }
}