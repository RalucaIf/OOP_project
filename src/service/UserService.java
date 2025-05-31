package service;

import model.User;
import model.enums.UserRole;
import repository.UserRepository;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = new UserRepository();
    }

    public void createUser(User user) {
        userRepository.create(user);
    }

    public List<User> getAllUsers() {
        return userRepository.getAll();
    }

    public List<User> searchByRole(UserRole role) {
        return userRepository.getByRole(role);
    }
}