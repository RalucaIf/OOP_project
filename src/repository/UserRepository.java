package repository;

import config.DBConfig;
import model.AdminUser;
import model.RegularUser;
import model.User;
import model.enums.UserRole;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements CRUDInterface<User> {

    @Override
    public User create(User user) {
        String sql = "INSERT INTO users ( username, password, role) VALUES (?, ?, ?) RETURNING id";
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());

            if (user instanceof AdminUser) {
                preparedStatement.setString(3, "ADMIN");
            } else if (user instanceof RegularUser) {
                preparedStatement.setString(3, "REGULAR_USER");
            } else {
                throw new IllegalArgumentException("Unsupported user type");
            }
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Set the generated ID on the user object
                    user.setId(resultSet.getInt("id"));
                }
            }
            return user;

        } catch (SQLException e) {
            throw new RuntimeException("Error creating user: " + user.getName(), e);
        }
    }

    @Override
    public User findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int userId = resultSet.getInt("id");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String role = resultSet.getString("role").toUpperCase();

                    if (role.equals("ADMIN")) {
                        return new AdminUser(userId, username, password);
                    } else if (role.equals("REGULAR_USER")) {
                        return new RegularUser(userId, username, password);
                    } else {
                        throw new IllegalArgumentException("Unsupported user role: " + role);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user by ID: " + id, e);
        }

        return null;
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();

        try (Connection connection = DBConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int userId = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String role = resultSet.getString("role").toUpperCase();

                if (role.equals("ADMIN")) {
                    users.add(new AdminUser(userId, username, password));
                } else if (role.equals("REGULAR_USER")) {
                    users.add(new RegularUser(userId, username, password));
                } else {
                    throw new IllegalArgumentException("Unsupported user role: " + role);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving all users", e);
        }

        return users;
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection connection = DBConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting user with ID: " + id, e);
        }
    }

    public List<User> getByRole(UserRole role) {
        String sql = "SELECT * FROM users WHERE role = ?";
        List<User> users = new ArrayList<>();

        try (Connection connection = DBConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, role.name());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int userId = resultSet.getInt("id");
                    String name = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String roleString = resultSet.getString("role").toUpperCase();

                    if (roleString.equals("ADMIN")) {
                        users.add(new AdminUser(userId, name, password));
                    } else if (roleString.equals("REGULAR_USER")) {
                        users.add(new RegularUser(userId, name, password));
                    } else if (roleString.equals("HACKER")) {
                        throw new UnsupportedOperationException("Hacker role is not supported yet.");
                    } else {
                        throw new IllegalArgumentException("Unsupported user role: " + roleString);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving users by role: " + role.name(), e);
        }
        return users;
    }
}