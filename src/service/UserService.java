package service;

import exception.InvalidDataException;
import exception.UserNotFoundException;
import model.User;
import model.enums.UserRole;
import repository.UserRepository;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepository;
    private final AuditService auditService = AuditService.getInstance();

    public UserService(UserRepository userRepository) {
        this.userRepository = new UserRepository();
    }

    public void createUser(User user) throws InvalidDataException {
        if (user == null || user.getName() == null || user.getName().isEmpty()) {
            throw new InvalidDataException("Invalid user data");
        }
        userRepository.create(user);
        auditService.writeToCSV("User created: " + user.getName());
    }

    public User getUserById(int id) throws UserNotFoundException {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new UserNotFoundException("User with id " + id + " not found.");
        }
        auditService.writeToCSV("User found: " + user.getName());
        return user;
    }

    public void deleteUserById(int id) throws UserNotFoundException {
        Optional<User> user = Optional.ofNullable(userRepository.findById(id));
        if (user.isEmpty()) throw new UserNotFoundException("User not found");
        userRepository.delete(id);
        auditService.writeToCSV("User deleted: " + id);
    }

    public List<User> getAllUsers() {
        List<User> users = userRepository.getAll();
        auditService.writeToCSV("Get all users");
        return users;
    }

    public List<User> searchByRole(UserRole role) {
        List<User> users = userRepository.getByRole(role);
        auditService.writeToCSV("Searched by role");
        return users;
    }
}