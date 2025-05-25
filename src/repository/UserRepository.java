package repository;

import exception.UserException;
import model.AdminUser;
import model.HackerUser;
import model.RegularUser;
import model.enums.UserRole;
import model.interfaces.InterfaceUser;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository {
    public boolean createUser(InterfaceUser user, Connection connection) {
        String sql = """
                    insert into users 
                    (id, name, password, role) 
                    values (?, ?, ?, ?)
                    """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // parameter binding
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, user.getRole().name());
            // execute query
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    public Optional<InterfaceUser> getUser(int id, Connection connection) {
        String sql = "select * from users where id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int userId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String password = resultSet.getString("password");
                UserRole role = UserRole.valueOf(resultSet.getString("role"));

                InterfaceUser user = switch (role) {
                    case ADMIN -> new AdminUser(userId, name, password);
                    case REGULAR_USER -> new RegularUser(userId, name, password);
                    case HACKER -> new HackerUser(userId, name, "unknown", password);
                };

                return Optional.of(user);
            } else {
                throw new UserException();
            }
        } catch (SQLException | UserException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<InterfaceUser> getAllUsers(Connection connection) {
        String sql = "select * from users";
        List<InterfaceUser> users = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) { // Process all rows
                    int userId = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String password = resultSet.getString("password");
                    UserRole role = UserRole.valueOf(resultSet.getString("role"));

                    InterfaceUser user = switch (role) {
                        case ADMIN -> new AdminUser(userId, name, password);
                        case REGULAR_USER -> new RegularUser(userId, name, password);
                        case HACKER -> new HackerUser(userId, name, "unknown", password);
                    };

                    users.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public boolean updateUser(InterfaceUser user, Connection connection) {
        String sql = "update users set name = ?, password = ?, role = ? where id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRole().name());
            preparedStatement.setInt(4, user.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteUser(int id, Connection connection) {
        String sql = "delete from users where id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected == 0){
                throw new UserException();
            }
        } catch (SQLException | UserException e) {
            e.printStackTrace();
        }
        return false;
    }
}