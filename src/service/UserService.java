package service;

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

    public void createUser(User user) {
        userRepository.create(user);
        auditService.writeToCSV("User created: " + user.getName());
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