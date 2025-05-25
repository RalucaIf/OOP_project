package service;

import model.interfaces.InterfaceUser;
import repository.UserRepository;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = new UserRepository();
    }

    public boolean createUser(InterfaceUser user, Connection connection) {
        return userRepository.createUser(user, connection);
    }

    public Optional<InterfaceUser> getUser(int id, Connection connection) {
        return userRepository.getUser(id,connection);
    }

    public List<InterfaceUser> getAllUsers(Connection connection) {
        return userRepository.getAllUsers(connection);
    }

    public boolean updateUser(InterfaceUser user, Connection connection) {
        return userRepository.updateUser(user, connection);
    }

    public boolean deleteUser(int id, Connection connection) {
        return userRepository.deleteUser(id, connection);
    }
}